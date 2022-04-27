package ca.benbingham.game.gameclasses.renderers;

import ca.benbingham.engine.graphics.Texture;
import ca.benbingham.engine.graphics.shaders.Shader;
import ca.benbingham.engine.graphics.shaders.ShaderProgram;
import ca.benbingham.engine.images.EImageModes;
import ca.benbingham.engine.images.Image;
import ca.benbingham.engine.util.FileReader;
import ca.benbingham.game.gameclasses.renderers.interfaces.IRenderer;
import ca.benbingham.game.planetstructure.Chunk;
import org.joml.Matrix4f;

import static ca.benbingham.engine.util.GLError.getOpenGLError;
import static ca.benbingham.engine.util.Printing.print;
import static ca.benbingham.engine.util.Printing.printError;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;

public class ChunkRenderer implements IRenderer {
    private MasterRenderer masterRenderer;

    private ShaderProgram shaderProgram;
    private Texture textureAtlas;
    private Matrix4f modelMatrix;

    public ChunkRenderer(MasterRenderer masterRenderer) {
        this.masterRenderer = masterRenderer;
    }

    @Override
    public void init() {
        shaderProgram();
        textureLoading();
    }

    @Override
    public void firstUpdate() {
        shaderProgram.use();
        shaderProgram.uploadUniform("view", masterRenderer.getCamera().getViewMatrix());
        shaderProgram.uploadUniform("projection", masterRenderer.getCamera().getProjectionMatrix());
        shaderProgram.uploadUniform("curvedWorld", false); //TODO world curving needs to be more precise.
        shaderProgram.uploadUniform("worldCurve", 1500f);
    }

    @Override
    public void lastUpdate() {

    }

    private void shaderProgram() {
        // default vertex shader
        String vertexSource = "";
        try {
            vertexSource = FileReader.readFile("/shaders/default/default.vert");
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
            fragmentSource = FileReader.readFile("/shaders/default/default.frag");
        }
        catch (Exception e) {
            printError("Default fragment shader could not be read from file");
        }

        Shader fragmentShader = new Shader(fragmentSource, GL_FRAGMENT_SHADER);

        if (!fragmentShader.checkShaderStatus()) {
            printError("Default fragment shader failed to compile");
        }

        // default shader program
        shaderProgram = new ShaderProgram();
        shaderProgram.attachShader(fragmentShader);
        shaderProgram.attachShader(vertexShader);
        shaderProgram.linkProgram();

        if (!shaderProgram.checkProgramLinkStatus()) {
            printError("Default shader program failed to link");
        }

        shaderProgram.detachShader(vertexShader);
        shaderProgram.detachShader(fragmentShader);

        vertexShader.delete();
        fragmentShader.delete();
    }

    private void textureLoading() {
        // atlas
        textureAtlas = new Texture();
        textureAtlas.bindImageData(new Image("Texture-Atlas.png", EImageModes.NEBULA_BYTE_BUFFER, true));
        textureAtlas.generateMipmaps();
        textureAtlas.setWrapSettings(GL_REPEAT);
        textureAtlas.setShrinkMode(GL_NEAREST);
        textureAtlas.setStretchMode(GL_NEAREST);

        glActiveTexture(GL_TEXTURE0);
        textureAtlas.bind();
    }

    public void renderChunk(Chunk chunk) {
        glDepthFunc(GL_LESS);
        glEnable(GL_CULL_FACE);
        glFrontFace(GL_CW);

        modelMatrix = new Matrix4f().translate(chunk.getCoordinates().x * Chunk.xSize, 0, chunk.getCoordinates().y * Chunk.zSize);
        shaderProgram.use();
        shaderProgram.uploadUniform("model", modelMatrix);

        chunk.getVAO().bind();
        glDrawElements(GL_TRIANGLES, chunk.getNumberOfVertices() * 6, GL_UNSIGNED_INT, 0);
        chunk.getVAO().unbind();

        glDisable(GL_CULL_FACE);
    }

    @Override
    public void delete() {
        shaderProgram.delete();
    }
}
