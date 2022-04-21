package ca.benbingham.engine.util;

import org.joml.Vector2i;

public class Util {
    public static boolean checkIfOnEdge(int max, Vector2i position) {
        if (position.x == 0 || position.y == 0 || position.x == max || position.y == max) {
            return true;
        }
        else {
            return false;
        }
    }

    public static boolean checkIfOnEdge(int xMax, int yMax, Vector2i position) {
        if (position.x == 0 || position.y == 0 || position.x == xMax || position.y == yMax) {
            return true;
        }
        else {
            return false;
        }
    }

    public static boolean checkIfOnEdge(int xMine, int yMin, int xMax, int yMax, Vector2i position) {
        if (position.x == xMine || position.y == yMin || position.x == xMax || position.y == yMax) {
            return true;
        }
        else {
            return false;
        }
    }
}
