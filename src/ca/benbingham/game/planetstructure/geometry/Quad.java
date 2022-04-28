package ca.benbingham.game.planetstructure.geometry;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.Arrays;

import static ca.benbingham.engine.util.Printing.print;

public class Quad {
    public Vertex[] vertices;
    private final int[] indices = {
            0, 1, 2,
            2, 3, 0
    };

    private float[] floatArrayOfQuad;

    private final int POSITION_LENGTH = 3;
    private final int TEXTURE_COORDINATE_LENGTH = 2;
    private final int NORMAL_LENGTH = 3;
    private final int VERTEX_LENGTH = POSITION_LENGTH + TEXTURE_COORDINATE_LENGTH + NORMAL_LENGTH;
    private final int NUMBER_OF_VERTICES = 4;

    public Quad() {
        vertices = new Vertex[NUMBER_OF_VERTICES];

        for (int i = 0; i < vertices.length; i++) {
            vertices[i] = new Vertex(new Vector3f(0.0f, 0.0f, 0.0f), new Vector2f(0.0f, 0.0f),new Vector3f());
        }
    }

    public float[] convertToFloatArray() {
        float[] array = new float[NUMBER_OF_VERTICES * VERTEX_LENGTH];

        for (int i = 0; i < vertices.length; i++) {
            array[VERTEX_LENGTH * i] = vertices[i].position.x;
            array[1 + VERTEX_LENGTH * i] = vertices[i].position.y;
            array[2 + VERTEX_LENGTH * i] = vertices[i].position.z;
            array[3 + VERTEX_LENGTH * i] = vertices[i].texCords.x;
            array[4 + VERTEX_LENGTH * i] = vertices[i].texCords.y;
            array[5 + VERTEX_LENGTH * i] = vertices[i].normals.x;
            array[6 + VERTEX_LENGTH * i] = vertices[i].normals.y;
            array[7 + VERTEX_LENGTH * i] = vertices[i].normals.z;
        }

        return array;
    }

    public void createFloatArrayForQuad() {
        floatArrayOfQuad = convertToFloatArray();
    }

    public void importData(float[] data) {
        if (data.length > (NUMBER_OF_VERTICES * VERTEX_LENGTH)) throw new IllegalArgumentException("Too much data given");
        if (data.length < (NUMBER_OF_VERTICES * VERTEX_LENGTH)) throw new IllegalArgumentException("Not enough data given");

        for (int i = 0; i < (data.length / VERTEX_LENGTH); i++) {
            this.vertices[i].position.x = data[0 + VERTEX_LENGTH * i];
            this.vertices[i].position.y = data[1 + VERTEX_LENGTH * i];
            this.vertices[i].position.z = data[2 + VERTEX_LENGTH * i];
            this.vertices[i].texCords.x = data[3 + VERTEX_LENGTH * i];
            this.vertices[i].texCords.y = data[4 + VERTEX_LENGTH * i];
            this.vertices[i].normals.x = data[5 + VERTEX_LENGTH * i];
            this.vertices[i].normals.y = data[6 + VERTEX_LENGTH * i];
            this.vertices[i].normals.z = data[7 + VERTEX_LENGTH * i];
        }
    }

    public void importData(float[] positionData, float[] textureCordData, float[] normals) {
        if (positionData.length > (NUMBER_OF_VERTICES * POSITION_LENGTH)) throw new IllegalArgumentException("Too much data given");
        if (positionData.length < (NUMBER_OF_VERTICES * POSITION_LENGTH)) throw new IllegalArgumentException("Not enough data given");

        if (textureCordData.length > (NUMBER_OF_VERTICES * TEXTURE_COORDINATE_LENGTH)) throw new IllegalArgumentException("Too much data given");
        if (textureCordData.length < (NUMBER_OF_VERTICES * TEXTURE_COORDINATE_LENGTH)) throw new IllegalArgumentException("Not enough data given");

        for (int i = 0; i < ((textureCordData.length + positionData.length + normals.length) / VERTEX_LENGTH); i++) {
            this.vertices[i].position.x = positionData[0 + POSITION_LENGTH * i];
            this.vertices[i].position.y = positionData[1 + POSITION_LENGTH * i];
            this.vertices[i].position.z = positionData[2 + POSITION_LENGTH * i];
            this.vertices[i].texCords.x = textureCordData[0 + TEXTURE_COORDINATE_LENGTH * i];
            this.vertices[i].texCords.y = textureCordData[1 + TEXTURE_COORDINATE_LENGTH * i];
            this.vertices[i].normals.x = normals[0 + NORMAL_LENGTH * i];
            this.vertices[i].normals.y = normals[1 + NORMAL_LENGTH * i];
            this.vertices[i].normals.z = normals[2 + NORMAL_LENGTH * i];
        }
    }

    public void translate(Matrix4f translation) {
        for (Vertex vertex : vertices) {
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

    public float[] getFloatArrayOfQuad() {
        return floatArrayOfQuad;
    }
}
