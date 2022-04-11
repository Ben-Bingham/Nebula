package ca.benbingham.game.planetstructure.planetgeneration;

import ca.benbingham.game.gameclasses.renderers.ChunkRenderer;
import ca.benbingham.game.planetstructure.Chunk;
import ca.benbingham.game.planetstructure.blocks.BlockList;
import org.joml.Vector2i;

import static ca.benbingham.engine.util.Printing.print;

public class ChunkGenerationManager {
    private final int renderDistance;
    private final int chunksPerSide;
    private final int totalChunks;

    private final ChunkRenderer chunkRenderer;

//    private final Chunk[] primaryChunks;
//    private final SingleChunkGenerator[] primaryChunkGenerators;

//    private final Chunk[] secondaryChunks;
//    private final SingleChunkGenerator[] secondaryChunkGenerators;

    private final Chunk[] chunks;
    private final MultiChunkGenerator multiChunkGenerator;

    private final Vector2i[][] chunkCords;

    public ChunkGenerationManager(int renderDistance, ChunkRenderer chunkRenderer, BlockList masterBlockList) {
        this.renderDistance = renderDistance;

        chunksPerSide = (renderDistance * 2) + 1;

        totalChunks = chunksPerSide * chunksPerSide;

        this.chunkRenderer = chunkRenderer;

//        primaryChunks = new Chunk[9];
//        primaryChunkGenerators = new SingleChunkGenerator[9];
//
//        for (int i = 0; i < primaryChunkGenerators.length; i++) {
//            primaryChunkGenerators[i] = new SingleChunkGenerator(masterBlockList);
//            primaryChunkGenerators[i].start();
//        }

//        secondaryChunks = new Chunk[totalChunks];
//        secondaryChunkGenerators = new SingleChunkGenerator[totalChunks];
        chunkCords = new Vector2i[chunksPerSide][chunksPerSide];
//
//        for (int i = 0; i < secondaryChunkGenerators.length; i++) {
//            secondaryChunkGenerators[i] = new SingleChunkGenerator(masterBlockList);
//            secondaryChunkGenerators[i].start();
//        }

        chunks = new Chunk[totalChunks];
        multiChunkGenerator = new MultiChunkGenerator(totalChunks, masterBlockList);
        multiChunkGenerator.start();
    }

    public void moveBetweenChunks(Vector2i playerChunkCords) {
        print("moved");
//        primaryChunkGenerators[0].setChunkCords(new Vector2i(playerChunkCords.x, playerChunkCords.y));
//        primaryChunkGenerators[1].setChunkCords(new Vector2i(playerChunkCords.x + 1, playerChunkCords.y));
//        primaryChunkGenerators[2].setChunkCords(new Vector2i(playerChunkCords.x - 1, playerChunkCords.y));
//        primaryChunkGenerators[3].setChunkCords(new Vector2i(playerChunkCords.x, playerChunkCords.y + 1));
//        primaryChunkGenerators[4].setChunkCords(new Vector2i(playerChunkCords.x, playerChunkCords.y - 1));
//        primaryChunkGenerators[5].setChunkCords(new Vector2i(playerChunkCords.x + 1, playerChunkCords.y + 1));
//        primaryChunkGenerators[6].setChunkCords(new Vector2i(playerChunkCords.x + 1, playerChunkCords.y - 1));
//        primaryChunkGenerators[7].setChunkCords(new Vector2i(playerChunkCords.x- 1, playerChunkCords.y - 1));
//        primaryChunkGenerators[8].setChunkCords(new Vector2i(playerChunkCords.x - 1, playerChunkCords.y + 1));
//
//        for (int i = 0; i < primaryChunkGenerators.length; i++) {
//            primaryChunkGenerators[i].unlock();
//        }

//        chunkCords[renderDistance + 1][renderDistance + 1] = playerChunkCords;
        int count = 0;

        int xDifference = playerChunkCords.x - renderDistance;
        int yDifference = playerChunkCords.y - renderDistance;
        Vector2i[] chunkCords1D = new Vector2i[totalChunks];

        for (int i = 0; i < chunksPerSide; i++) {
            for (int j = 0; j < chunksPerSide; j++) {
                chunkCords[i][j] = (new Vector2i(i + xDifference, j + yDifference));
                chunkCords1D[count] = chunkCords[i][j];
                count++;
            }
        }

        multiChunkGenerator.setChunkCords(chunkCords1D);
        multiChunkGenerator.unlock();

//        for (int i = 0; i < chunksPerSide; i++) {
//            for (int j = 0; j < chunksPerSide; j++) {
//                secondaryChunkGenerators[count].setChunkCords(chunkCords[i][j]);
//                secondaryChunkGenerators[count].unlock();
//
//                count++;
//            }
//        }
    }

    public void update() {
        // Primary Chunks.
//        for (int i = 0; i < primaryChunks.length; i++) {
//            if (primaryChunks[i] != null && primaryChunks[i].hasMesh()) {
//                chunkRenderer.renderChunk(primaryChunks[i]);
//            }
//        }
//
//        for (int i = 0; i < primaryChunkGenerators.length; i++) {
//            if (primaryChunkGenerators[i].isDone() && primaryChunkGenerators[i].getChunk() != null) {
//                primaryChunks[i] = primaryChunkGenerators[i].getChunk();
//                primaryChunks[i].bindMeshData();
//                primaryChunkGenerators[i].setDone(false);
//            }
//        }

        //Secondary Chunks.
//        for (int i = 0; i < secondaryChunks.length; i++) {
//            if (secondaryChunks[i] != null && secondaryChunks[i].hasMesh()) {
//                chunkRenderer.renderChunk(secondaryChunks[i]);
//            }
//        }
//
//        for (int i = 0; i < secondaryChunkGenerators.length; i++) {
//            if (secondaryChunkGenerators[i].isDone() && secondaryChunkGenerators[i].getChunk() != null) {
//                secondaryChunks[i] = secondaryChunkGenerators[i].getChunk();
//                secondaryChunks[i].bindMeshData();
//                secondaryChunkGenerators[i].setDone(false);
//            }
//        }

        // MultiChunkGenerator.
        for (int i = 0; i < totalChunks; i++) {
            if (chunks[i] != null && chunks[i].hasMesh()) {
                chunkRenderer.renderChunk(chunks[i]);
            }
        }

        for (int i = 0; i < totalChunks; i++) {
            if (multiChunkGenerator.isDone(i) && multiChunkGenerator.getChunk(i) != null) {

                chunks[i] = multiChunkGenerator.getChunk(i);
                chunks[i].bindMeshData();
                multiChunkGenerator.setDone(false, i);
            }
        }
    }

    public void updateRenderDistance() {

    }

    public void delete() {
//        for (SingleChunkGenerator chunkGenerator : primaryChunkGenerators) {
//            chunkGenerator.delete();
//        }

//        for (SingleChunkGenerator chunkGenerator : secondaryChunkGenerators) {
//            chunkGenerator.delete();
//        }

        multiChunkGenerator.delete();
    }
}
