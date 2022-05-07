package ca.benbingham.game.generation.planetgeneration;

import ca.benbingham.game.gameclasses.renderers.subrenderers.DistantBodyRenderer;
import ca.benbingham.game.interstellarobjects.bodys.CosmicBody;

import java.util.ArrayList;

public class DistantBodyGenerationManager {
    private DistantBodyRenderer distantBodyRenderer;

    public DistantBodyGenerationManager(DistantBodyRenderer distantBodyRenderer) {
        this.distantBodyRenderer = distantBodyRenderer;
    }

    public void setBodies(ArrayList<CosmicBody> bodies) {
        distantBodyRenderer.setActiveBodies(bodies);
    }

    public void terminate() {

    }
}
