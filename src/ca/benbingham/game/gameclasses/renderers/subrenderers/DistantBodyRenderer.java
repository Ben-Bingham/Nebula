package ca.benbingham.game.gameclasses.renderers.subrenderers;

import ca.benbingham.engine.graphics.openglobjects.VertexArrayObject;
import ca.benbingham.engine.graphics.openglobjects.VertexBufferObject;
import ca.benbingham.engine.graphics.shaders.ShaderProgram;
import ca.benbingham.engine.graphics.shaders.ShaderProgramGenerator;
import ca.benbingham.game.gameclasses.Game;
import ca.benbingham.game.gameclasses.renderers.MasterRenderer;
import ca.benbingham.game.gameclasses.renderers.IRenderer;
import ca.benbingham.game.interstellarobjects.CubeGeometry;
import ca.benbingham.game.interstellarobjects.bodys.CosmicBody;
import ca.benbingham.game.interstellarobjects.bodys.Moon;
import ca.benbingham.game.interstellarobjects.bodys.Planet;
import ca.benbingham.game.interstellarobjects.bodys.Star;
import org.joml.*;

import java.util.ArrayList;

import static ca.benbingham.engine.graphics.openglobjects.VertexArrayObject.createAttributePointer;
import static ca.benbingham.engine.graphics.openglobjects.VertexArrayObject.enableAttributePointer;
import static org.lwjgl.opengl.GL11.*;

public class DistantBodyRenderer implements IRenderer {
    private final MasterRenderer masterRenderer;

    private VertexArrayObject otherBodyVAO;
    private VertexBufferObject otherBodyVBO;

    private ShaderProgram shaderProgram;
    private ArrayList<CosmicBody> activeBodies;

    private Game game;

    public DistantBodyRenderer(MasterRenderer masterRenderer) {
        this.masterRenderer = masterRenderer;
        this.game = masterRenderer.getGame();
    }

    @Override
    public void init() {
        ShaderProgramGenerator shaderProgramGenerator = new ShaderProgramGenerator("/shaders/otherBodys/otherBodys.vert", "/shaders/otherBodys/otherBodys.frag", "Other Bodys");
        shaderProgram = shaderProgramGenerator.getShaderProgram();

        vaoInit();

        activeBodies = new ArrayList<>();
    }

    @Override
    public void firstUpdate() {
        glDisable(GL_CULL_FACE);
        glDepthFunc(GL_LESS);

        shaderProgram.use();
        shaderProgram.uploadUniform("view", game.player.getCamera().getViewMatrix());
        shaderProgram.uploadUniform("projection", game.player.getCamera().getProjectionMatrix());

        int r = 0;
        int g = 0;
        int b = 0;

        for (int i = 0; i < activeBodies.size(); i++) {
//            activeBodies.get(i).setOrbitProgress((activeBodies.get(i).getOrbitProgress() + (masterRenderer.getGame().getDeltaTime() / activeBodies.get(i).getYear()) * 360));
//
//            if (activeBodies.get(i).getOrbitProgress() >= 360) {
//                activeBodies.get(i).setOrbitProgress(0);
//            }
//
//            double x = Math.toRadians(activeBodies.get(i).getOrbitProgress());
//            double y = Math.toRadians(activeBodies.get(i).getOrbitTilt());
//            double R = activeBodies.get(i).getOrbitRadius();

            Matrix4f modelMatrix = new Matrix4f();
            modelMatrix = new Matrix4f().translate(activeBodies.get(i).globalPosition);
            modelMatrix.translate(new Vector3f(
//                    (float) (R * Math.cos(x)) + activeBodies.get(i).getParent().getGlobalPosition().x,
//                    (float) (R * Math.sin(x) * Math.sin(y)) + activeBodies.get(i).getParent().getGlobalPosition().x,
//                    (float) (R * Math.sin(x) * Math.cos(y)) + activeBodies.get(i).getParent().getGlobalPosition().x
            ));
//            Vector4f bodyGlobalPos4f = new Vector4f(activeBodies.get(i).getGlobalPosition().x, activeBodies.get(i).getGlobalPosition().y, activeBodies.get(i).getGlobalPosition().z, 1.0f);
//            bodyGlobalPos4f.mul(modelMatrix);
//
//            activeBodies.get(i).setGlobalPosition(new Vector3f(bodyGlobalPos4f.x, bodyGlobalPos4f.y, bodyGlobalPos4f.z));

            modelMatrix.scale((float) (activeBodies.get(i).radius * 2) + 256);

            shaderProgram.uploadUniform("model", modelMatrix);
            if (activeBodies.get(i) instanceof Planet) {
                r = 103;
                g = 230;
                b = 7;
            }
            else if (activeBodies.get(i) instanceof Star) {
                r = 230;
                g = 124;
                b = 18;
            }
            else if (activeBodies.get(i) instanceof Moon) {
                r = 112;
                g = 123;
                b = 140;
            }

            shaderProgram.uploadUniform("colour", new Vector4f(r, g, b, 1));
            otherBodyVAO.bind();
            glDrawArrays(GL_TRIANGLES, 0, 10000);
        }
    }

    @Override
    public void lastUpdate() {

    }

    private void vaoInit() {
        int positionSize = 3;
        int vertexSizeBytes = positionSize;

        otherBodyVAO = new VertexArrayObject();
        otherBodyVBO = new VertexBufferObject();

        otherBodyVAO.bind();
        otherBodyVBO.bind();
        otherBodyVBO.bindVertexData(CubeGeometry.vertices);

        createAttributePointer(0, positionSize, vertexSizeBytes, 0);
        enableAttributePointer(0);

        otherBodyVAO.unbind();
        otherBodyVBO.unbind();
    }

    public void addBody(CosmicBody activeBody) {
        if (!activeBodies.contains(activeBody)) {
            activeBodies.add(activeBody);
        }
    }

    public void setActiveBodies(ArrayList<CosmicBody> bodies) {
        activeBodies = bodies;
    }

    public ArrayList<CosmicBody> getActiveBodies() {
        return activeBodies;
    }

    @Override
    public void terminate() {
        shaderProgram.delete();
        otherBodyVBO.delete();
        otherBodyVAO.delete();
    }
}
