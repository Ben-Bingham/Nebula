package ca.benbingham.game.planetstructure.blocks;

import ca.benbingham.engine.images.Image;
import ca.benbingham.game.planetstructure.enums.EBlockFaces;
import ca.benbingham.game.planetstructure.enums.EBlockName;

/**
 * Same as a default block except it can and should receive random updates.
 */

public class BasicBlock extends Block {
    public BasicBlock(EBlockName name, short ID, Image texture) {
        super(name, ID, texture);
    }

    public BasicBlock(EBlockName name, short ID, Image texture, Image texture2, EBlockFaces face2) {
        super(name, ID, texture, texture2, face2);
    }

    public BasicBlock(EBlockName name, short ID, Image texture, Image texture2, EBlockFaces face2, Image texture3, EBlockFaces face3) {
        super(name, ID, texture, texture2, face2, texture3, face3);
    }

    public BasicBlock(EBlockName name, short ID, Image texture, Image texture2, EBlockFaces face2, Image texture3, EBlockFaces face3, Image texture4, EBlockFaces face4) {
        super(name, ID, texture, texture2, face2, texture3, face3, texture4, face4);
    }

    public BasicBlock(EBlockName name, short ID, Image texture, Image texture2, EBlockFaces face2, Image texture3, EBlockFaces face3, Image texture4, EBlockFaces face4, Image texture5, EBlockFaces face5) {
        super(name, ID, texture, texture2, face2, texture3, face3, texture4, face4, texture5, face5);
    }

    public BasicBlock(EBlockName name, short ID, Image texture1, EBlockFaces face1, Image texture2, EBlockFaces face2, Image texture3, EBlockFaces face3, Image texture4, EBlockFaces face4, Image texture5, EBlockFaces face5, Image texture6, EBlockFaces face6) {
        super(name, ID, texture1, face1, texture2, face2, texture3, face3, texture4, face4, texture5, face5, texture6, face6);
    }

    public void randomUpdate() {

    }
}
