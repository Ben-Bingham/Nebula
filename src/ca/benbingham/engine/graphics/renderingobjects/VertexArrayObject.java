package ca.benbingham.engine.graphics.renderingobjects;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class VertexArrayObject {
    private final int vertexArrayObject;

    public VertexArrayObject() {
        vertexArrayObject = glGenVertexArrays();
    }

    public void bind() {
        glBindVertexArray(vertexArrayObject);
    }

    public int getVertexArrayObject() {
        return vertexArrayObject;
    }

    public void unbind() {
        glBindVertexArray(0);
    }

    public static void createAttributePointer(int indexOfAttributeArray, int numberOfValuesInAttribute, int gapBetweenValues, int gapAtStart) {
        glVertexAttribPointer(indexOfAttributeArray, numberOfValuesInAttribute, GL_FLOAT, false, gapBetweenValues * Float.BYTES, (long) gapAtStart * Float.BYTES);
    }

    public static void enableAttributePointer(int index) {
        glEnableVertexAttribArray(index);
    }

    public static void disableAttributePointer(int index) {
        glDisableVertexAttribArray(index);
    }

    public void delete() { glDeleteBuffers(vertexArrayObject); }

}
