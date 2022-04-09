package ca.benbingham.game.planetstructure.geometry;

public class Mesh {
    private int numberOfVertices;
    private float[] vertices;
    private int[] indices;

    public int getNumberOfVertices() {
        return numberOfVertices;
    }

    public float[] getVertices() {
        return vertices;
    }

    public int[] getIndices() {
        return indices;
    }

    public void setNumberOfVertices(int numberOfVertices) {
        this.numberOfVertices = numberOfVertices;
    }

    public void setVertexData(float[] vertices) {
        this.vertices = vertices;
    }

    public void setIndexData(int[] indices) {
        this.indices = indices;
    }
}
