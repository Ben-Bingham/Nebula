package ca.benbingham.engine.util.math;

import ca.benbingham.engine.physics.Force;
import org.joml.Vector3f;

public class ForceMath {
    public static float add(Force force1, Force force2) {
        Force force = new Force(new Vector3f(force1.getDirection().x + force2.getDirection().x, force1.getDirection().y + force2.getDirection().y, force1.getDirection().z + force2.getDirection().z));
        return force.getMagnitude();
    }

    public static Vector3f normalize(Vector3f forceDirection) {
        float length = (float) Math.sqrt(Math.pow(forceDirection.x, 2) + Math.pow(forceDirection.y, 2) + Math.pow(forceDirection.z, 2));
        return new Vector3f(forceDirection.x / length, forceDirection.y / length, forceDirection.z / length);
    }
}