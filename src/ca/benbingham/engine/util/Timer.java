package ca.benbingham.engine.util;

import java.util.ArrayList;

import static ca.benbingham.engine.util.Printing.print;
import static org.lwjgl.glfw.GLFW.glfwGetTime;

public class Timer {
    private double startTime;
    private double endTime;
    private ArrayList<Double> times = new ArrayList<>();

    public void startInterval() {
        startTime = glfwGetTime();
    }

    public void endInterval() {
        endTime = (float) glfwGetTime();
        times.add((endTime - startTime) * 1000);
    }

    public void printAverageTimes() {
        float totalTimes = 0;
        for (Double time : times) {
            totalTimes += time;
        }
        print("It took " + (totalTimes / times.size()) + " milliseconds on average.");
    }

    public void printAverageTimes(String reason) {
        float totalTimes = 0;
        for (Double time : times) {
            totalTimes += time;
        }
        print("It took " + (totalTimes / times.size()) + " milliseconds on average to " + reason);
    }

    public void resetTimes() {
        times = new ArrayList<>();
    }
}
