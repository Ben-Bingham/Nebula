package ca.benbingham.engine.io;

import ca.benbingham.game.events.KeyboardPress;
import ca.benbingham.game.events.MousePosition;
import ca.benbingham.game.events.ScrollWheel;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import static ca.benbingham.engine.util.math.Vector3fMath.*;
import static java.lang.Math.*;
import static org.lwjgl.glfw.GLFW.*;

public class Camera {
    private float FOV;
    private final float maxFOV;
    private final float mouseSensitivity;
    private float baseMovementSpeed;
    private float movementSpeed;

    private final Window window;

    private float mouseX;
    private float mouseY;
    private float lastMouseX;
    private float lastMouseY;
    private float scrollOffsetY;

    private boolean wKey;
    private boolean aKey;
    private boolean sKey;
    private boolean dKey;
    private boolean spaceKey;
    private boolean shiftKey;

    private float yaw;
    private float pitch;
    private float zFar;

    private float yawOffset;
    private float pitchOffset;

    private Vector3f right;
    private Vector3f up;

    private Vector3f front;
    private Vector3f worldUp;

    private Vector3f position;

    private Matrix4f viewMatrix;
    private Matrix4f projectionMatrix;

    private float lastFrame;
    private float deltaTime;

    public Camera(Window window, float maxFOV, float mouseSensitivity, float movementSpeed, Vector3f spawnLocation, float zFar) {
        this.maxFOV = maxFOV;
        this.FOV = maxFOV;
        this.window = window;
        this.mouseSensitivity = mouseSensitivity;
        this.baseMovementSpeed = movementSpeed;
        this.zFar = zFar;

        this.lastMouseX = window.getWidth() / 2f;
        this.lastMouseY = window.getHeight() / 2f;

        yaw = -90f;

        front = new Vector3f(0.0f, 0.0f, -1.0f);
        worldUp = new Vector3f(0.0f, 1.0f, 0.0f);
        position = spawnLocation;

        projectionMatrix = new Matrix4f()
                .perspective((float) toRadians(this.getFOV()), (float) window.getWidth() / window.getHeight(), 0.1f, zFar);

        update();
    }

    public void mousePositionInput(MousePosition event) {
        this.mouseX = (float) event.xPos;
        this.mouseY = (float) event.yPos;

        processMouseMovement();
    }

    public void scrollWheelInput(ScrollWheel event) {
        scrollOffsetY = (float) event.yOffset;

        processMouseScroll();
    }

    private void processMouseMovement() {
        // Calculate the offsets
        yawOffset = mouseX - lastMouseX;
        pitchOffset = lastMouseY - mouseY;
        lastMouseX = mouseX;
        lastMouseY = mouseY;

        // Adjust the offsets to allow for a sensitivity
        yawOffset *= mouseSensitivity;
        pitchOffset *= mouseSensitivity;

        // Offset yaw and pitch
        yaw += yawOffset;
        pitch += pitchOffset;

        // Limit the pitch
        if (pitch > 89.9f)
            pitch = 89.9f;
        if (pitch < -89.9f)
            pitch = -89.9f;

        updateCameraVectors();
    }

    private void processMouseScroll() {
        baseMovementSpeed -= scrollOffsetY * -1;
        if (baseMovementSpeed < 0.01f)
            baseMovementSpeed = 0.01f;
        if (baseMovementSpeed > 10000)
            baseMovementSpeed = 10000;
    }

    private void processMovement() {
        if (wKey) {
            position = add(position, multiply(front, movementSpeed));
        }
        if (sKey) {
            position = subtract(position, multiply(front, movementSpeed));
        }
        if (aKey) {
            position = subtract(position, multiply(crossProduct(front, up).normalize(), movementSpeed));
        }
        if (dKey) {
            position = add(position, multiply(crossProduct(front, up).normalize(), movementSpeed));
        }
        if (spaceKey) {
            position = add(position, multiply(worldUp, movementSpeed));
        }
        if (shiftKey) {
            position = subtract(position, multiply(worldUp, movementSpeed));
        }
    }

    public void update() {
        processMovement();

        updateCameraVectors();
        updateViewMatrix();

        float currentFrame = (float) glfwGetTime();
        deltaTime = currentFrame - lastFrame;
        lastFrame = currentFrame;

        movementSpeed = baseMovementSpeed * deltaTime;
    }

    private void updateViewMatrix() {
        viewMatrix = new Matrix4f()
                .lookAt(position, add(position, front), up);
    }

    private void updateCameraVectors() {
        // Calculate the new front vector
        front.x = (float) (cos(toRadians(yaw)) * cos(toRadians(pitch)));
        front.y = (float) sin(toRadians(pitch));
        front.z = (float) (sin(toRadians(yaw)) * cos(toRadians(pitch)));

        // Use cross product to calculate the right vector and up vector
        right = crossProduct(front, worldUp).normalize();
        up = crossProduct(right, front).normalize();
    }

    public void resizeWindow() {
        projectionMatrix = new Matrix4f()
                .perspective((float) toRadians(this.getFOV()), (float) window.getWidth() / window.getHeight(), 0.1f, zFar);
    }

    public void keyboardPress(KeyboardPress event) {
        if (event.key == GLFW_KEY_W) {
            if (event.action == GLFW_PRESS) {
                wKey = true;
            }
            else if (event.action == GLFW_RELEASE) {
                wKey = false;
            }
        }
        if (event.key == GLFW_KEY_S) {
            if (event.action == GLFW_PRESS) {
                sKey = true;
            }
            else if (event.action == GLFW_RELEASE) {
                sKey = false;
            }
        }
        if (event.key == GLFW_KEY_A) {
            if (event.action == GLFW_PRESS) {
                aKey = true;
            }
            else if (event.action == GLFW_RELEASE) {
                aKey = false;
            }
        }
        if (event.key == GLFW_KEY_D) {
            if (event.action == GLFW_PRESS) {
                dKey = true;
            }
            else if (event.action == GLFW_RELEASE) {
                dKey = false;
            }
        }
        if (event.key == GLFW_KEY_SPACE) {
            if (event.action == GLFW_PRESS) {
                spaceKey = true;
            }
            else if (event.action == GLFW_RELEASE) {
                spaceKey = false;
            }
        }
        if (event.key == GLFW_KEY_LEFT_SHIFT) {
            if (event.action == GLFW_PRESS) {
                shiftKey = true;
            }
            else if (event.action == GLFW_RELEASE) {
                shiftKey = false;
            }
        }
    }

    public Matrix4f getViewMatrix() {
        return viewMatrix;
    }

    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }

    public float getBaseMovementSpeed() {
        return baseMovementSpeed;
    }

    public float getFOV() {
        return FOV;
    }

    public Vector3f getPosition() {
        return position;
    }

    public Vector3f getFront() {
        return front;
    }

    public void setMovementSpeed(float movementSpeed) {
        this.movementSpeed = movementSpeed;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }
}
