package ca.benbingham.game.planetstructure;

import org.joml.Vector2i;

public class Chunk {
    private Mesh mesh;
    private Vector2i coordinates;
    private boolean blockUpdate;

    public static final int xSize = 16;
    public static final int ySize = 256;
    public static final int zSize = 16;

    public Mesh getMesh() {
        return mesh;
    }

    public void setMesh(Mesh mesh) {
        this.mesh = mesh;
    }

    public Vector2i getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Vector2i coordinates) {
        this.coordinates = coordinates;
    }

    public boolean isBlockUpdate() {
        return blockUpdate;
    }

    public void setBlockUpdate(boolean blockUpdate) {
        this.blockUpdate = blockUpdate;
    }
}
