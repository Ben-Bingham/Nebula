package ca.benbingham.game.interstellarobjects;

import ca.benbingham.game.gameclasses.GameManager;
import ca.benbingham.game.gameclasses.Settings;
import ca.benbingham.game.gameclasses.renderers.Renderer;
import ca.benbingham.game.generation.universegeneration.UniverseGenerator;
import ca.benbingham.game.interstellarobjects.bodys.CosmicBody;

import java.util.ArrayList;

import static ca.benbingham.engine.util.math.Vector3fMath.distanceBetween;

/**
 * This class specifically controls Universe related things such as: Generating planets, rendering planets differently at different distances and more...
 */

public class UniverseManager {
    private GameManager manager;
    private Renderer renderer;

    private Universe universe;
    private CosmicBody activeBody;
    private ArrayList<CosmicBody> distantBodies;

    public UniverseManager(GameManager manager) {
        this.manager = manager;
        this.renderer = manager.renderer;

        distantBodies = new ArrayList<>();
    }

    public void init() {
        universe = new UniverseGenerator(1, 1, 1).createUniverse();
    }

    public void update() {
        CosmicBody cosmicBody;
        for (int i = 0; i < universe.getAllBodies().size(); i++) {
            cosmicBody = universe.getAllBodies().get(i);

            if (distanceBetween(cosmicBody.globalPosition, manager.game.player.getPosition()) < Settings.PLANET_RENDER_DISTANCE) {
                distantBodies.add(cosmicBody);
            }
            else {
                distantBodies.remove(cosmicBody); //TODO might not work
            }

            if (i == 1) {
                activeBody = cosmicBody;
            }
        }

        if (renderer.getActiveBody() != activeBody) {
            renderer.setActiveBody(activeBody, manager.game.player.getPosition());
        }

        renderer.setDistantActiveBodies(distantBodies);
    }
}
