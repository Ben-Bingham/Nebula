package ca.benbingham.game.gameclasses;

import ca.benbingham.engine.util.Timer;
import ca.benbingham.game.blocks.BlockList;
import ca.benbingham.game.planetstructure.Chunk;

import ca.benbingham.game.planetstructure.planetgeneration.SingleChunkGenerator;
import ca.benbingham.game.planetstructure.planetgeneration.TerrainGenerator;

import org.joml.Vector2i;
import org.joml.Vector3f;

import static ca.benbingham.engine.util.Printing.print;
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

    private Vector2i playerChunkCords;
    private Vector2i lastPlayerChunk;
    private Chunk[][] loadedChunks;
    private int renderDistance = 12;

    private float lastFrame;
    private float deltaTime;

    private BlockList masterBlockList;

    private SingleChunkGenerator playerChunkGenerator;
    private Chunk playerChunk;

    private SingleChunkGenerator[] secondaryChunkGenerators;
    private Chunk[] secondaryChunks;

    private Timer timer = new Timer();

    private TerrainGenerator generator;
    private int numb = 0;

    private boolean beforeFirstChunkCrossing = true;

    public void start() {
        init();

        while (gameOpen) {
            deltaTime = (float) (glfwGetTime() - lastFrame);
            lastFrame = (float) glfwGetTime();

            renderer.firstUpdate();

            playerChunkCords.x = (int) Math.floor(playerPosition.x / Chunk.xSize);
            playerChunkCords.y = (int) Math.floor(playerPosition.z / Chunk.zSize);

            if (playerChunkCords.x != lastPlayerChunk.x || playerChunkCords.y != lastPlayerChunk.y || beforeFirstChunkCrossing) { // Player moves between chunks
                print("movedChunk");
                playerChunkGenerator.setChunkCords(playerChunkCords);
                playerChunkGenerator.unlock();

                secondaryChunkGenerators[0].setChunkCords(new Vector2i(playerChunkCords.x + 1, playerChunkCords.y));
                secondaryChunkGenerators[1].setChunkCords(new Vector2i(playerChunkCords.x - 1, playerChunkCords.y));
                secondaryChunkGenerators[2].setChunkCords(new Vector2i(playerChunkCords.x, playerChunkCords.y + 1));
                secondaryChunkGenerators[3].setChunkCords(new Vector2i(playerChunkCords.x, playerChunkCords.y - 1));
                secondaryChunkGenerators[4].setChunkCords(new Vector2i(playerChunkCords.x + 1, playerChunkCords.y + 1));
                secondaryChunkGenerators[5].setChunkCords(new Vector2i(playerChunkCords.x + 1, playerChunkCords.y - 1));
                secondaryChunkGenerators[6].setChunkCords(new Vector2i(playerChunkCords.x- 1, playerChunkCords.y - 1));
                secondaryChunkGenerators[7].setChunkCords(new Vector2i(playerChunkCords.x - 1, playerChunkCords.y + 1));
                for (int i = 0; i < secondaryChunkGenerators.length; i++) {
                    secondaryChunkGenerators[i].unlock();
                }
                beforeFirstChunkCrossing = false;
            }

            lastPlayerChunk.x = playerChunkCords.x;
            lastPlayerChunk.y = playerChunkCords.y;

            // GAME LOOP
            if (playerChunk != null && playerChunk.hasMesh()) {
                renderer.renderChunk(playerChunk);
            }

            for (int i = 0; i < secondaryChunks.length; i++) {
                if (secondaryChunks[i] != null && secondaryChunks[i].hasMesh()) {
                    renderer.renderChunk(secondaryChunks[i]);
                }
            }

            if (playerChunkGenerator.isDone()) {
                playerChunk = playerChunkGenerator.getChunk();
                playerChunk.bindMeshData();
                playerChunkGenerator.setDone(false);
            }

            for (int i = 0; i < secondaryChunkGenerators.length; i++) {
                if (secondaryChunkGenerators[i].isDone()) {
                    secondaryChunks[i] = secondaryChunkGenerators[i].getChunk();
                    secondaryChunks[i].bindMeshData();
                    secondaryChunkGenerators[i].setDone(false);
                }
            }



            renderer.lastUpdate();
        }

        delete();
    }

//    private void generateChunkMesh(Chunk[][] chunkArray, int i, int j) {
////        Mesh mesh;
////        int posX, negX, posY, negY;
////        Chunk posXChunk, negXChunk, posYChunk, negYChunk;
////        posX = i + 1;
////        negX = i - 1;
////        posY = j + 1;
////        negY = j - 1;
////        try {
////            posXChunk = chunkArray[posX][j];
////        }
////        catch (IndexOutOfBoundsException e) {
////            posXChunk = null;
////        }
////
////        try {
////            negXChunk = chunkArray[negX][j];
////        }
////        catch (IndexOutOfBoundsException e) {
////            negXChunk = null;
////        }
////
////        try {
////            posYChunk = chunkArray[i][posY];
////        }
////        catch (IndexOutOfBoundsException e) {
////            posYChunk = null;
////        }
////
////        try {
////            negYChunk = chunkArray[i][negY];
////        }
////        catch (IndexOutOfBoundsException e) {
////            negYChunk = null;
////        }
////
////        mesh = generator.createChunkMesh(chunkArray[i][j], posXChunk, negXChunk, posYChunk, negYChunk);
////
////        chunkArray[i][j].setVBOData(0, mesh.getVertices());
////        chunkArray[i][j].setEBOData(0, mesh.getIndices());
////        chunkArray[i][j].setNumberOfVertices(mesh.getNumberOfVertices());
//        if (primaryChunkGenerators[i][j] == null) {
//            primaryChunkGenerators[i][j] = new SingleChunkGenerator(chunkArray, i, j, masterBlockList);
//            primaryChunkGenerators[i][j].start();
//        }
//        print(numb++);
//    }

    private void init() {
        masterBlockList = new BlockList();
        masterBlockList.init();

        renderer = new Renderer(this);

        renderer.init();

        playerChunkCords = new Vector2i(0, 0);
        lastPlayerChunk = new Vector2i(0, 0);

        playerChunkGenerator = new SingleChunkGenerator(masterBlockList);
        playerChunkGenerator.start();

        secondaryChunkGenerators = new SingleChunkGenerator[8];
        secondaryChunks = new Chunk[8];

        for (int i = 0; i < secondaryChunkGenerators.length; i++) {
            secondaryChunkGenerators[i] = new SingleChunkGenerator(masterBlockList);
            secondaryChunkGenerators[i].start();
        }

        //recreateLoadedChunkArray();
    }

//    private void recreateLoadedChunkArray() {
//        Chunk[][] oldLoadedChunks = loadedChunks;
//        Chunk[][] newLoadedChunks = new Chunk[renderDistance][renderDistance];
//        Vector2i[][] newChunkCords = new Vector2i[renderDistance][renderDistance];
//        int renderDistanceHalf = renderDistance / 2;
//
//        newChunkCords[renderDistanceHalf][renderDistanceHalf] = playerChunkCords;
//
//        int xDifference = playerChunkCords.x - renderDistanceHalf;
//        int yDifference = playerChunkCords.y - renderDistanceHalf;
//
//        for (int i = 0; i < renderDistance; i++) {
//            for (int j = 0; j < renderDistance; j++) {
//                if (newChunkCords[i][j] != playerChunkCords) {
//                    newChunkCords[i][j] = (new Vector2i(i + xDifference, j + yDifference)); // TODO might need to swap i and j or x and y
//                }
//            }
//        }
//
//        for (int i = 0; i < renderDistance; i++) {
//            for (int j = 0; j < renderDistance; j++) {
//
//                if (oldLoadedChunks[i][j] != null && oldLoadedChunks[i][j].getCoordinates() != null) {
//
//                    for (int k = 0; k < renderDistance; k++) {
//                        for (int l = 0; l < renderDistance; l++) {
//                            if (oldLoadedChunks[i][j].getCoordinates().x == newChunkCords[k][l].x && oldLoadedChunks[i][j].getCoordinates().y == newChunkCords[k][l].y) {
//                                newLoadedChunks[k][l] = oldLoadedChunks[i][j];
//                                if (determineIfOnEdgeOfGrid(new Vector2i(i, j), renderDistance, renderDistance) || determineIfOnEdgeOfGrid(new Vector2i(k, l), renderDistance, renderDistance)) {
//                                    newLoadedChunks[k][l].setNeedsMesh(true);
//                                }
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
//                    newLoadedChunks[i][j] = new Chunk(newChunkCords[i][j], generator);
//                }
//            }
//        }
//
//
//        for (int i = 0; i < renderDistance; i++) {
//            for (int j = 0; j < renderDistance; j++) {
//                if (newLoadedChunks[i][j].isNeedsMesh()) {
//                    generateChunkMesh(newLoadedChunks, i, j);
//                    newLoadedChunks[i][j].setNeedsMesh(false);
//                }
//            }
//        }
//
//        loadedChunks = newLoadedChunks;
//    }

    public void delete() {
        renderer.delete();
        playerChunkGenerator.delete();

        for (int i = 0; i < secondaryChunkGenerators.length; i++) {
            secondaryChunkGenerators[i].delete();
        }
//        for (int i = 0; i < renderDistance; i++) {
//            for (int j = 0; j < renderDistance; j++) {
//                loadedChunks[i][j].delete();
//            }
//        }
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
