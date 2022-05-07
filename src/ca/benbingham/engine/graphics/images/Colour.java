package ca.benbingham.engine.graphics.images;

import java.awt.Color;

public class Colour {
    public short R;
    public short G;
    public short B;
    public short A;

    public Colour(short R, short G, short B, short A) {
        this.R = R;
        this.G = G;
        this.B = B;
        this.A = A;
    }

    public Colour(Color color) {
        this.R = (short) color.getRed();
        this.G = (short) color.getGreen();
        this.B = (short) color.getBlue();
        this.A = (short) color.getAlpha();
    }

    public Colour(short R, short G, short B) {
        this.R = R;
        this.G = G;
        this.B = B;
        this.A = 255;
    }


    public Color toJavaColor() {
        return new Color(R, G, B, A);
    }
}
