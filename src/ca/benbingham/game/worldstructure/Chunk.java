package ca.benbingham.game.worldstructure;

import org.joml.Vector3f;

public class Chunk {
    public Block[][][] blocks;
    private final int chunkSizeX = 16;
    private final int chunkSizeY = 256;
    private final int chunkSizeZ = 16;

    public Chunk() {
        blocks = new Block[this.chunkSizeX][this.chunkSizeY][this.chunkSizeZ];

        for (int i = 0; i < chunkSizeX; i++) {
            for (int j = 0; j < chunkSizeY; j++) {
                for (int k = 0; k < chunkSizeZ; k++) {
                    blocks[i][j][k] = new Block(new Vector3f(i, j, k), this);
                }
            }
        }
    }

    public int getChunkSizeX() {
        return chunkSizeX;
    }

    public int getChunkSizeY() {
        return chunkSizeY;
    }

    public int getChunkSizeZ() {
        return chunkSizeZ;
    }
}
