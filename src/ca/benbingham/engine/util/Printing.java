package ca.benbingham.engine.util;

import org.joml.Vector2i;
import org.joml.Vector3f;

public class Printing {
    public static void print(String input) {
        System.out.println(input);
    }

    public static void print(int input) {
        System.out.println(input);
    }

    public static void print(double input) {
        System.out.println(input);
    }

    public static void print(float input) {
        System.out.println(input);
    }

    public static void print(boolean input) {
        if (input) {
            print("true");
        }
        else {
            print("false");
        }
    }

    public static void printError(String input) {System.err.println("ERROR: " + input);}

    public static void print(Vector2i input) {
        System.out.println("X: " + input.x + ", Y: " + input.y);
    }

    public static void print(Vector3f input) {
        System.out.println("X: " + input.x + ", Y: " + input.y + ", Z: " + input.z);
    }
}
