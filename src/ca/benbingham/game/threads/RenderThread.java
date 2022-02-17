package ca.benbingham.game.threads;

import ca.benbingham.engine.graphics.Texture;
import ca.benbingham.engine.graphics.renderingobjects.ElementBufferObject;
import ca.benbingham.engine.graphics.renderingobjects.VertexArrayObject;
import ca.benbingham.engine.graphics.renderingobjects.VertexBufferObject;
import ca.benbingham.engine.graphics.shaders.Shader;
import ca.benbingham.engine.graphics.shaders.ShaderProgram;
import ca.benbingham.engine.io.Camera;
import ca.benbingham.engine.io.Window;
import ca.benbingham.engine.util.FileReader;
import ca.benbingham.game.Quad;
import ca.benbingham.game.worldstructure.Chunk;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL;

import static ca.benbingham.engine.graphics.renderingobjects.VertexArrayObject.createAttributePointer;
import static ca.benbingham.engine.graphics.renderingobjects.VertexArrayObject.enableAttributePointer;
import static ca.benbingham.engine.util.Printing.printError;
import static java.lang.Math.toRadians;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;

public class RenderThread extends Thread {

    private ShaderProgram defaultShaderProgram;
    private Shader vertexShader;
    private Shader fragmentShader;

    private Texture testTexture;
    private Texture textureAtlas;

    private VertexArrayObject worldVAO;
    private VertexBufferObject worldVBO;
    private ElementBufferObject worldEBO;

    private Matrix4f projectionMatrix;
    private Matrix4f viewMatrix;
    private Matrix4f modelMatrix;

    private Camera camera;
    private Window window;

    private Chunk testChunk;

    private boolean meshCalculated;
    private int count;

    public RenderThread(Camera camera, Window window) {
        this.camera = camera;
        this.window = window;
    }

    @Override
    public void run() {
        init();
    }

    private void compileShaders() {
        // vertex shader
        String vertexSource = "";
        try {
            vertexSource = FileReader.readFile("/shaders/default.vert"); // TODO use lwjgl features instead
        }
        catch (Exception e) {
            printError("Vertex shader could not be read from file");
        }

        Shader vertexShader = new Shader(vertexSource, GL_VERTEX_SHADER);

        if (!vertexShader.checkShaderStatus()) {
            printError("Vertex shader failed to compile");
        }

        // fragment shader
        String fragmentSource = "";
        try {
            fragmentSource = FileReader.readFile("/shaders/default.frag");
        }
        catch (Exception e) {
            printError("Fragment shader could not be read from file");
        }

        Shader fragmentShader = new Shader(fragmentSource, GL_FRAGMENT_SHADER);

        if (!fragmentShader.checkShaderStatus()) {
            printError("Fragment shader failed to compile");
        }

        // shader program
        defaultShaderProgram = new ShaderProgram();
        defaultShaderProgram.attachShader(fragmentShader);
        defaultShaderProgram.attachShader(vertexShader);
        defaultShaderProgram.linkProgram();

        if (!defaultShaderProgram.checkProgramLinkStatus()) {
            printError("Default shader program failed to link");
        }

        defaultShaderProgram.detachShader(vertexShader);
        defaultShaderProgram.detachShader(fragmentShader);

        vertexShader.delete();
        fragmentShader.delete();
    }

    private void compileTextures() {
        // texture setup
        testTexture = new Texture();
        testTexture.bindImageData("assets/images/container2.png", true);
        testTexture.generateMipmaps();
        testTexture.setWrapSettings(GL_REPEAT);
        testTexture.setShrinkMode(GL_NEAREST_MIPMAP_NEAREST);
        testTexture.setStretchMode(GL_NEAREST);

        // atlas
        textureAtlas = new Texture();
        textureAtlas.bindImageData("assets/images/textureAtlas.png", true);
        textureAtlas.generateMipmaps();
        textureAtlas.setWrapSettings(GL_REPEAT);
        textureAtlas.setShrinkMode(GL_NEAREST);
        textureAtlas.setStretchMode(GL_NEAREST);

        glActiveTexture(GL_TEXTURE0);
        testTexture.bind();
    }

    private void renderInit() {
        // basic setup
        GL.createCapabilities();
        compileShaders();
        glEnable(GL_DEPTH_TEST);

        // VAO and VBO setup
        int positionSize = 3;
        int uvSize = 2;
        int vertexSizeBytes = (positionSize + uvSize);

        testChunk = new Chunk();

        // world
        worldVAO = new VertexArrayObject();
        worldVBO = new VertexBufferObject();
        worldEBO = new ElementBufferObject();

        worldVAO.bind();
        worldVBO.bind();
        worldVBO.bindVertexData(Integer.MAX_VALUE);
        worldEBO.bind();
        worldEBO.bindIndexData(Integer.MAX_VALUE);

        createAttributePointer(0, positionSize, vertexSizeBytes, 0);
        enableAttributePointer(0);
        createAttributePointer(1, uvSize, vertexSizeBytes, positionSize);
        enableAttributePointer(1);

        defaultShaderProgram.use();
        defaultShaderProgram.uploadUniform("texture1", 0);

        for (int i = 0; i < testChunk.getChunkSizeX(); i++) {
            for (int j = 0; j < testChunk.getChunkSizeY(); j++) {
                for (int k = 0; k < testChunk.getChunkSizeZ(); k++) {
                    testChunk.blocks[i][j][k].setNeighbours();
                    testChunk.blocks[i][j][k].determineVisibleFaces();
                }
            }
        }

//        projectionMatrix = new Matrix4f()
//                .perspective((float) toRadians(camera.getFOV()), (float) width / height, 0.1f, 100f);

        //glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
    }

    private void init() {
        compileShaders();
        compileTextures();
        renderInit();
    }

    private void update() {
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        // matrix setup
        viewMatrix = camera.getViewMatrix();

        defaultShaderProgram.use();

        defaultShaderProgram.uploadUniform("view", viewMatrix);
        defaultShaderProgram.uploadUniform("projection", projectionMatrix);

        glEnable(GL_CULL_FACE);
        glFrontFace(GL_CW);

        if (!meshCalculated) {
            count = recreateWorldMesh();
            meshCalculated = true;
        }

        modelMatrix = new Matrix4f().translate(0, 0, 0);
        defaultShaderProgram.uploadUniform("model", modelMatrix);

        worldVAO.bind();
        worldVBO.bind();

        glDrawElements(GL_TRIANGLES, count * 6, GL_UNSIGNED_INT, 0);

        glfwSwapBuffers(window.getWindow());
    }

    private int recreateWorldMesh() {
        int count = 0;
        int numberOfVertices = 4;
        int vertexSize = 5;
        int faceSize = vertexSize * numberOfVertices * Float.BYTES;
        int numberOfIndices = 6;

        worldVAO.bind();
        worldVBO.bind();

        Quad tempFace = new Quad();
        int[] tempIndices = new int[6];

        for (int y = 0; y < 16 * 4; y += 16) {
            for (int h = 0; h < 16 * 4; h += 16) {
                for (int i = 0; i < testChunk.getChunkSizeX(); i++) {
                    for (int j = 0; j < testChunk.getChunkSizeY(); j++) {
                        for (int k = 0; k < testChunk.getChunkSizeZ(); k++) {
                            for (int l = 0; l < testChunk.blocks[i][j][k].getFaces().length; l++) {
                                if (testChunk.blocks[i][j][k].getFaces()[l] != null) {
                                    tempFace.importData(testChunk.blocks[i][j][k].getFaces()[l].convertToFloatArray());

                                    tempFace.translate(new Matrix4f().translation(i, j, k));
                                    tempFace.translate(new Matrix4f().translation(h, 0, y));


                                    for (int m = 0; m < tempIndices.length; m++) {
                                        tempIndices[m] = tempFace.getIndices()[m] + count * 4;
                                    }

                                    glBufferSubData(GL_ELEMENT_ARRAY_BUFFER, (long) count * numberOfIndices * Integer.BYTES, tempIndices);
                                    glBufferSubData(GL_ARRAY_BUFFER, (long) count * faceSize, tempFace.convertToFloatArray());
                                    count++;
                                }
                            }
                        }
                    }
                }
            }
        }
        return count;
    }
}
