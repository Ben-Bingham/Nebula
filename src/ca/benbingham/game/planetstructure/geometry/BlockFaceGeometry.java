package ca.benbingham.game.planetstructure.geometry;

public class BlockFaceGeometry {
    // ------------------- Positions -------------------

    public static final float[] positiveXFacePositions = new float[]{
            0.5f,  0.5f,  0.5f,   // top-left
            0.5f,  0.5f, -0.5f,   // top-right
            0.5f, -0.5f, -0.5f,   // bottom-right
            0.5f, -0.5f,  0.5f,    // bottom-left
    };

    public static final float[] negativeXFacePositions = new float[]{
            -0.5f,  0.5f, -0.5f,   // top-left
            -0.5f,  0.5f,  0.5f,   // top-right
            -0.5f, -0.5f,  0.5f,   // bottom-right
            -0.5f, -0.5f, -0.5f,   // bottom-left
    };

    public static final float[] positiveYFacePositions = new float[]{
            -0.5f,  0.5f, -0.5f,   // top-left
             0.5f,  0.5f, -0.5f,  // top-right
             0.5f,  0.5f,  0.5f,  // bottom-right
            -0.5f,  0.5f,  0.5f,   // bottom-left
    };

    public static final float[] negativeYFacePositions = new float[]{
             0.5f, -0.5f, -0.5f,  // top-left
            -0.5f, -0.5f, -0.5f,  // top-right
            -0.5f, -0.5f,  0.5f,  // bottom-right
             0.5f, -0.5f,  0.5f,  // bottom-left
    };

    public static final float[] positiveZFacePositions = new float[]{
            -0.5f,  0.5f,  0.5f,  // top-left
             0.5f,  0.5f,  0.5f,  // top-right
             0.5f, -0.5f,  0.5f,  // bottom-right
            -0.5f, -0.5f,  0.5f,  // bottom-left
    };

    public static final float[] negativeZFacePositions = new float[]{
             0.5f, -0.5f, -0.5f,  // bottom-right
             0.5f,  0.5f, -0.5f,  // top-right
            -0.5f,  0.5f, -0.5f, // top-left
            -0.5f, -0.5f, -0.5f,  // Bottom-left
    };

    // ------------------- Normals -------------------

    public static final float[] positiveXFaceNormals = new float[]{
            1.0f, 0.0f, 0.0f, // top-left
            1.0f, 0.0f, 0.0f, // top-right
            1.0f, 0.0f, 0.0f, // bottom-right
            1.0f, 0.0f, 0.0f, // bottom-left
    };

    public static final float[] negativeXFaceNormals = new float[]{
            -1.0f, 0.0f, 0.0f, // top-left
            -1.0f, 0.0f, 0.0f, // top-right
            -1.0f, 0.0f, 0.0f, // bottom-right
            -1.0f, 0.0f, 0.0f, // bottom-left
    };

    public static final float[] positiveYFaceNormals = new float[]{
            0.0f, 1.0f, 0.0f, // top-left
            0.0f, 1.0f, 0.0f, // top-right
            0.0f, 1.0f, 0.0f, // bottom-right
            0.0f, 1.0f, 0.0f, // bottom-left
    };

    public static final float[] negativeYFaceNormals = new float[]{
            0.0f, -1.0f, 0.0f, // top-left
            0.0f, -1.0f, 0.0f, // top-right
            0.0f, -1.0f, 0.0f, // bottom-right
            0.0f, -1.0f, 0.0f, // bottom-left
    };

    public static final float[] positiveZFaceNormals = new float[]{
            0.0f, 0.0f, 1.0f, // top-left
            0.0f, 0.0f, 1.0f, // top-right
            0.0f, 0.0f, 1.0f, // bottom-right
            0.0f, 0.0f, 1.0f, // bottom-left
    };

    public static final float[] negativeZFaceNormals = new float[]{
            0.0f, 0.0f, -1.0f, // bottom-right
            0.0f, 0.0f, -1.0f, // top-right
            0.0f, 0.0f, -1.0f, // top-left
            0.0f, 0.0f, -1.0f, // Bottom-left
    };
}
