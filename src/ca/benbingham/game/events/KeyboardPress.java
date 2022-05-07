package ca.benbingham.game.events;

import ca.benbingham.engine.util.events.Event;

public class KeyboardPress implements Event {
    public long window;
    public int key;
    public int scancode;
    public int action;
    public int mods;

    public KeyboardPress(long window, int key, int scancode, int action, int mods) {
        this.window = window;
        this.key = key;
        this.scancode = scancode;
        this.action = action;
        this.mods = mods;
    }
}
