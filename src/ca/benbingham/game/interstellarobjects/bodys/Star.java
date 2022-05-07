package ca.benbingham.game.interstellarobjects.bodys;

import org.joml.Vector3f;

public class Star extends CosmicBody {
    private int circumference;
    private String name;

    public Star(String name, Vector3f localPosition, int circumference, Planet[] planets, long seed, float tilt) {
        super(circumference, localPosition, 1408, seed, planets, tilt);
        this.circumference = circumference;
        this.name = name;
    }
}