package ca.benbingham.game.gameclasses;

import ca.benbingham.engine.util.Timer;
import ca.benbingham.game.blocks.BlockList;
import ca.benbingham.game.planetstructure.Chunk;

import ca.benbingham.game.planetstructure.Mesh;
import ca.benbingham.game.planetstructure.planetgeneration.TerrainGenerator;
import org.joml.Vector2i;
import org.joml.Vector3f;

import static ca.benbingham.engine.util.math.Util.determineIfOnEdgeOfGrid;
import static org.lwjgl.glfw.GLFW.glfwGetTime;

public class Game {
    private int height = 400;
    private int width = 800;

    private final int defaultFOV = 45;
    private final float mouseSensitivity = 0.07f;
    private final float movementSpeed = 4f;

    private Renderer renderer;
    private boolean gameOpen = true;

    private Vector3f playerPosition = new Vector3f(0, 0, 0);

    private Vector2i playerChunk;
    private Vector2i lastPlayerChunk;
    private Chunk[][] loadedChunks;
    private int renderDistance = 12;



    private float lastFrame;
    private float deltaTime;

    private BlockList masterBlockList;

    private Timer timer = new Timer();

    private TerrainGenerator generator;
    private int numb = 0;

    public void start() {
        init();

        while (gameOpen) {
            deltaTime = (float) (glfwGetTime() - lastFrame);
            lastFrame = (float) glfwGetTime();

            renderer.firstUpdate();

            playerChunk.x = (int) Math.ceil(playerPosition.x / Chunk.xSize);
            playerChunk.y = (int) Math.ceil(playerPosition.z / Chunk.zSize);

            if (playerChunk.x != lastPlayerChunk.x || playerChunk.y != lastPlayerChunk.y) { // Player moves between chunks
                recreateLoadedChunkArray();
            }

            lastPlayerChunk.x = playerChunk.x;
            lastPlayerChunk.y = playerChunk.y;

            for (int i = 0; i < renderDistance; i++) {
                for (int j = 0; j < renderDistance; j++) {
                    renderer.renderChunk(loadedChunks[i][j]);
                }
            }

            renderer.lastUpdate();
        }

        delete();
    }

    private void generateChunkMesh(Chunk[][] chunkArray, int i, int j) {

        Mesh mesh;
        int posX, negX, posY, negY;
        Chunk posXChunk, negXChunk, posYChunk, negYChunk;
        posX = i + 1;
        negX = i - 1;
        posY = j + 1;
        negY = j - 1;
        try {
            posXChunk = chunkArray[posX][j];
        }
        catch (IndexOutOfBoundsException e) {
            posXChunk = null;
        }

        try {
            negXChunk = chunkArray[negX][j];
        }
        catch (IndexOutOfBoundsException e) {
            negXChunk = null;
        }

        try {
            posYChunk = chunkArray[i][posY];
        }
        catch (IndexOutOfBoundsException e) {
            posYChunk = null;
        }

        try {
            negYChunk = chunkArray[i][negY];
        }
        catch (IndexOutOfBoundsException e) {
            negYChunk = null;
        }

        mesh = generator.createChunkMesh(chunkArray[i][j], posXChunk, negXChunk, posYChunk, negYChunk);

        chunkArray[i][j].setVBOData(0, mesh.getVertices());
        chunkArray[i][j].setEBOData(0, mesh.getIndices());
        chunkArray[i][j].setNumberOfVertices(mesh.getNumberOfVertices());
    }

    private void init() {
        renderer = new Renderer(this);

        renderer.init();

        playerChunk = new Vector2i(0, 0);
        lastPlayerChunk = new Vector2i(0, 0);

        loadedChunks = new Chunk[renderDistance][renderDistance];

        for (int i = 0; i < renderDistance; i++) {
            for (int j = 0; j < renderDistance; j++) {
                loadedChunks[i][j] = new Chunk();
            }
        }

        masterBlockList = new BlockList();
        masterBlockList.init();

        generator = new TerrainGenerator(masterBlockList);
        recreateLoadedChunkArray();
    }
    private void recreateLoadedChunkArray() {

        Chunk[][] oldLoadedChunks = loadedChunks;
        Chunk[][] newLoadedChunks = new Chunk[renderDistance][renderDistance];
        Vector2i[][] newChunkCords = new Vector2i[renderDistance][renderDistance];
        int renderDistanceHalf = renderDistance / 2;

        newChunkCords[renderDistanceHalf][renderDistanceHalf] = playerChunk;

        int xDifference = playerChunk.x - renderDistanceHalf;
        int yDifference = playerChunk.y - renderDistanceHalf;

        for (int i = 0; i < renderDistance; i++) {
            for (int j = 0; j < renderDistance; j++) {
                if (newChunkCords[i][j] != playerChunk) {
                    newChunkCords[i][j] = (new Vector2i(i + xDifference, j + yDifference)); // TODO might need to swap i and j or x and y
                }
            }
        }


        for (int i = 0; i < renderDistance; i++) {
            for (int j = 0; j < renderDistance; j++) {

                if (oldLoadedChunks[i][j] != null && oldLoadedChunks[i][j].getCoordinates() != null) {

                    for (int k = 0; k < renderDistance; k++) {
                        for (int l = 0; l < renderDistance; l++) {
                            if (oldLoadedChunks[i][j].getCoordinates().x == newChunkCords[k][l].x && oldLoadedChunks[i][j].getCoordinates().y == newChunkCords[k][l].y) {
                                newLoadedChunks[k][l] = oldLoadedChunks[i][j];
                                if (determineIfOnEdgeOfGrid(new Vector2i(i, j), renderDistance, renderDistance) || determineIfOnEdgeOfGrid(new Vector2i(k, l), renderDistance, renderDistance)) {
                                    newLoadedChunks[k][l].setNeedsMesh(true);
                                }
                            }
                        }
                    }
                }
            }
        }

        for (int i = 0; i < renderDistance; i++) {
            for (int j = 0; j < renderDistance; j++) {
                if (newLoadedChunks[i][j] == null) {
                    newLoadedChunks[i][j] = new Chunk();
                    newLoadedChunks[i][j].setCoordinates(newChunkCords[i][j]);
                    newLoadedChunks[i][j].setBlocks(generator.createShortArrayForChunk(newLoadedChunks[i][j].getCoordinates()));
                }
            }
        }

        //timer.startInterval();
        for (int i = 0; i < renderDistance; i++) {
            for (int j = 0; j < renderDistance; j++) {
                if (newLoadedChunks[i][j].isNeedsMesh()) {
                    generateChunkMesh(newLoadedChunks, i, j);
                    newLoadedChunks[i][j].setNeedsMesh(false);
                }
            }
        }
        //timer.endInterval();
        loadedChunks = newLoadedChunks;

        //timer.printAverageTimes("Created Chunk");
        //timer.resetTimes();
    }

    public void delete() {
        renderer.delete();
        for (int i = 0; i < renderDistance; i++) {
            for (int j = 0; j < renderDistance; j++) {
                loadedChunks[i][j].delete();
            }
        }
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

    public void setGameOpen(boolean gameOpen) {
        this.gameOpen = gameOpen;
    }

    public void setPlayerPosition(Vector3f playerPosition) {
        this.playerPosition = playerPosition;
    }

    public int getRenderDistance() {
        return renderDistance;
    }
}
