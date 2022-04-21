package ca.benbingham.game.planetstructure.planetgeneration;

import ca.benbingham.game.gameclasses.renderers.ChunkRenderer;
import ca.benbingham.game.planetstructure.Chunk;
import ca.benbingham.game.planetstructure.blocks.BlockList;
import ca.benbingham.game.planetstructure.geometry.Mesh;
import ca.benbingham.game.planetstructure.planetgeneration.enums.EMovementDirection;
import org.joml.Vector2i;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static ca.benbingham.engine.util.Printing.print;
import static ca.benbingham.engine.util.Util.checkIfOnEdge;

public class ChunkGenerationManager {
    private final int renderDistance;
    private final int chunksPerSide;
//    private final int totalChunks;

    private final ChunkRenderer chunkRenderer;

    //private final Chunk[] chunks;


//    private final MultiChunkGenerator multiChunkGenerator;
    private final BlockList masterBlockList;
    private ArrayList<Chunk> activeChunks;
    private ArrayList<Vector2i> disallowedChunks;
//    private Vector2i[][] chunkCords;
//    private Vector2i[][] newChunkCords;
//    private Mesh[] chunkMeshes;

    int chunksMade;

    private final ExecutorService executorService;

    public ChunkGenerationManager(int renderDistance, ChunkRenderer chunkRenderer, BlockList masterBlockList) {
        this.renderDistance = renderDistance;

        chunksPerSide = (renderDistance * 2) + 1;

//        totalChunks = chunksPerSide * chunksPerSide;

        this.chunkRenderer = chunkRenderer;
        this.masterBlockList = masterBlockList;


//        inactiveChunks = new ArrayList<>();

        activeChunks = new ArrayList<>();
        disallowedChunks = new ArrayList<>();
//        chunkCords = new Vector2i[chunksPerSide][chunksPerSide];
//        newChunkCords = new Vector2i[chunksPerSide][chunksPerSide];
//        chunkMeshes = new Mesh[totalChunks];

//        chunks = new Chunk[totalChunks];
//        for (int i = 0; i < chunks.length; i++) {
//            chunks[i] = new Chunk();
//        }
//        multiChunkGenerator = new MultiChunkGenerator(totalChunks, masterBlockList);
//        multiChunkGenerator.start();

//        executorService = Executors.newFixedThreadPool((int) Math.floor((double) Runtime.getRuntime().availableProcessors() / 4));
        executorService = Executors.newFixedThreadPool(1);
    }

    public void moveBetweenChunks(Vector2i playerChunkCords) {
        ArrayList<Vector2i> acceptableChunkCords = new ArrayList<>();

        ArrayList<Vector2i> chunksNeeded = new ArrayList<>();

        int xDifference = playerChunkCords.x - renderDistance;
        int yDifference = playerChunkCords.y - renderDistance;

        for (int i = 0; i < chunksPerSide; i++) {
            for (int j = 0; j < chunksPerSide; j++) {
                acceptableChunkCords.add(new Vector2i(i + xDifference, j + yDifference));
            }
        }

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

//        for (int i = 0; i < activeChunks.size(); i++) {
//            for (int j = 0; j < disallowedChunks.size(); j++) {
//                if (activeChunks.get(i).getCoordinates().x == disallowedChunks.get(j).x && activeChunks.get(i).getCoordinates().y == disallowedChunks.get(j).y) {
//                    activeChunks.get(i).delete();
//                }
//            }
//        }
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

//        synchronized (this) {
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
//        }

        for (int i = 0; i < chunksNeeded.size(); i++) {
            executorService.execute(new SingleChunkGenerator(masterBlockList, chunksNeeded.get(i), this));
        }

        chunksNeeded.clear();
    }

    public void moveBetweenChunks(EMovementDirection movementDirection) {

//        int count = 0;
//
//        int xDifference = playerChunkCords.x - renderDistance;
//        int yDifference = playerChunkCords.y - renderDistance;
//        Vector2i[] chunkCords1D = new Vector2i[totalChunks];
//
//        for (int i = 0; i < chunksPerSide; i++) {
//            for (int j = 0; j < chunksPerSide; j++) {
//                chunkCords[i][j] = (new Vector2i(i + xDifference, j + yDifference));
//            }
//        }
//
//        for (int i = 0; i < chunksPerSide; i++) {
//            for (int j = 0; j < chunksPerSide; j++) {
//                chunkCords1D[count] = chunkCords[i][j];
//                count++;
//            }
//        }
//
//        multiChunkGenerator.setChunkCords(chunkCords1D);
//        multiChunkGenerator.unlock();
    }

    public void update() {
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

        synchronized (this) {
            for (int i = 0; i < activeChunks.size(); i++) {
                for (int j = 0; j < activeChunks.size(); j++) {
                    if (activeChunks.get(i) == activeChunks.get(j) && i != j) {
                        print("Repeat");
                    }
                }
            }
        }
    }

    public synchronized void setChunkData(Mesh mesh, Vector2i chunkCoordinates, SingleChunkGenerator singleChunkGenerator) {
//        this.chunks[i].setMesh(mesh);
//        this.chunks[i].setCoordinates(chunkCoordinates);
//        this.chunks[i].setHasMeshToBeBound(true);
//        if (inactiveChunks.size() > 1) {
//            inactiveChunks.get(0).setCoordinates(chunkCoordinates);
//            inactiveChunks.get(0).setMesh(mesh);
//            inactiveChunks.get(0).setHasMeshToBeBound(true);
//
//            activeChunks.add(inactiveChunks.get(0));
//        }
//        else {
        Chunk chunk = new Chunk();
//        print("new");
        chunksMade++;
        chunk.setCoordinates(chunkCoordinates);
        chunk.setMesh(mesh);
        chunk.setHasMeshToBeBound(true);

        synchronized (this) {
            activeChunks.add(chunk);
        }

//        }
    }

    public void delete() {
        executorService.shutdownNow();
    }
}
