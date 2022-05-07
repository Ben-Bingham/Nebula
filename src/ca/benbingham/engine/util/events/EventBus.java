package ca.benbingham.engine.util.events;

import java.util.*;

public class EventBus {
    private final List<EventListener> listeners = new ArrayList<>();

    public <T extends Event> void emit(T event) {
        for (EventListener listener : listeners) {
            listener.receiveEvents(event);
        }
    }

    public void addListener(EventListener listener) {
        listeners.add(listener);
    }
}
