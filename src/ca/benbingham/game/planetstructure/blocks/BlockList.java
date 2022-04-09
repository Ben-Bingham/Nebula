package ca.benbingham.game.planetstructure.blocks;

import ca.benbingham.engine.images.Image;
import ca.benbingham.engine.images.TextureAtlas;
import ca.benbingham.game.planetstructure.enums.EBlockName;


import java.util.ArrayList;

import static ca.benbingham.engine.images.EImageModes.*;
import static ca.benbingham.engine.util.Printing.print;
import static ca.benbingham.game.planetstructure.enums.EBlockFaces.*;

public class BlockList {
    private static short nextID = 0;

    public static Block AIR;
    public static Block STONE;
    public static Block GRASS;
    public static Block DIRT;
    public static Block BEDROCK;

    private final ArrayList<Block> blockList;

    private TextureAtlas textureAtlas;

    public BlockList() {
        this.blockList = new ArrayList<>();
    }

    public void init() {
        blockList.add(AIR = new Block(EBlockName.AIR, getNextID(), new Image("textures/blocks/air.png", NEBULA_BUFFERED_IMAGE, true)));

        blockList.add(STONE = new Block(EBlockName.STONE, getNextID(), new Image("textures/blocks/stone.png", NEBULA_BUFFERED_IMAGE, true)));
        blockList.add(GRASS = new Block(EBlockName.GRASS, getNextID(), new Image("textures/blocks/grass_side.png", NEBULA_BUFFERED_IMAGE, true), new Image("textures/blocks/grass_top.png", NEBULA_BUFFERED_IMAGE, true), NEBULA_TOP_FACE, new Image("textures/blocks/dirt.png", NEBULA_BUFFERED_IMAGE, true), NEBULA_BOTTOM_FACE));
        blockList.add(DIRT = new Block(EBlockName.DIRT, getNextID(), new Image("textures/blocks/dirt.png", NEBULA_BUFFERED_IMAGE, true)));
        blockList.add(BEDROCK = new Block(EBlockName.BEDROCK, getNextID(), new Image("textures/blocks/bedrock.png", NEBULA_BUFFERED_IMAGE, true)));

        ArrayList<Image> textures = new ArrayList<>();

        for (Block block : blockList) {
            textures.add(block.getFaces()[0].getImage());
            textures.add(block.getFaces()[1].getImage());
            textures.add(block.getFaces()[2].getImage());
            textures.add(block.getFaces()[3].getImage());
            textures.add(block.getFaces()[4].getImage());
            textures.add(block.getFaces()[5].getImage());
        }

        Image[] texture = new Image[textures.size()];
        texture = textures.toArray(texture);

        textureAtlas = new TextureAtlas(texture, "Texture-Atlas");

        for (Block block : blockList) {
            block.setTexCordData(textureAtlas);
            block.importGeometricData();
        }
    }

    public short getNextID() {
        short val = nextID;
        nextID++;
        return val;
    }

    public short getBlockIDWithName(EBlockName name) {
        for (Block block : blockList) {
            if (block.getName() == name) {
                return block.getID();
            }
        }
        return -1;
    }

    public Block getBlockWithID(short ID) {
        for (Block block : blockList) {
            if (block.getID() == ID) {
                return block;
            }
        }
        return null;
    }
}
