package ca.benbingham.game.worldstructure;


import ca.benbingham.game.Quad;
import org.joml.Vector3f;

import static ca.benbingham.engine.util.Printing.print;

public class Block {
    private final Vector3f relativeLocation;
    private final Vector3f absoluteLocation;

    private final int numberOfFaces = 6;
    private final Crumb parentCrumb;

    private Block positiveYNeighbour;
    private Block negativeYNeighbour;
    private Block positiveXNeighbour;
    private Block negativeXNeighbour;
    private Block positiveZNeighbour;
    private Block negativeZNeighbour;

    public boolean positiveXFaceVisibility;
    public boolean negativeXFaceVisibility;
    public boolean positiveYFaceVisibility;
    public boolean negativeYFaceVisibility;
    public boolean positiveZFaceVisibility;
    public boolean negativeZFaceVisibility;

    private final Quad[] faces;

    private final float[] positiveXFace = new float[]{
            0.5f,  0.5f,  0.5f,   // top-left
            0.5f,  0.5f, -0.5f,   // top-right
            0.5f, -0.5f, -0.5f,   // bottom-right
            0.5f, -0.5f,  0.5f,    // bottom-left
    };

    private final float[] positiveXFaceTexCords = new float[]{
            0.0f, 1.0f,  // top-left
            1.0f, 1.0f,  // top-right
            1.0f, 0.0f,  // bottom-right
            0.0f, 0.0f,   // bottom-left
   };

    private final float[] negativeXFace = new float[]{
            -0.5f,  0.5f, -0.5f,   // top-left
            -0.5f,  0.5f,  0.5f,   // top-right
            -0.5f, -0.5f,  0.5f,   // bottom-right
            -0.5f, -0.5f, -0.5f,   // bottom-left
    };

    private final float[] negativeXFaceTexCords = new float[]{
            1.0f, 1.0f,  // top-left
            1.0f, 0.0f,  // top-right
            0.0f, 0.0f,  // bottom-right
            0.0f, 1.0f,  // bottom-left
    };

    private final float[] positiveYFace = new float[]{
            -0.5f,  0.5f, -0.5f,   // top-left
            0.5f,  0.5f, -0.5f,  // top-right
            0.5f,  0.5f,  0.5f,  // bottom-right
            -0.5f,  0.5f,  0.5f,   // bottom-left
    };

    private final float[] positiveYFaceTexCords = new float[]{
            0.0f, 1.0f,  // top-left
            1.0f, 1.0f, // top-right
            1.0f, 0.0f, // bottom-right
            0.0f, 0.0f,  // bottom-left
    };

    private final float[] negativeYFace = new float[]{
            0.5f, -0.5f, -0.5f,  // top-left
            -0.5f, -0.5f, -0.5f,  // top-right
            -0.5f, -0.5f,  0.5f,  // bottom-right
            0.5f, -0.5f,  0.5f,  // bottom-left
    };

    private final float[] negativeYFaceTexCords = new float[]{
            0.0f, 1.0f,  // top-left
            1.0f, 1.0f,  // top-right
            1.0f, 0.0f,  // bottom-right
            0.0f, 0.0f,  // bottom-left
    };

    private final float[] positiveZFace = new float[]{
            -0.5f,  0.5f,  0.5f,  // top-left
            0.5f,  0.5f,  0.5f,  // top-right
            0.5f, -0.5f,  0.5f,  // bottom-right
            -0.5f, -0.5f,  0.5f,  // bottom-left
    };

    private final float[] positiveZFaceTexCords = new float[]{
            0.0f, 1.0f,   // top-left
            1.0f, 1.0f,   // top-right
            1.0f, 0.0f,   // bottom-right
            0.0f, 0.0f,   // bottom-left
    };

    private final float[] negativeZFace = new float[]{
            0.5f, -0.5f, -0.5f,  // bottom-right
            0.5f,  0.5f, -0.5f,  // top-right
            -0.5f,  0.5f, -0.5f, // top-left
            -0.5f, -0.5f, -0.5f,  // Bottom-left
    };

    private final float[] negativeZFaceTexCords = new float[]{
            1.0f, 0.0f,  // bottom-right
            1.0f, 1.0f,  // top-right
            0.0f, 1.0f,  // top-left
            0.0f, 0.0f,  // bottom-left
    };

    public Block(Vector3f relativeLocation, Crumb parentCrumb) {
        this.relativeLocation = relativeLocation;
        this.parentCrumb = parentCrumb;

        absoluteLocation = new Vector3f();
        absoluteLocation.x = relativeLocation.x + parentCrumb.getAbsoluteLocation().x * parentCrumb.getCrumbSizeX();
        absoluteLocation.y = relativeLocation.y;
        absoluteLocation.z = relativeLocation.z + parentCrumb.getAbsoluteLocation().y * parentCrumb.getCrumbSizeZ();

        faces = new Quad[numberOfFaces];
        for (int i = 0; i < faces.length; i++) {
            faces[i] = new Quad();
        }
    }

    public void setNeighbours() {
        int posX = (int) relativeLocation.x + 1;
        int negX = (int) relativeLocation.x - 1;

        int posY = (int) relativeLocation.y + 1;
        int negY = (int) relativeLocation.y - 1;

        int posZ = (int) relativeLocation.z + 1;
        int negZ = (int) relativeLocation.z - 1;

        if (posX > parentCrumb.getCrumbSizeX() - 1) {

            if (parentCrumb.getPositiveXNeighbour() != null) {
                positiveXNeighbour = parentCrumb.getPositiveXNeighbour().blocks[0][(int) relativeLocation.y][(int) relativeLocation.z];
            }
            else if (parentCrumb.getParentChunk().getPositiveXNeighbour() != null) {
                positiveXNeighbour = parentCrumb.getParentChunk().getPositiveXNeighbour().crumbs[0][(int) parentCrumb.getRelativeLocation().y].blocks[0][(int) relativeLocation.y][(int) relativeLocation.z];
            }
            else { this.positiveXNeighbour = null; }
        }
        else { this.positiveXNeighbour = parentCrumb.blocks[posX][(int) relativeLocation.y][(int) relativeLocation.z]; }

        if (negX < 0) {

            if (parentCrumb.getNegativeXNeighbour() != null) {
                negativeXNeighbour = parentCrumb.getNegativeXNeighbour().blocks[parentCrumb.getCrumbSizeX() - 1][(int) relativeLocation.y][(int) relativeLocation.z];
            }
            else if (parentCrumb.getParentChunk().getNegativeXNeighbour() != null) {
                negativeXNeighbour = parentCrumb.getParentChunk().getNegativeXNeighbour().crumbs[parentCrumb.getParentChunk().getChunkSize() - 1][(int) parentCrumb.getRelativeLocation().y].blocks[parentCrumb.getCrumbSizeX() - 1][(int) relativeLocation.y][(int) relativeLocation.z];
            }
            else { this.negativeXNeighbour = null; }
        }
        else { this.negativeXNeighbour = parentCrumb.blocks[negX][(int) relativeLocation.y][(int) relativeLocation.z]; }

        if (posY > parentCrumb.getCrumbSizeY() - 1) { this.positiveYNeighbour = null; }
        else { this.positiveYNeighbour = parentCrumb.blocks[(int) relativeLocation.x][posY][(int) relativeLocation.z]; }

        if (negY < 0) { this.negativeYNeighbour = null; }
        else { this.negativeYNeighbour = parentCrumb.blocks[(int) relativeLocation.x][negY][(int) relativeLocation.z]; }

        if (posZ > parentCrumb.getCrumbSizeZ() - 1) {

            if (parentCrumb.getPositiveYNeighbour() != null) {
                positiveZNeighbour = parentCrumb.getPositiveYNeighbour().blocks[(int) relativeLocation.x][(int) relativeLocation.y][0];
            }
            else if (parentCrumb.getParentChunk().getPositiveYNeighbour() != null) {
                positiveZNeighbour = parentCrumb.getParentChunk().getPositiveYNeighbour().crumbs[(int) parentCrumb.getRelativeLocation().x][0].blocks[(int) relativeLocation.x][(int) relativeLocation.y][0];
            }
            else { this.positiveZNeighbour = null; }
        }
        else { this.positiveZNeighbour = parentCrumb.blocks[(int) relativeLocation.x][(int) relativeLocation.y][posZ]; }

        if (negZ < 0) {

            if (parentCrumb.getNegativeYNeighbour() != null) {
                negativeZNeighbour = parentCrumb.getNegativeYNeighbour().blocks[(int) relativeLocation.x][(int) relativeLocation.y][parentCrumb.getCrumbSizeZ() - 1];
            }
            else if (parentCrumb.getParentChunk().getNegativeYNeighbour() != null) {
                negativeZNeighbour = parentCrumb.getParentChunk().getNegativeYNeighbour().crumbs[(int) parentCrumb.getRelativeLocation().x][parentCrumb.getParentChunk().getChunkSize() - 1].blocks[(int) relativeLocation.x][(int) relativeLocation.y][parentCrumb.getCrumbSizeX() - 1];
            }
            else { this.negativeZNeighbour = null; }
        }
        else { this.negativeZNeighbour = parentCrumb.blocks[(int) relativeLocation.x][(int) relativeLocation.y][negZ]; }
    }

    public void determineVisibleFaces() {
        if (this.positiveXNeighbour == null) { this.positiveXFaceVisibility = true; }
        else { this.positiveXFaceVisibility = false; }

        if (this.negativeXNeighbour == null) { this.negativeXFaceVisibility = true; }
        else { this.negativeXFaceVisibility = false; }

        if (this.positiveYNeighbour == null) { this.positiveYFaceVisibility = true; }
        else { this.positiveYFaceVisibility = false; }

        if (this.negativeYNeighbour == null) { this.negativeYFaceVisibility = true; }
        else { this.negativeYFaceVisibility = false; }

        if (this.positiveZNeighbour == null) { this.positiveZFaceVisibility = true; }
        else { this.positiveZFaceVisibility = false; }

        if (this.negativeZNeighbour == null) { this.negativeZFaceVisibility = true; }
        else { this.negativeZFaceVisibility = false; }

        updateFaceArray();
    }

    public void updateFaceArray() {
        if (positiveXFaceVisibility) { faces[0].importData(positiveXFace, positiveXFaceTexCords); }
        else { faces[0] = null; }
        if (negativeXFaceVisibility) { faces[1].importData(negativeXFace, negativeXFaceTexCords); }
        else { faces[1] = null; }
        if (positiveYFaceVisibility) { faces[2].importData(positiveYFace, positiveYFaceTexCords); }
        else { faces[2] = null; }
        if (negativeYFaceVisibility) { faces[3].importData(negativeYFace, negativeYFaceTexCords); }
        else { faces[3] = null; }
        if (positiveZFaceVisibility) { faces[4].importData(positiveZFace, positiveZFaceTexCords); }
        else { faces[4] = null; }
        if (negativeZFaceVisibility) { faces[5].importData(negativeZFace, negativeZFaceTexCords); }
        else { faces[5] = null; }
    }

    public Quad[] getFaces() {
        return faces;
    }

    public Vector3f getAbsoluteLocation() {
        return absoluteLocation;
    }
}