package ca.benbingham.game.gameclasses.renderers.subrenderers;

import ca.benbingham.engine.graphics.Texture;
import ca.benbingham.engine.graphics.shaders.ShaderProgram;
import ca.benbingham.engine.graphics.shaders.ShaderProgramGenerator;
import ca.benbingham.engine.graphics.images.enums.EImageModes;
import ca.benbingham.engine.graphics.images.Image;
import ca.benbingham.game.gameclasses.Game;
import ca.benbingham.game.gameclasses.renderers.MasterRenderer;
import ca.benbingham.game.gameclasses.renderers.IRenderer;
import ca.benbingham.game.interstellarobjects.bodys.CosmicBody;
import ca.benbingham.game.planetstructure.Chunk;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import static ca.benbingham.engine.util.Printing.print;
import static ca.benbingham.engine.util.math.Vector3fMath.subtract;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

public class ChunkRenderer implements IRenderer {
    private MasterRenderer masterRenderer;

    private ShaderProgram shaderProgram;
    private Texture textureAtlas;
    private Matrix4f modelMatrix;

    private Game game;

    public ChunkRenderer(MasterRenderer masterRenderer) {
        this.masterRenderer = masterRenderer;
        this.game = masterRenderer.getGame();
    }

    @Override
    public void init() {
        shaderProgram();
        textureLoading();
    }

    @Override
    public void firstUpdate() {
        shaderProgram.use();
        shaderProgram.uploadUniform("view", game.player.getCamera().getViewMatrix());
        shaderProgram.uploadUniform("projection", game.player.getCamera().getProjectionMatrix());
        shaderProgram.uploadUniform("curvedWorld", true); //TODO world curving needs to be more precise.
        shaderProgram.uploadUniform("worldCurve", 1500f);
        shaderProgram.uploadUniform("lightPos", new Vector3f(0, 200, 0));
        shaderProgram.uploadUniform("lightColor", new Vector3f(1, 1, 1));

        shaderProgram.uploadUniform("playerPosition", masterRenderer.getGame().player.getPosition());

        shaderProgram.uploadUniform("rotation", masterRenderer.getPlanetRotationMatrix());
    }

    @Override
    public void lastUpdate() {

    }

    private void shaderProgram() {
        ShaderProgramGenerator chunkShaderProgramGenerator = new ShaderProgramGenerator("/shaders/default/default.vert", "/shaders/default/default.frag", "Chunk");
        shaderProgram = chunkShaderProgramGenerator.getShaderProgram();
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

    public void renderChunk(Chunk chunk, CosmicBody cosmicBody) {
        glDepthFunc(GL_LESS);
        glEnable(GL_CULL_FACE);
        glFrontFace(GL_CW);
//        print("X: " + (chunk.getCoordinates().x * Chunk.xSize) + (subtract(cosmicBody.getGlobalPosition(), masterRenderer.getGame().getPlayer().getPosition()).x));
//        print("Z: " + (chunk.getCoordinates().y * Chunk.zSize) + (subtract(cosmicBody.getGlobalPosition(), masterRenderer.getGame().getPlayer().getPosition()).z));
        modelMatrix = new Matrix4f().translate(
                (chunk.getCoordinates().x * Chunk.xSize) + (subtract(cosmicBody.globalPosition, masterRenderer.getGame().player.getPosition()).x) * 1,
                0,
                (chunk.getCoordinates().y * Chunk.zSize) + (subtract(cosmicBody.globalPosition, masterRenderer.getGame().player.getPosition()).z) * 1
//                (chunk.getCoordinates().x * Chunk.xSize) + cosmicBody.getGlobalPosition().x,
//                0,
//                (chunk.getCoordinates().y * Chunk.zSize) + cosmicBody.getGlobalPosition().z
        );
        shaderProgram.use();
        shaderProgram.uploadUniform("model", modelMatrix);

        chunk.getVAO().bind();
        glDrawElements(GL_TRIANGLES, chunk.getNumberOfVertices() * 6, GL_UNSIGNED_INT, 0);
        chunk.getVAO().unbind();

        glDisable(GL_CULL_FACE);
    }

    @Override
    public void terminate() {
        shaderProgram.delete();
    }
}
