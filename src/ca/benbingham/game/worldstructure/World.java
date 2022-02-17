package ca.benbingham.game.worldstructure;

public class World {
    private final int loadedChunksX = 9;
    private final int loadedChunksY = 9;

    private ca.benbingham.game.worldstructure.Chunk[][] loadedChunks;

    public World() {
        loadedChunks = new ca.benbingham.game.worldstructure.Chunk[loadedChunksX][loadedChunksY];
    }
}
