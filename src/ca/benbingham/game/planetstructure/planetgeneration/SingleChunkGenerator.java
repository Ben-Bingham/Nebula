package ca.benbingham.game.planetstructure.planetgeneration;

import ca.benbingham.game.planetstructure.blocks.BlockList;
import ca.benbingham.game.planetstructure.Chunk;
import ca.benbingham.game.planetstructure.geometry.Mesh;
import org.joml.Vector2i;

import static ca.benbingham.engine.util.Printing.print;

public class SingleChunkGenerator implements Runnable {
    private Chunk chunk, posXChunk, negXChunk, posYChunk, negYChunk;

    private Vector2i chunkCords;
    private ChunkGenerationManager manager;
    private final TerrainGenerator terrainGenerator;

    public SingleChunkGenerator(BlockList masterBlockList, Vector2i chunkCords, ChunkGenerationManager manager) {
        this.terrainGenerator = new TerrainGenerator(masterBlockList);
        this.chunkCords = chunkCords;
        this.manager = manager;
    }

    @Override
    public void run() {
        makeChunk(chunkCords);
    }

    private void makeChunk(Vector2i chunkCords) {
        int posX, negX, posY, negY;

        posX = chunkCords.x + 1;
        negX = chunkCords.x - 1;
        posY = chunkCords.y + 1;
        negY = chunkCords.y - 1;

        chunk = new Chunk(chunkCords, terrainGenerator);
        posXChunk = new Chunk(new Vector2i(posX, chunkCords.y), terrainGenerator);
        negXChunk = new Chunk(new Vector2i(negX, chunkCords.y), terrainGenerator);
        posYChunk = new Chunk(new Vector2i(chunkCords.x, posY), terrainGenerator);
        negYChunk = new Chunk(new Vector2i(chunkCords.x, negY), terrainGenerator);

        Mesh mesh = terrainGenerator.createChunkMesh(chunk, posXChunk, negXChunk, posYChunk, negYChunk);

        manager.setChunkData(mesh, chunkCords, this);
    }

    public void delete() {

    }
}
