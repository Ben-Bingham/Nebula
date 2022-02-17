package ca.benbingham.engine.io;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;

import static ca.benbingham.engine.util.Printing.printError;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    private int height;
    private int width;
    private String title;
    private long window;

    public Window(int height, int width, String title, boolean fullscreen) {
        this.height = height;
        this.width = width;
        this.title = title;

        this.create(fullscreen);
    }

    public boolean create(boolean maxSize) {
        // Setup Error Callback
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW
        if (!glfwInit()) {
            printError("GLFW failed to initialize");
            return false;
        }

        // Configure GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        if (maxSize) {
            GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            width = vidMode.width();
            height = vidMode.height();
        }

        // Initialize GLFW Window
        window = glfwCreateWindow(width, height, title, NULL, NULL);
        if (window == NULL) {
            printError("GLFW Window failed to create");
            glfwTerminate();
            return false;
        }

        glfwMakeContextCurrent(window);

        glfwSwapInterval(1);
        glfwShowWindow(window);
        return true;
    }

    public void centerWindow() {
        GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());

        glfwSetWindowPos(
                window,
                (vidMode.width() - width) / 2,
                (vidMode.height() - height) / 2
        );
    }

    public void destroy() {
        glfwTerminate();
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getWindow() {
        return window;
    }

}