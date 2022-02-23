package ca.benbingham.game.gameclasses;

import ca.benbingham.game.Quad;
import ca.benbingham.game.worldstructure.Block;
import ca.benbingham.game.worldstructure.Chunk;
import org.joml.Matrix4f;

import java.util.ArrayList;

import static ca.benbingham.engine.util.ArrayUtil.*;
import static ca.benbingham.engine.util.Printing.print;

public class Updater {
    private Game game;

    private float[] worldVertices;

    private int[] worldIndices;

    private Chunk testChunk;

    private Block[][][] oldChunkData;

    private int numberOfVertices;

    private boolean blockUpdate = true;

    public Updater(Game game) {
        this.game = game;
    }

    public void init() {
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
    
    public void update() {
        if (blockUpdate) {
            recreateChunkMesh(testChunk);
            blockUpdate = false;
        }

        game.setNewWorldData(testChunk.blocks != oldChunkData);

        oldChunkData = testChunk.blocks;
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
        }
        numberOfVertices = count;

        worldVertices = floatListToArray(totalVertices);
        worldIndices = intListToArray(totalIndices);

    }

    public float[] getWorldVertexData() {
        return worldVertices;
    }

    public int[] getWorldIndexData() {
        return worldIndices;
    }

    public int getNumberOfVertices() {
        return numberOfVertices;
    }
}
