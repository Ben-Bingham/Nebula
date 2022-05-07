package ca.benbingham.game.interstellarobjects.bodys;

import ca.benbingham.game.interstellarobjects.bodys.CosmicBody;

public class CosmicBodyRenderingData {
    private final long seed;
    private final double defaultNoiseFrequency;
    private final CosmicBody cosmicBody;

    public CosmicBodyRenderingData(CosmicBody cosmicBody, Long seed) {
        this.seed = seed;
        this.cosmicBody = cosmicBody;
        this.defaultNoiseFrequency = generateDefaultNoiseFrequency();
    }

    public double generateDefaultNoiseFrequency() {
        return (cosmicBody.circumference - 1) / 50f;
    }

    public long getSeed() {
        return seed;
    }

    public double getDefaultNoiseFrequency() {
        return defaultNoiseFrequency;
    }
}
