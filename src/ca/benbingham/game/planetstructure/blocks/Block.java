package ca.benbingham.game.planetstructure.blocks;

import ca.benbingham.engine.graphics.renderingdata.BlockRenderingData;
import ca.benbingham.engine.graphics.images.Image;
import ca.benbingham.game.planetstructure.enums.EBlockFaces;
import ca.benbingham.game.planetstructure.enums.EBlockName;

public class Block {
    private final short ID;
    private final EBlockName name;
    private BlockRenderingData renderingData;

    public Block(EBlockName name, short ID, Image texture) {
        this.name = name;
        this.ID = ID;
        this.renderingData = new BlockRenderingData(texture);
    }

    public Block(EBlockName name, short ID, Image[] texture, EBlockFaces[] blockFaces) {
        this.name = name;
        this.ID = ID;
        this.renderingData = new BlockRenderingData(texture, blockFaces);
    }

    public EBlockName getName() {
        return name;
    }

    public BlockRenderingData getRenderingData() {
        return renderingData;
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