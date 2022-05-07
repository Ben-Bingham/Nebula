package ca.benbingham.game.planetstructure.enums;

public enum EPlanetTypes { //TODO make one of these for star types and moon types
    NEBULA_DEFAULT_PLANET(5520f),
    NEBULA_SNOW_PLANET(1.0f),
    NEBULA_DESERT_PLANET(1.0f),
    NEBULA_OCEAN_PLANET(1.0f),
    NEBULA_LAVA_PLANET(1.0f);

    private float density;

    EPlanetTypes(float density) {
        this.density = density;
    }

    public float getDensity() {
        return density;
    }
}
