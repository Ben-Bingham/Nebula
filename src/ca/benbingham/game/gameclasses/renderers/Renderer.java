package ca.benbingham.game.gameclasses.renderers;

import ca.benbingham.game.events.ChunkCrossing;
import ca.benbingham.engine.util.events.Event;
import ca.benbingham.engine.util.events.EventListener;
import ca.benbingham.game.events.KeyboardPress;
import ca.benbingham.game.events.states.*;
import ca.benbingham.game.gameclasses.Game;
import ca.benbingham.game.interstellarobjects.bodys.CosmicBody;
import ca.benbingham.game.generation.planetgeneration.ChunkGenerationManager;
import ca.benbingham.game.generation.planetgeneration.DistantBodyGenerationManager;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class Renderer implements EventListener {
    private Game game;

    private CosmicBody activeBody;
    private ArrayList<CosmicBody> distantActiveBodies;
    private ChunkGenerationManager chunkGenerationManager;
    private DistantBodyGenerationManager distantBodyGenerationManager;
    private MasterRenderer masterRenderer;

    public Renderer(Game game) {
        this.game = game;

        masterRenderer = new MasterRenderer(this.game);
        masterRenderer.init();

        chunkGenerationManager = new ChunkGenerationManager( masterRenderer.chunkRenderer, this.game.getMasterBlockList());
        distantBodyGenerationManager = new DistantBodyGenerationManager(masterRenderer.distantBodyRenderer);

        distantActiveBodies = new ArrayList<>();
    }

    public void render() {
        chunkGenerationManager.update();
        distantBodyGenerationManager.setBodies(distantActiveBodies);
    }

    @Override
    public void receiveEvents(Event event) {
        if (event instanceof ChunkCrossing) {
            chunkGenerationManager.crossChunk(((ChunkCrossing) event).newPlayerChunkCords);
        }
        else if (event instanceof Terminate) {
            terminate();
        }
        else if (event instanceof FirstUpdate) {
            masterRenderer.firstUpdate();
        }
        else if (event instanceof LastUpdate) {
            masterRenderer.lastUpdate();
        }
        else if (event instanceof KeyboardPress) {
            masterRenderer.debugRenderer.processInput((KeyboardPress) event);
        }
    }

    public void terminate() {
        chunkGenerationManager.delete();
        distantBodyGenerationManager.terminate();
        masterRenderer.terminate();
    }

    public CosmicBody getActiveBody() {
        return activeBody;
    }

    public void setActiveBody(CosmicBody activeBody, Vector3f position) {
        this.activeBody = activeBody;
        chunkGenerationManager.setActivePlanet(activeBody, position);
    }

    public List<CosmicBody> getDistantActiveBodies() {
        return distantActiveBodies;
    }

    public void setDistantActiveBodies(ArrayList<CosmicBody> distantActiveBodies) {
        this.distantActiveBodies = distantActiveBodies;
    }
}
