package ca.benbingham.game.planetstructure.geometry;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class Vertex {
    public Vector3f position;
    public Vector2f texCords;
    public Vector3f normals;

    public Vertex(Vector3f position, Vector2f texCords, Vector3f normals) {
        this.position = position;
        this.texCords = texCords;
        this.normals = normals;
    }

    @Override
    public String toString() {
        String string = "";

        string += position.toString();
        string += texCords.toString();
        string += normals.toString();

        return string;
    }

    public float[] getAsFloatArray() {
        float[] array = new float[8];
        array[0] = position.x;
        array[1] = position.y;
        array[2] = position.z;
        array[3] = texCords.x;
        array[4] = texCords.y;
        array[5] = normals.x;
        array[6] = normals.y;
        array[7] = normals.z;
        return array;
    }
}
