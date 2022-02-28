package ca.benbingham.game.worldstructure;

import org.joml.Vector2f;

public class Chunk {
    public Crumb[][] crumbs;
    private final Vector2f relativeLocation;
    private final Vector2f absoluteLocation;
    private final int chunkSize = 10;

    private Planet parentPlanet;

    private Chunk positiveXNeighbour;
    private Chunk negativeXNeighbour;
    private Chunk positiveYNeighbour;
    private Chunk negativeYNeighbour;

    public Chunk(Planet parentPlanet, Vector2f relativeLocation) {
        this.parentPlanet = parentPlanet;
        this.relativeLocation = relativeLocation;

        absoluteLocation = new Vector2f();

        absoluteLocation.x = relativeLocation.x;
        absoluteLocation.y = relativeLocation.y;

        crumbs = new Crumb[chunkSize][chunkSize];

        for (int i = 0; i < chunkSize; i++) {
            for (int j = 0; j < chunkSize; j++) {
                crumbs[i][j] = new Crumb(this, new Vector2f(i, j));
            }
        }
    }

    public void initCrumbs() {
        for (int i = 0; i < chunkSize; i++) {
            for (int j = 0; j < chunkSize; j++) {
                crumbs[i][j].setNeighbours();
            }
        }
    }

    public void setNeighbours() {
        int posX = (int) relativeLocation.x + 1;
        int negX = (int) relativeLocation.x - 1;

        int posY = (int) relativeLocation.y + 1;
        int negY = (int) relativeLocation.y - 1;

        if (posX > parentPlanet.getSize() - 1) { this.positiveXNeighbour = null; }
        else { this.positiveXNeighbour = parentPlanet.chunks[posX][(int) relativeLocation.y]; }

        if (negX < 0) { negativeXNeighbour = null; }
        else { this.negativeXNeighbour = parentPlanet.chunks[negX][(int) relativeLocation.y]; }

        if (posY > parentPlanet.getSize() - 1) { this.positiveYNeighbour = null; }
        else { this.positiveYNeighbour = parentPlanet.chunks[(int) relativeLocation.x][posY]; }

        if (negY < 0) { this.negativeYNeighbour = null; }
        else { this.negativeYNeighbour = parentPlanet.chunks[(int) relativeLocation.x][negY]; }
    }

    public int getChunkSize() {
        return chunkSize;
    }

    public Vector2f getAbsoluteLocation() {
        return absoluteLocation;
    }

    public Chunk getPositiveXNeighbour() {
        return positiveXNeighbour;
    }

    public Chunk getNegativeXNeighbour() {
        return negativeXNeighbour;
    }

    public Chunk getPositiveYNeighbour() {
        return positiveYNeighbour;
    }

    public Chunk getNegativeYNeighbour() {
        return negativeYNeighbour;
    }
}
