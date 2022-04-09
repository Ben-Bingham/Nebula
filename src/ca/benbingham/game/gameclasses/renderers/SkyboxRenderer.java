package ca.benbingham.game.gameclasses.renderers;

import ca.benbingham.engine.graphics.CubeMap;
import ca.benbingham.engine.graphics.renderingobjects.VertexArrayObject;
import ca.benbingham.engine.graphics.renderingobjects.VertexBufferObject;
import ca.benbingham.engine.graphics.shaders.Shader;
import ca.benbingham.engine.graphics.shaders.ShaderProgram;
import ca.benbingham.engine.images.EImageModes;
import ca.benbingham.engine.images.Image;
import ca.benbingham.engine.util.FileReader;
import ca.benbingham.game.gameclasses.renderers.interfaces.IRenderer;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import static ca.benbingham.engine.graphics.renderingobjects.VertexArrayObject.createAttributePointer;
import static ca.benbingham.engine.graphics.renderingobjects.VertexArrayObject.enableAttributePointer;
import static ca.benbingham.engine.util.Printing.print;
import static ca.benbingham.engine.util.Printing.printError;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL13.GL_TEXTURE1;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;

public class SkyboxRenderer implements IRenderer {
    private ShaderProgram skyboxShaderProgram;
    private CubeMap skybox;

    private VertexArrayObject skyboxVAO;
    private VertexBufferObject skyboxVBO;

    private MasterRenderer masterRenderer;

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

    public SkyboxRenderer(MasterRenderer masterRenderer) {
        this.masterRenderer = masterRenderer;
    }

    @Override
    public void init() {
        shaderProgram();
        textureLoading();
        VAOInit();
    }

    @Override
    public void firstUpdate() {

    }

    @Override
    public void lastUpdate() {
        glDepthFunc(GL_LEQUAL);
        glDisable(GL_CULL_FACE);
        skyboxShaderProgram.use();
        skyboxShaderProgram.uploadUniform("view", new Matrix4f(new Matrix3f(masterRenderer.getCamera().getViewMatrix())));
        skyboxShaderProgram.uploadUniform("projection", masterRenderer.getCamera().getProjectionMatrix());

        glActiveTexture(GL_TEXTURE1);
        skybox.bind();

        skyboxVAO.bind();
        glDrawArrays(GL_TRIANGLES, 0, 36);
        skyboxVAO.unbind();

        glDepthFunc(GL_LESS);
        glEnable(GL_CULL_FACE);
    }

    private void shaderProgram() {
        // skybox vertex shader
        String skyboxVertexSource = "";
        try {
            skyboxVertexSource = FileReader.readFile("/shaders/skybox/skybox.vert");
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
            skyboxFragmentSource = FileReader.readFile("/shaders/skybox/skybox.frag");
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

    private void textureLoading() {
        String[] skyboxFilePaths = {
                "skybox/right.jpg",
                "skybox/left.jpg",
                "skybox/top.jpg",
                "skybox/bottom.jpg",
                "skybox/front.jpg",
                "skybox/back.jpg"
        };

        Image[] images = new Image[skyboxFilePaths.length];

        for (int i = 0; i < images.length; i++) {
            images[i] = new Image(skyboxFilePaths[i], EImageModes.NEBULA_BYTE_BUFFER, false);
        }

        skybox = new CubeMap();
        skybox.bindImageData(images);
        skybox.setShrinkMode(GL_LINEAR);
        skybox.setStretchMode(GL_LINEAR);
        skybox.setWrapSettings(GL_CLAMP_TO_EDGE);
    }

    private void VAOInit() {
        int positionSize = 3;
        int vertexSizeBytes;

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
    }

    @Override
    public void delete() {
        skyboxVBO.delete();
        skyboxVAO.delete();

        skybox.delete();

        skyboxShaderProgram.delete();
    }
}
