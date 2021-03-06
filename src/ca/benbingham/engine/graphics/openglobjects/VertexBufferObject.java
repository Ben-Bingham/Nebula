package ca.benbingham.engine.graphics.openglobjects;

import static org.lwjgl.opengl.GL15.*;

public class VertexBufferObject {
    private final int vertexBufferObject;

    public VertexBufferObject() {
        vertexBufferObject = glGenBuffers();
    }

    public void bind() {
        glBindBuffer(GL_ARRAY_BUFFER, vertexBufferObject);
    }

    public void bindVertexData(float[] vertexArray) {
        glBufferData(GL_ARRAY_BUFFER, vertexArray, GL_STATIC_DRAW);
    }

    public void setMaxDataSize(int size) {
        glBufferData(GL_ARRAY_BUFFER, size, GL_DYNAMIC_DRAW);
    }

    public void bindVertexData(int offset, float[] vertexArray) {
        glBufferSubData(GL_ARRAY_BUFFER, offset, vertexArray);
    }

    public void unbind() {
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    public int getVertexBufferObject() {
        return vertexBufferObject;
    }

    public void delete() {
        glDeleteBuffers(vertexBufferObject);
    }

}
