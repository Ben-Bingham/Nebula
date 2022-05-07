package ca.benbingham.game.planetstructure.blocks;

import ca.benbingham.engine.graphics.images.Image;
import ca.benbingham.game.planetstructure.enums.EBlockFaces;
import ca.benbingham.game.planetstructure.enums.EBlockName;

/**
 * Same as basic block except it can and should receive updates at fixed intervals.
 */

public class AdvancedBlock extends BasicBlock {
    public AdvancedBlock(EBlockName name, short ID, Image texture) {
        super(name, ID, texture);
    }

    public AdvancedBlock(EBlockName name, short ID, Image[] textures, EBlockFaces[] blockFaces) {
        super(name, ID, textures, blockFaces);
    }

    public void fixedUpdate() {

    }

    @Override
    public void randomUpdate() {

    }
}
