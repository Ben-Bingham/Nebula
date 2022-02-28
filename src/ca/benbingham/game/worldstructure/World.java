package ca.benbingham.game.worldstructure;

public class World {
    private final int loadedChunksX = 9;
    private final int loadedChunksY = 9;

    private Crumb[][] loadedCrumbs;

    public World() {
        loadedCrumbs = new Crumb[loadedChunksX][loadedChunksY];
    }
}
