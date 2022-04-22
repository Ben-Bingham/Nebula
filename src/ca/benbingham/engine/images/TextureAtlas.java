package ca.benbingham.engine.images;

import org.joml.Vector2f;
import org.joml.Vector2i;
import org.lwjgl.opengl.ATIMeminfo;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import static ca.benbingham.engine.util.Printing.print;

public class TextureAtlas {
    private final Image[] images;
    private final int pixelsPerSideOfAtlasX;
    private final int pixelsPerSideOfAtlasY;
    private final int texturesPerSideOfAtlas;
    private float[][] texCords;

    public TextureAtlas(Image[] images, String textureAtlasName) {
        this.images = images;

        texturesPerSideOfAtlas = (short) Math.ceil(Math.sqrt(images.length));

        pixelsPerSideOfAtlasX = (short) (texturesPerSideOfAtlas * images[0].getWidth());
        pixelsPerSideOfAtlasY = (short) (texturesPerSideOfAtlas * images[0].getHeight());

        BufferedImage textureAtlas = new BufferedImage(pixelsPerSideOfAtlasX, pixelsPerSideOfAtlasY, BufferedImage.TYPE_INT_ARGB);
        Graphics2D atlasGraphics = textureAtlas.createGraphics();

        BufferedImage texture;

        int xOffset, yOffset;

        for (int i = 0; i < images.length; i++) {
            texture = images[i].getBufferedImage();

            texture = rotateImage(texture, -90); // TODO this might modify the original image as well (the one stored in the image class)


            if (i < texturesPerSideOfAtlas) {
                xOffset = i * images[i].getWidth();
                yOffset = 0;
            }
            else {
                int textureNumber = i;
                int rows = 0;
                while (textureNumber + 1 > texturesPerSideOfAtlas) {
                    textureNumber -= texturesPerSideOfAtlas;
                    rows++;
                }

                xOffset = textureNumber * images[i].getWidth();
                yOffset = rows * images[i].getHeight();
            }

            atlasGraphics.drawImage(texture, xOffset, yOffset, null);
        }

        try {
            ImageIO.write(textureAtlas, "png", new File("assets/images/" + textureAtlasName + ".png"));
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        createTexCords();
    }

    private BufferedImage rotateImage(BufferedImage buffImage, double angle) {
        double radian = Math.toRadians(angle);
        double sin = Math.abs(Math.sin(radian));
        double cos = Math.abs(Math.cos(radian));

        int width = buffImage.getWidth();
        int height = buffImage.getHeight();

        int nWidth = (int) Math.floor((double) width * cos + (double) height * sin);
        int nHeight = (int) Math.floor((double) height * cos + (double) width * sin);

        BufferedImage rotatedImage = new BufferedImage(
                nWidth, nHeight, BufferedImage.TYPE_INT_ARGB);

        Graphics2D graphics = rotatedImage.createGraphics();

        graphics.setRenderingHint(
                RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BICUBIC);

        graphics.translate((nWidth - width) / 2, (nHeight - height) / 2);
        // rotation around the center point
        graphics.rotate(radian, (double) (width / 2), (double) (height / 2));
        graphics.drawImage(buffImage, 0, 0, null);
        graphics.dispose();

        return rotatedImage;
    }

    private void createTexCords() {
        Vector2f[][] cornerCords = new Vector2f[texturesPerSideOfAtlas + 1][texturesPerSideOfAtlas + 1];
        float boxValueX = (float) 1 / texturesPerSideOfAtlas;
        float boxValueY = (float) 1 / texturesPerSideOfAtlas;

        for (int i = 0; i < texturesPerSideOfAtlas + 1; i++) {
            for (int j = 0; j < texturesPerSideOfAtlas + 1; j++) {
                cornerCords[i][j] = new Vector2f(i, j);

                cornerCords[i][j].x *= boxValueX;
                cornerCords[i][j].y *= boxValueY;
            }
        }

        texCords = new float[texturesPerSideOfAtlas * texturesPerSideOfAtlas][4];
        float[][][] allTexCords = new float[texturesPerSideOfAtlas][texturesPerSideOfAtlas][4];

        for (int i = 0; i < texturesPerSideOfAtlas; i++) {
            for (int j = 0; j < texturesPerSideOfAtlas; j++) {
                allTexCords[i][j] = new float[]{
                        cornerCords[0 + i][1 + j].x, cornerCords[0 + i][1 + j].y,
                        cornerCords[1 + i][1 + j].x, cornerCords[1 + i][1 + j].y,
                        cornerCords[1 + i][0 + j].x, cornerCords[1 + i][0 + j].y,
                        cornerCords[0 + i][0 + j].x, cornerCords[0 + i][0 + j].y
                };
            }
        }

        int count = 0;
        for (int j = texturesPerSideOfAtlas - 1; j > 0; j--) {
            for (int i = 0; i < texturesPerSideOfAtlas; i++) {
                texCords[count] = allTexCords[i][j];

                count++;
            }
        }
    }

    public Image[] getImages() {
        return images;
    }

    public float[][] getTexCords() {
        return texCords;
    }
}
