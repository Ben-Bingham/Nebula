package ca.benbingham.game.worldstructure;


import ca.benbingham.game.Quad;
import org.joml.Vector3f;

public class Block {
    private final Vector3f location;

    private final int numberOfFaces = 6;
    private final ca.benbingham.game.worldstructure.Chunk parentChunk;

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

    public Block(Vector3f location, ca.benbingham.game.worldstructure.Chunk parentChunk) {
        this.location = location;
        this.parentChunk = parentChunk;

        faces = new Quad[numberOfFaces];
        for (int i = 0; i < faces.length; i++) {
            faces[i] = new Quad();
        }
    }

    public void setNeighbours() {
        int posX = (int) location.x + 1;
        int negX = (int) location.x - 1;

        int posY = (int) location.y + 1;
        int negY = (int) location.y - 1;

        int posZ = (int) location.z + 1;
        int negZ = (int) location.z - 1;

        if (posX > parentChunk.getChunkSizeX() - 1) { this.positiveXNeighbour = null; }
        else { this.positiveXNeighbour = parentChunk.blocks[posX][(int)location.y][(int)location.z]; }

        if (negX < 0) { negativeXNeighbour = null; }
        else { this.negativeXNeighbour = parentChunk.blocks[negX][(int)location.y][(int)location.z]; }

        if (posY > parentChunk.getChunkSizeY() - 1) { this.positiveYNeighbour = null; }
        else { this.positiveYNeighbour = parentChunk.blocks[(int)location.x][posY][(int)location.z]; }

        if (negY < 0) { this.negativeYNeighbour = null; }
        else { this.negativeYNeighbour = parentChunk.blocks[(int)location.x][negY][(int)location.z]; }

        if (posZ > parentChunk.getChunkSizeZ() - 1) { this.positiveZNeighbour = null; }
        else { this.positiveZNeighbour = parentChunk.blocks[(int)location.x][(int)location.y][posZ]; }

        if (negZ < 0) { this.negativeZNeighbour = null; }
        else { this.negativeZNeighbour = parentChunk.blocks[(int)location.x][(int)location.y][negZ]; }
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
}