package ca.benbingham.game.generation.noise;

public class TileableOpenSimplexNoise2D {
    private static final double TWO_PI = Math.PI * 2;

    private final long seed;
    private final int xLength;
    private final int yLength;

    public TileableOpenSimplexNoise2D(long seed, int xLength, int yLength) {
        this.seed = seed;
        this.xLength = xLength;
        this.yLength = yLength;
    }

    public double noise(double x, double y, double frequency, double amplitude) {
        double value;

        double fNX = x / xLength;
        double fNY = y / yLength;
        double fRdx = fNX * TWO_PI;
        double fRdy = fNY * TWO_PI;
        double a = Math.sin(fRdx);
        double b = Math.cos(fRdx);
        double c = Math.sin(fRdy);
        double d = Math.cos(fRdy);

        value = OpenSimplex2S.noise4_ImproveXYZ(seed, a * frequency, b * frequency, c * frequency, d * frequency);

        value *= amplitude;

        return value;
    }

    public double octavePerlin(double x, double y, double frequency, double amplitude, int octaves, double persistence) {
        float total = 0f;

        double maxValue = 0;
        for(int i = 0;i < octaves; i++) {
            total += this.noise(x, y, frequency, amplitude);

            maxValue += amplitude;

            amplitude *= persistence;
            frequency *= 2;
        }
        return total/maxValue;
    }
}
