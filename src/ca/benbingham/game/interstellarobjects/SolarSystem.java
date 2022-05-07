package ca.benbingham.game.interstellarobjects;

import ca.benbingham.game.interstellarobjects.bodys.Star;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import static ca.benbingham.engine.util.math.Vector3fMath.add;

public class SolarSystem {
    private String name;
    private Vector3f localPosition;
    private Vector3f globalPosition;
    private Star[] stars;

    public SolarSystem(String name, Vector3f localPosition, Star[] stars) {
        this.name = name;
        this.localPosition = localPosition;
        this.stars = stars;
    }

    public Vector3f getGlobalPosition() {
        return globalPosition;
    }

    public Vector3f getLocalPosition() {
        return localPosition;
    }

    public void setGlobalPosition(Vector3f globalPosition) {
        this.globalPosition = globalPosition;
    }

    public Star[] getStars() {
        return stars;
    }
}
