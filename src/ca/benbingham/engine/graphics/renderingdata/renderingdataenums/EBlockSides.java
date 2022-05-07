package ca.benbingham.engine.graphics.renderingdata.renderingdataenums;

public enum EBlockSides {
    POSITIVE_X(0),
    NEGATIVE_X(1),
    POSITIVE_Y(2),
    NEGATIVE_Y(3),
    POSITIVE_Z(4),
    NEGATIVE_Z(5);

    private final int value;

    EBlockSides(int val) {
        this.value = val;
    }

    public int getValue() {
        return value;
    }
}
