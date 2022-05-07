package ca.benbingham.engine.physics;

import ca.benbingham.game.interstellarobjects.SolarSystem;
import ca.benbingham.game.interstellarobjects.bodys.CosmicBody;
import org.joml.Vector3f;

import static ca.benbingham.engine.physics.Constants.GRAVITATIONAL_CONSTANT;
import static ca.benbingham.engine.util.math.ForceMath.add;
import static ca.benbingham.engine.util.math.ForceMath.normalize;
import static ca.benbingham.engine.util.math.Vector3fMath.distanceBetween;
import static ca.benbingham.engine.util.math.Vector3fMath.subtract;

public class CosmicBodyPhysics {
    private Force netForce;
    private float gravitationFieldStrengthAtGround;
    private final CosmicBody cosmicBody;
    private float orbitSpeed;

    public CosmicBodyPhysics(CosmicBody body) {
        this.cosmicBody = body;
    }

    public void calculateNetForce(CosmicBody[] parents) {
        netForce = new Force(new Vector3f(0, 0, 0));

        if (parents != null) {
            float mass = cosmicBody.mass;
            Vector3f globalPosition = cosmicBody.globalPosition;
            float radius = cosmicBody.radius;

            for (int i = 0; i < parents.length; i++) {
                CosmicBody parent = parents[i];
                float parentMass = parent.mass;
                Vector3f parentGlobalCords = parent.globalPosition;
                float parentRadius = parent.radius;

                Force force = new Force(
                        normalize(subtract(globalPosition, parentGlobalCords)),
                        (float) ((GRAVITATIONAL_CONSTANT * mass * parentMass) / Math.pow(distanceBetween(parentGlobalCords, globalPosition) + radius + parentRadius, 2))
                );
                add(netForce, force);
            }
        }
    }

    public void calculateNetForce(SolarSystem solarSystem) {
        netForce = new Force(new Vector3f(0, 0, 0));

        if (solarSystem != null) {
//            float mass = cosmicBody.mass;
//            Vector3f globalPosition = cosmicBody.globalPosition;
//            float radius = cosmicBody.radius;
//
//            float parentMass = 0;
//            Vector3f parentGlobalCords = solarSystem.getGlobalPosition();
//            float parentRadius = 0;

            netForce = new Force(
                    new Vector3f(0)
            );
        }
    }

    public void calculateGravitationField() {
        gravitationFieldStrengthAtGround = (float) ((GRAVITATIONAL_CONSTANT * cosmicBody.mass) / Math.pow(cosmicBody.radius, 2));
    }

    public float calculateGravitationField(float altitude) {
        return (float) (((GRAVITATIONAL_CONSTANT * cosmicBody.mass) / Math.pow(cosmicBody.radius + altitude, 2)));
    }

    public void calculateOrbitSpeed() {
        orbitSpeed = (float) Math.sqrt((netForce.getMagnitude() * cosmicBody.orbitRadius) / cosmicBody.mass);
    }

    public float getOrbitSpeed() {
        return orbitSpeed;
    }
}
