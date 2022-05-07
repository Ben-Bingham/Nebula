package ca.benbingham.engine.graphics.renderingdata;

import ca.benbingham.engine.graphics.images.Image;
import ca.benbingham.engine.graphics.images.TextureAtlas;
import ca.benbingham.game.planetstructure.BlockFace;
import ca.benbingham.game.planetstructure.enums.EBlockFaces;
import ca.benbingham.game.planetstructure.geometry.BlockFaceGeometry;

import static ca.benbingham.engine.util.Util.contains;
import static ca.benbingham.game.planetstructure.enums.EBlockFaces.*;

public class BlockRenderingData {
    private final int NUMBER_OF_FACES = 6;
    private BlockFace[] faces;

    public BlockRenderingData(Image texture) {
        this.faces = new BlockFace[NUMBER_OF_FACES];
        for (int i = 0; i < faces.length; i++) {
            faces[i] = new BlockFace();
        }

        for (BlockFace face : faces) {
            face.setImage(texture);
        }
    }

    public BlockRenderingData(Image[] textures, EBlockFaces[] blockFaces) {
        if (textures.length > blockFaces.length) throw new IllegalArgumentException("Too many textures / not enough faces given.");
        if (textures.length < blockFaces.length) throw new IllegalArgumentException("Too many faces / not enough textures given.");
        if (contains(blockFaces, NEBULA_ALL_FACES)) throw new IllegalArgumentException("Wrong constructor used to set all faces to the same texture.");


        this.faces = new BlockFace[NUMBER_OF_FACES];

        for (int i = 0; i < this.faces.length; i++) {
            this.faces[i] = new BlockFace();
        }

        for (int i = 0; i < blockFaces.length; i++) {
            if (blockFaces[i] == NEBULA_ALL_FACES) {
                for (BlockFace face : faces) {
                    face.setImage(textures[i]);
                }
            }
            else if (blockFaces[i] == NEBULA_FRONT_BACK_LEFT_RIGHT) {
                faces[NEBULA_FRONT_FACE.getValue()].setImage(textures[i]);
                faces[NEBULA_BACK_FACE.getValue()].setImage(textures[i]);
                faces[NEBULA_LEFT_FACE.getValue()].setImage(textures[i]);
                faces[NEBULA_RIGHT_FACE.getValue()].setImage(textures[i]);
            }
            else if (blockFaces[i] == NEBULA_FRONT_FACE) {
                faces[NEBULA_FRONT_FACE.getValue()].setImage(textures[i]);
            }
            else if (blockFaces[i] == NEBULA_BACK_FACE) {
                faces[NEBULA_BACK_FACE.getValue()].setImage(textures[i]);
            }
            else if (blockFaces[i] == NEBULA_TOP_FACE) {
                faces[NEBULA_TOP_FACE.getValue()].setImage(textures[i]);
            }
            else if (blockFaces[i] == NEBULA_BOTTOM_FACE) {
                faces[NEBULA_BOTTOM_FACE.getValue()].setImage(textures[i]);
            }
            else if (blockFaces[i] == NEBULA_RIGHT_FACE) {
                faces[NEBULA_RIGHT_FACE.getValue()].setImage(textures[i]);
            }
            else if (blockFaces[i] == NEBULA_LEFT_FACE) {
                faces[NEBULA_LEFT_FACE.getValue()].setImage(textures[i]);
            }
        }
    }

    public void setTexCordData(TextureAtlas atlas) {
        for (int i = 0; i < NUMBER_OF_FACES; i++) {
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
}
