package ca.benbingham.game.planetstructure;

import ca.benbingham.engine.graphics.images.Image;
import ca.benbingham.game.planetstructure.geometry.Quad;

public class BlockFace {
    private Image image;
    private Quad quad;
    private float[] texCords;

    public BlockFace() {
        quad = new Quad();
        texCords = new float[8];
    }

    public Image getImage() {
        return image;
    }

    public Quad getQuad() {
        return quad;
    }

    public float[] getTexCords() {
        return texCords;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public void setTexCords(float[] texCords) {
        this.texCords = texCords;
    }

    public void setTexCords(int i, float value) {
        texCords[i] = value;
    }

    public void setQuad(Quad quad) {
        this.quad = quad;
    }
}
