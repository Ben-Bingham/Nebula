package ca.benbingham.game.worldstructure;

import ca.benbingham.game.worldstructure.enums.EPlanetBiome;
import org.joml.Vector2f;

public class Planet {

    private final EPlanetBiome biome;
    private final String name;

    private final int size;

    public Chunk[][] chunks;

    public Planet(EPlanetBiome biome, String name, int size) {
        this.biome = biome;
        this.size = size;

        this.name = name;

        chunks = new Chunk[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                chunks[i][j] = new Chunk(this, new Vector2f(i, j));
            }
        }

        initChunks();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                chunks[i][j].initCrumbs();
            }
        }

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                for (int k = 0; k < chunks[i][j].getChunkSize(); k++) {
                    for (int l = 0; l < chunks[i][j].getChunkSize(); l++) {
                        chunks[i][j].crumbs[k][l].initBlocks();
                    }
                }
            }
        }
    }

    public void initChunks() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                chunks[i][j].setNeighbours();
            }
        }
    }

    public int getSize() {
        return size;
    }
}
