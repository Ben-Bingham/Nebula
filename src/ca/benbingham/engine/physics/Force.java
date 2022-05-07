package ca.benbingham.engine.physics;

import org.joml.Vector3f;

public class Force {
    private float magnitude;
    private Vector3f direction;

    public Force(Vector3f direction) {
        this.direction = direction;
        updateMagnitude();
    }

    /**
     * @param direction
     * Needs to be normalized.
     */
    public Force(Vector3f direction, float magnitude) {
        this.direction = direction;
        this.magnitude = magnitude;
    }

    private void updateMagnitude() {
        magnitude = (float) Math.sqrt(Math.pow(direction.x, 2) + Math.pow(direction.y, 2) + Math.pow(direction.z, 2));
    }

    public float getMagnitude() {
        updateMagnitude();
        return magnitude;
    }

    public Vector3f getDirection() {
        return direction;
    }

    public void setDirection(Vector3f direction) {
        this.direction = direction;
        updateMagnitude();
    }
}
