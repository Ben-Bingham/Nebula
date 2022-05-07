package ca.benbingham.game.generation.planetgeneration;

import ca.benbingham.game.planetstructure.Chunk;
import ca.benbingham.game.planetstructure.geometry.Mesh;
import org.joml.Vector2i;


public class SingleChunkGenerator implements Runnable {
    private Chunk chunk, posXChunk, negXChunk, posYChunk, negYChunk;

    private Vector2i chunkCords;
    private ChunkGenerationManager manager;
    private final TerrainGenerator terrainGenerator;

    public SingleChunkGenerator(TerrainGenerator terrainGenerator, Vector2i chunkCords, ChunkGenerationManager manager) {
        this.terrainGenerator = terrainGenerator;
        this.chunkCords = chunkCords;
        this.manager = manager;
    }

    @Override
    public void run() {
        makeChunk(chunkCords);
    }

    private void makeChunk(Vector2i chunkCord) {
        int posX, negX, posY, negY;

        posX = chunkCord.x + 1; //TODO
        negX = chunkCord.x - 1;
        posY = chunkCord.y + 1;
        negY = chunkCord.y - 1;

        chunk = new Chunk(chunkCord, terrainGenerator);
        posXChunk = new Chunk(new Vector2i(posX, chunkCord.y), terrainGenerator);
        negXChunk = new Chunk(new Vector2i(negX, chunkCord.y), terrainGenerator);
        posYChunk = new Chunk(new Vector2i(chunkCord.x, posY), terrainGenerator);
        negYChunk = new Chunk(new Vector2i(chunkCord.x, negY), terrainGenerator);

        Mesh mesh = terrainGenerator.createChunkMesh(chunk, posXChunk, negXChunk, posYChunk, negYChunk);

        manager.setChunkData(mesh, chunkCord);
    }

    public void delete() {

    }
}
