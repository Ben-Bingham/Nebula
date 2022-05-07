package ca.benbingham.game.gameclasses.renderers.subrenderers;

import ca.benbingham.engine.graphics.CubeMap;
import ca.benbingham.engine.graphics.openglobjects.VertexArrayObject;
import ca.benbingham.engine.graphics.openglobjects.VertexBufferObject;
import ca.benbingham.engine.graphics.shaders.ShaderProgram;
import ca.benbingham.engine.graphics.shaders.ShaderProgramGenerator;
import ca.benbingham.engine.graphics.images.enums.EImageModes;
import ca.benbingham.engine.graphics.images.Image;
import ca.benbingham.game.gameclasses.Game;
import ca.benbingham.game.gameclasses.renderers.MasterRenderer;
import ca.benbingham.game.gameclasses.renderers.IRenderer;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import static ca.benbingham.engine.graphics.openglobjects.VertexArrayObject.createAttributePointer;
import static ca.benbingham.engine.graphics.openglobjects.VertexArrayObject.enableAttributePointer;
import static ca.benbingham.engine.util.Printing.print;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL13.GL_TEXTURE1;
import static org.lwjgl.opengl.GL13.glActiveTexture;

public class SkyboxRenderer implements IRenderer {
    private ShaderProgram skyboxShaderProgram;
    private CubeMap skybox;

    private VertexArrayObject skyboxVAO;
    private VertexBufferObject skyboxVBO;

    private MasterRenderer masterRenderer;

    private Game game;

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
        this.game = masterRenderer.getGame();
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
        skyboxShaderProgram.uploadUniform("view", new Matrix4f(new Matrix3f(game.player.getCamera().getViewMatrix()))); //TODO might not need to make a second matrix here
        skyboxShaderProgram.uploadUniform("projection", game.player.getCamera().getProjectionMatrix());
        skyboxShaderProgram.uploadUniform("rotation", masterRenderer.getPlanetRotationMatrix());

        glActiveTexture(GL_TEXTURE1);
        skybox.bind();

        skyboxVAO.bind();
        glDrawArrays(GL_TRIANGLES, 0, 36);
        skyboxVAO.unbind();

        glDepthFunc(GL_LESS);
        glEnable(GL_CULL_FACE);
    }

    private void shaderProgram() {
        ShaderProgramGenerator skyboxShaderProgramGenerator = new ShaderProgramGenerator("/shaders/skybox/skybox.vert", "/shaders/skybox/skybox.frag", "Skybox");
        skyboxShaderProgram = skyboxShaderProgramGenerator.getShaderProgram();
    }

    private void textureLoading() {
//        String[] skyboxFilePaths = {
//                "skybox/day time with mountains/right.jpg",
//                "skybox/day time with mountains/left.jpg",
//                "skybox/day time with mountains/top.jpg",
//                "skybox/day time with mountains/bottom.jpg",
//                "skybox/day time with mountains/front.jpg",
//                "skybox/day time with mountains/back.jpg"
//        };
        String[] skyboxFilePaths = {
                "skybox/Space (1)/right.png",
                "skybox/Space (1)/left.png",
                "skybox/Space (1)/top.png",
                "skybox/Space (1)/bottom.png",
                "skybox/Space (1)/front.png",
                "skybox/Space (1)/back.png"
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
    public void terminate() {
        skyboxVAO.delete();
        skyboxVBO.delete();

        skybox.delete();

        skyboxShaderProgram.delete();
    }
}
