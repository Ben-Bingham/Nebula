package ca.benbingham.game.planetstructure.planetgeneration;

import ca.benbingham.game.blocks.BlockList;
import ca.benbingham.game.planetstructure.Chunk;
import ca.benbingham.game.planetstructure.Mesh;
import org.joml.Vector2i;

import static ca.benbingham.engine.util.Printing.print;

public class SingleChunkGenerator extends Thread {
    private Chunk chunk;
    private Vector2i chunkCords;
    private Vector2i lastChunkCords;
    private final TerrainGenerator terrainGenerator;

    private Mesh mesh;

    // true means: locked
    // false means: unlocked
    private volatile boolean lock = true;

    private boolean done = false;
    private volatile boolean kill = false;

    public SingleChunkGenerator(BlockList masterBlockList) {
        this.terrainGenerator = new TerrainGenerator(masterBlockList);
    }

    public SingleChunkGenerator(BlockList masterBlockList, TerrainGenerator generator) {
        this.terrainGenerator = generator;
    }

    @Override
    public void run() {
        while (lock) {
            Thread.onSpinWait();
        }

        makeChunk(chunkCords);
        done = true;

        //while (!kill) {
//            print("b");
//            if (chunkCords != null && lastChunkCords != null) {
//                if (chunkCords.x != lastChunkCords.x && chunkCords.y != lastChunkCords.y) {
//                    done = false;
//                    makeChunk(chunkCords);
//
//                    lastChunkCords.x = chunkCords.x;
//                    lastChunkCords.y = chunkCords.y;
//                    done = true;
//
//                }
//            }
//            else {
//                try {
//                    this.wait();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
        //}
    }

    private void makeChunk(Vector2i chunkCords) {
        this.chunk = new Chunk(chunkCords, terrainGenerator);

        int posX, negX, posY, negY;
        Chunk posXChunk, negXChunk, posYChunk, negYChunk;

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

//    public void setChunk(Chunk chunk) {
//        this.chunk = chunk;
//    }

    public Vector2i getChunkCords() {
        return chunkCords;
    }

    public void setChunkCords(Vector2i chunkCords) {
        this.chunkCords = chunkCords;
    }

    public boolean isDone() {
        return done;
    }

    public void delete() {
        this.kill = true;
    }

    public Chunk getChunk() {
        return chunk;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public void unlock() {
        lock = false;
    }

    public void lock() {
        lock = true;
    }
}
