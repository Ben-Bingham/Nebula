package ca.benbingham.game.gameclasses;

import ca.benbingham.engine.util.Timer;
import ca.benbingham.game.blocks.BlockList;
import ca.benbingham.game.planetstructure.Block;
import ca.benbingham.game.planetstructure.Chunk;

import ca.benbingham.game.planetstructure.Mesh;
import ca.benbingham.game.planetstructure.planetgeneration.ChunkMeshGenerator;
import ca.benbingham.game.planetstructure.planetgeneration.TerrainGenerator;
import org.joml.Vector2i;
import org.joml.Vector3f;

import java.time.chrono.ThaiBuddhistEra;
import java.util.ArrayList;
import java.util.Arrays;

import static ca.benbingham.engine.util.GLError.getOpenGLError;
import static ca.benbingham.engine.util.Printing.print;
import static ca.benbingham.engine.util.math.Util.snapToRange;
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
    private int renderDistance = 4;

    private ChunkMeshGenerator[][] chunkMeshGenerators;

    private float lastFrame;
    private float deltaTime;

    private Chunk testChunk;
    //private Chunk[][] testChunks;
    //private Block[][][][][] worldBlocks;
    private short[][][][][] worldBlocks;
    private BlockList masterBlockList;

    private Timer timer = new Timer();

    private TerrainGenerator generator;

    public void start() {
        init();

        // test chunk

        testChunk = new Chunk();

        testChunk.setCoordinates(new Vector2i(0, 0));

//        ChunkMeshGenerator chunkMeshGenerator = new ChunkMeshGenerator(testChunk.getCoordinates());
//        chunkMeshGenerator.run();

        testChunk.setBlocks(generator.createShortArrayForChunk(testChunk.getCoordinates()));
        timer.startInterval();
        Mesh mesh = generator.createChunkMesh(testChunk, null, null, null, null);
        timer.endInterval();
        //print(Arrays.toString(mesh.getIndices()));
        //print(mesh.getNumberOfVertices());

        testChunk.setVBOData(0, mesh.getVertices());
        testChunk.setEBOData(0, mesh.getIndices());
        testChunk.setNumberOfVertices(mesh.getNumberOfVertices());
//        testChunk.setVBOData(0, renderer.vertices);
//        testChunk.setEBOData(0, renderer.cubeIndices);
//        testChunk.setNumberOfVertices(36);





        // test chunks
//        testChunks = new Chunk[renderDistance][renderDistance];
//        worldBlocks = new short[renderDistance][renderDistance][Chunk.xSize][Chunk.ySize][Chunk.zSize];
//
//        TerrainGenerator terrainGenerator = new TerrainGenerator(masterBlockList);
//        Mesh mesh;
//
//        for (int i = 0; i < renderDistance; i++) {
//            for (int j = 0; j < renderDistance; j++) {
//                timer.startInterval();
//                testChunks[i][j] = new Chunk();
//                testChunks[i][j].setCoordinates(new Vector2i(i, j));
//
//                worldBlocks[i][j] = terrainGenerator.createBlockArrayForChunk(new Vector2i(i, j)); // Takes about 1 milliseconds
//
//                int posX, negX, posY, negY;
//                posX = snapToRange(i + 1, 0, renderDistance - 1); //TODO method needs to be changed
//                negX = snapToRange(i - 1, 0, renderDistance - 1);
//                posY = snapToRange(j + 1, 0, renderDistance - 1);
//                negY = snapToRange(j - 1, 0, renderDistance - 1);
//
//                mesh = terrainGenerator.createChunkMesh(worldBlocks[i][j], worldBlocks[posX][j], worldBlocks[negX][j], worldBlocks[i][posY], worldBlocks[i][negY]); // Takes about 20 milliseconds
//
//
//
//                testChunks[i][j].setVBOData(0, mesh.getVertices()); // Takes about 1 milliseconds
//                testChunks[i][j].setEBOData(0, mesh.getIndices());
//                testChunks[i][j].setNumberOfVertices(mesh.getNumberOfVertices());
//                timer.endInterval();
//            }
//        }

        timer.printAverageTimes("Create chunk.");

        while (gameOpen) {
            deltaTime = (float) (glfwGetTime() - lastFrame);
            lastFrame = (float) glfwGetTime();

            renderer.firstUpdate();

            playerChunk.x = (int) Math.ceil(playerPosition.x / Chunk.xSize);
            playerChunk.y = (int) Math.ceil(playerPosition.z / Chunk.zSize);

            if (playerChunk.x != lastPlayerChunk.x || playerChunk.y != lastPlayerChunk.y) { // Player moves between chunks
                //recreateLoadedChunkArray();
            }

            lastPlayerChunk.x = playerChunk.x;
            lastPlayerChunk.y = playerChunk.y;

            //renderer.renderChunk(testChunk);

//            for (int i = 0; i < renderDistance; i++) {
//                for (int j = 0; j < renderDistance; j++) {
//                    renderer.renderChunk(testChunks[i][j]);
//                }
//            }

            renderer.renderChunk(testChunk);

//            for (int i = 0; i < renderDistance; i++) {
//                for (int j = 0; j < renderDistance; j++) {
//                    if (chunkMeshGenerators[i][j] != null) {
//                        if (chunkMeshGenerators[i][j].isDone()) {
//                            if (chunkMeshGenerators[i][j].getChunkCords().x == loadedChunks[i][j].getCoordinates().x && chunkMeshGenerators[i][j].getChunkCords().y == loadedChunks[i][j].getCoordinates().y) {
//                                loadedChunks[i][j] = new Chunk();
//                                loadedChunks[i][j].setCoordinates(chunkMeshGenerators[i][j].getChunkCords());
//                                loadedChunks[i][j].setVBOData(0, chunkMeshGenerators[i][j].getMesh().getVertices());
//                                loadedChunks[i][j].setEBOData(0, chunkMeshGenerators[i][j].getMesh().getIndices());
//                                loadedChunks[i][j].setNumberOfVertices(chunkMeshGenerators[i][j].getMesh().getNumberOfVertices());
//                                loadedChunks[i][j].setHasMesh(true);
//
//                                //loadedChunks[i][j] = chunkMeshGenerators[i][j].getChunk();
//                            }
////                            chunkMeshGenerators[i][j].getChunk().setVBOData(0, chunkMeshGenerators[i][j].getChunk().getMesh().getVertices());
////                            chunkMeshGenerators[i][j].getChunk().setEBOData(0, chunkMeshGenerators[i][j].getChunk().getMesh().getIndices());
//
//                            chunkMeshGenerators[i][j].kill();
//
//                            if (i == 0 && j == 0) {
//                                print("It took: " + timer + " seconds."); //This brokey TODO
//                                timer = 0;
//                            }
//                            chunkMeshGenerators[i][j] = null;
//                        }
//                    }
//                }
//            }
//
//            for (int i = 0; i < renderDistance; i++) {
//                for (int j = 0; j < renderDistance; j++) {
//                    if (loadedChunks[i][j].isBlockUpdate()) {
//                        generateChunk(loadedChunks[i][j].getCoordinates(), i, j);
////                        chunkMeshGenerators[i][j] = new ChunkMeshGenerator(loadedChunks[i][j].getCoordinates());
////                        chunkMeshGenerators[i][j].start();
//                        //loadedChunks[i][j].setMesh(generator.createChunkMesh(loadedChunks[i][j].getCoordinates()));
//                        //loadedChunks[i][j].setBlockUpdate(false);
//                    }
//                    else if (!loadedChunks[i][j].hasMesh()) {
//                        generateChunk(loadedChunks[i][j].getCoordinates(), i, j);
////                        chunkMeshGenerators[i][j] = new ChunkMeshGenerator(loadedChunks[i][j].getCoordinates());
////                        chunkMeshGenerators[i][j].start();
//                        //loadedChunks[i][j].setMesh(generator.createChunkMesh(loadedChunks[i][j].getCoordinates()));
//                    }
//                    if (loadedChunks[i][j].hasMesh()) {
//                        renderer.renderChunk(loadedChunks[i][j]);
//                    }
//                }
//            }

            renderer.lastUpdate();
        }

        delete();
    }

//    private void generateChunk(Vector2i chunkCord, int i, int j) {
//        int x = chunkCord.x;
//        int y = chunkCord.y;
//        if (chunkMeshGenerators[i][j] == null) {
//            chunkMeshGenerators[i][j] = new ChunkMeshGenerator(new Vector2i(x, y));
//            //chunkMeshGenerators[i][j].setChunk(new Chunk());
//            chunkMeshGenerators[i][j].start();
//        }
//    }

    private void init() {
        renderer = new Renderer(this);

        renderer.init();

        playerChunk = new Vector2i(0, 0);
        lastPlayerChunk = new Vector2i(0, 0);

        loadedChunks = new Chunk[renderDistance][renderDistance];
        chunkMeshGenerators = new ChunkMeshGenerator[renderDistance][renderDistance];

        for (int i = 0; i < renderDistance; i++) {
            for (int j = 0; j < renderDistance; j++) {
                //loadedChunks[i][j] = new Chunk();
                chunkMeshGenerators[i][j] = null;
            }
        }

        masterBlockList = new BlockList();
        masterBlockList.init();

        //recreateLoadedChunkArray();

        generator = new TerrainGenerator(masterBlockList);
    }

//    private void recreateLoadedChunkArray() {
//        Vector2i[][] newChunkCords = new Vector2i[renderDistance][renderDistance];
//        Chunk[][] newLoadedChunks = new Chunk[renderDistance][renderDistance];
//        int renderDistanceHalf = renderDistance / 2;
//
//        //loadedChunks[renderDistanceHalf][renderDistanceHalf].setCoordinates(playerChunk);
//        newChunkCords[renderDistanceHalf][renderDistanceHalf] = playerChunk;
//
//        int xDifference = playerChunk.x - renderDistanceHalf;
//        int yDifference = playerChunk.y - renderDistanceHalf;
//
//        for (int i = 0; i < renderDistance; i++) {
//            for (int j = 0; j < renderDistance; j++) {
//                if (newChunkCords[i][j] != playerChunk) {
//                    newChunkCords[i][j] = (new Vector2i(i + xDifference, j + yDifference)); // TODO might need to swap i and j or x and y
//                }
//            }
//        }
//
//        for (int i = 0; i < renderDistance; i++) {
//            for (int j = 0; j < renderDistance; j++) {
//
//                for (int k = 0; k < renderDistance; k++) {
//                    for (int l = 0; l < renderDistance; l++) {
//                        if (loadedChunks[i][j].getCoordinates() != null) {
//                            if (loadedChunks[i][j].getCoordinates().x == newChunkCords[k][l].x && loadedChunks[i][j].getCoordinates().y == newChunkCords[k][l].y) {
//                                newLoadedChunks[k][l] = loadedChunks[i][j];
//                            }
//                        }
//                    }
//                }
//            }
//        }
//
//        for (int i = 0; i < renderDistance; i++) {
//            for (int j = 0; j < renderDistance; j++) {
//                if (newLoadedChunks[i][j] == null) {
//                    newLoadedChunks[i][j] = new Chunk();
//                    newLoadedChunks[i][j].setCoordinates(newChunkCords[i][j]);
//                }
//            }
//        }
//
//        loadedChunks = newLoadedChunks;
//    }

    public void delete() {
        renderer.delete();
        //testChunk.delete();
        for (int i = 0; i < renderDistance; i++) {
            for (int j = 0; j < renderDistance; j++) {
                //testChunks[i][j].delete();
            }
        }
        testChunk.delete();
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
