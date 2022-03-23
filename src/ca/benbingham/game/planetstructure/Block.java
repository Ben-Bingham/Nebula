package ca.benbingham.game.planetstructure;


import ca.benbingham.engine.util.Timer;
import ca.benbingham.game.Quad;
import ca.benbingham.game.planetstructure.enums.EBlockName;
import org.joml.Vector2i;
import org.joml.Vector3f;

import static ca.benbingham.engine.util.Printing.print;

public class Block {
    private final short ID;
    private EBlockName name;
    private String texture; //TODO

    private final int numberOfFaces = 6;

    private final Quad[] faces;

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

    private final Quad posXFace = new Quad();
    private final Quad negXFace = new Quad();
    private final Quad posYFace = new Quad();
    private final Quad negYFace = new Quad();
    private final Quad posZFace = new Quad();
    private final Quad negZFace = new Quad();

    public Block(EBlockName name, short ID) {
        this.name = name;
        this.ID = ID;

        this.faces = new Quad[numberOfFaces];
        for (int i = 0; i < faces.length; i++) {
            faces[i] = new Quad();
        }

        posXFace.importData(positiveXFace, positiveXFaceTexCords);
        negXFace.importData(negativeXFace, negativeXFaceTexCords);
        posYFace.importData(positiveYFace, positiveYFaceTexCords);
        negYFace.importData(negativeYFace, negativeYFaceTexCords);
        posZFace.importData(positiveZFace, positiveZFaceTexCords);
        negZFace.importData(negativeZFace, negativeZFaceTexCords);
        updateFaceArray();

        for (Quad face : faces) {
            face.createFloatArrayForQuad();
        }
    }

    public void updateFaceArray() {
        faces[0] = posXFace;
        faces[1] = negXFace;
        faces[2] = posYFace;
        faces[3] = negYFace;
        faces[4] = posZFace;
        faces[5] = negZFace;
    }

    public Quad[] getFaces() {
        return faces;
    }

    public EBlockName getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Block{" +
                "name=" + name +
                '}';
    }

    public short getID() {
        return ID;
    }
}