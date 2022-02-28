package ca.benbingham.game.gameclasses;

import ca.benbingham.game.Quad;
import ca.benbingham.game.worldstructure.Block;
import ca.benbingham.game.worldstructure.Chunk;
import ca.benbingham.game.worldstructure.Crumb;
import ca.benbingham.game.worldstructure.Planet;
import ca.benbingham.game.worldstructure.enums.EPlanetBiome;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;

import static ca.benbingham.engine.util.ArrayUtil.*;
import static ca.benbingham.engine.util.Printing.print;

public class Updater {
    private Game game;

    private float[] worldVertices;

    private int[] worldIndices;

    private Planet testPlanet;

    private Block[][][] oldChunkData;

    private int numberOfVertices;

    private boolean blockUpdate = true;

    private long numb = 0;

    public Updater(Game game) {
        this.game = game;
    }

    public void init() {
        testPlanet = new Planet(EPlanetBiome.FOREST, "Test", 10);
    }

    public void update() {
        if (blockUpdate) {
            recreateCrumbMesh(testPlanet);
            blockUpdate = false;
        }

        //game.setNewWorldData(testCrumb.blocks != oldChunkData);

        //oldChunkData = testCrumb.blocks;

        //print(numb);
    }

    public void destroy() {

    }

//    private int[] testIndices = {
//            0, 1, 2, 3, 4, 5,
//            6, 7, 8, 9, 10, 11,
//            12, 13, 14, 15, 16, 17,
//            18, 19, 20, 21, 22, 23,
//            24, 25, 26, 27, 28, 29,
//            30, 31, 32, 33, 34, 35
//    };
//
//    private float[] testVertices = {
//            -0.5f, -0.5f, -0.5f,  0.0f, 0.0f,
//            0.5f, -0.5f, -0.5f,  1.0f, 0.0f,
//            0.5f,  0.5f, -0.5f,  1.0f, 1.0f,
//            0.5f,  0.5f, -0.5f,  1.0f, 1.0f,
//            -0.5f,  0.5f, -0.5f,  0.0f, 1.0f,
//            -0.5f, -0.5f, -0.5f,  0.0f, 0.0f,
//
//            -0.5f, -0.5f,  0.5f,  0.0f, 0.0f,
//            0.5f, -0.5f,  0.5f,  1.0f, 0.0f,
//            0.5f,  0.5f,  0.5f,  1.0f, 1.0f,
//            0.5f,  0.5f,  0.5f,  1.0f, 1.0f,
//            -0.5f,  0.5f,  0.5f,  0.0f, 1.0f,
//            -0.5f, -0.5f,  0.5f,  0.0f, 0.0f,
//
//            -0.5f,  0.5f,  0.5f,  1.0f, 0.0f,
//            -0.5f,  0.5f, -0.5f,  1.0f, 1.0f,
//            -0.5f, -0.5f, -0.5f,  0.0f, 1.0f,
//            -0.5f, -0.5f, -0.5f,  0.0f, 1.0f,
//            -0.5f, -0.5f,  0.5f,  0.0f, 0.0f,
//            -0.5f,  0.5f,  0.5f,  1.0f, 0.0f,
//
//            0.5f,  0.5f,  0.5f,  1.0f, 0.0f,
//            0.5f,  0.5f, -0.5f,  1.0f, 1.0f,
//            0.5f, -0.5f, -0.5f,  0.0f, 1.0f,
//            0.5f, -0.5f, -0.5f,  0.0f, 1.0f,
//            0.5f, -0.5f,  0.5f,  0.0f, 0.0f,
//            0.5f,  0.5f,  0.5f,  1.0f, 0.0f,
//
//            -0.5f, -0.5f, -0.5f,  0.0f, 1.0f,
//            0.5f, -0.5f, -0.5f,  1.0f, 1.0f,
//            0.5f, -0.5f,  0.5f,  1.0f, 0.0f,
//            0.5f, -0.5f,  0.5f,  1.0f, 0.0f,
//            -0.5f, -0.5f,  0.5f,  0.0f, 0.0f,
//            -0.5f, -0.5f, -0.5f,  0.0f, 1.0f,
//
//            -0.5f,  0.5f, -0.5f,  0.0f, 1.0f,
//            0.5f,  0.5f, -0.5f,  1.0f, 1.0f,
//            0.5f,  0.5f,  0.5f,  1.0f, 0.0f,
//            0.5f,  0.5f,  0.5f,  1.0f, 0.0f,
//            -0.5f,  0.5f,  0.5f,  0.0f, 0.0f,
//            -0.5f,  0.5f, -0.5f,  0.0f, 1.0f
//    };

    private void recreateCrumbMesh(Planet planet) {
        int count = 0;

        Quad currentFace = new Quad();

        ArrayList<Integer> totalIndices = new ArrayList<>();
        ArrayList<Float> totalVertices = new ArrayList<>();

        Vector3f blockAbsoluteLocation;

        float[] tempFaceVertices;
        for (int x = 0; x < planet.getSize(); x++) {
            for (int y = 0; y < planet.getSize(); y++) {

                for (int o = 0; o < planet.chunks[x][y].getChunkSize(); o++) {
                    for (int p = 0; p <planet.chunks[x][y].getChunkSize(); p++) {

                        for (int i = 0; i < planet.chunks[x][y].crumbs[o][p].getCrumbSizeX(); i++) {
                            for (int j = 0; j < planet.chunks[x][y].crumbs[o][p].getCrumbSizeY(); j++) {
                                for (int k = 0; k < planet.chunks[x][y].crumbs[o][p].getCrumbSizeZ(); k++) {

                                    for (int l = 0; l < planet.chunks[x][y].crumbs[o][p].blocks[i][j][k].getFaces().length; l++) {
                                        if (planet.chunks[x][y].crumbs[o][p].blocks[i][j][k].getFaces()[l] != null) {
                                            currentFace.importData(planet.chunks[x][y].crumbs[o][p].blocks[i][j][k].getFaces()[l].convertToFloatArray());

                                            blockAbsoluteLocation = planet.chunks[x][y].crumbs[o][p].blocks[i][j][k].getAbsoluteLocation();
                                            currentFace.translate(new Matrix4f().translation(blockAbsoluteLocation.x, blockAbsoluteLocation.y, blockAbsoluteLocation.z));

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
                }
            }
        }

        print(count);

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
