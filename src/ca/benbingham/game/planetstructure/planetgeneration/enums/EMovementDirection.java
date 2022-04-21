package ca.benbingham.game.planetstructure.planetgeneration.enums;

public enum EMovementDirection {
    NEBULA_POSITIVE_X(0),
    NEBULA_POSITIVE_Y(1),
    NEBULA_NEGATIVE_X(2),
    NEBULA_NEGATIVE_Y(3);

    private final int value;

    EMovementDirection(int val) {
        this.value = val;
    }

    public int getValue() {
        return value;
    }
}
