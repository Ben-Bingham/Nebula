package ca.benbingham.game.gameclasses.renderers;

import ca.benbingham.engine.graphics.renderingobjects.VertexArrayObject;
import ca.benbingham.engine.graphics.renderingobjects.VertexBufferObject;
import ca.benbingham.engine.graphics.shaders.Shader;
import ca.benbingham.engine.graphics.shaders.ShaderProgram;
import ca.benbingham.engine.util.FileReader;
import ca.benbingham.game.gameclasses.renderers.interfaces.IRenderer;
import ca.benbingham.game.planetstructure.Chunk;
import ca.benbingham.game.planetstructure.ChunkDebugLineData;
import org.joml.Matrix4f;
import org.joml.Vector4f;

import static ca.benbingham.engine.graphics.renderingobjects.VertexArrayObject.createAttributePointer;
import static ca.benbingham.engine.graphics.renderingobjects.VertexArrayObject.enableAttributePointer;
import static ca.benbingham.engine.util.GLError.getOpenGLError;
import static ca.benbingham.engine.util.Printing.print;
import static ca.benbingham.engine.util.Printing.printError;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_LESS;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;

public class DebugRenderer implements IRenderer {
    private MasterRenderer masterRenderer;

    private ShaderProgram shaderProgram;

    private VertexArrayObject chunkLinesVAO;
    private VertexBufferObject chunkLinesVBO;

    private VertexArrayObject secondaryChunkLinesVAO;
    private VertexBufferObject secondaryChunkLinesVBO;

    private ChunkDebugLineData chunkDebugLineData;
    private short chunkDebugLines = 0;

    private Matrix4f modelMatrix;

    public DebugRenderer(MasterRenderer masterRenderer) {
        this.masterRenderer = masterRenderer;
    }

    @Override
    public void init() {
        chunkDebugLineData = new ChunkDebugLineData();

        shaderProgram();
        VAOInit();
    }

    @Override
    public void firstUpdate() {
        shaderProgram.use();
        shaderProgram.uploadUniform("view", masterRenderer.getCamera().getViewMatrix());
        shaderProgram.uploadUniform("projection", masterRenderer.getCamera().getProjectionMatrix());
        shaderProgram.uploadUniform("curvedWorld", false);
        shaderProgram.uploadUniform("worldCurve", 150f);

        processInput();

        glDepthFunc(GL_LESS);
        glDisable(GL_CULL_FACE);

        //TODO Chunk debug lines are scuffed
        if (chunkDebugLines != 0) {
            for (int i = 0; i < masterRenderer.getGame().getRenderDistance(); i++) {
                for (int j = 0; j < masterRenderer.getGame().getRenderDistance(); j++) {
                    modelMatrix = new Matrix4f().translate(Chunk.xSize * i, 0, Chunk.zSize * j);

                    if (chunkDebugLines == 1) {
                        shaderProgram.use();
                        shaderProgram.uploadUniform("model", modelMatrix);
                        shaderProgram.uploadUniform("color", new Vector4f(0, 1, 0, 1));

                        chunkLinesVAO.bind();
                        glDrawArrays(GL_LINES, 0, 2);
                    }
                    else if (chunkDebugLines == 2) {
                        shaderProgram.use();
                        shaderProgram.uploadUniform("model", modelMatrix);
                        shaderProgram.uploadUniform("color", new Vector4f(0, 1, 0, 1));

                        chunkLinesVAO.bind();
                        glDrawArrays(GL_LINES, 0, 2);

                        shaderProgram.uploadUniform("color", new Vector4f(1, 0, 0, 1));

                        secondaryChunkLinesVAO.bind();
                        glDrawArrays(GL_LINES, 0, chunkDebugLineData.secondaryChunkLines.length);
                    }
                    getOpenGLError(new Throwable());
                }
            }
        }
    }

    @Override
    public void lastUpdate() {

    }

    private void shaderProgram() {
        // debug vertex shader
        String debugVertexSource = "";
        try {
            debugVertexSource = FileReader.readFile("/shaders/debug/debug.vert");
        }
        catch (Exception e) {
            printError("Debug vertex shader could not be read from file");
        }

        Shader debugVertexShader = new Shader(debugVertexSource, GL_VERTEX_SHADER);

        if (!debugVertexShader.checkShaderStatus()) {
            printError("Debug vertex shader failed to compile");
        }

        // skybox fragment shader
        String debugFragmentSource = "";
        try {
            debugFragmentSource = FileReader.readFile("/shaders/debug/debug.frag");
        }
        catch (Exception e) {
            printError("Debug fragment shader could not be read from file");
        }

        Shader debugFragmentShader = new Shader(debugFragmentSource, GL_FRAGMENT_SHADER);

        if (!debugFragmentShader.checkShaderStatus()) {
            printError("Debug fragment shader failed to compile");
        }

        // skybox shader program
        shaderProgram = new ShaderProgram();
        shaderProgram.attachShader(debugVertexShader);
        shaderProgram.attachShader(debugFragmentShader);
        shaderProgram.linkProgram();

        if (!shaderProgram.checkProgramLinkStatus()) {
            printError("Debug shader program failed to link");
        }

        shaderProgram.detachShader(debugVertexShader);
        shaderProgram.detachShader(debugFragmentShader);

        debugVertexShader.delete();
        debugFragmentShader.delete();
    }

    private void VAOInit() {
        int positionSize = 3;
        int vertexSizeBytes = positionSize;

        // Primary chunk lines.
        chunkLinesVAO = new VertexArrayObject();
        chunkLinesVBO = new VertexBufferObject();

        chunkLinesVAO.bind();
        chunkLinesVBO.bind();
        chunkLinesVBO.bindVertexData(chunkDebugLineData.primaryChunkLines);

        createAttributePointer(0, positionSize, vertexSizeBytes, 0);
        enableAttributePointer(0);

        chunkLinesVAO.unbind();
        chunkLinesVBO.unbind();

        // Secondary chunk lines.
        secondaryChunkLinesVAO = new VertexArrayObject();
        secondaryChunkLinesVBO = new VertexBufferObject();

        secondaryChunkLinesVAO.bind();
        secondaryChunkLinesVBO.bind();
        secondaryChunkLinesVBO.bindVertexData(chunkDebugLineData.secondaryChunkLines);

        createAttributePointer(0, positionSize, vertexSizeBytes, 0);
        enableAttributePointer(0);

        secondaryChunkLinesVAO.unbind();
        secondaryChunkLinesVBO.unbind();
    }

    private void processInput() {
        if(glfwGetKey(masterRenderer.getWindow().getWindow(), GLFW_KEY_ESCAPE) == GLFW_PRESS) {
            glfwSetWindowShouldClose(masterRenderer.getWindow().getWindow(), true);
        }

        glfwSetKeyCallback(masterRenderer.getWindow().getWindow(), (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_F9 && action == GLFW_PRESS) {
                if (chunkDebugLines != 2) {
                    chunkDebugLines++;
                }
                else {
                    chunkDebugLines = 0;
                }
            }
        });
    }

    @Override
    public void delete() {
        shaderProgram.delete();

        chunkLinesVAO.delete();
        chunkLinesVBO.delete();

        secondaryChunkLinesVAO.delete();
        secondaryChunkLinesVBO.delete();
    }
}
