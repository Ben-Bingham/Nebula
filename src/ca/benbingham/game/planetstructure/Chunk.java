package ca.benbingham.game.planetstructure;

import ca.benbingham.engine.graphics.renderingobjects.ElementBufferObject;
import ca.benbingham.engine.graphics.renderingobjects.VertexArrayObject;
import ca.benbingham.engine.graphics.renderingobjects.VertexBufferObject;
import org.joml.Vector2i;

import static ca.benbingham.engine.graphics.renderingobjects.VertexArrayObject.createAttributePointer;
import static ca.benbingham.engine.graphics.renderingobjects.VertexArrayObject.enableAttributePointer;
import static ca.benbingham.engine.util.GLError.getOpenGLError;

public class Chunk {
    //private Mesh mesh;
    private Vector2i coordinates;
    private boolean blockUpdate;

    public static final int xSize = 16;
    public static final int ySize = 256;
    public static final int zSize = 16;

    private boolean[] subChunkVisibility;

    private VertexArrayObject VAO;
    private VertexBufferObject VBO;
    private ElementBufferObject EBO;
    private int numberOfVertices;

    private boolean hasMesh = false;

    public Chunk() {
        int numberOfSubChunks = ySize / 16;
        subChunkVisibility = new boolean[numberOfSubChunks];


        VAO = new VertexArrayObject();
        VBO = new VertexBufferObject();
        EBO = new ElementBufferObject();

        VAO.bind();
        VBO.bind();
        VBO.setMaxDataSize(ySize * xSize * zSize * 36);

        EBO.bind();
        EBO.setMaxDataSize(ySize * xSize * zSize * 36);
        int positionSize = 3;
        int uvSize = 2;
        int vertexSizeBytes = (positionSize + uvSize);

        createAttributePointer(0, positionSize, vertexSizeBytes, 0);
        enableAttributePointer(0);
        createAttributePointer(1, uvSize, vertexSizeBytes, positionSize);
        enableAttributePointer(1);

    }

//    public Mesh getMesh() {
//        return mesh;
//    }
//
//    public void setMesh(Mesh mesh) {
//        this.mesh = mesh;
//    }

    public Vector2i getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Vector2i coordinates) {
        this.coordinates = coordinates;
    }

    public boolean isBlockUpdate() {
        return blockUpdate;
    }

    public void setBlockUpdate(boolean blockUpdate) {
        this.blockUpdate = blockUpdate;
    }

    public VertexArrayObject getVAO() {
        return VAO;
    }

    public VertexBufferObject getVBO() {
        return VBO;
    }

    public ElementBufferObject getEBO() {
        return EBO;
    }

    public void setVBOData(int offset, float[] data) {
        VAO.bind();
        VBO.bind();
        VBO.bindVertexData(offset, data);
    }

    public void setEBOData(int offset, int[] data) {
        VAO.bind();
        EBO.bind();
        EBO.bindIndexData(offset, data);
    }

    public void setNumberOfVertices(int numberOfVertices) {
        this.numberOfVertices = numberOfVertices;
    }

    public void setHasMesh(boolean hasMesh) {
        this.hasMesh = hasMesh;
    }

    public boolean hasMesh() {
        return hasMesh;
    }

    public int getNumberOfVertices() {
        return numberOfVertices;
    }

    public void delete() {
        VAO.delete();
        VBO.delete();
        EBO.delete();
    }
}
