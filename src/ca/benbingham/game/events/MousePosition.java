package ca.benbingham.game.events;

import ca.benbingham.engine.util.events.Event;

public class MousePosition implements Event {
    public long window;
    public double xPos;
    public double yPos;

    public MousePosition(long window, double xPos, double yPos) {
        this.window = window;
        this.xPos = xPos;
        this.yPos = yPos;
    }
}
