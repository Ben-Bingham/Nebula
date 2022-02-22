package ca.benbingham.game.threads;

import ca.benbingham.game.Quad;
import ca.benbingham.game.worldstructure.Chunk;
import org.joml.Matrix4f;

import java.util.ArrayList;

import static ca.benbingham.engine.util.ArrayUtil.*;
import static ca.benbingham.engine.util.Printing.print;

public class UpdateThread extends Thread{
    private final SyncThread syncThread;
    private int width;
    private int height;

    private Game game;

    private float deltaTime = 0.0f;
    private float lastFrame = 0.0f;

    private boolean meshCalculated = false;
    private int count;

    private float[] worldVertices;
    private float[] oldWorldVertices;

    private int[] worldIndices;
    private int[] oldWorldIndices;

    private Chunk testChunk;

    int test = 0;

    // flags
    private boolean startProcess = false;
    private int numberOfVertices;

    public UpdateThread(SyncThread syncThread) {
        this.syncThread = syncThread;
        this.game = syncThread.getGame();

        this.height = game.getHeight();
        this.width = game.getWidth();
    }

    @Override
    public void run() {
        print("Update thread created");
        init();

        // Game loop
        while (syncThread.isGameOpen()) {
            update();
        }
        
        destroy();
    }

    private void init() {
        testChunk = new Chunk();

        for (int i = 0; i < testChunk.getChunkSizeX(); i++) {
            for (int j = 0; j < testChunk.getChunkSizeY(); j++) {
                for (int k = 0; k < testChunk.getChunkSizeZ(); k++) {
                    testChunk.blocks[i][j][k].setNeighbours();
                    testChunk.blocks[i][j][k].determineVisibleFaces();
                }
            }
        }
    }
    
    private void update() {
        if (startProcess) {
            frameProcess();
        }
    }

    private void frameProcess() {
        //print("update frame process");

        startProcess = false;
        recreateChunkMesh(testChunk);

        syncThread.setFirstFrameDataCreated(true);



        if (test < 60) {
            syncThread.setNewWorldData(true);
            test ++;
        }
        else {
            syncThread.setNewWorldData(false);

        }





        //syncThread.setNewWorldDataFlag(worldVertices != oldWorldVertices || worldIndices != oldWorldIndices);

        oldWorldVertices = worldVertices;
        oldWorldIndices = worldIndices;

        //try { wait(); } catch (InterruptedException ignored) { }
        syncThread.toggleUpdateThreadStatusFlag();
    }

    public void destroy() {

    }

    private void recreateChunkMesh(Chunk chunk) {
        int count = 0;

        Quad tempFace = new Quad();

        ArrayList<Integer> totalIndices = new ArrayList<>();
        ArrayList<Float> totalVertices = new ArrayList<>();
        float[] tempFaceVertices;

        for (int i = 0; i < chunk.getChunkSizeX(); i++) {
            for (int j = 0; j < chunk.getChunkSizeY(); j++) {
                for (int k = 0; k < chunk.getChunkSizeZ(); k++) {
                    for (int l = 0; l < chunk.blocks[i][j][k].getFaces().length; l++) {
                        if (chunk.blocks[i][j][k].getFaces()[l] != null) {
                            tempFace.importData(chunk.blocks[i][j][k].getFaces()[l].convertToFloatArray());

                            tempFace.translate(new Matrix4f().translation(i, j, k));

                            for (int m = 0; m < tempFace.getIndices().length; m++) {
                                totalIndices.add(tempFace.getIndices()[m] + count * 4);
                            }

                            for (int m = 0; m < tempFace.vertices.length; m++) {
                                tempFaceVertices = tempFace.vertices[m].getAsFloatArray();
                                for (int n = 0; n < 5; n++) {
                                    totalVertices.add(tempFaceVertices[n]);
                                }
                            }

                            count++;
                        }
                    }
                }
            }
            numberOfVertices = count;
        }

        worldVertices = floatListToArray(totalVertices);
        worldIndices = intListToArray(totalIndices);

    }

    public float[] getWorldVertexData() {
        return worldVertices;
    }

    public int[] getWorldIndexData() {
        return worldIndices;
    }

    public int getCount() {
        return count;
    }

    public synchronized void setStartProcess(boolean startProcess) {
        this.startProcess = startProcess;
    }

    public int getNumberOfVertices() {
        return numberOfVertices;
    }
}
