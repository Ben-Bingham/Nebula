package ca.benbingham.game.planetstructure.enums;

public enum EBlockFaces {
    NEBULA_FRONT_FACE(0),
    NEBULA_BACK_FACE(1),
    NEBULA_TOP_FACE(2),
    NEBULA_BOTTOM_FACE(3),
    NEBULA_RIGHT_FACE(4),
    NEBULA_LEFT_FACE(5),
    NEBULA_ALL_FACES(6),
    NEBULA_TOP_BOTTOM_FACES(7),
    NEBULA_FRONT_BACK_LEFT_RIGHT(8);

    private final int value;

    EBlockFaces(int val) {
        this.value = val;
    }

    public int getValue() {
        return value;
    }
}
