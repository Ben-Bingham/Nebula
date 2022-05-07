package ca.benbingham.game.interstellarobjects.bodys;

import ca.benbingham.game.planetstructure.enums.EPlanetTypes;
import org.joml.Vector3f;

/**
 * Any natural satellite that orbits a star.
 */

public class Planet extends CosmicBody {
    private EPlanetTypes type;
    private int halfCircumference;

    public Planet(Vector3f localPosition, EPlanetTypes type, int circumference, long seed, Moon[] moons, float tilt) {
        super(circumference, localPosition, type.getDensity(), seed, moons, tilt);

        this.type = type;

        halfCircumference = (this.circumference - 1) / 2;
    }

    public EPlanetTypes getType() {
        return type;
    }

    public int getHalfCircumference() {
        return halfCircumference;
    }
}
