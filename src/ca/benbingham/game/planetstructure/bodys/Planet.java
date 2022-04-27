package ca.benbingham.game.planetstructure.bodys;

import ca.benbingham.game.planetstructure.enums.EPlanetTypes;

import static ca.benbingham.engine.util.Printing.print;

public class Planet {
    private EPlanetTypes type;
    private double seed;
    private int circumference;
    private int halfCircumference;
    private double diameter;
    private double defaultNoiseFrequency;

    public Planet(EPlanetTypes type, double seed, int circumference) {
        this.type = type;
        this.seed = seed;

        if (circumference % 2 == 0) {
            this.circumference = (circumference + 1);
        }
        else {
            this.circumference = circumference;
        }
        halfCircumference = (this.circumference - 1) / 2;

        diameter = circumference / Math.PI;
        defaultNoiseFrequency = (this.circumference - 1) / 50f;
    }

    public EPlanetTypes getType() {
        return type;
    }

    public double getSeed() {
        return seed;
    }

    public int getCircumference() {
        return circumference;
    }

    public int getHalfCircumference() {
        return halfCircumference;
    }

    public double getDiameter() {
        return diameter;
    }

    public double getDefaultNoiseFrequency() {
        return defaultNoiseFrequency;
    }
}
