package ca.benbingham.engine.graphics;

import ca.benbingham.engine.graphics.images.Image;

import java.nio.ByteBuffer;

import static ca.benbingham.engine.util.Printing.printError;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;
import static org.lwjgl.stb.STBImage.*;

public class Texture {
    private Image image;
    private final int texture;

    public Texture() {
        texture = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texture);
    }

    public void bindImageData(Image image) {
        this.image = image;

        ByteBuffer byteBufferImage = image.getByteBufferImage();

        if (byteBufferImage != null) {
            if (image.getChannels() == 3) {
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, image.getWidth(), image.getHeight(), 0, GL_RGB, GL_UNSIGNED_BYTE, byteBufferImage);
            }
            else if(image.getChannels() == 4) {
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, image.getWidth(), image.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, byteBufferImage);
            }
            else {
                printError("Unknown number of channels in image (Texture)");
            }

            stbi_image_free(byteBufferImage);
        }
        else {
            printError("Could not load image for texture " + image.getPath());
        }
    }

    public void setWrapSettings(int wrapMode) {
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, wrapMode);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, wrapMode);
    }

    public void setStretchMode(int stretchMode) {
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, stretchMode);
    }

    public void setShrinkMode(int shrinkMode) {
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, shrinkMode);
    }

    public void generateMipmaps() {
        glGenerateMipmap(GL_TEXTURE_2D);
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, texture);
    }

    public void unbind() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public void delete() { glDeleteTextures(texture);}

    public int getTexture() {
        return texture;
    }
}
