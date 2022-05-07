package ca.benbingham.game.interstellarobjects.bodys;

import org.joml.Vector3f;

/**
 * Any natural object that orbits a planet.
 */

public class Moon extends CosmicBody {
    public Moon(int circumference, Vector3f position, float density, long seed, float tilt) {
        super(circumference, position, density, seed, null, tilt);
    }
}
