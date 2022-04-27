package ca.benbingham.game.gameclasses;

import ca.benbingham.game.planetstructure.blocks.BlockList;
import ca.benbingham.game.gameclasses.renderers.MasterRenderer;
import ca.benbingham.game.planetstructure.Chunk;

import ca.benbingham.game.planetstructure.bodys.Planet;
import ca.benbingham.game.planetstructure.enums.EPlanetTypes;
import ca.benbingham.game.planetstructure.planetgeneration.ChunkGenerationManager;

import ca.benbingham.game.util.FPSCounter;
import org.joml.Vector2i;
import org.joml.Vector3f;

import static ca.benbingham.engine.util.GLError.getOpenGLError;

public class Game {
    private MasterRenderer masterRenderer;
    private boolean gameOpen = true;

    private Vector3f playerPosition = new Vector3f(0, 0, 0);

    private Vector2i playerChunkCords;
    private Vector2i lastPlayerChunk;
    private final int renderDistance = 15;
    private BlockList masterBlockList;

    private Planet activePlanet;

    private ChunkGenerationManager chunkGenerationManager;

    private boolean beforeFirstChunkCrossing = true;

    FPSCounter fpsCounter = new FPSCounter(false);

    public void start() {
        init();

        while (gameOpen) {
            masterRenderer.firstUpdate();

            playerChunkCords.x = (int) Math.floor(playerPosition.x / Chunk.xSize);
            playerChunkCords.y = (int) Math.floor(playerPosition.z / Chunk.zSize);

            if (playerChunkCords.x != lastPlayerChunk.x || playerChunkCords.y != lastPlayerChunk.y || beforeFirstChunkCrossing) { // Player moves between chunks.
                chunkGenerationManager.moveBetweenChunks(playerChunkCords, beforeFirstChunkCrossing);
                beforeFirstChunkCrossing = false;
            }

            lastPlayerChunk.x = playerChunkCords.x;
            lastPlayerChunk.y = playerChunkCords.y;

            // GAME LOOP
            chunkGenerationManager.update();
            fpsCounter.update();

            masterRenderer.lastUpdate();

            getOpenGLError(new Throwable());
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

        activePlanet = new Planet(EPlanetTypes.NEBULA_DEFAULT_PLANET, 2398479234874f, 400000);

        chunkGenerationManager = new ChunkGenerationManager(renderDistance, masterRenderer.chunkRenderer, masterBlockList, activePlanet);
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

    public Vector2i getPlayerChunkCords() {
        return playerChunkCords;
    }

    public Planet getActivePlanet() {
        return activePlanet;
    }

    public void delete() {
        masterRenderer.delete();
        chunkGenerationManager.delete();
    }
}