package ca.benbingham.game.gameclasses.renderers;

import ca.benbingham.engine.io.Camera;
import ca.benbingham.engine.io.Window;
import ca.benbingham.game.gameclasses.Game;
import ca.benbingham.game.gameclasses.renderers.interfaces.IRenderer;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL32.GL_TEXTURE_CUBE_MAP_SEAMLESS;

public class MasterRenderer implements IRenderer {
    public ChunkRenderer chunkRenderer;
    public DebugRenderer debugRenderer;
    public SkyboxRenderer skyboxRenderer;

    private int windowHeight = 1080;
    private int windowWidth = 1920;

    private Camera camera;
    private Window window;
    private final Game game;

    private final int defaultFOV = 45;
    private final float mouseSensitivity = 0.07f;
    private final float movementSpeed = 4f;

    public MasterRenderer(Game game) {
        this.game = game;
    }

    private void renderInit() {
        window = new Window(windowHeight, windowWidth, "Nebula", true);

        camera = new Camera(window, defaultFOV, mouseSensitivity, movementSpeed, new Vector3f(1, 62, 1), 1000);

        window.create();

        GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        windowHeight = vidMode.height();
        windowWidth = vidMode.width();

        window.centerWindow();

        glfwSetInputMode(window.getWindow(), GLFW_CURSOR, GLFW_CURSOR_DISABLED);

        GL.createCapabilities();
        //Callback debugProc = GLUtil.setupDebugMessageCallback(); //prints OpenGL debug info to the console
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_TEXTURE_CUBE_MAP_SEAMLESS);

        //glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
    }

    @Override
    public void init() {
        chunkRenderer = new ChunkRenderer(this);
        debugRenderer = new DebugRenderer(this);
        skyboxRenderer = new SkyboxRenderer(this);

        renderInit();

        chunkRenderer.init();
        debugRenderer.init();
        skyboxRenderer.init();
    }

    @Override
    public void firstUpdate() {
        if (glfwWindowShouldClose(window.getWindow())) {
            game.setGameOpen(false);
        }

        camera.update();

        game.setPlayerPosition(camera.getPosition());

        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        chunkRenderer.firstUpdate();
        skyboxRenderer.firstUpdate();
        debugRenderer.firstUpdate();
    }

    @Override
    public void lastUpdate() {
        chunkRenderer.lastUpdate();
        debugRenderer.lastUpdate();
        skyboxRenderer.lastUpdate();

        glfwSwapBuffers(window.getWindow());
        glfwPollEvents();
    }

    public void resizeWindow() {
        camera.resizeWindow();
        window.resizeWindow();
    }

    public Camera getCamera() {
        return camera;
    }

    public Game getGame() {
        return game;
    }

    public Window getWindow() {
        return window;
    }

    @Override
    public void delete() {
        glfwTerminate();

        chunkRenderer.delete();
        debugRenderer.delete();
        skyboxRenderer.delete();
    }
}