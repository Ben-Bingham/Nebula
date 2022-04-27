package ca.benbingham.game.planetstructure.geometry;

public class BlockFaceGeometry {
    public static final float[] positiveXFace = new float[]{
            0.5f,  0.5f,  0.5f,   // top-left
            0.5f,  0.5f, -0.5f,   // top-right
            0.5f, -0.5f, -0.5f,   // bottom-right
            0.5f, -0.5f,  0.5f,    // bottom-left
    };

    public static final float[] negativeXFace = new float[]{
            -0.5f,  0.5f, -0.5f,   // top-left
            -0.5f,  0.5f,  0.5f,   // top-right
            -0.5f, -0.5f,  0.5f,   // bottom-right
            -0.5f, -0.5f, -0.5f,   // bottom-left
    };

    public static final float[] positiveYFace = new float[]{
            -0.5f,  0.5f, -0.5f,   // top-left
             0.5f,  0.5f, -0.5f,  // top-right
             0.5f,  0.5f,  0.5f,  // bottom-right
            -0.5f,  0.5f,  0.5f,   // bottom-left
    };

    public static final float[] negativeYFace = new float[]{
             0.5f, -0.5f, -0.5f,  // top-left
            -0.5f, -0.5f, -0.5f,  // top-right
            -0.5f, -0.5f,  0.5f,  // bottom-right
             0.5f, -0.5f,  0.5f,  // bottom-left
    };

    public static final float[] positiveZFace = new float[]{
            -0.5f,  0.5f,  0.5f,  // top-left
             0.5f,  0.5f,  0.5f,  // top-right
             0.5f, -0.5f,  0.5f,  // bottom-right
            -0.5f, -0.5f,  0.5f,  // bottom-left
    };

    public static final float[] negativeZFace = new float[]{
             0.5f, -0.5f, -0.5f,  // bottom-right
             0.5f,  0.5f, -0.5f,  // top-right
            -0.5f,  0.5f, -0.5f, // top-left
            -0.5f, -0.5f, -0.5f,  // Bottom-left
    };
}
