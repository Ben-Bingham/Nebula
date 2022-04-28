package ca.benbingham.game.planetstructure.blocks;

import ca.benbingham.engine.graphics.renderingdata.BlockRenderingData;
import ca.benbingham.engine.images.Image;
import ca.benbingham.engine.images.TextureAtlas;
import ca.benbingham.game.planetstructure.BlockFace;
import ca.benbingham.game.planetstructure.enums.EBlockFaces;
import ca.benbingham.game.planetstructure.enums.EBlockName;
import ca.benbingham.game.planetstructure.geometry.BlockFaceGeometry;

import static ca.benbingham.engine.util.Printing.print;
import static ca.benbingham.game.planetstructure.enums.EBlockFaces.NEBULA_LEFT_FACE;

/**
 * This class handles exclusively the geometric and texture data related to all blocks
 */

public class Block {
    private final short ID;
    private final EBlockName name;
//    private final BlockRenderingData renderingData;

    private final int numberOfFaces = 6;

    private BlockFace[] faces;

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

        for (BlockFace instance : faces) {
            if (instance.getImage() == null) {
                throw new RuntimeException("Tried individually specifying a texture for each face but a face was specified multiple times.");
            }
        }
    }

    public void setTexCordData(TextureAtlas atlas) {
        for (int i = 0; i < numberOfFaces; i++) {
            for (int j = 0; j < atlas.getImages().length; j++) {
                if (faces[i].getImage() == atlas.getImages()[j]) {
                    if (i == NEBULA_LEFT_FACE.getValue()) {
                        faces[i].setTexCords(0, atlas.getTexCords()[j][6]);
                        faces[i].setTexCords(1, atlas.getTexCords()[j][7]);
                        faces[i].setTexCords(2, atlas.getTexCords()[j][0]);
                        faces[i].setTexCords(3, atlas.getTexCords()[j][1]);
                        faces[i].setTexCords(4, atlas.getTexCords()[j][2]);
                        faces[i].setTexCords(5, atlas.getTexCords()[j][3]);
                        faces[i].setTexCords(6, atlas.getTexCords()[j][4]);
                        faces[i].setTexCords(7, atlas.getTexCords()[j][5]);
                    }
                    else {
                        faces[i].setTexCords(atlas.getTexCords()[j]);
                    }
                }
            }
        }
    }

    public void importGeometricData() {
        faces[0].getQuad().importData(BlockFaceGeometry.positiveXFacePositions, faces[0].getTexCords(),BlockFaceGeometry.positiveXFaceNormals);
        faces[1].getQuad().importData(BlockFaceGeometry.negativeXFacePositions, faces[1].getTexCords(),BlockFaceGeometry.negativeXFaceNormals);
        faces[2].getQuad().importData(BlockFaceGeometry.positiveYFacePositions, faces[2].getTexCords(),BlockFaceGeometry.positiveYFaceNormals);
        faces[3].getQuad().importData(BlockFaceGeometry.negativeYFacePositions, faces[3].getTexCords(),BlockFaceGeometry.negativeYFaceNormals);
        faces[4].getQuad().importData(BlockFaceGeometry.positiveZFacePositions, faces[4].getTexCords(),BlockFaceGeometry.positiveZFaceNormals);
        faces[5].getQuad().importData(BlockFaceGeometry.negativeZFacePositions, faces[5].getTexCords(),BlockFaceGeometry.negativeZFaceNormals);

        for (BlockFace face : faces) {
            face.getQuad().createFloatArrayForQuad();
        }
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
}