package ca.benbingham.engine.io;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import static ca.benbingham.engine.util.Vector3fMath.*;
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

    private float yawOffset;
    private float pitchOffset;

    private Vector3f right;
    private Vector3f up;

    private Vector3f front;
    private Vector3f worldUp;

    private Vector3f position;

    private Matrix4f viewMatrix;

    private float lastFrame;
    private float deltaTime;

    public Camera(Window window, float maxFOV, float mouseSensitivity, float movementSpeed) {
        this.maxFOV = maxFOV;
        this.FOV = maxFOV;
        this.window = window;
        this.mouseSensitivity = mouseSensitivity;
        this.baseMovementSpeed = movementSpeed;

        this.lastMouseX = window.getWidth() / 2f;
        this.lastMouseY = window.getHeight() /2f;

        yaw = -90f;

        front = new Vector3f(0.0f, 0.0f, -1.0f);
        worldUp = new Vector3f(0.0f, 1.0f, 0.0f);
        position = new Vector3f(0.0f, 0.0f, 3.0f);
    }
    public Camera(Window window, float maxFOV, float mouseSensitivity, float movementSpeed, Vector3f spawnLocation) {
        this.maxFOV = maxFOV;
        this.FOV = maxFOV;
        this.window = window;
        this.mouseSensitivity = mouseSensitivity;
        this.baseMovementSpeed = movementSpeed;

        this.lastMouseX = window.getWidth() / 2f;
        this.lastMouseY = window.getHeight() /2f;

        yaw = -90f;

        front = new Vector3f(0.0f, 0.0f, -1.0f);
        worldUp = new Vector3f(0.0f, 1.0f, 0.0f);
        position = spawnLocation;
    }

    private void receiveMousePositionInput() {
        glfwSetCursorPosCallback(window.getWindow(), (window, xPos, yPos) -> {
            this.mouseX = (float) xPos;
            this.mouseY = (float) yPos;

            processMouseMovement();
        });
    }
    private void receiveScrollWheelInput() {
        glfwSetScrollCallback(window.getWindow(), (window, xOffset, yOffset) -> {
            scrollOffsetY = (float) yOffset;

            processMouseScroll();
        });
    }

    private void receiveMovementInput() {
        if (glfwGetKey(window.getWindow(), GLFW_KEY_W) == GLFW_PRESS) {
            wKey = true;
        }
        if (glfwGetKey(window.getWindow(), GLFW_KEY_S) == GLFW_PRESS) {
            sKey = true;
        }
        if (glfwGetKey(window.getWindow(), GLFW_KEY_A) == GLFW_PRESS) {
            aKey = true;
        }
        if (glfwGetKey(window.getWindow(), GLFW_KEY_D) == GLFW_PRESS) {
            dKey = true;
        }
        if (glfwGetKey(window.getWindow(), GLFW_KEY_SPACE) == GLFW_PRESS) {
            spaceKey = true;
        }
        if (glfwGetKey(window.getWindow(), GLFW_KEY_LEFT_SHIFT) == GLFW_PRESS) {
            shiftKey = true;
        }

        processMovement();
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
        if (baseMovementSpeed > 100)
            baseMovementSpeed = 100;
    }

    private void processMovement() {
        if (wKey) {
            position = add(position, multiply(front, movementSpeed));
            wKey = false;
        }
        if (sKey) {
            position = subtract(position, multiply(front, movementSpeed));
            sKey = false;
        }
        if (aKey) {
            position = subtract(position, multiply(crossProduct(front, up).normalize(), movementSpeed));
            aKey = false;
        }
        if (dKey) {
            position = add(position, multiply(crossProduct(front, up).normalize(), movementSpeed));
            dKey = false;
        }
        if (spaceKey) {
            position = add(position, multiply(worldUp, movementSpeed));
            spaceKey = false;
        }
        if (shiftKey) {
            position = subtract(position, multiply(worldUp, movementSpeed));
            shiftKey = false;
        }
    }

    public void update() {
        receiveMousePositionInput();
        receiveScrollWheelInput();
        receiveMovementInput();

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

    public Matrix4f getViewMatrix() {
        return viewMatrix;
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
}
