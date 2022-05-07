package ca.benbingham.game.events;

import ca.benbingham.engine.util.events.Event;

public class ScrollWheel implements Event {
    public long window;
    public double xOffset;
    public double yOffset;

    public ScrollWheel(long window, double xOffset, double yOffset) {
        this.window = window;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }
}
