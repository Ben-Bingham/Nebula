package ca.benbingham.engine.util;

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

    public static void printError(String input) {System.err.println("ERROR: " + input);}
}
