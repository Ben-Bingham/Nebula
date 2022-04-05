package ca.benbingham.game.planetstructure.planetgeneration;

import ca.benbingham.game.blocks.BlockList;
import ca.benbingham.game.planetstructure.Chunk;
import ca.benbingham.game.planetstructure.Mesh;
import org.joml.Vector2i;

public class MultiChunkGenerator extends Thread{

    private Chunk[] chunks;
    private final TerrainGenerator terrainGenerator;

    private Mesh mesh;

    private boolean done = false;
    private volatile boolean kill = false;

    public MultiChunkGenerator(BlockList masterBlockList) {
        this.terrainGenerator = new TerrainGenerator(masterBlockList);
    }

    @Override
    public void run() {

    }

    private void makeChunks(Chunk[] chunks) {
        this.chunks = chunks;

        int posX, negX, posY, negY;
        Chunk posXChunk, negXChunk, posYChunk, negYChunk;
        for (int i = 0; i < chunks.length; i++) {
            Vector2i chunkCords = chunks[i].getCoordinates();

            posX = chunkCords.x + 1;
            negX = chunkCords.x - 1;
            posY = chunkCords.y + 1;
            negY = chunkCords.y - 1;

            posXChunk = new Chunk(new Vector2i(posX, chunkCords.y), terrainGenerator);
            negXChunk = new Chunk(new Vector2i(negX, chunkCords.y), terrainGenerator);
            posYChunk = new Chunk(new Vector2i(chunkCords.x, posY), terrainGenerator);
            negYChunk = new Chunk(new Vector2i(chunkCords.x, negY), terrainGenerator);

            mesh = terrainGenerator.createChunkMesh(chunks[i], posXChunk, negXChunk, posYChunk, negYChunk);
        }

        done = true;

        while (!kill) {
            Thread.onSpinWait();
        }
    }

    public Mesh getMesh() {
        return mesh;
    }

    public boolean isDone() {
        return done;
    }

    public void delete() {
        this.kill = true;
    }
}
