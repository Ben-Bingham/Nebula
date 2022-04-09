package ca.benbingham.game.gameclasses;

import ca.benbingham.game.planetstructure.blocks.BlockList;
import ca.benbingham.game.gameclasses.renderers.MasterRenderer;
import ca.benbingham.game.planetstructure.Chunk;

import ca.benbingham.game.planetstructure.planetgeneration.SingleChunkGenerator;

import org.joml.Vector2i;
import org.joml.Vector3f;

public class Game {
    private MasterRenderer masterRenderer;
    private boolean gameOpen = true;

    private Vector3f playerPosition = new Vector3f(0, 0, 0);

    private Vector2i playerChunkCords;
    private Vector2i lastPlayerChunk;
    private final int renderDistance = 12;

    private BlockList masterBlockList;

    private SingleChunkGenerator playerChunkGenerator;
    private Chunk playerChunk;

    private SingleChunkGenerator[] secondaryChunkGenerators;
    private Chunk[] secondaryChunks;

    private boolean beforeFirstChunkCrossing = true;

    public void start() {
        init();

        while (gameOpen) {
            masterRenderer.firstUpdate();

            playerChunkCords.x = (int) Math.floor(playerPosition.x / Chunk.xSize);
            playerChunkCords.y = (int) Math.floor(playerPosition.z / Chunk.zSize);

            if (playerChunkCords.x != lastPlayerChunk.x || playerChunkCords.y != lastPlayerChunk.y || beforeFirstChunkCrossing) { // Player moves between chunks
                playerChunkGenerator.setChunkCords(playerChunkCords);
                playerChunkGenerator.unlock();
//TODO make a chunk manager and a multi chunk generator
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
                masterRenderer.chunkRenderer.renderChunk(playerChunk);
            }

            for (int i = 0; i < secondaryChunks.length; i++) {
                if (secondaryChunks[i] != null && secondaryChunks[i].hasMesh()) {
                    masterRenderer.chunkRenderer.renderChunk(secondaryChunks[i]);
                }
            }

            if (playerChunkGenerator.isDone() && playerChunkGenerator.getChunk() != null) {
                playerChunk = playerChunkGenerator.getChunk();
                playerChunk.bindMeshData();
                playerChunkGenerator.setDone(false);
            }

            for (int i = 0; i < secondaryChunkGenerators.length; i++) {
                if (secondaryChunkGenerators[i].isDone() && secondaryChunkGenerators[i].getChunk() != null) {
                    secondaryChunks[i] = secondaryChunkGenerators[i].getChunk();
                    secondaryChunks[i].bindMeshData();
                    secondaryChunkGenerators[i].setDone(false);
                }
            }

            masterRenderer.lastUpdate();
        }

        delete();
    }

    private void init() {
        masterBlockList = new BlockList();
        masterBlockList.init();

        masterRenderer = new MasterRenderer(this);

        masterRenderer.init();

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
    }

    public void resizeWindow() {
        masterRenderer.resizeWindow();
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

    public void delete() {
        masterRenderer.delete();
        playerChunkGenerator.delete();

        for (int i = 0; i < secondaryChunkGenerators.length; i++) {
            secondaryChunkGenerators[i].delete();
        }
    }
}