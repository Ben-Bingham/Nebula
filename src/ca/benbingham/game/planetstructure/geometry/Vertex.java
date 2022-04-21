package ca.benbingham.game.planetstructure.geometry;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class Vertex {
    public Vector3f position;
    public Vector2f texCords;

    public Vertex(Vector3f position, Vector2f texCords) {
        this.position = position;
        this.texCords = texCords;
    }

    @Override
    public String toString() {
        String string = "";

        string += position.toString();
        string += texCords.toString();

        return string;
    }

    public float[] getAsFloatArray() {
        float[] array = new float[5];
        array[0] = position.x;
        array[1] = position.y;
        array[2] = position.z;
        array[3] = texCords.x;
        array[4] = texCords.y;
        return array;
    }
}
