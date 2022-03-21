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
import static ca.benbingham.engine.util.Printing.print;

public class TerrainGenerator {
    public Mesh createChunkMesh(Vector2i chunkCords) {
        Block[][][] blocks = createBlockArrayForChunk(chunkCords);
        Block positiveXNeighbour;
        Block negativeXNeighbour;
        Block positiveYNeighbour;
        Block negativeYNeighbour;

        for (int i = 0; i < Chunk.xSize; i++) {
            for (int j = 0; j < Chunk.ySize; j++) {
                for (int k = 0; k < Chunk.zSize; k++) {
                    positiveXNeighbour = findBlockAtPosition(chunkCords, new Vector3f(i + 1, j, k)); //TODO Only works because of the basics of the algorithm
                    negativeXNeighbour = findBlockAtPosition(chunkCords, new Vector3f(i - 1, j, k));
                    positiveYNeighbour = findBlockAtPosition(chunkCords, new Vector3f(i, j, k + 1));
                    negativeYNeighbour = findBlockAtPosition(chunkCords, new Vector3f(i, j, k - 1));

                    blocks[i][j][k].setNeighbours(blocks, positiveXNeighbour, negativeXNeighbour, positiveYNeighbour, negativeYNeighbour);
                    blocks[i][j][k].determineVisibleFaces();
                }
            }
        }

        return createMeshFromBlocksArray(blocks);
    }

    private Block[][][] createBlockArrayForChunk(Vector2i chunkCords) {
        Block[][][] blocks = new Block[Chunk.xSize][Chunk.ySize][Chunk.zSize];

        int val = 60;

        for (int i = 0; i < Chunk.xSize; i++) {
            for (int j = 0; j < Chunk.ySize; j++) {
                for (int k = 0; k < Chunk.zSize; k++) {
                    //val = (int) Math.ceil(-0.5 * (i^2 - 65));
                    if (j < val) {
                        blocks[i][j][k] = new Block(EBlockName.CONTAINER, new Vector3f(i, j, k), chunkCords);
                    } else {
                        blocks[i][j][k] = new Block(EBlockName.AIR, new Vector3f(i, j, k), chunkCords);
                    }
                }
            }
        }

        return blocks;
    }

    private Block findBlockAtPosition(Vector2i chunkCords, Vector3f cords) {
        if (cords.y < 60) {
            return new Block(EBlockName.CONTAINER, cords, chunkCords);
        }
        else {
            return new Block(EBlockName.AIR, cords, chunkCords);
        }
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
                    if (blocks[i][j][k].getName() != EBlockName.AIR) {
                        if (!blocks[i][j][k].isVisibleFace())

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
        }

        mesh.setNumberOfVertices(count);
        mesh.setVertexData(floatListToArray(totalVertices));
        mesh.setIndexData(intListToArray(totalIndices));

        //print("Chunk created");

        return mesh;
    }
}