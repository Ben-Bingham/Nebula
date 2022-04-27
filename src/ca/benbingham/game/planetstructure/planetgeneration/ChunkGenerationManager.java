package ca.benbingham.game.planetstructure.planetgeneration;

import ca.benbingham.game.gameclasses.renderers.ChunkRenderer;
import ca.benbingham.game.planetstructure.Chunk;
import ca.benbingham.game.planetstructure.blocks.BlockList;
import ca.benbingham.game.planetstructure.bodys.Planet;
import ca.benbingham.game.planetstructure.geometry.Mesh;

import org.joml.Vector2i;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static ca.benbingham.engine.util.Printing.print;

public class ChunkGenerationManager {
    private final int renderDistance;
    private final int chunksPerSide;

    private final ChunkRenderer chunkRenderer;

    private final BlockList masterBlockList;
    private ArrayList<Chunk> activeChunks;
    private ArrayList<Vector2i> disallowedChunks;
    ArrayList<Vector2i> acceptableChunkCords;
    ArrayList<Vector2i> chunksNeeded;

    private Planet activeBody;
    private TerrainGenerator terrainGenerator;

    private final ExecutorService executorService;

    public ChunkGenerationManager(int renderDistance, ChunkRenderer chunkRenderer, BlockList masterBlockList, Planet activeBody) {
        this.renderDistance = renderDistance;

        chunksPerSide = (renderDistance * 2) + 1;

        this.chunkRenderer = chunkRenderer;
        this.masterBlockList = masterBlockList;
        this.activeBody = activeBody;

        terrainGenerator = new TerrainGenerator(masterBlockList, activeBody);

        activeChunks = new ArrayList<>();
        disallowedChunks = new ArrayList<>();
        acceptableChunkCords = new ArrayList<>();
        chunksNeeded = new ArrayList<>();
        executorService = Executors.newFixedThreadPool(4);
    }

    public void moveBetweenChunks(Vector2i playerChunkCords, boolean worldJustLoaded) {
        List<Vector2i> oldAcceptableChunkCords = new ArrayList<>(acceptableChunkCords);
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

        if (!worldJustLoaded) {
            for (int i = 0; i < acceptableChunkCords.size(); i++) {
                if (!oldAcceptableChunkCords.contains(acceptableChunkCords.get(i))) {
                    executorService.execute(new SingleChunkGenerator(terrainGenerator, acceptableChunkCords.get(i), this));
                }
            }
        }
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
    }

    private void addAllChunksToQueue() {
        synchronized (this) {
            for (int i = 0; i < acceptableChunkCords.size(); i++) {
                executorService.execute(new SingleChunkGenerator(terrainGenerator, acceptableChunkCords.get(i), this));
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

    private void changePlanet(Planet body) { //TODO needs to be tested
        terrainGenerator = new TerrainGenerator(masterBlockList, body);
        activeChunks.clear();
        addAllChunksToQueue();
    }

    public void delete() {
        executorService.shutdownNow();
    }
}
