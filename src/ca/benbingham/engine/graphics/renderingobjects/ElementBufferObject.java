package ca.benbingham.engine.graphics.renderingobjects;

import static org.lwjgl.opengl.GL15.*;

public class ElementBufferObject {
    private final int elementBufferObject;

    public ElementBufferObject() {
        elementBufferObject = glGenBuffers();
    }

    public void bind() {
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, elementBufferObject);
    }

    public void bindIndexData(int[] indexData) {
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexData, GL_STATIC_DRAW);
    }

    public void setMaxDataSize(int size) {
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, size, GL_DYNAMIC_DRAW);
    }

    public void bindIndexData(int offset, int[] indexArray) {
        glBufferSubData(GL_ELEMENT_ARRAY_BUFFER, offset, indexArray);
    }

    public void unbind() {
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    public void delete() { glDeleteBuffers(elementBufferObject); }

}
