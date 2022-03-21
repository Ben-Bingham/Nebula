package ca.benbingham.game.gameclasses;

import ca.benbingham.engine.graphics.CubeMap;
import ca.benbingham.engine.graphics.Texture;
import ca.benbingham.engine.graphics.renderingobjects.ElementBufferObject;
import ca.benbingham.engine.graphics.renderingobjects.VertexArrayObject;
import ca.benbingham.engine.graphics.renderingobjects.VertexBufferObject;
import ca.benbingham.engine.graphics.shaders.Shader;
import ca.benbingham.engine.graphics.shaders.ShaderProgram;
import ca.benbingham.engine.io.Camera;
import ca.benbingham.engine.io.Window;
import ca.benbingham.engine.util.FileReader;
import ca.benbingham.game.planetstructure.Chunk;

import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLUtil;
import org.lwjgl.system.Callback;

import static ca.benbingham.engine.graphics.renderingobjects.VertexArrayObject.createAttributePointer;
import static ca.benbingham.engine.graphics.renderingobjects.VertexArrayObject.enableAttributePointer;
import static ca.benbingham.engine.util.GLError.getOpenGLError;
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
import static org.lwjgl.opengl.GL32.GL_TEXTURE_CUBE_MAP_SEAMLESS;

public class Renderer {
    private int height;
    private int width;

    private ShaderProgram defaultShaderProgram;
    private ShaderProgram skyboxShaderProgram;

    private Texture testTexture;
    private Texture textureAtlas;
    private CubeMap skybox;

    // world
//    private VertexArrayObject worldVAO;
//    private VertexBufferObject worldVBO;
//    private ElementBufferObject worldEBO;

    // skybox
    private VertexArrayObject skyboxVAO;
    private VertexBufferObject skyboxVBO;

    // cube
//    private VertexArrayObject cubeVAO;
//    private VertexBufferObject cubeVBO;
//    private ElementBufferObject cubeEBO;

    private Matrix4f projectionMatrix;
    private Matrix4f viewMatrix;
    private Matrix4f modelMatrix;

    private Camera camera;
    private Window window;
    private Game game;

    private float deltaTime;
    private float lastFrame = 0;

    float[] vertices = {
            -0.5f, -0.5f, -0.5f,  0.0f, 0.0f,
            0.5f, -0.5f, -0.5f,  1.0f, 0.0f,
            0.5f,  0.5f, -0.5f,  1.0f, 1.0f,
            0.5f,  0.5f, -0.5f,  1.0f, 1.0f,
            -0.5f,  0.5f, -0.5f,  0.0f, 1.0f,
            -0.5f, -0.5f, -0.5f,  0.0f, 0.0f,

            -0.5f, -0.5f,  0.5f,  0.0f, 0.0f,
            0.5f, -0.5f,  0.5f,  1.0f, 0.0f,
            0.5f,  0.5f,  0.5f,  1.0f, 1.0f,
            0.5f,  0.5f,  0.5f,  1.0f, 1.0f,
            -0.5f,  0.5f,  0.5f,  0.0f, 1.0f,
            -0.5f, -0.5f,  0.5f,  0.0f, 0.0f,

            -0.5f,  0.5f,  0.5f,  1.0f, 0.0f,
            -0.5f,  0.5f, -0.5f,  1.0f, 1.0f,
            -0.5f, -0.5f, -0.5f,  0.0f, 1.0f,
            -0.5f, -0.5f, -0.5f,  0.0f, 1.0f,
            -0.5f, -0.5f,  0.5f,  0.0f, 0.0f,
            -0.5f,  0.5f,  0.5f,  1.0f, 0.0f,

            0.5f,  0.5f,  0.5f,  1.0f, 0.0f,
            0.5f,  0.5f, -0.5f,  1.0f, 1.0f,
            0.5f, -0.5f, -0.5f,  0.0f, 1.0f,
            0.5f, -0.5f, -0.5f,  0.0f, 1.0f,
            0.5f, -0.5f,  0.5f,  0.0f, 0.0f,
            0.5f,  0.5f,  0.5f,  1.0f, 0.0f,

            -0.5f, -0.5f, -0.5f,  0.0f, 1.0f,
            0.5f, -0.5f, -0.5f,  1.0f, 1.0f,
            0.5f, -0.5f,  0.5f,  1.0f, 0.0f,
            0.5f, -0.5f,  0.5f,  1.0f, 0.0f,
            -0.5f, -0.5f,  0.5f,  0.0f, 0.0f,
            -0.5f, -0.5f, -0.5f,  0.0f, 1.0f,

            -0.5f,  0.5f, -0.5f,  0.0f, 1.0f,
            0.5f,  0.5f, -0.5f,  1.0f, 1.0f,
            0.5f,  0.5f,  0.5f,  1.0f, 0.0f,
            0.5f,  0.5f,  0.5f,  1.0f, 0.0f,
            -0.5f,  0.5f,  0.5f,  0.0f, 0.0f,
            -0.5f,  0.5f, -0.5f,  0.0f, 1.0f
    };

    private int[] cubeIndices = {
            0, 1, 2,
            3, 4, 5,

            6, 7, 8,
            9, 10, 11,

            12, 13, 14,
            15, 16, 17,

            18, 19, 20,
            21, 22, 23,

            24, 25, 26,
            27, 28, 29,

            30, 31, 32,
            33, 34, 35,
    };

    private final float[] skyboxVertices = {
            // positions
            -1.0f,  1.0f, -1.0f,
            -1.0f, -1.0f, -1.0f,
            1.0f, -1.0f, -1.0f,
            1.0f, -1.0f, -1.0f,
            1.0f,  1.0f, -1.0f,
            -1.0f,  1.0f, -1.0f,

            -1.0f, -1.0f,  1.0f,
            -1.0f, -1.0f, -1.0f,
            -1.0f,  1.0f, -1.0f,
            -1.0f,  1.0f, -1.0f,
            -1.0f,  1.0f,  1.0f,
            -1.0f, -1.0f,  1.0f,

            1.0f, -1.0f, -1.0f,
            1.0f, -1.0f,  1.0f,
            1.0f,  1.0f,  1.0f,
            1.0f,  1.0f,  1.0f,
            1.0f,  1.0f, -1.0f,
            1.0f, -1.0f, -1.0f,

            -1.0f, -1.0f,  1.0f,
            -1.0f,  1.0f,  1.0f,
            1.0f,  1.0f,  1.0f,
            1.0f,  1.0f,  1.0f,
            1.0f, -1.0f,  1.0f,
            -1.0f, -1.0f,  1.0f,

            -1.0f,  1.0f, -1.0f,
            1.0f,  1.0f, -1.0f,
            1.0f,  1.0f,  1.0f,
            1.0f,  1.0f,  1.0f,
            -1.0f,  1.0f,  1.0f,
            -1.0f,  1.0f, -1.0f,

            -1.0f, -1.0f, -1.0f,
            -1.0f, -1.0f,  1.0f,
            1.0f, -1.0f, -1.0f,
            1.0f, -1.0f, -1.0f,
            -1.0f, -1.0f,  1.0f,
            1.0f, -1.0f,  1.0f
    };

    public Renderer(Game game) {
        this.game = game;
        this.height = game.getHeight();
        this.width = game.getWidth();
    }

    private void compileShaders() {
        // default vertex shader
        String vertexSource = "";
        try {
            vertexSource = FileReader.readFile("/shaders/default.vert"); // TODO use lwjgl features instead
        }
        catch (Exception e) {
            printError("Default vertex shader could not be read from file");
        }

        Shader vertexShader = new Shader(vertexSource, GL_VERTEX_SHADER);

        if (!vertexShader.checkShaderStatus()) {
            printError("Default vertex shader failed to compile");
        }

        // default fragment shader
        String fragmentSource = "";
        try {
            fragmentSource = FileReader.readFile("/shaders/default.frag");
        }
        catch (Exception e) {
            printError("Default fragment shader could not be read from file");
        }

        Shader fragmentShader = new Shader(fragmentSource, GL_FRAGMENT_SHADER);

        if (!fragmentShader.checkShaderStatus()) {
            printError("Default fragment shader failed to compile");
        }

        // default shader program
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

        // skybox vertex shader
        String skyboxVertexSource = "";
        try {
            skyboxVertexSource = FileReader.readFile("/shaders/skybox.vert"); // TODO use lwjgl features instead
        }
        catch (Exception e) {
            printError("Skybox vertex shader could not be read from file");
        }

        Shader skyboxVertexShader = new Shader(skyboxVertexSource, GL_VERTEX_SHADER);

        if (!skyboxVertexShader.checkShaderStatus()) {
            printError("Skybox vertex shader failed to compile");
        }

        // skybox fragment shader
        String skyboxFragmentSource = "";
        try {
            skyboxFragmentSource = FileReader.readFile("/shaders/skybox.frag");
        }
        catch (Exception e) {
            printError("Skybox fragment shader could not be read from file");
        }

        Shader skyboxFragmentShader = new Shader(skyboxFragmentSource, GL_FRAGMENT_SHADER);

        if (!skyboxFragmentShader.checkShaderStatus()) {
            printError("Skybox fragment shader failed to compile");
        }

        // skybox shader program
        skyboxShaderProgram = new ShaderProgram();
        skyboxShaderProgram.attachShader(skyboxFragmentShader);
        skyboxShaderProgram.attachShader(skyboxVertexShader);
        skyboxShaderProgram.linkProgram();

        if (!skyboxShaderProgram.checkProgramLinkStatus()) {
            printError("Skybox shader program failed to link");
        }

        skyboxShaderProgram.detachShader(skyboxVertexShader);
        skyboxShaderProgram.detachShader(skyboxFragmentShader);

        skyboxVertexShader.delete();
        skyboxFragmentShader.delete();
    }

    private void compileTextures() {
        // texture setup

        testTexture = new Texture();
        testTexture.bindImageData("assets/images/container2.png", true);
        testTexture.generateMipmaps();
        testTexture.setWrapSettings(GL_REPEAT);
        testTexture.setShrinkMode(GL_LINEAR_MIPMAP_NEAREST);
        testTexture.setStretchMode(GL_NEAREST);






        // atlas
//        textureAtlas = new Texture();
//        textureAtlas.bindImageData("assets/images/textureAtlas.png", true);
//        textureAtlas.generateMipmaps();
//        textureAtlas.setWrapSettings(GL_REPEAT);
//        textureAtlas.setShrinkMode(GL_NEAREST);
//        textureAtlas.setStretchMode(GL_NEAREST);

        glActiveTexture(GL_TEXTURE0);
        testTexture.bind();

        // skybox
        String[] skyboxFilePaths = {
                "assets/images/skybox/right.jpg",
                "assets/images/skybox/left.jpg",
                "assets/images/skybox/top.jpg",
                "assets/images/skybox/bottom.jpg",
                "assets/images/skybox/front.jpg",
                "assets/images/skybox/back.jpg"
        };

        skybox = new CubeMap();
        skybox.bindImageData(skyboxFilePaths, false);
        skybox.setShrinkMode(GL_LINEAR);
        skybox.setStretchMode(GL_LINEAR);
        skybox.setWrapSettings(GL_CLAMP_TO_EDGE);
    }

    private void renderInit() {
        window = new Window(height, width, "Nebula", true);

        camera = new Camera(window, game.getDefaultFOV(), game.getMouseSensitivity(), game.getMovementSpeed(), new Vector3f(0, 62, 0));

        window.create();

        GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        height = vidMode.height(); //TODO update height universally
        width = vidMode.width();

        window.centerWindow();

        glfwSetInputMode(window.getWindow(), GLFW_CURSOR, GLFW_CURSOR_DISABLED);

        // basic setup
        GL.createCapabilities();
        //Callback debugProc = GLUtil.setupDebugMessageCallback(); //prints OpenGL debug info to the console
        compileShaders();
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_TEXTURE_CUBE_MAP_SEAMLESS);

        // VAO and VBO setup
        int positionSize = 3;
        int uvSize = 2;
        int vertexSizeBytes = (positionSize + uvSize);

        // world
//        worldVAO = new VertexArrayObject();
//        worldVBO = new VertexBufferObject();
//        worldEBO = new ElementBufferObject();
//
//        worldVAO.bind();
//        worldVBO.bind();
//        worldVBO.setMaxDataSize(Integer.MAX_VALUE); //TODO Can be lowered (probably)
//        worldEBO.bind();
//        worldEBO.setMaxDataSize(Integer.MAX_VALUE);
//
//        createAttributePointer(0, positionSize, vertexSizeBytes, 0);
//        enableAttributePointer(0);
//        createAttributePointer(1, uvSize, vertexSizeBytes, positionSize);
//        enableAttributePointer(1);
//
//        defaultShaderProgram.use();
//        defaultShaderProgram.uploadUniform("texture1", 0);
//
//        worldVAO.unbind();
//        worldVBO.unbind();
//        worldEBO.unbind();

        // cube
//        cubeVAO = new VertexArrayObject();
//        cubeVBO = new VertexBufferObject();
//        cubeEBO = new ElementBufferObject();
//
//        cubeVAO.bind();
//        cubeVBO.bind();
//        cubeVBO.bindVertexData(vertices);
//        cubeEBO.bind();
//        cubeEBO.bindIndexData(cubeIndices);
//
//        createAttributePointer(0, positionSize, vertexSizeBytes, 0);
//        enableAttributePointer(0);
//        createAttributePointer(1, uvSize, vertexSizeBytes, positionSize);
//        enableAttributePointer(1);
//
//        defaultShaderProgram.use();
//        defaultShaderProgram.uploadUniform("texture1", 0);
//
//        cubeVAO.unbind();
//        cubeVBO.unbind();
//        cubeEBO.unbind();

        // skybox
        vertexSizeBytes = positionSize;

        skyboxVAO = new VertexArrayObject();
        skyboxVBO = new VertexBufferObject();

        skyboxVAO.bind();
        skyboxVBO.bind();
        skyboxVBO.bindVertexData(skyboxVertices);

        createAttributePointer(0, positionSize, vertexSizeBytes, 0);
        enableAttributePointer(0);

        skyboxShaderProgram.use();
        skyboxShaderProgram.uploadUniform("texture1", 1);

        skyboxVAO.unbind();
        skyboxVBO.unbind();

        //glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);

        projectionMatrix = camera.getProjectionMatrix();
    }

    public void init() {
        renderInit();
        compileTextures();
    }

    public void firstUpdate() {
        if (glfwWindowShouldClose(window.getWindow())) {
            game.setGameOpen(false);
        }

        camera.update();

        game.setPlayerPosition(camera.getPosition());

//        swapBuffers();

        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        // matrix setup
        viewMatrix = camera.getViewMatrix();

        defaultShaderProgram.use();

        defaultShaderProgram.uploadUniform("view", viewMatrix);
        defaultShaderProgram.uploadUniform("projection", projectionMatrix);

        if(glfwGetKey(window.getWindow(), GLFW_KEY_ESCAPE) == GLFW_PRESS) {
            glfwSetWindowShouldClose(window.getWindow(), true);
        }
    }

    public void lastUpdate() {
        glDepthFunc(GL_LEQUAL);
        glDisable(GL_CULL_FACE);
        skyboxShaderProgram.use();
        skyboxShaderProgram.uploadUniform("view", new Matrix4f(new Matrix3f(camera.getViewMatrix())));
        skyboxShaderProgram.uploadUniform("projection", projectionMatrix);

        glActiveTexture(GL_TEXTURE1);
        skybox.bind();

        skyboxVAO.bind();
        glDrawArrays(GL_TRIANGLES, 0, 36);
        glDepthFunc(GL_LESS);

        swapBuffers();
        glfwPollEvents();

        deltaTime = (float) (glfwGetTime() - lastFrame);
        lastFrame = (float) glfwGetTime();
        //print("FPS:" + (1 / deltaTime));
    }

    public void renderChunk(Chunk chunk) {
        glDepthFunc(GL_LESS);
//        worldVAO.bind();
//        worldVBO.bind();
//        worldEBO.bind();

        modelMatrix = new Matrix4f().translate(chunk.getCoordinates().x * Chunk.xSize, 0, chunk.getCoordinates().y * Chunk.zSize);

        defaultShaderProgram.use();
        defaultShaderProgram.uploadUniform("model", modelMatrix);

        //glBufferSubData(GL_ELEMENT_ARRAY_BUFFER, 0, chunk.getMesh().getIndices());
//        worldEBO.bindVertexData(0, chunk.getMesh().getIndices());
//        worldVBO.bindVertexData(0, chunk.getMesh().getVertices());

        //chunk.getVAO().bind();
//        chunk.getVBO().bind();
//        chunk.getEBO().bind();

        glEnable(GL_CULL_FACE);
        glFrontFace(GL_CW);

//        glDrawElements(GL_TRIANGLES, 36, GL_UNSIGNED_INT, 0);


        chunk.getVAO().bind();

        glDrawElements(GL_TRIANGLES, chunk.getNumberOfVertices() * 6, GL_UNSIGNED_INT, 0);

        chunk.getVAO().unbind();
    }

    public void swapBuffers() {
        glfwSwapBuffers(window.getWindow());
    }

    public void delete() {
        glfwTerminate();
//        worldEBO.delete();
//        worldVBO.delete();
//        worldVAO.delete();
        skyboxVBO.delete();
        skyboxVAO.delete();



//        textureAtlas.delete();
        testTexture.delete();
        skybox.delete();

        defaultShaderProgram.delete();
    }
}
