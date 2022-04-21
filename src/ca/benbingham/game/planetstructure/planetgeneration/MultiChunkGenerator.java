package ca.benbingham.game.planetstructure.planetgeneration;

import ca.benbingham.game.planetstructure.Chunk;
import ca.benbingham.game.planetstructure.blocks.BlockList;
import org.joml.Vector2i;

import java.util.Arrays;

import static ca.benbingham.engine.util.Printing.print;

public class MultiChunkGenerator extends Thread{
    private Chunk[] chunks;
    private Chunk posXChunk, negXChunk, posYChunk, negYChunk;

    private Vector2i[] chunkCords;
    private final TerrainGenerator terrainGenerator;

    // true means: locked
    // false means: unlocked
    private boolean[] done;
    private volatile boolean kill = false;

    public MultiChunkGenerator(int numberOfChunks, BlockList masterBlockList) {
        done = new boolean[numberOfChunks];
        Arrays.fill(done, false);

        chunkCords = new Vector2i[numberOfChunks];
        chunks = new Chunk[numberOfChunks];

        this.terrainGenerator = new TerrainGenerator(masterBlockList);
    }

    @Override
    public void run() {
        this.lock();

        while(!kill) {
            for (int i = 0; i < chunkCords.length; i++) {
                if (chunkCords[i] != null) {
                    done[i] = false;
                    makeChunk(chunkCords[i], i);
                    done[i] = true;
                }
            }
            this.lock();
        }
    }

    private void makeChunk(Vector2i chunkCords, int i) {
//        if (this.chunks[i] != null) {
//            this.chunks[i].delete();
//        }
        this.chunks[i] = new Chunk(new Vector2i(chunkCords.x, chunkCords.y), terrainGenerator);

        int posX, negX, posY, negY;

        posX = chunkCords.x + 1;
        negX = chunkCords.x - 1;
        posY = chunkCords.y + 1;
        negY = chunkCords.y - 1;

        posXChunk = new Chunk(new Vector2i(posX, chunkCords.y), terrainGenerator);
        negXChunk = new Chunk(new Vector2i(negX, chunkCords.y), terrainGenerator);
        posYChunk = new Chunk(new Vector2i(chunkCords.x, posY), terrainGenerator);
        negYChunk = new Chunk(new Vector2i(chunkCords.x, negY), terrainGenerator);
        if (chunks[i] != null) {
            chunks[i].setMesh(terrainGenerator.createChunkMesh(chunks[i], posXChunk, negXChunk, posYChunk, negYChunk));
        }
        posXChunk = null;
        negXChunk = null;
        posYChunk = null;
        negYChunk = null;
    }

    public void setChunkCords(Vector2i[] chunkCords) {
        this.chunkCords = chunkCords;
    }

    public boolean isDone(int i) {
        return done[i];
    }

    public Chunk getChunk(int i) {
        return chunks[i];
    }

    public void setDone(boolean done, int i) {
        this.done[i] = done;
    }

    public void unlock() {
        synchronized (this) {
            notifyAll();
        }
    }

    public void lock() {
        synchronized (this) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void delete() {
        this.kill = true;
        for (Chunk chunk : chunks) {
            if (chunk != null) {
                chunk.delete();
            }
        }
        this.unlock();
    }
}
