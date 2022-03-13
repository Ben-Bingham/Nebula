package ca.benbingham.game.planetstructure.planetgeneration;

import ca.benbingham.game.Quad;
import ca.benbingham.game.planetstructure.Block;
import ca.benbingham.game.planetstructure.Chunk;
import ca.benbingham.game.planetstructure.Mesh;
import ca.benbingham.game.planetstructure.enums.EBlockName;
import org.joml.Matrix4f;
import org.joml.Vector2i;
import org.joml.Vector3f;

import java.util.ArrayList;

import static ca.benbingham.engine.util.ArrayUtil.floatListToArray;
import static ca.benbingham.engine.util.ArrayUtil.intListToArray;

public class TerrainGenerator {
    public Mesh createChunkMesh(Vector2i chunkCords) {
        Block[][][] blocks = new Block[Chunk.xSize][Chunk.ySize][Chunk.zSize];

        for (int i = 0; i < Chunk.xSize; i++) {
            for (int j = 0; j < Chunk.ySize; j++) {
                for (int k = 0; k < Chunk.zSize; k++) {
                    if (j < 60) {
                        blocks[i][j][k] = new Block(EBlockName.CONTAINER, new Vector3f(i, j, k), chunkCords);
                    } else {
                        blocks[i][j][k] = new Block(EBlockName.AIR, new Vector3f(i, j, k), chunkCords);
                    }
                }
            }
        }

        for (int i = 0; i < Chunk.xSize; i++) {
            for (int j = 0; j < Chunk.ySize; j++) {
                for (int k = 0; k < Chunk.zSize; k++) {
                    blocks[i][j][k].setNeighbours(blocks);
                    blocks[i][j][k].determineVisibleFaces();
                }
            }
        }

        return createMeshFromBlocksArray(blocks);
    }


    private Mesh createMeshFromBlocksArray(Block[][][] blocks) {
        Mesh mesh = new Mesh();

        int count = 0;

        Quad currentFace = new Quad();

        ArrayList<Integer> totalIndices = new ArrayList<>();
        ArrayList<Float> totalVertices = new ArrayList<>();

        Vector3f blockRelativeLocation;


        float[] tempFaceVertices;

        for (int i = 0; i < Chunk.xSize; i++) {
            for (int j = 0; j < Chunk.ySize; j++) {
                for (int k = 0; k < Chunk.zSize; k++) {

                    for (int l = 0; l < blocks[i][j][k].getFaces().length; l++) {
                        if (blocks[i][j][k].getFaces()[l] != null) {
                            currentFace.importData(blocks[i][j][k].getFaces()[l].convertToFloatArray());

                            blockRelativeLocation = blocks[i][j][k].getRelativeLocation();
                            currentFace.translate(new Matrix4f().translation(blockRelativeLocation.x, blockRelativeLocation.y, blockRelativeLocation.z));


                            for (int m = 0; m < currentFace.getIndices().length; m++) {
                                totalIndices.add(currentFace.getIndices()[m] + count * 4);
                            }

                            for (int m = 0; m < currentFace.vertices.length; m++) {
                                tempFaceVertices = currentFace.vertices[m].getAsFloatArray();
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

        mesh.setNumberOfVertices(count);
        mesh.setVertexData(floatListToArray(totalVertices));
        mesh.setIndexData(intListToArray(totalIndices));

        return mesh;
    }
}