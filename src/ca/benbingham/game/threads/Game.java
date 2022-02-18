package ca.benbingham.game.threads;

import ca.benbingham.engine.io.Camera;
import ca.benbingham.engine.io.Window;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFWVidMode;

import static org.lwjgl.glfw.GLFW.*;

public class Game {
    private Camera camera;
    private Window window;

    private int height = 400;
    private int width = 800;

    private final int defaultFOV = 45;
    private final float mouseSensitivity = 0.07f;

    public Game() {

    }

    public void initialize() {
        RenderThread renderThread = new RenderThread(this, camera, window);
        renderThread.start();

        window = renderThread.getWindow();

        // set up window and camera


        glfwSetInputMode(window.getWindow(), GLFW_CURSOR, GLFW_CURSOR_DISABLED);

        camera = new Camera(window, defaultFOV, mouseSensitivity, 4f, new Vector3f(10, 258, 10));

        // create game threads
        MainThread mainThread = new MainThread(camera, window);
        mainThread.start();


    }

    public void destroy() {
        window.destroy();
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}
