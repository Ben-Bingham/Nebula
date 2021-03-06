package ca.benbingham.game.planetstructure.blocks;

import ca.benbingham.engine.graphics.images.Image;
import ca.benbingham.engine.graphics.images.TextureAtlas;
import ca.benbingham.game.planetstructure.enums.EBlockFaces;
import ca.benbingham.game.planetstructure.enums.EBlockName;


import java.util.ArrayList;

import static ca.benbingham.engine.graphics.images.enums.EImageModes.*;
import static ca.benbingham.engine.util.Printing.print;
import static ca.benbingham.game.planetstructure.enums.EBlockFaces.*;

public class BlockList {
    private static short nextID = 0;

    public static Block AIR;
    public static Block STONE;
    public static Block GRASS;
    public static Block DIRT;
    public static Block PLANET_CORE;
    public static Block RED;
    public static Block BLUE;
    public static Block GREEN;
    public static Block YELLOW;

    private final ArrayList<Block> blockList;

    private TextureAtlas textureAtlas;

    public BlockList() {
        this.blockList = new ArrayList<>();
    }

    public void init() {
        blockList.add(AIR = new Block(EBlockName.AIR, getNextID(), new Image("textures/blocks/air.png", NEBULA_BUFFERED_IMAGE, true)));

        blockList.add(STONE = new Block(EBlockName.STONE, getNextID(), new Image("textures/blocks/stone.png", NEBULA_BUFFERED_IMAGE, true)));
        blockList.add(GRASS = new Block(EBlockName.GRASS, getNextID(),
                new Image[] {
                        new Image("textures/blocks/grass_side.png", NEBULA_BUFFERED_IMAGE, true),
                        new Image("textures/blocks/grass_top.png", NEBULA_BUFFERED_IMAGE, true),
                        new Image("textures/blocks/dirt.png", NEBULA_BUFFERED_IMAGE, true)
                },
                new EBlockFaces[] {
                        NEBULA_FRONT_BACK_LEFT_RIGHT,
                        NEBULA_TOP_FACE,
                        NEBULA_BOTTOM_FACE
                }));
        blockList.add(DIRT = new Block(EBlockName.DIRT, getNextID(), new Image("textures/blocks/dirt.png", NEBULA_BUFFERED_IMAGE, true)));
        blockList.add(PLANET_CORE = new Block(EBlockName.PLANET_CORE, getNextID(), new Image("textures/blocks/planet_core.png", NEBULA_BUFFERED_IMAGE, true)));
        blockList.add(RED = new Block(EBlockName.RED, getNextID(), new Image("textures/blocks/TestBlocks/RedTestTexture.png", NEBULA_BUFFERED_IMAGE, true)));
        blockList.add(BLUE = new Block(EBlockName.BLUE, getNextID(), new Image("textures/blocks/TestBlocks/BlueTestTexture.png", NEBULA_BUFFERED_IMAGE, true)));
        blockList.add(GREEN = new Block(EBlockName.GREEN, getNextID(), new Image("textures/blocks/TestBlocks/GreenTestTexture.png", NEBULA_BUFFERED_IMAGE, true)));
        blockList.add(YELLOW = new Block(EBlockName.YELLOW, getNextID(), new Image("textures/blocks/TestBlocks/YellowTestTexture.png", NEBULA_BUFFERED_IMAGE, true)));

        initializeTextureAtlas();
    }

    private void initializeTextureAtlas() {
        ArrayList<Image> textures = new ArrayList<>();

        for (Block block : blockList) {
            if (!textures.contains(block.getRenderingData().getFaces()[0].getImage())) {
                textures.add(block.getRenderingData().getFaces()[0].getImage());
            }
            if (!textures.contains(block.getRenderingData().getFaces()[1].getImage())) {
                textures.add(block.getRenderingData().getFaces()[1].getImage());
            }
            if (!textures.contains(block.getRenderingData().getFaces()[2].getImage())) {
                textures.add(block.getRenderingData().getFaces()[2].getImage());
            }
            if (!textures.contains(block.getRenderingData().getFaces()[3].getImage())) {
                textures.add(block.getRenderingData().getFaces()[3].getImage());
            }
            if (!textures.contains(block.getRenderingData().getFaces()[4].getImage())) {
                textures.add(block.getRenderingData().getFaces()[4].getImage());
            }
            if (!textures.contains(block.getRenderingData().getFaces()[5].getImage())) {
                textures.add(block.getRenderingData().getFaces()[5].getImage());
            }
        }

        Image[] texturesArray = new Image[textures.size()];
        texturesArray = textures.toArray(texturesArray);

        textureAtlas = new TextureAtlas(texturesArray, "Texture-Atlas");

        for (Block block : blockList) {
            block.getRenderingData().setTexCordData(textureAtlas);
            block.getRenderingData().importGeometricData();
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
