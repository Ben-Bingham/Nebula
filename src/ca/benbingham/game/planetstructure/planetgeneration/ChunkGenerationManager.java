package ca.benbingham.game.planetstructure.planetgeneration;

import ca.benbingham.game.gameclasses.renderers.ChunkRenderer;
import ca.benbingham.game.planetstructure.Chunk;
import ca.benbingham.game.planetstructure.blocks.BlockList;
import ca.benbingham.game.planetstructure.geometry.Mesh;

import org.joml.Vector2i;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static ca.benbingham.engine.util.Printing.print;
import static org.lwjgl.glfw.GLFW.glfwGetTime;

public class ChunkGenerationManager {
    private final int renderDistance;
    private final int chunksPerSide;

    private final ChunkRenderer chunkRenderer;

    private final BlockList masterBlockList;
    private ArrayList<Chunk> activeChunks;
    private ArrayList<Vector2i> disallowedChunks;
    ArrayList<Vector2i> acceptableChunkCords;
    ArrayList<Vector2i> chunksNeeded;

    private float timeSinceLastChunkCrossing = 0;
    private float lastTime = 0;

    private boolean chunksMade;

    private final ExecutorService executorService;

    public ChunkGenerationManager(int renderDistance, ChunkRenderer chunkRenderer, BlockList masterBlockList) {
        this.renderDistance = renderDistance;

        chunksPerSide = (renderDistance * 2) + 1;

        this.chunkRenderer = chunkRenderer;
        this.masterBlockList = masterBlockList;

        activeChunks = new ArrayList<>();
        disallowedChunks = new ArrayList<>();
        acceptableChunkCords = new ArrayList<>();
        chunksNeeded = new ArrayList<>();
        executorService = Executors.newFixedThreadPool(4);

        lastTime = (float) glfwGetTime();
    }

    public void moveBetweenChunks(Vector2i playerChunkCords, boolean worldJustLoaded) {
        acceptableChunkCords.clear();
        disallowedChunks.clear();

        int xDifference = playerChunkCords.x - renderDistance;
        int yDifference = playerChunkCords.y - renderDistance;

        for (int i = 0; i < chunksPerSide; i++) {
            for (int j = 0; j < chunksPerSide; j++) {
                acceptableChunkCords.add(new Vector2i(i + xDifference, j + yDifference));
            }
        }

        if (worldJustLoaded) {
            synchronized (this) {
                addAllChunksToQueue();
            }
        }

        timeSinceLastChunkCrossing = 0;
        chunksMade = false;
    }

    public void update() {
        // Removes disallowed chunks
        synchronized (this) {
            for (int i = 0; i < activeChunks.size(); i++) {
                boolean canExist = false;
                for (Vector2i acceptableChunkCord : acceptableChunkCords) {
                    if (activeChunks.get(i).getCoordinates().x == acceptableChunkCord.x && activeChunks.get(i).getCoordinates().y == acceptableChunkCord.y) {
                        canExist = true;
                        break;
                    }
                }
                if (!canExist) {
                    disallowedChunks.add(activeChunks.get(i).getCoordinates());
                }
            }

            int numb = 0;
            for (int i = 0; i < activeChunks.size(); i++) {
                for (int j = 0; j < disallowedChunks.size(); j++) {
                    if (activeChunks.get(i - numb).getCoordinates().x == disallowedChunks.get(j).x && activeChunks.get(i - numb).getCoordinates().y == disallowedChunks.get(j).y) {
                        activeChunks.get(i - numb).delete();
                        activeChunks.remove(i - numb);
                        numb++;
                        break;
                    }
                }
            }
            disallowedChunks.clear();

            // Adds to the queue all needed chunks after staying still for a bit
            if (timeSinceLastChunkCrossing > 500000 || timeSinceLastChunkCrossing > 0.5 && !chunksMade) {
                if (activeChunks.size() > 0) {
                    for (int i = 0; i < acceptableChunkCords.size(); i++) {
                        boolean hasChunk = false;
                        for (int j = 0; j < activeChunks.size(); j++) {
                            if (activeChunks.get(j) != null && acceptableChunkCords.get(i) != null) {
                                if (activeChunks.get(j).getCoordinates().x == acceptableChunkCords.get(i).x && activeChunks.get(j).getCoordinates().y == acceptableChunkCords.get(i).y) {
                                    hasChunk = true;
                                    break;
                                }
                            }
                        }
                        if (!hasChunk) {
                            chunksNeeded.add(acceptableChunkCords.get(i));
                        }
                    }
                    for (int i = 0; i < chunksNeeded.size(); i++) {
                        executorService.execute(new SingleChunkGenerator(masterBlockList, chunksNeeded.get(i), this));
                    }
                    chunksNeeded.clear();
                }
                else {
                    addAllChunksToQueue();
                }

                chunksMade = true;
            }

            if (timeSinceLastChunkCrossing > 500000) {
                timeSinceLastChunkCrossing = 0;
            }
        }

        synchronized (this) {
            for (int i = 0; i < activeChunks.size(); i++) {
                if(activeChunks.get(i).hasMeshToBeBound()) {
                    if (activeChunks.get(i).needsVAOInit()) {
                        activeChunks.get(i).bindMeshData();
                    }
                    else {
                        activeChunks.get(i).bindNewMeshData();
                    }
                }
                if (activeChunks.get(i) != null && activeChunks.get(i).hasMesh()) {
                    chunkRenderer.renderChunk(activeChunks.get(i));
                }
            }
        }

        float deltaTime = (float) glfwGetTime() - lastTime;
        lastTime = (float) glfwGetTime();
        timeSinceLastChunkCrossing += deltaTime;
    }

    private void addAllChunksToQueue() {
        synchronized (this) {
            for (int i = 0; i < acceptableChunkCords.size(); i++) {
                executorService.execute(new SingleChunkGenerator(masterBlockList, acceptableChunkCords.get(i), this));
            }
        }
    }

    public synchronized void setChunkData(Mesh mesh, Vector2i chunkCoordinates) {
        if (acceptableChunkCords.contains(chunkCoordinates)) {
            Chunk chunk = new Chunk();
            chunk.setCoordinates(chunkCoordinates);
            chunk.setMesh(mesh);
            chunk.setHasMeshToBeBound(true);

            synchronized (this) {
                activeChunks.add(chunk);
            }
        }
    }

    public void delete() {
        executorService.shutdownNow();
    }
}
