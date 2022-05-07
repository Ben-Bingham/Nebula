package ca.benbingham.game.gameclasses;

import ca.benbingham.engine.util.events.Event;
import ca.benbingham.engine.util.events.EventListener;
import ca.benbingham.game.events.states.*;
import ca.benbingham.game.gameclasses.renderers.Renderer;
import ca.benbingham.game.interstellarobjects.UniverseManager;

/**
 * This class handles more gameplay elements in comparison to the Game class, like: When to display certain menus, Saving, Loading, and more...
 */

public class GameManager implements EventListener {

    private UniverseManager universeManager;
    public Renderer renderer;
    public Game game;

    public GameManager(Game game) {
        this.game = game;
        renderer = game.renderer;
        universeManager = new UniverseManager(this);
    }

    private void init() {
        universeManager.init();
    }

    private void firstUpdate() {
        universeManager.update();
    }

    private void update() {
        renderer.render();
    }

    private void lastUpdate() {

    }

    private void terminate() {

    }

    @Override
    public void receiveEvents(Event event) {
        if (event instanceof Init) {
            init();
        }
        else if (event instanceof FirstUpdate) {
            firstUpdate();
        }
        else if (event instanceof Update) {
            update();
        }
        else if (event instanceof LastUpdate) {
            lastUpdate();
        }
        else if (event instanceof Terminate) {
            terminate();
        }
    }
}
