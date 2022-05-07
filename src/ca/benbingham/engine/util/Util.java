package ca.benbingham.engine.util;

import ca.benbingham.game.planetstructure.enums.EBlockFaces;
import org.joml.Vector2i;

public class Util {
    public static double map(double value, double inputStart, double inputEnd, double outputStart, double outputEnd) {
        double slope;
        double output;

        slope = (outputEnd - outputStart) / (inputEnd - inputStart);
        output = outputStart + slope * (value - inputStart);

        return output;
    }

    public static boolean contains(EBlockFaces[] array, EBlockFaces element) {
        for (EBlockFaces eBlockSides : array) {
            if (eBlockSides == element) {
                return true;
            }
        }
        return false;
    }
}
