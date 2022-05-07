package ca.benbingham.game.planetstructure;

import ca.benbingham.engine.graphics.openglobjects.ElementBufferObject;
import ca.benbingham.engine.graphics.openglobjects.VertexArrayObject;
import ca.benbingham.engine.graphics.openglobjects.VertexBufferObject;
import ca.benbingham.game.planetstructure.blocks.Block;
import ca.benbingham.game.planetstructure.geometry.Mesh;
import ca.benbingham.game.generation.planetgeneration.TerrainGenerator;
import org.joml.Vector2i;

import java.util.ArrayList;

import static ca.benbingham.engine.graphics.openglobjects.VertexArrayObject.createAttributePointer;
import static ca.benbingham.engine.graphics.openglobjects.VertexArrayObject.enableAttributePointer;
import static ca.benbingham.engine.util.Printing.print;

public class Chunk {
    private Vector2i coordinates;
    private boolean blockUpdate; //TODO
    private boolean hasMesh = false;
    private boolean hasMeshToBeBound = false;
    private boolean needsVAOInit = true;
    private boolean needsMesh = false;

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

    private int distanceFromPlayerChunk = -1;

    public Chunk(Vector2i coordinates, TerrainGenerator generator) {
        this.coordinates = coordinates;

        extraBlockData = new ArrayList<>();
        blocks = generator.createShortArrayForChunk(coordinates);
    }

    public Chunk() {

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
        int normalSize = 3;
        int vertexSizeBytes = (positionSize + uvSize + normalSize);
//        int vertexSizeBytes = (positionSize + uvSize);

        createAttributePointer(0, positionSize, vertexSizeBytes, 0);
        enableAttributePointer(0);
        createAttributePointer(1, uvSize, vertexSizeBytes, positionSize);
        enableAttributePointer(1);
        createAttributePointer(2, normalSize, vertexSizeBytes, positionSize + uvSize);
        enableAttributePointer(2);

        this.setVBOData(0, mesh.getVertices());
        this.setEBOData(0, mesh.getIndices());
        this.setNumberOfVertices(mesh.getNumberOfVertices());

        hasMesh = true;
        hasMeshToBeBound = false;
        needsVAOInit = false;
    }

    public void bindNewMeshData() {
        VAO.bind();
        this.setVBOData(0, mesh.getVertices()); //TODO might need to clear out all data first
        this.setEBOData(0, mesh.getIndices());
        this.setNumberOfVertices(mesh.getNumberOfVertices());

        hasMesh = true;
        hasMeshToBeBound = false;
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

    public boolean needsVAOInit() {
        return needsVAOInit;
    }

    public void setCoordinates(Vector2i coordinates) {
        this.coordinates = coordinates;
    }

    public boolean hasMesh() {
        return hasMesh;
    }

    public boolean hasMeshToBeBound() {
        return hasMeshToBeBound;
    }

    public void setHasMeshToBeBound(boolean hasMeshToBeBound) {
        this.hasMeshToBeBound = hasMeshToBeBound;
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
