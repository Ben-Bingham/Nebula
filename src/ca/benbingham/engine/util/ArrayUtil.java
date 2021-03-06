package ca.benbingham.engine.util;

import java.util.ArrayList;

public class ArrayUtil {
    public static float[] floatListToArray(ArrayList<Float> list) {
        float[] array = new float[list.size()];
        for (int i = 0; i < list.size(); i++) {
            array[i] = list.get(i);
        }

        return array;
    }

    public static int[] intListToArray(ArrayList<Integer> list) {
        int[] array = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            array[i] = list.get(i);
        }

        return array;
    }

    public static ArrayList<Float> floatArrayToList(float[] array) {
        ArrayList<Float> list = new ArrayList<>();
        for (int i = 0; i < array.length; i++) {
            list.add(array[i]);
        }

        return list;
    }

    public static ArrayList<Integer> intArrayToList(int[] array) {
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i < array.length; i++) {
            list.add(array[i]);
        }

        return list;
    }

    public static float[] combineTwoFloatArrays(float[] array1, float[] array2) {
        float[] temp = new float[array1.length + array2.length];
        for (int i = 0; i < array1.length; i++) {
            temp[i] = array1[i];
        }

        for (int i = array1.length; i < array2.length + array1.length; i++) {
            temp[i] = array2[i];
        }

        return temp;
    }
}
