package ca.benbingham.engine.util.math;

import org.joml.Vector2i;

public class Util {
    public static int snapToRange(int val, int min, int max) {
        if (val < min) {
            val = min;
        }
        if (val > max) {
            val = max;
        }
        return  val;
    }

    public static int inRangeOrMinMinusOne(int val, int min, int max) {
        if (val >= min && val <= max) {
            return val;
        }
        else {
            return min - 1;
        }
    }

    public static boolean determineIfOnEdgeOfGrid(Vector2i position, int xMax, int yMax) {
        if (position.x == 0 || position.x == xMax || position.y == 0 || position.y == yMax) {
            return true;
        }
        return false;
    }
}
