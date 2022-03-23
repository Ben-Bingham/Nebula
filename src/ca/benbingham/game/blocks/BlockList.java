package ca.benbingham.game.blocks;

import ca.benbingham.game.planetstructure.Block;
import ca.benbingham.game.planetstructure.enums.EBlockName;

import java.util.ArrayList;

public class BlockList {
    private static short nextID = 0;

    public static Block AIR;
    public static Block STONE;

    private final ArrayList<Block> blockList;

    public BlockList() {
        this.blockList = new ArrayList<>();
    }

    public void init() {
        blockList.add(AIR = new Block(EBlockName.AIR, getNextID()));
        blockList.add(STONE = new Block(EBlockName.STONE, getNextID()));
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
//    public static Block AIR = new Block(EBlockName.AIR);
//    public static Block STONE = new Block(EBlockName.STONE);
}
