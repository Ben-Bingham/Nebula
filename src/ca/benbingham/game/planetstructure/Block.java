package ca.benbingham.game.planetstructure;

import ca.benbingham.engine.images.Image;
import ca.benbingham.engine.images.TextureAtlas;
import ca.benbingham.game.planetstructure.enums.EBlockFaces;
import ca.benbingham.game.planetstructure.enums.EBlockName;

import java.util.Arrays;

import static ca.benbingham.engine.util.Printing.print;

public class Block {
    private final short ID;
    private final EBlockName name;
    private Image[] textures;

    private final int numberOfFaces = 6;

    private BlockFace[] faces;

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

//    private final Quad posXFace = new Quad();
//    private final Quad negXFace = new Quad();
//    private final Quad posYFace = new Quad();
//    private final Quad negYFace = new Quad();
//    private final Quad posZFace = new Quad();
//    private final Quad negZFace = new Quad();

    private BlockFace posXFace = new BlockFace();
    private BlockFace negXFace = new BlockFace();
    private BlockFace posYFace = new BlockFace();
    private BlockFace negYFace = new BlockFace();
    private BlockFace posZFace = new BlockFace();
    private BlockFace negZFace = new BlockFace();

    public Block(EBlockName name, short ID, Image texture) {
        this.name = name;
        this.ID = ID;

        this.faces = new BlockFace[numberOfFaces];
        for (int i = 0; i < faces.length; i++) {
            faces[i] = new BlockFace();
        }

        for (BlockFace face : faces) {
            face.setImage(texture);
        }
    }

    public Block(EBlockName name, short ID, Image texture, Image texture2, EBlockFaces face2) {
        this.name = name;
        this.ID = ID;

        this.faces = new BlockFace[numberOfFaces];
        for (int i = 0; i < faces.length; i++) {
            faces[i] = new BlockFace();
        }

        for (BlockFace face : faces) {
            face.setImage(texture);
        }

        faces[face2.getValue()].setImage(texture2);
    }

    public Block(EBlockName name, short ID, Image texture, Image texture2, EBlockFaces face2, Image texture3, EBlockFaces face3) {
        this.name = name;
        this.ID = ID;

        this.faces = new BlockFace[numberOfFaces];
        for (int i = 0; i < faces.length; i++) {
            faces[i] = new BlockFace();
        }

        for (BlockFace face : faces) {
            face.setImage(texture);
        }

        faces[face2.getValue()].setImage(texture2);
        faces[face3.getValue()].setImage(texture3);

    }

    public Block(EBlockName name, short ID, Image texture, Image texture2, EBlockFaces face2, Image texture3, EBlockFaces face3, Image texture4, EBlockFaces face4) {
        this.name = name;
        this.ID = ID;

        this.faces = new BlockFace[numberOfFaces];
        for (int i = 0; i < faces.length; i++) {
            faces[i] = new BlockFace();
        }

        for (int i = 0; i < faces.length; i++) {
            faces[i].setImage(texture);
        }

        faces[face2.getValue()].setImage(texture2);
        faces[face3.getValue()].setImage(texture3);
        faces[face4.getValue()].setImage(texture4);
    }

    public Block(EBlockName name, short ID, Image texture, Image texture2, EBlockFaces face2, Image texture3, EBlockFaces face3, Image texture4, EBlockFaces face4, Image texture5, EBlockFaces face5) {
        this.name = name;
        this.ID = ID;

        this.faces = new BlockFace[numberOfFaces];
        for (int i = 0; i < faces.length; i++) {
            faces[i] = new BlockFace();
        }

        for (int i = 0; i < faces.length; i++) {
            faces[i].setImage(texture);
        }

        faces[face2.getValue()].setImage(texture2);
        faces[face3.getValue()].setImage(texture3);
        faces[face4.getValue()].setImage(texture4);
        faces[face5.getValue()].setImage(texture5);
    }

    public Block(EBlockName name, short ID, Image texture1, EBlockFaces face1, Image texture2, EBlockFaces face2, Image texture3, EBlockFaces face3, Image texture4, EBlockFaces face4, Image texture5, EBlockFaces face5, Image texture6, EBlockFaces face6) {
        this.name = name;
        this.ID = ID;

        this.faces = new BlockFace[numberOfFaces];
        for (int i = 0; i < faces.length; i++) {
            faces[i] = new BlockFace();
        }

        faces[face1.getValue()].setImage(texture1);
        faces[face2.getValue()].setImage(texture2);
        faces[face3.getValue()].setImage(texture3);
        faces[face4.getValue()].setImage(texture4);
        faces[face5.getValue()].setImage(texture5);
        faces[face6.getValue()].setImage(texture6);

        for (Image instance : textures) {
            if (instance == null) {
                throw new RuntimeException("Tried individually specifying a texture for each face but a face was specified multiple times.");
            }
        }
    }

    public void setTexCordData(TextureAtlas atlas) {
//        for (int i = 0; i < atlas.getImages().length; i++) {
//            for (int k = 0; k < numberOfFaces; k++) {
//                if (atlas.getImages()[i] == faces[i].getImage()) {
//                    faces[i].setTexCords(atlas.getTexCords()[i]);
//                }
//            }
//        }

//        for (int i = 0; i < atlas.getImages().length; i++) {
//            for (int j = 0; j < numberOfFaces; j++) {
//                if (atlas.getImages()[i] == faces[j].getImage()) {
//                    faces[j].setTexCords(atlas.getTexCords()[i]);
//                }
//            }
//        }

        for (int i = 0; i < numberOfFaces; i++) {
            for (int j = 0; j < atlas.getImages().length; j++) {
                if (faces[i].getImage() == atlas.getImages()[j]) {
                    faces[i].setTexCords(atlas.getTexCords()[j]);
                }
            }
        }
    }

    public void importGeometricData() {
        faces[0].getQuad().importData(positiveXFace, faces[0].getTexCords());
        faces[1].getQuad().importData(negativeXFace, faces[1].getTexCords());
        faces[2].getQuad().importData(positiveYFace, faces[2].getTexCords());
        faces[3].getQuad().importData(negativeYFace, faces[3].getTexCords());
        faces[4].getQuad().importData(positiveZFace, faces[4].getTexCords());
        faces[5].getQuad().importData(negativeZFace, faces[5].getTexCords());
        updateFaceVariables();

        for (BlockFace face : faces) {
            face.getQuad().createFloatArrayForQuad();
        }
    }

    public void updateFaceVariables() {
        posXFace = faces[0];
        negXFace = faces[1];
        posYFace = faces[2];
        negYFace = faces[3];
        posZFace = faces[4];
        negZFace = faces[5];
    }

    public BlockFace[] getFaces() {
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

    public Image[] getTextures() {
        return textures;
    }
}