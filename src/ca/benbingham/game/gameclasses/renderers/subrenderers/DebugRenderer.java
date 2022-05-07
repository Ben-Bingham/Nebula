package ca.benbingham.game.gameclasses.renderers.subrenderers;

import ca.benbingham.engine.graphics.openglobjects.VertexArrayObject;
import ca.benbingham.engine.graphics.openglobjects.VertexBufferObject;
import ca.benbingham.engine.graphics.shaders.ShaderProgram;
import ca.benbingham.engine.graphics.shaders.ShaderProgramGenerator;
import ca.benbingham.game.events.KeyboardPress;
import ca.benbingham.game.gameclasses.Game;
import ca.benbingham.game.gameclasses.Settings;
import ca.benbingham.game.gameclasses.renderers.MasterRenderer;
import ca.benbingham.game.gameclasses.renderers.IRenderer;
import ca.benbingham.game.planetstructure.Chunk;
import ca.benbingham.game.planetstructure.ChunkDebugLineData;
import org.joml.Matrix4f;
import org.joml.Vector2i;
import org.joml.Vector4f;

import static ca.benbingham.engine.graphics.openglobjects.VertexArrayObject.createAttributePointer;
import static ca.benbingham.engine.graphics.openglobjects.VertexArrayObject.enableAttributePointer;
import static ca.benbingham.engine.util.GLError.getOpenGLError;
import static ca.benbingham.engine.util.Printing.print;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_F9;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_LESS;

public class DebugRenderer implements IRenderer {
    private MasterRenderer masterRenderer;

    private ShaderProgram shaderProgram;

    private VertexArrayObject chunkLinesVAO;
    private VertexBufferObject chunkLinesVBO;

    private VertexArrayObject secondaryChunkLinesVAO;
    private VertexBufferObject secondaryChunkLinesVBO;

    private VertexArrayObject trinaryChunkLinesVAO;
    private VertexBufferObject trinaryChunkLinesVBO;

    private ChunkDebugLineData chunkDebugLineData;
    private short chunkDebugLines = 0;

    private Matrix4f modelMatrix;

    private Game game;

    public DebugRenderer(MasterRenderer masterRenderer) {
        this.masterRenderer = masterRenderer;
    }

    @Override
    public void init() {
        chunkDebugLineData = new ChunkDebugLineData();

        this.game = masterRenderer.getGame();

        shaderProgram();
        VAOInit();
    }

    @Override
    public void firstUpdate() {
        shaderProgram.use();
        shaderProgram.uploadUniform("view", game.player.getCamera().getViewMatrix());
        shaderProgram.uploadUniform("projection", game.player.getCamera().getProjectionMatrix());
        shaderProgram.uploadUniform("curvedWorld", false);
        shaderProgram.uploadUniform("worldCurve", 150f);

        glDepthFunc(GL_LESS);
        glDisable(GL_CULL_FACE);

        if (chunkDebugLines != 0) {
            Vector2i playerChunkCords = game.player.getChunkCords();

            int renderDistance = Settings.CHUNK_RENDER_DISTANCE;

            int xDifference = playerChunkCords.x - renderDistance;
            int yDifference = playerChunkCords.y - renderDistance;

            int chunksPerSide = (renderDistance * 2) + 1;

            Vector2i[][] chunkCords = new Vector2i[chunksPerSide][chunksPerSide];

            for (int i = 0; i < chunksPerSide; i++) {
                for (int j = 0; j < chunksPerSide; j++) {
                    chunkCords[i][j] = (new Vector2i(i + xDifference, j + yDifference));
                }
            }

            if (chunkDebugLines == 1) {
                for (int i = 0; i < chunksPerSide; i++) {
                    for (int j = 0; j < chunksPerSide; j++) {
                        modelMatrix = new Matrix4f().translate(Chunk.xSize * chunkCords[i][j].x, 0, Chunk.zSize * chunkCords[i][j].y);

                        shaderProgram.use();
                        shaderProgram.uploadUniform("model", modelMatrix);
                        shaderProgram.uploadUniform("color", new Vector4f(1, 0, 0, 1));

                        chunkLinesVAO.bind();
                        glDrawArrays(GL_LINES, 0, 2);
                    }
                }
            }
            else if (chunkDebugLines == 2) {
                modelMatrix = new Matrix4f().translate(Chunk.xSize * playerChunkCords.x, 0, Chunk.zSize * playerChunkCords.y);
                shaderProgram.use();
                shaderProgram.uploadUniform("model", modelMatrix);
                shaderProgram.uploadUniform("color", new Vector4f(0, 1, 0, 1));

                secondaryChunkLinesVAO.bind();
                glDrawArrays(GL_LINES, 0, chunkDebugLineData.secondaryChunkLines.length);
            }
            else if (chunkDebugLines == 3) {
//                shaderProgram.use();
//                shaderProgram.uploadUniform("color", new Vector4f(0, 0, 1, 1));
//
//                trinaryChunkLinesVBO.bind();
//                trinaryChunkLinesVBO.bindVertexData(chunkDebugLineData.createTrinaryChunkLines(masterRenderer.getGame().getActivePlanet().getHalfCircumference()));
//
//                int circumference = masterRenderer.getGame().getActivePlanet().getCircumference();
//                int halfCircumference = masterRenderer.getGame().getActivePlanet().getHalfCircumference();
//
//                modelMatrix = new Matrix4f().translate( (circumference * Chunk.xSize * (playerChunkCords.x / circumference)), 0, (circumference * Chunk.zSize * (playerChunkCords.y /circumference)));
//                shaderProgram.uploadUniform("model", modelMatrix);
//                trinaryChunkLinesVAO.bind();
//                glDrawArrays(GL_LINES, 0, 8); //TODO

            }
            getOpenGLError(new Throwable());
        }
    }

    @Override
    public void lastUpdate() {

    }

    private void shaderProgram() {
        ShaderProgramGenerator debugPrimaryShaderProgramGenerator = new ShaderProgramGenerator("/shaders/debug/debug.vert", "/shaders/debug/debug.frag", "Primary Debug");
        shaderProgram = debugPrimaryShaderProgramGenerator.getShaderProgram();
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

        // Trinary chunk lines.
        trinaryChunkLinesVAO = new VertexArrayObject();
        trinaryChunkLinesVBO = new VertexBufferObject();

        trinaryChunkLinesVAO.bind();
        trinaryChunkLinesVBO.bind();
        trinaryChunkLinesVBO.setMaxDataSize(8);

        createAttributePointer(0, positionSize, vertexSizeBytes, 0);
        enableAttributePointer(0);

        trinaryChunkLinesVAO.unbind();
        trinaryChunkLinesVBO.unbind();
    }

    public void processInput(KeyboardPress event) {
        if (event.key == GLFW_KEY_F9 && event.action == GLFW_PRESS) {
            if (chunkDebugLines != 3) {
                chunkDebugLines++;
            }
            else {
                chunkDebugLines = 0;
            }
        }
    }

    public short getChunkDebugLines() {
        return chunkDebugLines;
    }

    public void setChunkDebugLines(short chunkDebugLines) {
        this.chunkDebugLines = chunkDebugLines;
    }

    @Override
    public void terminate() {
        shaderProgram.delete();

        chunkLinesVAO.delete();
        chunkLinesVBO.delete();

        secondaryChunkLinesVAO.delete();
        secondaryChunkLinesVBO.delete();

        trinaryChunkLinesVBO.delete();
        trinaryChunkLinesVAO.delete();
    }
}
