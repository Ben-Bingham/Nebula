package ca.benbingham.engine.images;

import org.lwjgl.BufferUtils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static ca.benbingham.engine.images.EImageModes.*;
import static org.lwjgl.stb.STBImage.*;

public class Image {
    private static final String absolutePath = "assets/images/";
    private String path;
    private BufferedImage bufferedImage;
    private ByteBuffer byteBufferImage;
    private int height;
    private int width;
    private int channels;

    public Image(String path, EImageModes mode, Boolean flipImage) {
        this.path = absolutePath + path;

        if (mode == NEBULA_BYTE_BUFFER) {
            byteBufferImage = loadImageAsByteBuffer(flipImage);
        }

        if (mode == NEBULA_BUFFERED_IMAGE) {
            bufferedImage = loadImageAsBufferedImage(flipImage);
        }
    }

    private BufferedImage loadImageAsBufferedImage(boolean flipImage) {
        ByteBuffer byteBufferImage = loadImageAsByteBuffer(flipImage);

        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D textureGraphics = bufferedImage.createGraphics();
        int pixelNumber = 0;
        Colour colour;

        for (int i = 0; i < this.width; i++) {
            for (int j = 0; j < this.height; j++) {
                colour = new Colour(
                        (short) (byteBufferImage.get((pixelNumber * 4) + 0) & 0xFF),
                        (short) (byteBufferImage.get((pixelNumber * 4) + 1) & 0xFF),
                        (short) (byteBufferImage.get((pixelNumber * 4) + 2) & 0xFF),
                        (short) (byteBufferImage.get((pixelNumber * 4) + 3) & 0xFF)
                );

                textureGraphics.setColor(colour.toJavaColor());
                textureGraphics.drawLine(i, j, i, j);

                pixelNumber++;
            }
        }

        stbi_image_free(byteBufferImage);

        return bufferedImage;
    }

    private ByteBuffer loadImageAsByteBuffer(boolean flipImage) {
        stbi_set_flip_vertically_on_load(flipImage);

        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        IntBuffer channels = BufferUtils.createIntBuffer(1);
        ByteBuffer image = stbi_load(path, width, height, channels, 0);

        if (image != null) {
            this.width = width.get(0);
            this.height = height.get(0);
            this.channels = channels.get(0);

            return image;
        }
        else {
            throw new RuntimeException("Image: " + path + " failed to be imported as an image");
        }
    }

    public void delete() {
        stbi_image_free(byteBufferImage);
    }

    public BufferedImage getBufferedImage() {
        return bufferedImage;
    }

    public ByteBuffer getByteBufferImage() {
        return byteBufferImage;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public int getChannels() {
        return channels;
    }

    public String getPath() {
        return path;
    }

    @Override
    public String toString() {
        return "Image{" +
                "path='" + path + '\'' +
                '}';
    }
}
