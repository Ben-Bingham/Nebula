package ca.benbingham.game.gameclasses;

import ca.benbingham.engine.collisions.HitBox;
import ca.benbingham.engine.io.Camera;
import ca.benbingham.game.events.ChunkCrossing;
import ca.benbingham.engine.util.events.Event;
import ca.benbingham.engine.util.events.EventListener;
import ca.benbingham.game.events.KeyboardPress;
import ca.benbingham.game.events.MousePosition;
import ca.benbingham.game.events.ScrollWheel;
import ca.benbingham.game.events.states.FirstUpdate;
import ca.benbingham.game.planetstructure.Chunk;
import org.joml.Vector2i;
import org.joml.Vector3f;

public class Player implements EventListener {
    private Camera camera;
    private HitBox hitBox; //TODO
    private Vector3f position;
    private Vector2i chunkCords;
    private Vector2i lastChunkCords;
    private Game game;

    public Player(Game game) {
        this.game = game;

        camera = new Camera(game.getWindow(),
                Settings.FIELD_OF_VIEW,
                Settings.MOUSE_SENSITIVITY,
                Settings.MOVEMENT_SPEED,
                new Vector3f(2000, 800, 2000),
                Settings.PLANET_RENDER_DISTANCE + (0.02f * Settings.CHUNK_RENDER_DISTANCE)
        );

        position = new Vector3f(0);
        chunkCords = new Vector2i(0);
        lastChunkCords = new Vector2i(0);
    }

    public void update() {
        camera.update();
        position = camera.getPosition();

        chunkCords.x = (int) Math.floor(position.x / Chunk.xSize);
        chunkCords.y = (int) Math.floor(position.z / Chunk.zSize);

        if (chunkCords.x != lastChunkCords.x || chunkCords.y != lastChunkCords.y) { // Player moves between chunks.
            game.eventBus.emit(new ChunkCrossing(position, chunkCords));
            //TODO create a matrix that offsets the movement of the player that can be applied to all other objects in the universe (planets).
        }

        lastChunkCords.x = chunkCords.x;
        lastChunkCords.y = chunkCords.y;
    }

    @Override
    public void receiveEvents(Event event) {
        if (event instanceof FirstUpdate) {
            update();
        }
        else if (event instanceof KeyboardPress) {
            camera.keyboardPress((KeyboardPress) event);
        }
        else if (event instanceof ScrollWheel) {
            camera.scrollWheelInput((ScrollWheel) event);
        }
        else if (event instanceof MousePosition) {
            camera.mousePositionInput((MousePosition) event);
        }
    }

    public Vector3f getPosition() {
        return position;
    }

    public Camera getCamera() {
        return camera;
    }

    public Vector2i getChunkCords() {
        return chunkCords;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
        camera.setPosition(position);
    }
}
