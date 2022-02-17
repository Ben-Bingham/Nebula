package ca.benbingham.game;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.Arrays;

public class Quad {
    public Vertex[] vertices;
    private final int[] indices = {
            0, 1, 2,
            2, 3, 0
    };

    private final int positionLength = 3;
    private final int textureCoordinateLength = 2;
    private final int vertexLength = positionLength + textureCoordinateLength;
    private final int numberOfVertices = 4;

    public Quad() {
        vertices = new Vertex[numberOfVertices];

        for (int i = 0; i < vertices.length; i++) {
            vertices[i] = new Vertex(new Vector3f(0.0f, 0.0f, 0.0f), new Vector2f(0.0f, 0.0f));
        }
    }

    public float[] convertToFloatArray() {
        float[] array = new float[numberOfVertices * vertexLength];

        for (int i = 0; i < vertices.length; i++) {
            array[vertexLength * i] = vertices[i].position.x;
            array[1 + vertexLength * i] = vertices[i].position.y;
            array[2 + vertexLength * i] = vertices[i].position.z;
            array[3 + vertexLength * i] = vertices[i].texCords.x;
            array[4 + vertexLength * i] = vertices[i].texCords.y;
        }

        return array;
    }

    public void importData(float[] data) {
        if (data.length > (numberOfVertices * vertexLength)) throw new IllegalArgumentException("Too much data given");
        if (data.length < (numberOfVertices * vertexLength)) throw new IllegalArgumentException("Not enough data given");

        for (int i = 0; i < (data.length / vertexLength); i++) {
            this.vertices[i].position.x = data[0 + vertexLength * i];
            this.vertices[i].position.y = data[1 + vertexLength * i];
            this.vertices[i].position.z = data[2 + vertexLength * i];
            this.vertices[i].texCords.x = data[3 + vertexLength * i];
            this.vertices[i].texCords.y = data[4 + vertexLength * i];
        }
    }

    public void importData(float[] positionData, float[] textureCordData) {
        if (positionData.length > (numberOfVertices * positionLength)) throw new IllegalArgumentException("Too much data given");
        if (positionData.length < (numberOfVertices * positionLength)) throw new IllegalArgumentException("Not enough data given");

        if (textureCordData.length > (numberOfVertices * textureCoordinateLength)) throw new IllegalArgumentException("Too much data given");
        if (textureCordData.length < (numberOfVertices * textureCoordinateLength)) throw new IllegalArgumentException("Not enough data given");

        for (int i = 0; i < ((textureCordData.length  + positionData.length) / vertexLength); i++) {
            this.vertices[i].position.x = positionData[0 + positionLength * i];
            this.vertices[i].position.y = positionData[1 + positionLength * i];
            this.vertices[i].position.z = positionData[2 + positionLength * i];
            this.vertices[i].texCords.x = textureCordData[0 + textureCoordinateLength * i];
            this.vertices[i].texCords.y = textureCordData[1 + textureCoordinateLength * i];
        }
    }

    public void translate(Matrix4f translation) {
        for (ca.benbingham.game.Vertex vertex : vertices) {
            Vector4f temp = new Vector4f(
                    vertex.position.x,
                    vertex.position.y,
                    vertex.position.z,
                    1.0f
            );

            temp = temp.mul(translation);

            vertex.position.x = temp.x;
            vertex.position.y = temp.y;
            vertex.position.z = temp.z;
        }
    }

    @Override
    public String toString() {
        return Arrays.toString(vertices);
    }

    public int[] getIndices() {
        return indices;
    }
}
