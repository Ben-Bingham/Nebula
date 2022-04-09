package ca.benbingham.game.planetstructure;

import ca.benbingham.engine.graphics.renderingobjects.ElementBufferObject;
import ca.benbingham.engine.graphics.renderingobjects.VertexArrayObject;
import ca.benbingham.engine.graphics.renderingobjects.VertexBufferObject;
import ca.benbingham.game.planetstructure.blocks.Block;
import ca.benbingham.game.planetstructure.geometry.Mesh;
import ca.benbingham.game.planetstructure.planetgeneration.TerrainGenerator;
import org.joml.Vector2i;

import java.util.ArrayList;

import static ca.benbingham.engine.graphics.renderingobjects.VertexArrayObject.createAttributePointer;
import static ca.benbingham.engine.graphics.renderingobjects.VertexArrayObject.enableAttributePointer;

public class Chunk {
    private Vector2i coordinates;
    private boolean blockUpdate; //TODO
    private boolean hasMesh = false;

    private Mesh mesh;

    public static final int xSize = 16;
    public static final int ySize = 256;
    public static final int zSize = 16;

    private VertexArrayObject VAO;
    private VertexBufferObject VBO;
    private ElementBufferObject EBO;
    private int numberOfVertices;

    private short[][][] blocks;
    private ArrayList<Block> extraBlockData; //TODO

    public Chunk(Vector2i coordinates, TerrainGenerator generator) {
        this.coordinates = coordinates;

        extraBlockData = new ArrayList<>();
        blocks = generator.createShortArrayForChunk(coordinates);
    }

    public void bindMeshData() {
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

        this.setVBOData(0, mesh.getVertices());
        this.setEBOData(0, mesh.getIndices());
        this.setNumberOfVertices(mesh.getNumberOfVertices());

        hasMesh = true;
    }

    public Vector2i getCoordinates() {
        return coordinates;
    }

    public VertexArrayObject getVAO() {
        return VAO;
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

    public int getNumberOfVertices() {
        return numberOfVertices;
    }

    public short[][][] getBlocks() {
        return blocks;
    }

    public void setMesh(Mesh mesh) {
        this.mesh = mesh;
    }

    public boolean hasMesh() {
        return hasMesh;
    }

    public void delete() {
        if (VAO != null) {
            VAO.delete();
        }
        if (VBO != null) {
            VBO.delete();
        }
        if (EBO != null) {
            EBO.delete();
        }
    }

    @Override
    public String toString() {
        return "Chunk{" +
                "coordinates=" + coordinates +
                '}';
    }
}
