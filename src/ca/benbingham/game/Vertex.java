package ca.benbingham.game;

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
}
