package ca.benbingham.game.gameclasses;

import ca.benbingham.game.planetstructure.Chunk;

import ca.benbingham.game.planetstructure.planetgeneration.TerrainGenerator;
import org.joml.Vector2i;
import org.joml.Vector3f;

import static ca.benbingham.engine.util.Printing.print;

public class Game {
    private int height = 400;
    private int width = 800;

    private final int defaultFOV = 45;
    private final float mouseSensitivity = 0.07f;
    private final float movementSpeed = 4f;

    private Renderer renderer;
//    private Updater updater;
    private boolean newWorldData = true;
    private boolean gameOpen = true;

    private Vector3f playerPosition = new Vector3f(0, 0, 0);
    private int playerChunkXCord = 0;
    private int playerChunkYCord = 0;
    private int playerCrumbXCord = 0;
    private int playerCrumbYCord = 0;

//    private Planet testPlanet;

    private Vector2i playerChunk;
    private Chunk[][] loadedChunks;
    private int renderDistance = 2;

    private TerrainGenerator generator;

    public void start() {
        init();

        while (gameOpen) {


            renderer.update();
            //renderer.renderChunk(loadedChunks[0][0]);
            //renderer.swapBuffers();

            recreateLoadedChunkArray(); // TODO only call this when the player moves to a new chunk

            for (int i = 0; i < renderDistance; i++) {
                for (int j = 0; j < renderDistance; j++) {
                    if (loadedChunks[i][j].isBlockUpdate()) {
                        loadedChunks[i][j].setMesh(generator.createChunkMesh(loadedChunks[i][j].getCoordinates()));
                        loadedChunks[i][j].setBlockUpdate(false);
                    }
                    else if (loadedChunks[i][j].getMesh() == null) {
                        loadedChunks[i][j].setMesh(generator.createChunkMesh(loadedChunks[i][j].getCoordinates()));
                    }

                    renderer.renderChunk(loadedChunks[i][j]);
                }
            }

            //updater.update();

        }

        StringBuilder string = new StringBuilder();
        for (int i = 0; i < renderDistance; i++) {
            string.append("\n");
            for (int j = 0; j < renderDistance; j++) {
                string.append(loadedChunks[i][j].getCoordinates());
            }
        }

        print(string.toString());

        destroy();
    }

    private void init() {
        // TODO find what chunk the player is in
        playerChunk = new Vector2i(0, 0);

        loadedChunks = new Chunk[renderDistance][renderDistance];

        for (int i = 0; i < renderDistance; i++) {
            for (int j = 0; j < renderDistance; j++) {
                loadedChunks[i][j] = new Chunk();
            }
        }

        generator = new TerrainGenerator();

        renderer = new Renderer(this);

//        updater = new Updater(this);
//        renderer = new Renderer(this);

//        testPlanet = new Planet(EPlanetBiome.FOREST, "Test", 10, EWorldGeneratorVersion.ALPHA1_0_0);

//        PlanetTerrainGenerator generator = new PlanetTerrainGenerator(testPlanet);

//        testPlanet.setGenerator(generator);

//        generator.generate(testPlanet);

//        renderer.setActivePlanet(testPlanet);

//        updater.init();
        renderer.init();
    }

    private void recreateLoadedChunkArray() {
        int renderDistanceHalf = renderDistance / 2;
        loadedChunks[renderDistanceHalf][renderDistanceHalf].setCoordinates(playerChunk);

        int xDifference = playerChunk.x - renderDistanceHalf;
        int yDifference = playerChunk.y - renderDistanceHalf;

        for (int i = 0; i < renderDistance; i++) {
            for (int j = 0; j < renderDistance; j++) {
                if (loadedChunks[i][j].getCoordinates() != playerChunk) {
                    loadedChunks[i][j].setCoordinates(new Vector2i(i + xDifference, j + yDifference)); // TODO might need to swap i and j or x and y
                }
            }
        }
    }

    public void destroy() {
        //updater.destroy();
        renderer.destroy();
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
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

    public void setGameOpen(boolean gameOpen) {
        this.gameOpen = gameOpen;
    }

    public boolean isNewWorldData() {
        return newWorldData;
    }

//    public Updater getUpdater() {
//        return updater;
//    }

    public Vector3f getPlayerPosition() {
        return playerPosition;
    }

    public void setPlayerPosition(Vector3f playerPosition) {
        this.playerPosition = playerPosition;
    }

    public int getPlayerChunkXCord() {
        return playerChunkXCord;
    }

    public void setPlayerChunkXCord(int playerChunkXCord) {
        this.playerChunkXCord = playerChunkXCord;
    }

    public int getPlayerChunkYCord() {
        return playerChunkYCord;
    }

    public void setPlayerChunkYCord(int playerChunkYCord) {
        this.playerChunkYCord = playerChunkYCord;
    }

    public int getPlayerCrumbXCord() {
        return playerCrumbXCord;
    }

    public void setPlayerCrumbXCord(int playerCrumbXCord) {
        this.playerCrumbXCord = playerCrumbXCord;
    }

    public int getPlayerCrumbYCord() {
        return playerCrumbYCord;
    }

    public void setPlayerCrumbYCord(int playerCrumbYCord) {
        this.playerCrumbYCord = playerCrumbYCord;
    }

//    public Planet getPlanet() {
//        return testPlanet;
//    }
//
//    public void setPlanet(Planet planet) {
//        this.testPlanet = testPlanet;
//    }
}
