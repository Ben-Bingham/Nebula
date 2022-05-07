package ca.benbingham.game.planetstructure.blocks;

import ca.benbingham.engine.graphics.images.Image;
import ca.benbingham.game.planetstructure.enums.EBlockFaces;
import ca.benbingham.game.planetstructure.enums.EBlockName;

/**
 * Same as a default block except it can and should receive random updates.
 */

public class BasicBlock extends Block {
    public BasicBlock(EBlockName name, short ID, Image texture) {
        super(name, ID, texture);
    }

    public BasicBlock(EBlockName name, short ID, Image[] textures, EBlockFaces[] blockFaces) {
        super(name, ID, textures, blockFaces);
    }

    public void randomUpdate() {

    }
}
