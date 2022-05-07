package ca.benbingham.game.gameclasses;

import ca.benbingham.engine.io.Window;
import ca.benbingham.engine.util.events.Event;
import ca.benbingham.engine.util.events.EventBus;
import ca.benbingham.engine.util.events.EventListener;
import ca.benbingham.game.events.KeyboardPress;
import ca.benbingham.game.events.MousePosition;
import ca.benbingham.game.events.ScrollWheel;
import ca.benbingham.game.events.states.*;
import ca.benbingham.game.gameclasses.renderers.Renderer;
import ca.benbingham.game.planetstructure.blocks.BlockList;

import ca.benbingham.engine.util.FPSCounter;
import org.lwjgl.opengl.GL;

import static ca.benbingham.engine.util.GLError.getOpenGLError;
import static ca.benbingham.engine.util.Printing.print;
import static org.lwjgl.glfw.GLFW.*;

/**
 * This class handles the big picture parts of the game, like:
 *      Events,
 *      Player creation,
 *      Window Creation,
 *      Controls,
 *      Time,
 *      and more...
 */

public class Game implements EventListener {
    public EventBus eventBus;
    private final Event initEvent = new Init();
    private final Event firstUpdateEvent = new FirstUpdate();
    private final Update updateEvent = new Update();
    private final Event lastUpdateEvent = new LastUpdate();
    private final Event terminateEvent = new Terminate();

    public Renderer renderer;

    private boolean gameOpen = true;

    public Player player;
    private GameManager manager;

    public Window window;

    private BlockList masterBlockList;

    private double deltaTime = 0;
    private double lastFrame = 0;

    FPSCounter fpsCounter = new FPSCounter(false);

    public void start() {
        init();

        while (gameOpen) {
            firstUpdate();
            update();
            lastUpdate();
        }

        terminate();
    }

    private void init() {
        window = new Window(Settings.WINDOW_HEIGHT, Settings.WINDOW_WIDTH, "Nebula", true);

        window.create();
        window.centerWindow();

        player = new Player(this);

        glfwSetInputMode(window.getWindow(), GLFW_CURSOR, GLFW_CURSOR_DISABLED);

        GL.createCapabilities();

        masterBlockList = new BlockList();
        masterBlockList.init();

        renderer = new Renderer(this);

        manager = new GameManager(this);

        eventBus = new EventBus();
        eventBus.addListener(manager);
        eventBus.addListener(player);
        eventBus.addListener(this);
        eventBus.addListener(renderer);

        eventBus.emit(initEvent);

        IOInput();

        //Callback debugProc = GLUtil.setupDebugMessageCallback(); //prints OpenGL debug info to the console
        //glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
    }

    private void firstUpdate() {
        eventBus.emit(firstUpdateEvent);
    }

    private void update() {
        updateEvent.deltaTime = deltaTime;
        eventBus.emit(updateEvent);

        deltaTime = (float) (glfwGetTime() - lastFrame);
        lastFrame = (float) glfwGetTime();

        fpsCounter.update();
    }

    private void lastUpdate() {
        eventBus.emit(lastUpdateEvent);

        glfwSwapBuffers(window.getWindow());
        glfwPollEvents();

        getOpenGLError(new Throwable());
    }

    // --------------------------------------------------- EVENTS ------------------------------------------------------

    public void IOInput() {
        glfwSetKeyCallback(window.getWindow(), (window, key, scancode, action, mods) -> {
            eventBus.emit(new KeyboardPress(window, key, scancode, action, mods));
        });

        glfwSetScrollCallback(window.getWindow(), (window, xOffset, yOffset) -> {
            eventBus.emit(new ScrollWheel(window, xOffset, yOffset));
        });

        glfwSetCursorPosCallback(window.getWindow(), (window, xPos, yPos) -> {
            eventBus.emit(new MousePosition(window, xPos, yPos));
        });
    }

    private void keyboardInput(KeyboardPress event) {
        if (event.key == GLFW_KEY_ESCAPE && event.action == GLFW_PRESS) {
            glfwWindowShouldClose(window.getWindow());
            gameOpen = false;
        }
    }

    @Override
    public void receiveEvents(Event event) {
        if (event instanceof KeyboardPress) {
            keyboardInput((KeyboardPress) event);
        }
    }

    public void terminate() {
        eventBus.emit(terminateEvent);

        glfwSetWindowShouldClose(window.getWindow(), true);
        window.destroy();
    }

    // ------------------------------------------------ SETTERS & GETTERS ----------------------------------------------

    public void setGameOpen(boolean gameOpen) {
        this.gameOpen = gameOpen;
    }

    public Window getWindow() {
        return window;
    }

    public BlockList getMasterBlockList() {
        return masterBlockList;
    }

    public double getDeltaTime() {
        return deltaTime;
    }
}