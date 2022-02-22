package ca.benbingham.game.threads;

import ca.benbingham.engine.io.Camera;
import ca.benbingham.engine.io.Window;
import org.joml.Vector3f;

import static ca.benbingham.engine.util.Printing.print;

public class Game {
    private Camera camera;
    private Window window;

    private int height = 400;
    private int width = 800;

    private final int defaultFOV = 45;
    private final float mouseSensitivity = 0.07f;
    private final float movementSpeed = 4f;

    private RenderThread renderThread;
    private UpdateThread updateThread;
    private SyncThread syncThread;

    public void initialize() {
        syncThread = new SyncThread(this);
        syncThread.start();

//        // create game threads
//        renderThread = new RenderThread(this, camera, window);
//        renderThread.start();
//
//        updateThread = new UpdateThread(this, camera, window);
//        updateThread.start();
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

    public int getDefaultFOV() {
        return defaultFOV;
    }

    public float getMouseSensitivity() {
        return mouseSensitivity;
    }

    public float getMovementSpeed() {
        return movementSpeed;
    }
}
