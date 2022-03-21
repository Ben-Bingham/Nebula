package ca.benbingham.game.planetstructure.planetgeneration;

import ca.benbingham.game.planetstructure.Mesh;
import org.joml.Vector2i;

public class ChunkMeshGenerator extends Thread{
    private final Vector2i chunkCords;
    private TerrainGenerator generator;
    private boolean isDone = false;
    private Mesh mesh;

    public ChunkMeshGenerator(Vector2i chunkCords) {
        this.chunkCords = chunkCords;
    }

    @Override
    public void run() {
        generator = new TerrainGenerator();

        mesh = generator.createChunkMesh(chunkCords);
        isDone = true;
    }

    public void kill() {
        isDone = false;
    }

    public boolean isDone() {
        return isDone;
    }

    public Vector2i getChunkCords() {
        return chunkCords;
    }

    public Mesh getMesh() {
        return mesh;
    }
}
