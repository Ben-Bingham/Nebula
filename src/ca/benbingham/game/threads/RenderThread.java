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

import java.util.ArrayList;
import java.util.Arrays;

import static ca.benbingham.engine.graphics.renderingobjects.VertexArrayObject.createAttributePointer;
import static ca.benbingham.engine.graphics.renderingobjects.VertexArrayObject.enableAttributePointer;
import static ca.benbingham.engine.util.ArrayUtil.*;
import static ca.benbingham.engine.util.Printing.print;
import static ca.benbingham.engine.util.Printing.printError;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;

public class RenderThread extends Thread {
    private SyncThread syncThread;
    private UpdateThread updateThread;

    private int height;
    private int width;
    private ShaderProgram defaultShaderProgram;

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
    private Game game;

    private int count;

    private float[] worldVertices;
    private int[] worldIndices;

    private int numberOfVertices;

    // flags
    private boolean startProcess = false;

    public RenderThread(SyncThread syncThread) {
        this.syncThread = syncThread;
        this.updateThread = syncThread.getUpdateThread();

        this.game = syncThread.getGame();
        this.height = game.getHeight();
        this.width = game.getWidth();
    }

    @Override
    public void run() {
        print("Render thread created");

        init();

        while(syncThread.isGameOpen()) {
            update();
        }

        destroy();
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
        window = new Window(height, width, "Nebula", true);

        camera = new Camera(window, game.getDefaultFOV(), game.getMouseSensitivity(), game.getMovementSpeed(), new Vector3f(10, 258, 10));

        window.create();

        GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        height = vidMode.height(); //TODO update height universally
        width = vidMode.width();

        window.centerWindow();

        glfwSetInputMode(window.getWindow(), GLFW_CURSOR, GLFW_CURSOR_DISABLED);

        // basic setup
        GL.createCapabilities();
        compileShaders();
        glEnable(GL_DEPTH_TEST);

        // VAO and VBO setup
        int positionSize = 3;
        int uvSize = 2;
        int vertexSizeBytes = (positionSize + uvSize);

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

        //glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);

        projectionMatrix = camera.getProjectionMatrix();
    }

    private void init() {
        renderInit();
        compileShaders();
        compileTextures();
    }

    private void update() {
        //syncThread.setGameOpen(!glfwWindowShouldClose(window.getWindow()));

        if (glfwWindowShouldClose(window.getWindow())) {
            //syncThread.setGameOpen(false);
            syncThread.destroy(); // TODO needs to be removed at some point
        }

        camera.update();

        if (startProcess) {
            frameProcess();
        }

        glfwPollEvents();

        if(glfwGetKey(window.getWindow(), GLFW_KEY_ESCAPE) == GLFW_PRESS) {
            glfwSetWindowShouldClose(window.getWindow(), true);
        }
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

        modelMatrix = new Matrix4f().translate(0, 0, 0);
        defaultShaderProgram.uploadUniform("model", modelMatrix);

        worldVAO.bind();
        worldVBO.bind();
        //print(syncThread.isNewWorldData());
        if (syncThread.isNewWorldData()) {

            glBufferSubData(GL_ELEMENT_ARRAY_BUFFER, 0, worldIndices);
            glBufferSubData(GL_ARRAY_BUFFER, 0, worldVertices);
            print("a");
        }

        glDrawElements(GL_TRIANGLES, numberOfVertices * 6, GL_UNSIGNED_INT, 0);

        glfwSwapBuffers(window.getWindow());
    }

    private void frameProcess() {
        //print("render frame process");
        startProcess = false;
        if (syncThread.isFirstFrameDataCreated()) {
            if (syncThread.isNewWorldData()) {
                numberOfVertices = updateThread.getNumberOfVertices();

                worldVertices = updateThread.getWorldVertexData();
                worldIndices = updateThread.getWorldIndexData();
            }
            render();
        }

        //try { wait(); } catch (InterruptedException ignored) { }
        syncThread.toggleRenderThreadStatusFlag();
    }

    public void destroy() {
        glfwTerminate();
        worldEBO.delete();
        worldVBO.delete();
        worldVAO.delete();
        textureAtlas.delete();
        testTexture.delete();
        defaultShaderProgram.delete();
    }

//    private int recreateWorldMesh() {
//        int count = 0;
//
//        worldVAO.bind();
//        worldVBO.bind();
//
//        Quad tempFace = new Quad();
//
//        totalIndices = new ArrayList<>();
//        totalVertices = new ArrayList<>();
//        float[] tempFaceVertices;
//
//        for (int i = 0; i < testChunk.getChunkSizeX(); i++) {
//            for (int j = 0; j < testChunk.getChunkSizeY(); j++) {
//                for (int k = 0; k < testChunk.getChunkSizeZ(); k++) {
//                    for (int l = 0; l < testChunk.blocks[i][j][k].getFaces().length; l++) {
//                        if (testChunk.blocks[i][j][k].getFaces()[l] != null) {
//                            tempFace.importData(testChunk.blocks[i][j][k].getFaces()[l].convertToFloatArray());
//
//                            tempFace.translate(new Matrix4f().translation(i, j, k));
//
//                            for (int m = 0; m < tempFace.getIndices().length; m++) {
//                                totalIndices.add(tempFace.getIndices()[m] + count * 4);
//                            }
//
//                            for (int m = 0; m < tempFace.vertices.length; m++) {
//                                tempFaceVertices = tempFace.vertices[m].getAsFloatArray();
//                                for (int n = 0; n < 5; n++) {
//                                    totalVertices.add(tempFaceVertices[n]);
//                                }
//                            }
//
//                            count++;
//                        }
//                    }
//                }
//            }
//        }
//        return count;
//    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setStartProcess(boolean startProcess) {
        this.startProcess = startProcess;
    }
}
