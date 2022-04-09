package ca.benbingham.game.planetstructure.planetgeneration;

import ca.benbingham.game.planetstructure.blocks.BlockList;
import ca.benbingham.game.planetstructure.Chunk;
import org.joml.Vector2i;

import static ca.benbingham.engine.util.Printing.print;

public class SingleChunkGenerator extends Thread {
    private Chunk chunk;
    private Chunk posXChunk, negXChunk, posYChunk, negYChunk;

    private Vector2i chunkCords;
    private final TerrainGenerator terrainGenerator;

    private volatile boolean lock = true;
    // true means: locked
    // false means: unlocked

    private boolean done = false;
    private volatile boolean kill = false;

    public SingleChunkGenerator(BlockList masterBlockList) {
        this.terrainGenerator = new TerrainGenerator(masterBlockList);
    }

    @Override
    public void run() {
        while(!kill) {
            while (lock) {
                Thread.onSpinWait();
            }

            done = false;
            makeChunk(chunkCords);
            done = true;
            this.lock();
        }
    }

    private void makeChunk(Vector2i chunkCords) {
        this.chunk = new Chunk(new Vector2i(chunkCords.x, chunkCords.y), terrainGenerator);

        int posX, negX, posY, negY;

        posX = chunkCords.x + 1;
        negX = chunkCords.x - 1;
        posY = chunkCords.y + 1;
        negY = chunkCords.y - 1;

        posXChunk = new Chunk(new Vector2i(posX, chunkCords.y), terrainGenerator);
        negXChunk = new Chunk(new Vector2i(negX, chunkCords.y), terrainGenerator);
        posYChunk = new Chunk(new Vector2i(chunkCords.x, posY), terrainGenerator);
        negYChunk = new Chunk(new Vector2i(chunkCords.x, negY), terrainGenerator);

        chunk.setMesh(terrainGenerator.createChunkMesh(chunk, posXChunk, negXChunk, posYChunk, negYChunk));
    }

    public void setChunkCords(Vector2i chunkCords) {
        this.chunkCords = chunkCords;
    }

    public boolean isDone() {
        return done;
    }

    public void delete() {
        this.kill = true;
        this.unlock();
    }

    public Chunk getChunk() {
        return chunk;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public void unlock() {
        if (this.chunk != null) {
            this.chunk.delete();
            this.chunk = null;
        }
        lock = false;
    }

    public void lock() {
        lock = true;
    }
}
