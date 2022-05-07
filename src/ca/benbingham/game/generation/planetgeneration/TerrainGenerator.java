package ca.benbingham.game.generation.planetgeneration;

import ca.benbingham.engine.graphics.renderingdata.BlockRenderingData;
import ca.benbingham.game.interstellarobjects.bodys.CosmicBody;
import ca.benbingham.game.interstellarobjects.bodys.Moon;
import ca.benbingham.game.interstellarobjects.bodys.Planet;
import ca.benbingham.game.interstellarobjects.bodys.Star;
import ca.benbingham.game.planetstructure.blocks.BlockList;
import ca.benbingham.game.planetstructure.BlockFace;
import ca.benbingham.game.planetstructure.Chunk;
import ca.benbingham.game.planetstructure.enums.EPlanetTypes;
import ca.benbingham.game.planetstructure.geometry.Mesh;
import ca.benbingham.game.planetstructure.enums.EBlockName;
import ca.benbingham.game.generation.noise.TerrainNoiseGenerator;
import org.joml.Matrix4f;
import org.joml.Vector2i;

import java.util.ArrayList;

import static ca.benbingham.engine.util.ArrayUtil.floatListToArray;
import static ca.benbingham.engine.util.ArrayUtil.intListToArray;

public class TerrainGenerator {
    private final BlockList blockList;
    private final TerrainNoiseGenerator terrainNoiseGenerator;
    private final CosmicBody cosmicBody;

    private final short stoneID;
    private final short dirtID;
    private final short planetCoreID;
    private final short grassID;
    private final short airID;
    private final short redID;
    private final short blueID;
    private final short greenID;
    private final short yellowID;

    public TerrainGenerator(BlockList masterBlockList, CosmicBody cosmicBody) {
        this.blockList = masterBlockList;
        this.cosmicBody = cosmicBody;
        terrainNoiseGenerator = new TerrainNoiseGenerator(this.cosmicBody);

        stoneID = blockList.getBlockIDWithName(EBlockName.STONE);
        airID = blockList.getBlockIDWithName(EBlockName.AIR);
        dirtID = blockList.getBlockIDWithName(EBlockName.DIRT);
        planetCoreID = blockList.getBlockIDWithName(EBlockName.PLANET_CORE);
        grassID = blockList.getBlockIDWithName(EBlockName.GRASS);
        redID = blockList.getBlockIDWithName(EBlockName.RED);
        blueID = blockList.getBlockIDWithName(EBlockName.BLUE);
        greenID = blockList.getBlockIDWithName(EBlockName.GREEN);
        yellowID = blockList.getBlockIDWithName(EBlockName.YELLOW);
    }

    public Mesh createChunkMesh(Chunk chunk, Chunk posXChunk, Chunk negXChunk, Chunk posYChunk, Chunk negYChunk) {
        short posXNeighbour, negXNeighbour, posYNeighbour, negYNeighbour, posZNeighbour, negZNeighbour;
//        Block currentBlock;
        BlockRenderingData currentBlockData;
        BlockFace currentFace = new BlockFace();
        Mesh chunkMesh = new Mesh();
        int count = 0;
        ArrayList<Integer> totalIndices = new ArrayList<>();
        ArrayList<Float> totalVertices = new ArrayList<>();
        float[] tempFaceVertices;

        short[][][] activeChunkBlocks = chunk.getBlocks();

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
                        posXNeighbour = activeChunkBlocks[(i + 1)][j][k];
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
                        negXNeighbour = activeChunkBlocks[(i - 1)][j][k];
                    }


                    if ((j + 1) > Chunk.ySize - 1) {
                        posYNeighbour = airID;
                    }
                    else {
                        posYNeighbour = activeChunkBlocks[i][(j + 1)][k];
                    }

                    if ((j - 1) < 0) {
                        negYNeighbour = airID;
                    }
                    else {
                        negYNeighbour = activeChunkBlocks[i][(j - 1)][k];
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
                        posZNeighbour = activeChunkBlocks[i][j][(k + 1)];
                    }

                    if ((k - 1) < 0) {
                        if (negYChunk != null) {
                            negZNeighbour = negYChunk.getBlocks()[i][j][Chunk.zSize - 1];
                        }
                        else {
                            negZNeighbour = airID;
                        }
                    } else {
                        negZNeighbour = activeChunkBlocks[i][j][(k - 1)];
                    }

                    if (activeChunkBlocks[i][j][k] != airID) {
                        currentBlockData = blockList.getBlockWithID(activeChunkBlocks[i][j][k]).getRenderingData();
                        for (int l = 0; l < currentBlockData.getFaces().length; l++) {
                            if ((l == 0 && posXNeighbour == airID) || (l == 1 && negXNeighbour == airID) || (l == 2 && posYNeighbour == airID) || (l == 3 && negYNeighbour == airID) || (l == 4 && posZNeighbour == airID) || (l == 5 && negZNeighbour == airID)) {

                                currentFace.getQuad().importData(currentBlockData.getFaces()[l].getQuad().getFloatArrayOfQuad());
                                currentFace.getQuad().translate(new Matrix4f().translation(i, j, k));

                                for (int m = 0; m < currentFace.getQuad().getIndices().length; m++) {
                                    totalIndices.add(currentFace.getQuad().getIndices()[m] + count * 4);
                                }

                                for (int m = 0; m < currentFace.getQuad().vertices.length; m++) {
                                    tempFaceVertices = currentFace.getQuad().vertices[m].getAsFloatArray();
                                    for (int n = 0; n < 8; n++) {
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
        double[][] noiseValues = terrainNoiseGenerator.generateNoiseForChunk(chunkCords);

        for (int i = 0; i < Chunk.xSize; i++) {
            for (int j = 0; j < Chunk.ySize; j++) {
                for (int k = 0; k < Chunk.zSize; k++) {

                    if (cosmicBody instanceof Planet) {
                        if (((Planet) cosmicBody).getType() == EPlanetTypes.NEBULA_DEFAULT_PLANET) {
                            if (j == noiseValues[i][k]) {
                                blocks[i][j][k] = grassID;
                            }
                            else if (j < noiseValues[i][k] && j >= noiseValues[i][k] - 3) {
                                blocks[i][j][k] = dirtID;
                            }
                            else if (j < noiseValues[i][k] - 3 && j >= 1) {
                                blocks[i][j][k] = stoneID;
                            }
                            else if (j < 1) {
                                blocks[i][j][k] = planetCoreID;
                            }
                            else {
                                blocks[i][j][k] = airID;
                            }
                        }

                        // Generation testing 2
//                    if (noiseValues[i][k] != 0) {
//                        if (j <= noiseValues[i][k]) {
//                            for (int l = 0; l < Chunk.ySize; l += 10) {
//                                if (j == 1 + l || j == 2 + l) {
//                                    blocks[i][j][k] = redID;
//                                    break;
//                                } else if (j == 3 + l || j == 4 + l) {
//                                    blocks[i][j][k] = blueID;
//                                    break;
//                                } else if (j == 5 + l || j == 6 + l) {
//                                    blocks[i][j][k] = greenID;
//                                    break;
//                                } else if (j == 7 + l || j == 8 + l){
//                                    blocks[i][j][k] = yellowID;
//                                    break;
//                                } else if (j == 9 + l || j == 0 + l) {
//                                    blocks[i][j][k] = planetCoreID;
//                                    break;
//                                }
//                            }
//                        }
//                    }
                    }
                    else if (cosmicBody instanceof Moon) {
                        if (j < 60) {
                            blocks[i][j][k] = blueID;
                        }
                    }
                    else if (cosmicBody instanceof Star) {
                        if (j < 60) {
                            blocks[i][j][k] = redID;
                        }
                    }
                }
            }
        }
        return blocks;
    }
}