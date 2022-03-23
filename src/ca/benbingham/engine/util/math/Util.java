package ca.benbingham.engine.util.math;

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
}
