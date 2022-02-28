package ca.benbingham.game.worldstructure;

import org.joml.Vector2f;
import org.joml.Vector3f;

import static ca.benbingham.engine.util.Printing.print;

public class Crumb {
    private final Vector2f relativeLocation;
    private final Vector2f absoluteLocation;

    public Block[][][] blocks;
    private final int crumbSizeX = 10;
    private final int crumbSizeY = 1;
    private final int crumbSizeZ = 10;

    private Crumb positiveXNeighbour;
    private Crumb negativeXNeighbour;
    private Crumb positiveYNeighbour;
    private Crumb negativeYNeighbour;

    private Chunk parentChunk;

    public Crumb(Chunk parentChunk, Vector2f relativeLocation) {
        this.parentChunk = parentChunk;

        this.relativeLocation = relativeLocation;

        absoluteLocation = new Vector2f();

        absoluteLocation.x = relativeLocation.x + parentChunk.getAbsoluteLocation().x * parentChunk.getChunkSize();
        absoluteLocation.y = relativeLocation.y + parentChunk.getAbsoluteLocation().y * parentChunk.getChunkSize();

        blocks = new Block[this.crumbSizeX][this.crumbSizeY][this.crumbSizeZ];

        for (int i = 0; i < crumbSizeX; i++) {
            for (int j = 0; j < crumbSizeY; j++) {
                for (int k = 0; k < crumbSizeZ; k++) {
                    blocks[i][j][k] = new Block(new Vector3f(i, j, k), this);
                }
            }
        }
    }

    public void initBlocks() {
        for (int i = 0; i < crumbSizeX; i++) {
            for (int j = 0; j < crumbSizeY; j++) {
                for (int k = 0; k < crumbSizeZ; k++) {
                    blocks[i][j][k].setNeighbours();
                    blocks[i][j][k].determineVisibleFaces();
                }
            }
        }
    }

    public void setNeighbours() {
        int posX = (int) relativeLocation.x + 1;
        int negX = (int) relativeLocation.x - 1;

        int posY = (int) relativeLocation.y + 1;
        int negY = (int) relativeLocation.y - 1;

        if (posX > parentChunk.getChunkSize() - 1) { this.positiveXNeighbour = null; }
        else { this.positiveXNeighbour = parentChunk.crumbs[posX][(int) relativeLocation.y]; }

        if (negX < 0) { negativeXNeighbour = null; }
        else { this.negativeXNeighbour = parentChunk.crumbs[negX][(int) relativeLocation.y]; }

        if (posY > parentChunk.getChunkSize() - 1) { this.positiveYNeighbour = null; }
        else { this.positiveYNeighbour = parentChunk.crumbs[(int) relativeLocation.x][posY]; }

        if (negY < 0) { this.negativeYNeighbour = null; }
        else { this.negativeYNeighbour = parentChunk.crumbs[(int) relativeLocation.x][negY]; }
    }

    public int getCrumbSizeX() {
        return crumbSizeX;
    }

    public int getCrumbSizeY() {
        return crumbSizeY;
    }

    public int getCrumbSizeZ() {
        return crumbSizeZ;
    }

    public Vector2f getAbsoluteLocation() {
        return absoluteLocation;
    }

    public Vector2f getRelativeLocation() {
        return relativeLocation;
    }

    public Crumb getPositiveXNeighbour() {
        return positiveXNeighbour;
    }

    public Crumb getNegativeXNeighbour() {
        return negativeXNeighbour;
    }

    public Crumb getPositiveYNeighbour() {
        return positiveYNeighbour;
    }

    public Crumb getNegativeYNeighbour() {
        return negativeYNeighbour;
    }

    public Chunk getParentChunk() {
        return parentChunk;
    }
}
