package ca.benbingham.game.planetstructure;


import ca.benbingham.game.Quad;
import ca.benbingham.game.planetstructure.enums.EBlockName;
import org.joml.Vector2i;
import org.joml.Vector3f;

public class Block {
    private EBlockName name;
    private String texture;

    private final Vector3f relativeLocation;
    private final Vector3f absoluteLocation;

    private final int numberOfFaces = 6;

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

    private boolean visibleFace;

    private static final float[] positiveXFace = new float[]{
            0.5f,  0.5f,  0.5f,   // top-left
            0.5f,  0.5f, -0.5f,   // top-right
            0.5f, -0.5f, -0.5f,   // bottom-right
            0.5f, -0.5f,  0.5f,    // bottom-left
    };

    private static final float[] positiveXFaceTexCords = new float[]{
            0.0f, 1.0f,  // top-left
            1.0f, 1.0f,  // top-right
            1.0f, 0.0f,  // bottom-right
            0.0f, 0.0f,   // bottom-left
   };

    private static final float[] negativeXFace = new float[]{
            -0.5f,  0.5f, -0.5f,   // top-left
            -0.5f,  0.5f,  0.5f,   // top-right
            -0.5f, -0.5f,  0.5f,   // bottom-right
            -0.5f, -0.5f, -0.5f,   // bottom-left
    };

    private static final float[] negativeXFaceTexCords = new float[]{
            1.0f, 1.0f,  // top-left
            1.0f, 0.0f,  // top-right
            0.0f, 0.0f,  // bottom-right
            0.0f, 1.0f,  // bottom-left
    };

    private static final float[] positiveYFace = new float[]{
            -0.5f,  0.5f, -0.5f,   // top-left
            0.5f,  0.5f, -0.5f,  // top-right
            0.5f,  0.5f,  0.5f,  // bottom-right
            -0.5f,  0.5f,  0.5f,   // bottom-left
    };

    private static final float[] positiveYFaceTexCords = new float[]{
            0.0f, 1.0f,  // top-left
            1.0f, 1.0f, // top-right
            1.0f, 0.0f, // bottom-right
            0.0f, 0.0f,  // bottom-left
    };

    private static final float[] negativeYFace = new float[]{
            0.5f, -0.5f, -0.5f,  // top-left
            -0.5f, -0.5f, -0.5f,  // top-right
            -0.5f, -0.5f,  0.5f,  // bottom-right
            0.5f, -0.5f,  0.5f,  // bottom-left
    };

    private static final float[] negativeYFaceTexCords = new float[]{
            0.0f, 1.0f,  // top-left
            1.0f, 1.0f,  // top-right
            1.0f, 0.0f,  // bottom-right
            0.0f, 0.0f,  // bottom-left
    };

    private static final float[] positiveZFace = new float[]{
            -0.5f,  0.5f,  0.5f,  // top-left
            0.5f,  0.5f,  0.5f,  // top-right
            0.5f, -0.5f,  0.5f,  // bottom-right
            -0.5f, -0.5f,  0.5f,  // bottom-left
    };

    private static final float[] positiveZFaceTexCords = new float[]{
            0.0f, 1.0f,   // top-left
            1.0f, 1.0f,   // top-right
            1.0f, 0.0f,   // bottom-right
            0.0f, 0.0f,   // bottom-left
    };

    private static final float[] negativeZFace = new float[]{
            0.5f, -0.5f, -0.5f,  // bottom-right
            0.5f,  0.5f, -0.5f,  // top-right
            -0.5f,  0.5f, -0.5f, // top-left
            -0.5f, -0.5f, -0.5f,  // Bottom-left
    };

    private static final float[] negativeZFaceTexCords = new float[]{
            1.0f, 0.0f,  // bottom-right
            1.0f, 1.0f,  // top-right
            0.0f, 1.0f,  // top-left
            0.0f, 0.0f,  // bottom-left
    };

    public Block(EBlockName name, Vector3f relativeLocation, Vector2i chunkCords) {
        this.name = name;

        this.relativeLocation = relativeLocation;

        this.faces = new Quad[numberOfFaces];
        for (int i = 0; i < faces.length; i++) {
            faces[i] = new Quad();
        }

        absoluteLocation = new Vector3f();
        absoluteLocation.x = relativeLocation.x + chunkCords.x * Chunk.xSize;
        absoluteLocation.y = relativeLocation.y;
        absoluteLocation.z = relativeLocation.z + chunkCords.y * Chunk.zSize;
    }

    public void setNeighbours(Block[][][] chunk, Block[][][] posXChunk, Block[][][] negXChunk, Block[][][] posYChunk, Block[][][] negYChunk) {
        int posX = (int) relativeLocation.x + 1;
        int negX = (int) relativeLocation.x - 1;

        int posY = (int) relativeLocation.y + 1;
        int negY = (int) relativeLocation.y - 1;

        int posZ = (int) relativeLocation.z + 1;
        int negZ = (int) relativeLocation.z - 1;

        if (posX > Chunk.xSize - 1) {
            positiveXNeighbour = posXChunk[0][(int) relativeLocation.y][(int) relativeLocation.z];
        }
        else {
            positiveXNeighbour = chunk[posX][(int) relativeLocation.y][(int) relativeLocation.z];
        }

        if (negX < 0) {
            negativeXNeighbour = negXChunk[Chunk.xSize - 1][(int) relativeLocation.y][(int) relativeLocation.z];
        }
        else {
            negativeXNeighbour = chunk[negX][(int) relativeLocation.y][(int) relativeLocation.z];
        }

        if (posY > Chunk.ySize - 1) {
            positiveYNeighbour = null;
        }
        else {
            positiveYNeighbour = chunk[(int) relativeLocation.x][posY][(int) relativeLocation.z];
        }

        if (negY < 0) {
            negativeYNeighbour = null;
        }
        else {
            negativeYNeighbour = chunk[(int) relativeLocation.x][negY][(int) relativeLocation.z];
        }

        if (posZ > Chunk.zSize - 1) {
            positiveZNeighbour = posYChunk[(int) relativeLocation.x][(int) relativeLocation.y][0];
        }
        else {
            positiveZNeighbour = chunk[(int) relativeLocation.x][(int) relativeLocation.y][posZ];
        }

        if (negZ < 0) {
            negativeZNeighbour = posYChunk[(int) relativeLocation.x][(int) relativeLocation.y][Chunk.zSize - 1];
        }
        else {
            negativeZNeighbour = chunk[(int) relativeLocation.x][(int) relativeLocation.y][negZ];
        }
    }

    public void setNeighbours(Block[][][] chunk, Block posXNeighbour, Block negXNeighbour, Block posYNeighbour, Block negYNeighbour) {
        int posX = (int) relativeLocation.x + 1;
        int negX = (int) relativeLocation.x - 1;

        int posY = (int) relativeLocation.y + 1;
        int negY = (int) relativeLocation.y - 1;

        int posZ = (int) relativeLocation.z + 1;
        int negZ = (int) relativeLocation.z - 1;

        if (posX > Chunk.xSize - 1) {
            positiveXNeighbour = posXNeighbour;
        }
        else {
            positiveXNeighbour = chunk[posX][(int) relativeLocation.y][(int) relativeLocation.z];
        }

        if (negX < 0) {
            negativeXNeighbour = negXNeighbour;
        }
        else {
            negativeXNeighbour = chunk[negX][(int) relativeLocation.y][(int) relativeLocation.z];
        }

        if (posY > Chunk.ySize - 1) {
            positiveYNeighbour = null;
        }
        else {
            positiveYNeighbour = chunk[(int) relativeLocation.x][posY][(int) relativeLocation.z];
        }

        if (negY < 0) {
            negativeYNeighbour = null;
        }
        else {
            negativeYNeighbour = chunk[(int) relativeLocation.x][negY][(int) relativeLocation.z];
        }

        if (posZ > Chunk.zSize - 1) {
            positiveZNeighbour = posYNeighbour;
        }
        else {
            positiveZNeighbour = chunk[(int) relativeLocation.x][(int) relativeLocation.y][posZ];
        }

        if (negZ < 0) {
            negativeZNeighbour = negYNeighbour;
        }
        else {
            negativeZNeighbour = chunk[(int) relativeLocation.x][(int) relativeLocation.y][negZ];
        }
    }

    public void determineVisibleFaces() {
        if (this.positiveXNeighbour == null || positiveXNeighbour.getName() == EBlockName.AIR) { this.positiveXFaceVisibility = true; }
        else { this.positiveXFaceVisibility = false; }

        if (this.negativeXNeighbour == null || negativeXNeighbour.getName() == EBlockName.AIR) { this.negativeXFaceVisibility = true; }
        else { this.negativeXFaceVisibility = false; }

        if (this.positiveYNeighbour == null || positiveYNeighbour.getName() == EBlockName.AIR) { this.positiveYFaceVisibility = true; }
        else { this.positiveYFaceVisibility = false; }

        if (this.negativeYNeighbour == null || negativeYNeighbour.getName() == EBlockName.AIR) { this.negativeYFaceVisibility = true; }
        else { this.negativeYFaceVisibility = false; }

        if (this.positiveZNeighbour == null || positiveZNeighbour.getName() == EBlockName.AIR) { this.positiveZFaceVisibility = true; }
        else { this.positiveZFaceVisibility = false; }

        if (this.negativeZNeighbour == null || negativeZNeighbour.getName() == EBlockName.AIR) { this.negativeZFaceVisibility = true; }
        else { this.negativeZFaceVisibility = false; }

        updateFaceArray();
        determineVisibleFaceTotal();
    }

    private void determineVisibleFaceTotal() {
        if (positiveXFaceVisibility && negativeXFaceVisibility && positiveYFaceVisibility && negativeYFaceVisibility && positiveZFaceVisibility && negativeZFaceVisibility) {
            visibleFace = true;
        }
        else {
            visibleFace = false;
        }
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

    public Vector3f getRelativeLocation() {
        return relativeLocation;
    }

    public EBlockName getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Block{" +
                "name=" + name +
                ", relativeLocation=" + relativeLocation +
                '}';
    }

    public void setPositiveYNeighbour(Block positiveYNeighbour) {
        this.positiveYNeighbour = positiveYNeighbour;
    }

    public void setNegativeYNeighbour(Block negativeYNeighbour) {
        this.negativeYNeighbour = negativeYNeighbour;
    }

    public void setPositiveXNeighbour(Block positiveXNeighbour) {
        this.positiveXNeighbour = positiveXNeighbour;
    }

    public void setNegativeXNeighbour(Block negativeXNeighbour) {
        this.negativeXNeighbour = negativeXNeighbour;
    }

    public void setPositiveZNeighbour(Block positiveZNeighbour) {
        this.positiveZNeighbour = positiveZNeighbour;
    }

    public void setNegativeZNeighbour(Block negativeZNeighbour) {
        this.negativeZNeighbour = negativeZNeighbour;
    }

    public boolean isVisibleFace() {
        return visibleFace;
    }
}