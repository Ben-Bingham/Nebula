package ca.benbingham.game.worldstructure;

public class World {
    private final int loadedChunksX = 9;
    private final int loadedChunksY = 9;

    private Chunk[][] loadedChunks;

    public World() {
        loadedChunks = new Chunk[loadedChunksX][loadedChunksY];
    }
}
