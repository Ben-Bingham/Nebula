package ca.benbingham.game.util;

import static ca.benbingham.engine.util.Printing.print;
import static org.lwjgl.glfw.GLFW.glfwGetTime;

public class FPSCounter {
    private float lastFrame = 0;

    private boolean on = true;

    public FPSCounter (boolean startOn) {
        on = startOn;
    }

    public void update() {
        if (on) {
            float deltaTime = (float) (glfwGetTime() - lastFrame);
            lastFrame = (float) glfwGetTime();
            print("FPS:" + (1 / deltaTime));
        }
    }

    public void enable() {
        on = true;
    }

    public void disable() {
        on = false;
    }
}
