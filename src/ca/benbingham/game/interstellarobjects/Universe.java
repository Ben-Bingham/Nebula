package ca.benbingham.game.interstellarobjects;

import ca.benbingham.game.interstellarobjects.bodys.*;

import java.util.ArrayList;
import java.util.List;

public class Universe {
    private Galaxy[] galaxies;
    private List<CosmicBody> allBodies; //TODO fine for now but with a giant universe this will not work

    public Universe(Galaxy[] galaxies) {
        this.galaxies = galaxies;

        allBodies = new ArrayList<>();
    }

    public Galaxy[] getGalaxies() {
        return galaxies;
    }

    public List<CosmicBody> getAllBodies() {
        return allBodies;
    }

    public void addBody(CosmicBody body) {
        this.allBodies.add(body);
    }
}
