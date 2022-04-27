package ca.benbingham.game.planetstructure.planetgeneration.noise;

import ca.benbingham.game.planetstructure.Chunk;
import ca.benbingham.game.planetstructure.bodys.Planet;
import org.joml.Vector2i;

import static ca.benbingham.engine.util.Util.map;

public class TerrainNoiseGenerator {

    private TileableOpenSimplexNoise2D tileableNoise2D;
    private Planet planet;

    public TerrainNoiseGenerator(Planet planet) {
        this.planet = planet;
        tileableNoise2D = new TileableOpenSimplexNoise2D((long) this.planet.getSeed(), planet.getCircumference() * Chunk.xSize, planet.getCircumference() * Chunk.zSize);
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
                lowNoise = tileableNoise2D.octavePerlin(i + (cordsToGenerate.x * Chunk.xSize), j + (cordsToGenerate.y * Chunk.zSize), planet.getDefaultNoiseFrequency(), 10, 3, 0.05);
                lowNoise *= 20;

                highNoise = tileableNoise2D.octavePerlin(i + (cordsToGenerate.x * Chunk.xSize), j + (cordsToGenerate.y * Chunk.zSize), planet.getDefaultNoiseFrequency(), 80, 6, 0.5);
                highNoise *= 60;

                syncNoise = tileableNoise2D.octavePerlin(i + (cordsToGenerate.x * Chunk.xSize), j + (cordsToGenerate.y * Chunk.zSize), planet.getDefaultNoiseFrequency(), 4, 1, 0.1);

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

        if (Math.abs(globalCords.x) <= planet.getHalfCircumference() && Math.abs(globalCords.y) <= planet.getHalfCircumference()) {
            cordsToGenerate = new Vector2i(globalCords);
        }
        else {
            while (workingCords.x > planet.getHalfCircumference()) { //TODO these while loops could cause a huge performance impact after traveling very far.
                workingCords.x -= planet.getCircumference();
            }
            while (workingCords.y > planet.getHalfCircumference()) {
                workingCords.y -= planet.getCircumference();
            }
            while (workingCords.x < (planet.getHalfCircumference() * -1)) {
                workingCords.x += planet.getCircumference();
            }
            while (workingCords.y < (planet.getHalfCircumference() * -1)) {
                workingCords.y += planet.getCircumference();
            }
            cordsToGenerate = workingCords;
        }

        return cordsToGenerate;
    }
}
