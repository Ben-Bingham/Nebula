package ca.benbingham.game.interstellarobjects.bodys;

import ca.benbingham.engine.physics.CosmicBodyPhysics;
import ca.benbingham.game.interstellarobjects.SolarSystem;
import ca.benbingham.game.planetstructure.Chunk;
import org.joml.Vector3f;

import static ca.benbingham.engine.util.Printing.print;
import static ca.benbingham.engine.util.math.CircleMath.volume;
import static ca.benbingham.engine.util.math.Vector3fMath.*;

public class CosmicBody {
    public final int circumference;
    public final float radius;
    public final float mass;
    public final Vector3f localPosition; // in meters
    public Vector3f globalPosition;
    public final float orbitRadius;
    public final float orbitCircumference;
    public float year;
    public double orbitProgress;
    public final float orbitTilt;
    public CosmicBody[] children;

    private CosmicBodyPhysics physicsData;
    private CosmicBodyRenderingData renderingData;

    public CosmicBody(int circumference, Vector3f localPosition, float density, long seed, CosmicBody[] children, float orbitTilt) {
        if (circumference % 2 == 0) {
            this.circumference = (circumference + 1);
        }
        else {
            this.circumference = circumference;
        }

        this.radius = (float) (((this.circumference * Chunk.xSize) / Math.PI) / 2);

        this.localPosition = localPosition;

        this.mass = (float) (density * volume(radius));
        this.orbitRadius = (float) distanceBetween(new Vector3f(0, 0, 0), localPosition);
        this.orbitCircumference = (float) (orbitRadius * 2 * Math.PI);

        this.orbitTilt = orbitTilt;
        this.orbitProgress = 0;
        this.children = children;

        physicsData = new CosmicBodyPhysics(this);
        renderingData = new CosmicBodyRenderingData(this, seed);
    }

    public void init(CosmicBody[] parents) { //TODO might not need to be an array maybe it should just be the one parent
        globalPosition = add(localPosition, parents[0].globalPosition);
        physicsData.calculateGravitationField();
        physicsData.calculateNetForce(parents);

        this.year = orbitCircumference / physicsData.getOrbitSpeed();
    }

    public void init(SolarSystem solarSystem) {
        globalPosition = add(localPosition, solarSystem.getGlobalPosition());
        physicsData.calculateGravitationField();
        physicsData.calculateNetForce(solarSystem);

        this.year = orbitCircumference / physicsData.getOrbitSpeed();
    }

    public CosmicBodyPhysics getPhysicsData() {
        return physicsData;
    }

    public CosmicBodyRenderingData getRenderingData() {
        return renderingData;
    }
}