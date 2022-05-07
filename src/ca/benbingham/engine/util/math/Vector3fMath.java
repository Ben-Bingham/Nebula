package ca.benbingham.engine.util.math;

import org.joml.Vector3f;

public class Vector3fMath {
    public static Vector3f add(Vector3f vector1, Vector3f vector2) {
        return new Vector3f(vector1.x + vector2.x, vector1.y + vector2.y, vector1.z + vector2.z);
    }

    public static Vector3f subtract(Vector3f vector1, Vector3f vector2) {
        return new Vector3f(vector1.x - vector2.x, vector1.y - vector2.y, vector1.z - vector2.z);
    }

    public static Vector3f multiply(Vector3f vector1, float scalar) {
        return new Vector3f(vector1.x * scalar, vector1.y * scalar, vector1.z * scalar);
    }

    public static Vector3f crossProduct(Vector3f vector1, Vector3f vector2) {
        Vector3f result = new Vector3f();
        result.x = (vector1.y * vector2.z) - (vector1.z * vector2.y);
        result.y = (vector1.z * vector2.x) - (vector1.x * vector2.z);
        result.z = (vector1.x * vector2.y) - (vector1.y * vector2.x);

        return result;
    }

    public static double distanceBetween(Vector3f vector1, Vector3f vector2) {
        double xComponent = Math.pow(vector2.x - vector1.x, 2);
        double yComponent = Math.pow(vector2.y - vector1.y, 2);
        double zComponent = Math.pow(vector2.z - vector1.z, 2);

        return Math.sqrt(xComponent + yComponent + zComponent);
    }
}
