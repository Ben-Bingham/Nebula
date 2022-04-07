package ca.benbingham.game.planetstructure.planetgeneration;

import ca.benbingham.engine.util.Timer;
import ca.benbingham.game.Quad;
import ca.benbingham.game.blocks.BlockList;
import ca.benbingham.game.planetstructure.Block;
import ca.benbingham.game.planetstructure.BlockFace;
import ca.benbingham.game.planetstructure.Chunk;
import ca.benbingham.game.planetstructure.Mesh;
import ca.benbingham.game.planetstructure.enums.EBlockName;
import org.joml.Matrix4f;
import org.joml.Vector2i;

import java.util.ArrayList;
import java.util.Arrays;

import static ca.benbingham.engine.util.ArrayUtil.floatListToArray;
import static ca.benbingham.engine.util.ArrayUtil.intListToArray;
import static ca.benbingham.engine.util.Printing.print;

public class TerrainGenerator {
    private final BlockList blockList;
    private final short stoneID;
    private final short dirtID;
    private final short bedrockID;
    private final short grassID;
    private final short airID;
    private Timer timer = new Timer();

    public TerrainGenerator(BlockList masterBlockList) {
        this.blockList = masterBlockList;

        stoneID = blockList.getBlockIDWithName(EBlockName.STONE);
        airID = blockList.getBlockIDWithName(EBlockName.AIR);
        dirtID = blockList.getBlockIDWithName(EBlockName.DIRT);
        bedrockID = blockList.getBlockIDWithName(EBlockName.BEDROCK);
        grassID = blockList.getBlockIDWithName(EBlockName.GRASS);
    }

    public Mesh createChunkMesh(Chunk chunk, Chunk posXChunk, Chunk negXChunk, Chunk posYChunk, Chunk negYChunk) {
        short posXNeighbour, negXNeighbour, posYNeighbour, negYNeighbour, posZNeighbour, negZNeighbour;
        Block currentBlock;
        BlockFace currentFace = new BlockFace();
        Mesh chunkMesh = new Mesh();
        int count = 0;
        ArrayList<Integer> totalIndices = new ArrayList<>();
        ArrayList<Float> totalVertices = new ArrayList<>();
        float[] tempFaceVertices;

        int temp = 0;

        for (int i = 0; i < Chunk.xSize; i++) {
            for (int j = 0; j < Chunk.ySize; j++) {
                for (int k = 0; k < Chunk.zSize; k++) {
                    if ((i + 1) > Chunk.xSize - 1) {
                        if (posXChunk != null) {
                            posXNeighbour = posXChunk.getBlocks()[0][j][k];
                        }
                        else {
                            posXNeighbour = airID;
                        }
                    }
                    else {
                        posXNeighbour = chunk.getBlocks()[(i + 1)][j][k];
                    }

                    if ((i - 1) < 0) {
                        if (negXChunk != null) {
                            negXNeighbour = negXChunk.getBlocks()[Chunk.xSize - 1][j][k];
                        }
                        else {
                            negXNeighbour = airID;
                        }
                    }
                    else {
                        negXNeighbour = chunk.getBlocks()[(i - 1)][j][k];
                    }


                    if ((j + 1) > Chunk.ySize - 1) {
                        posYNeighbour = airID;
                    }
                    else {
                        posYNeighbour = chunk.getBlocks()[i][(j + 1)][k];
                    }

                    if ((j - 1) < 0) {
                        negYNeighbour = airID;
                    }
                    else {
                        negYNeighbour = chunk.getBlocks()[i][(j - 1)][k];
                    }


                    if ((k + 1) > Chunk.zSize - 1) {
                        if (posYChunk != null) {
                            posZNeighbour = posYChunk.getBlocks()[i][j][0];
                        }
                        else {
                            posZNeighbour = airID;
                        }
                    }
                    else {
                        posZNeighbour = chunk.getBlocks()[i][j][(k + 1)];
                    }

                    if ((k - 1) < 0) {
                        if (negYChunk != null) {
                            negZNeighbour = negYChunk.getBlocks()[i][j][Chunk.zSize - 1];
                        }
                        else {
                            negZNeighbour = airID;
                        }
                    } else {
                        negZNeighbour = chunk.getBlocks()[i][j][(k - 1)];
                    }

                    if (chunk.getBlocks()[i][j][k] != airID) {
                        currentBlock = blockList.getBlockWithID(chunk.getBlocks()[i][j][k]);
                        for (int l = 0; l < currentBlock.getFaces().length; l++) {
                            if ((l == 0 && posXNeighbour == airID) || (l == 1 && negXNeighbour == airID) || (l == 2 && posYNeighbour == airID) || (l == 3 && negYNeighbour == airID) || (l == 4 && posZNeighbour == airID) || (l == 5 && negZNeighbour == airID)) {

                                currentFace.getQuad().importData(currentBlock.getFaces()[l].getQuad().getFloatArrayOfQuad());
                                currentFace.getQuad().translate(new Matrix4f().translation(i, j, k));

                                for (int m = 0; m < currentFace.getQuad().getIndices().length; m++) {
                                    totalIndices.add(currentFace.getQuad().getIndices()[m] + count * 4);
                                }

                                for (int m = 0; m < currentFace.getQuad().vertices.length; m++) {
                                    tempFaceVertices = currentFace.getQuad().vertices[m].getAsFloatArray();
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
                    //val = (int) Math.ceil((chunkCords.x ^ 2) - (chunkCords.y ^ 2));
                    if (chunkCords.x != 0) {
                        if (j == 60) {
                            blocks[i][j][k] = grassID;
                        }
                        else if (j < 60 && j >= 57) {
                            blocks[i][j][k] = dirtID;
                        }
                        else if (j < 57 && j >= 6) {
                            blocks[i][j][k] = stoneID;
                        }
                        else if (j < 6) {
                            blocks[i][j][k] = bedrockID;
                        }
                        else {
                            blocks[i][j][k] = airID;
                        }
                    }


                }
            }
        }

        return blocks;
    }
}