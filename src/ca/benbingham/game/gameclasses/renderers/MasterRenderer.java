package ca.benbingham.game.gameclasses.renderers;

import ca.benbingham.game.gameclasses.Game;

import ca.benbingham.game.gameclasses.renderers.subrenderers.ChunkRenderer;
import ca.benbingham.game.gameclasses.renderers.subrenderers.DebugRenderer;
import ca.benbingham.game.gameclasses.renderers.subrenderers.DistantBodyRenderer;
import ca.benbingham.game.gameclasses.renderers.subrenderers.SkyboxRenderer;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL32.GL_TEXTURE_CUBE_MAP_SEAMLESS;

public class MasterRenderer implements IRenderer {
    public ChunkRenderer chunkRenderer;
    public DebugRenderer debugRenderer;
    public SkyboxRenderer skyboxRenderer;
    public DistantBodyRenderer distantBodyRenderer;

    private int windowHeight = 1080;
    private int windowWidth = 1920;

    private final Game game;

    private float planetRotation;
    private Matrix4f rotation;

    public MasterRenderer(Game game) {
        this.game = game;
    }

    private void renderInit() {
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_TEXTURE_CUBE_MAP_SEAMLESS);

        planetRotation = 0;
    }

    @Override
    public void init() {
        chunkRenderer = new ChunkRenderer(this);
        debugRenderer = new DebugRenderer(this);
        skyboxRenderer = new SkyboxRenderer(this);
        distantBodyRenderer = new DistantBodyRenderer(this);

        renderInit();

        chunkRenderer.init();
        debugRenderer.init();
        skyboxRenderer.init();
        distantBodyRenderer.init();
    }

    @Override
    public void firstUpdate() {
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        rotation = new Matrix4f().rotation((float) Math.toRadians(planetRotation), new Vector3f(0, 0, 1).normalize());
        planetRotation += 0.001;
        if (planetRotation >= 360) {
            planetRotation = 0;
        }

        chunkRenderer.firstUpdate();
        skyboxRenderer.firstUpdate();
        debugRenderer.firstUpdate();
        distantBodyRenderer.firstUpdate();
    }

    @Override
    public void lastUpdate() {
        chunkRenderer.lastUpdate();
        debugRenderer.lastUpdate();
        skyboxRenderer.lastUpdate();
        distantBodyRenderer.lastUpdate();
    }

    public Game getGame() {
        return game;
    }

    public Matrix4f getPlanetRotationMatrix() {
        return rotation;
    }

    @Override
    public void terminate() {
        chunkRenderer.terminate();
        debugRenderer.terminate();
        skyboxRenderer.terminate();
        distantBodyRenderer.terminate();
    }
}