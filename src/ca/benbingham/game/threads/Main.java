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
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import java.util.Arrays;

import static ca.benbingham.engine.graphics.renderingobjects.VertexArrayObject.createAttributePointer;
import static ca.benbingham.engine.graphics.renderingobjects.VertexArrayObject.enableAttributePointer;
import static ca.benbingham.engine.util.Printing.print;
import static ca.benbingham.engine.util.Printing.printError;
import static java.lang.Math.toRadians;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;

public class Main extends Thread{
    private int width = 800;
    private int height = 600;

    private Window window;

    private ShaderProgram defaultShaderProgram;

    private VertexArrayObject worldVAO;
    private VertexBufferObject worldVBO;
    private ElementBufferObject worldEBO;

    private Texture testTexture;
    private Texture textureAtlas;

    private Matrix4f modelMatrix;
    private Matrix4f viewMatrix;
    private Matrix4f projectionMatrix;

    private Chunk testChunk;

    private Camera camera;

    private float deltaTime = 0.0f;
    private float lastFrame = 0.0f;

    private final float mouseSensitivity = 0.07f;

    private final float defaultFOV = 45f;

    private boolean meshCalculated = false;
    private int count;


    public static void main(String[] args) {
        Main mainThread = new Main();
        mainThread.start();
    }

    public void run() {
        init();
        renderInitialization();

        // Game loop
        while (!glfwWindowShouldClose(window.getWindow())) {
            update();
            render();
        }
        
        destroy();
    }

    private void init() {
        window = new Window(height, width, "LWJGL test", true);

        GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        height = vidMode.height();
        width = vidMode.width();

        window.centerWindow();

        glfwSetInputMode(window.getWindow(), GLFW_CURSOR, GLFW_CURSOR_DISABLED);

        camera = new Camera(window, defaultFOV, mouseSensitivity, 4f, new Vector3f(10, 258, 10));
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

    private void renderInitialization() {
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

        projectionMatrix = new Matrix4f()
                .perspective((float) toRadians(camera.getFOV()), (float) width / height, 0.1f, 100f);

        //glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
    }
    
    private void render() {
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
    
    private void update() {
        glfwPollEvents();

        if(glfwGetKey(window.getWindow(), GLFW_KEY_ESCAPE) == GLFW_PRESS) {
            glfwSetWindowShouldClose(window.getWindow(), true);
        }

        camera.update();

        float currentFrame = (float) glfwGetTime();
        deltaTime = currentFrame - lastFrame;
        lastFrame = currentFrame;

        // prints fps to console
        //print(1 / deltaTime);
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

    private void destroy() {
        window.destroy();
        worldVBO.delete();
        worldVAO.delete();
        worldEBO.delete();

        testTexture.delete();
        textureAtlas.delete();

        defaultShaderProgram.delete();
    }
}
