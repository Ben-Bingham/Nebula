package ca.benbingham.game.generation.noise;

import ca.benbingham.game.interstellarobjects.bodys.CosmicBody;
import ca.benbingham.game.planetstructure.Chunk;
import org.joml.Vector2i;

import static ca.benbingham.engine.util.Util.map;

public class TerrainNoiseGenerator {

    private TileableOpenSimplexNoise2D tileableNoise2D;
    private CosmicBody cosmicBody;
    private long seed;

    public TerrainNoiseGenerator(CosmicBody cosmicBody) {
        this.cosmicBody = cosmicBody;
        this.seed = cosmicBody.getRenderingData().getSeed();
        tileableNoise2D = new TileableOpenSimplexNoise2D(seed, cosmicBody.circumference * Chunk.xSize, cosmicBody.circumference * Chunk.zSize);
    }

    public double[][] generateNoiseForChunk(Vector2i chunkCords) {
        double[][] noiseValues = new double[Chunk.xSize][Chunk.zSize];
        double lowNoise;
        double highNoise;
        double syncNoise;
        double finalNoise;

        Vector2i cordsToGenerate = convertGlobalCordsToFiniteCords(chunkCords);

        for (int i = 0; i < noiseValues.length; i++) {
            for (int j = 0; j < noiseValues[0].length; j++) {
                lowNoise = tileableNoise2D.octavePerlin(i + (cordsToGenerate.x * Chunk.xSize), j + (cordsToGenerate.y * Chunk.zSize), cosmicBody.getRenderingData().getDefaultNoiseFrequency(), 10, 3, 0.05);
                lowNoise *= 20;

                highNoise = tileableNoise2D.octavePerlin(i + (cordsToGenerate.x * Chunk.xSize), j + (cordsToGenerate.y * Chunk.zSize), cosmicBody.getRenderingData().getDefaultNoiseFrequency(), 80, 6, 0.5);
                highNoise *= 60;

                syncNoise = tileableNoise2D.octavePerlin(i + (cordsToGenerate.x * Chunk.xSize), j + (cordsToGenerate.y * Chunk.zSize), cosmicBody.getRenderingData().getDefaultNoiseFrequency(), 4, 1, 0.1);

                syncNoise = map(syncNoise, -1, 1, 0, 1);

                finalNoise = (lowNoise * syncNoise) + (highNoise * (1 - syncNoise));

                noiseValues[i][j] = finalNoise;

                noiseValues[i][j] = (int) noiseValues[i][j];
                noiseValues[i][j] += 60;
            }
        }
        return noiseValues;
    }

    private Vector2i convertGlobalCordsToFiniteCords(Vector2i globalCords) {
        Vector2i cordsToGenerate;
        Vector2i workingCords = new Vector2i(globalCords);

        int halfCircumference = (cosmicBody.circumference - 1) / 2;

        if (Math.abs(globalCords.x) <= halfCircumference && Math.abs(globalCords.y) <= halfCircumference) {
            cordsToGenerate = new Vector2i(globalCords);
        }
        else {
            while (workingCords.x > halfCircumference) { //TODO these while loops could cause a huge performance impact after traveling very far.
                workingCords.x -= cosmicBody.circumference;
            }
            while (workingCords.y > halfCircumference) {
                workingCords.y -= cosmicBody.circumference;
            }
            while (workingCords.x < (halfCircumference * -1)) {
                workingCords.x += cosmicBody.circumference;
            }
            while (workingCords.y < (halfCircumference * -1)) {
                workingCords.y += cosmicBody.circumference;
            }
            cordsToGenerate = workingCords;
        }

        return cordsToGenerate;
    }
}
