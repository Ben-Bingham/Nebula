package ca.benbingham.game.gameclasses;

public class Game {
    private int height = 400;
    private int width = 800;

    private final int defaultFOV = 45;
    private final float mouseSensitivity = 0.07f;
    private final float movementSpeed = 4f;

    private Renderer renderer;
    private Updater updater;
    private boolean newWorldData = true;
    private boolean gameOpen = true;

    public void start() {
        init();

        while (gameOpen) {
            updater.update();
            renderer.update();
        }

        destroy();
    }

    private void init() {
        updater = new Updater(this);
        renderer = new Renderer(this);

        updater.init();
        renderer.init();
    }

    public void destroy() {
        updater.destroy();
        renderer.destroy();
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

    public void setNewWorldData(boolean newWorldData) {
        this.newWorldData = newWorldData;
    }

    public boolean isGameOpen() {
        return gameOpen;
    }

    public void setGameOpen(boolean gameOpen) {
        this.gameOpen = gameOpen;
    }

    public boolean isNewWorldData() {
        return newWorldData;
    }

    public Updater getUpdater() {
        return updater;
    }
}
