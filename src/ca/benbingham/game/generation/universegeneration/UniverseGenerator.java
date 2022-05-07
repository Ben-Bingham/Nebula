package ca.benbingham.game.generation.universegeneration;

import ca.benbingham.game.interstellarobjects.Galaxy;
import ca.benbingham.game.interstellarobjects.SolarSystem;
import ca.benbingham.game.interstellarobjects.Universe;
import ca.benbingham.game.interstellarobjects.bodys.CosmicBody;
import ca.benbingham.game.interstellarobjects.bodys.Moon;
import ca.benbingham.game.interstellarobjects.bodys.Planet;
import ca.benbingham.game.interstellarobjects.bodys.Star;
import ca.benbingham.game.planetstructure.enums.EPlanetTypes;
import org.joml.Vector3f;

import static ca.benbingham.engine.util.Printing.print;
import static ca.benbingham.engine.util.math.Vector3fMath.add;

public class UniverseGenerator {
    private int numberOfGalaxies;
    private int solarSystemsPerGalaxy;
    private int planetsPerSolarSystem;

    public UniverseGenerator(int numberOfGalaxies, int solarSystemsPerGalaxy, int planetsPerSolarSystem) {
        this.numberOfGalaxies = numberOfGalaxies;
        this.solarSystemsPerGalaxy = solarSystemsPerGalaxy;
        this.planetsPerSolarSystem = planetsPerSolarSystem;
    }

    public Universe createUniverse() {
        Moon moon = new Moon(5, new Vector3f(450, 450, -450), 3340, 137813897L, 15);
        Planet earth = new Planet(new Vector3f(2000, 0, 2000), EPlanetTypes.NEBULA_DEFAULT_PLANET, 15, 378167312736L, new Moon[]{moon}, 23);
        Star sun = new Star("Sun", new Vector3f(0, 0, 0), 40, new Planet[]{earth}, 3871893718973L, 0);

        SolarSystem solarSystem = new SolarSystem("System 1", new Vector3f(0, 0, 0), new Star[]{sun});
        Galaxy galaxy = new Galaxy("Milky Way 2.0", new Vector3f(0, 0, 0), new SolarSystem[]{solarSystem});

        Universe universe = new Universe(new Galaxy[]{galaxy});

        Galaxy galaxy1;
        SolarSystem solarSystem1;
        Star star1;
        Planet planet1;
        Moon moon1;

        for (int n = 0; n < universe.getGalaxies().length; n++) {
            galaxy1 = universe.getGalaxies()[n];

            for (int m = 0; m < galaxy1.getSolarSystems().length; m++) {
                solarSystem1 = galaxy1.getSolarSystems()[m];

                solarSystem1.setGlobalPosition(add(solarSystem1.getLocalPosition(), galaxy1.getGlobalPosition()));

                for (int k = 0; k < solarSystem.getStars().length; k++) {
                    star1 = solarSystem.getStars()[k];
                    star1.init(solarSystem);
                    universe.addBody(star1);

                    for (int i = 0; i < star1.children.length; i++) {
                        if (star1.children[i] instanceof Planet) {
                            planet1 = (Planet) star1.children[i];
                        }
                        else {
                            throw new IllegalArgumentException("A stars children must be planets.");
                        }
                        planet1.init(new CosmicBody[]{star1});
                        universe.addBody(planet1);

                        for (int j = 0; j < planet1.children.length; j++) {
                            if (planet1.children[j] instanceof Moon) {
                                moon1 = (Moon) planet1.children[j];
                            }
                            else {
                                throw new IllegalArgumentException("A planets children must be moons.");
                            }

                            moon1.init(new CosmicBody[] {planet1});
                            universe.addBody(moon1);
                        }
                    }
                }
            }
        }

        return universe;
    }
}
