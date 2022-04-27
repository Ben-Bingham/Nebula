package ca.benbingham.game.planetstructure.planetgeneration;

import ca.benbingham.game.planetstructure.blocks.BlockList;
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

    private void makeChunk(Vector2i representingCoordinates) {
        int posX, negX, posY, negY;

        posX = representingCoordinates.x + 1; //TODO
        negX = representingCoordinates.x - 1;
        posY = representingCoordinates.y + 1;
        negY = representingCoordinates.y - 1;

        chunk = new Chunk(representingCoordinates, terrainGenerator);
        posXChunk = new Chunk(new Vector2i(posX, representingCoordinates.y), terrainGenerator);
        negXChunk = new Chunk(new Vector2i(negX, representingCoordinates.y), terrainGenerator);
        posYChunk = new Chunk(new Vector2i(representingCoordinates.x, posY), terrainGenerator);
        negYChunk = new Chunk(new Vector2i(representingCoordinates.x, negY), terrainGenerator);

        Mesh mesh = terrainGenerator.createChunkMesh(chunk, posXChunk, negXChunk, posYChunk, negYChunk);

        manager.setChunkData(mesh, representingCoordinates);
    }

    public void delete() {

    }
}
