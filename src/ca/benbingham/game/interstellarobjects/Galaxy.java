package ca.benbingham.game.interstellarobjects;

import org.joml.Vector3f;

public class Galaxy {
    private String name;
    private Vector3f localPosition;
    private Vector3f globalPosition;
    private SolarSystem[] solarSystems; //TODO make this an adjustable size array list

    public Galaxy(String name, Vector3f localPosition, SolarSystem[] solarSystems) {
        this.name = name;
        this.localPosition = localPosition;
        this.solarSystems = solarSystems;

        generateGlobalPosition();
    }

    public Vector3f getGlobalPosition() {
        return globalPosition;
    }

    public void generateGlobalPosition() {
        this.globalPosition = localPosition;
    }

    public SolarSystem[] getSolarSystems() {
        return solarSystems;
    }
}
