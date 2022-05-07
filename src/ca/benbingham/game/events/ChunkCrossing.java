package ca.benbingham.game.events;

import ca.benbingham.engine.util.events.Event;
import org.joml.Vector2i;
import org.joml.Vector3f;

public class ChunkCrossing implements Event {
    public Vector3f newPlayerPosition;
    public Vector2i newPlayerChunkCords;

    public ChunkCrossing(Vector3f newPlayerPosition, Vector2i newPlayerChunkCords) {
        this.newPlayerPosition = newPlayerPosition;
        this.newPlayerChunkCords = newPlayerChunkCords;
    }
}
