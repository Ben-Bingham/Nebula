package ca.benbingham.game.planetstructure.planetgeneration;

import ca.benbingham.game.Quad;
import ca.benbingham.game.blocks.BlockList;
import ca.benbingham.game.planetstructure.Block;
import ca.benbingham.game.planetstructure.Chunk;
import ca.benbingham.game.planetstructure.Mesh;
import ca.benbingham.game.planetstructure.enums.EBlockName;
import org.joml.Matrix4f;
import org.joml.Vector2i;

import java.util.ArrayList;

import static ca.benbingham.engine.util.ArrayUtil.floatListToArray;
import static ca.benbingham.engine.util.ArrayUtil.intListToArray;

public class TerrainGenerator {
    private final BlockList blockList;
    private final short stoneID;
    private final short airID;

    public TerrainGenerator(BlockList masterBlockList) {
        this.blockList = masterBlockList;

        stoneID = blockList.getBlockIDWithName(EBlockName.STONE);
        airID = blockList.getBlockIDWithName(EBlockName.AIR);
    }

    public Mesh createChunkMesh(Chunk chunk, Chunk posXChunk, Chunk negXChunk, Chunk posYChunk, Chunk negYChunk) {
        int posX, negX, posY, negY, posZ, negZ;
        short posXNeighbour, negXNeighbour, posYNeighbour, negYNeighbour, posZNeighbour, negZNeighbour;
        Block currentBlock;
        Quad currentFace = new Quad();
        Mesh chunkMesh = new Mesh();
        int count = 0;
        ArrayList<Integer> totalIndices = new ArrayList<>();
        ArrayList<Float> totalVertices = new ArrayList<>();
        float[] tempFaceVertices;

        for (int i = 0; i < Chunk.xSize; i++) {
            for (int j = 0; j < Chunk.ySize; j++) {
                for (int k = 0; k < Chunk.zSize; k++) {

                    posX = i + 1;
                    negX = i - 1;
                    posY = j + 1;
                    negY = j - 1;
                    posZ = k + 1;
                    negZ = k - 1;

                    if (posX > Chunk.xSize - 1) {
                        if (posXChunk != null) {
                            posXNeighbour = posXChunk.getBlocks()[0][j][k];
                        }
                        else {
                            posXNeighbour = airID;
                        }
                    }
                    else {
                        posXNeighbour = chunk.getBlocks()[posX][j][k];
                    }

                    if (negX < 0) {
                        if (negXChunk != null) {
                            negXNeighbour = negXChunk.getBlocks()[Chunk.xSize - 1][j][k];
                        }
                        else {
                            negXNeighbour = airID;
                        }
                    }
                    else {
                        negXNeighbour = chunk.getBlocks()[negX][j][k];
                    }


                    if (posY > Chunk.ySize - 1) {
                        posYNeighbour = airID;
                    }
                    else {
                        posYNeighbour = chunk.getBlocks()[i][posY][k];
                    }

                    if (negY < 0) {
                        negYNeighbour = airID;
                    }
                    else {
                        negYNeighbour = chunk.getBlocks()[i][negY][k];
                    }


                    if (posZ > Chunk.zSize - 1) {
                        if (posYChunk != null) {
                            posZNeighbour = posYChunk.getBlocks()[i][j][0];
                        }
                        else {
                            posZNeighbour = airID;
                        }
                    }
                    else {
                        posZNeighbour = chunk.getBlocks()[i][j][posZ];
                    }

                    if (negZ < 0) {
                        if (negYChunk != null) {
                            negZNeighbour = negYChunk.getBlocks()[i][j][Chunk.zSize - 1];
                        }
                        else {
                            negZNeighbour = airID;
                        }
                    } else {
                        negZNeighbour = chunk.getBlocks()[i][j][negZ];
                    }

                    if (chunk.getBlocks()[i][j][k] != airID) {
                        currentBlock = blockList.getBlockWithID(chunk.getBlocks()[i][j][k]);
                        for (int l = 0; l < currentBlock.getFaces().length; l++) {
                            if (l == 0 && posXNeighbour == airID || l == 1 && negXNeighbour == airID || l == 2 && posYNeighbour == airID || l == 3 && negYNeighbour == airID || l == 4 && posZNeighbour == airID || l == 5 && negZNeighbour == airID) {
                                currentFace.importData(currentBlock.getFaces()[l].getFloatArrayOfQuad());

                                currentFace.translate(new Matrix4f().translation(i, j, k));

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

        chunkMesh.setNumberOfVertices(count);
        chunkMesh.setVertexData(floatListToArray(totalVertices));
        chunkMesh.setIndexData(intListToArray(totalIndices));

        return chunkMesh;
    }

    public short[][][] createShortArrayForChunk(Vector2i chunkCords) {
        short[][][] blocks = new short[Chunk.xSize][Chunk.ySize][Chunk.zSize];
        int val = 60;

        for (int i = 0; i < Chunk.xSize; i++) {
            for (int j = 0; j < Chunk.ySize; j++) {
                for (int k = 0; k < Chunk.zSize; k++) {
                    //val = (int) Math.ceil(-0.5 * (i^2 - 65));
                    if (j < val) {
                        blocks[i][j][k] = stoneID;
                    } else {
                        blocks[i][j][k] = airID;
                    }
                }
            }
        }

        return blocks;
    }
}