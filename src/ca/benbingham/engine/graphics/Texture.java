package ca.benbingham.engine.graphics;

import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static ca.benbingham.engine.util.GLError.getOpenGLError;
import static ca.benbingham.engine.util.Printing.printError;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;
import static org.lwjgl.stb.STBImage.*;

public class Texture {
    private String filePath;
    private final int texture;

    public Texture() {
        texture = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texture);
    }

    public void bindImageData(String filepath, boolean flipImage) {
        stbi_set_flip_vertically_on_load(flipImage);

        this.filePath = filepath;

        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        IntBuffer channels = BufferUtils.createIntBuffer(1);
        ByteBuffer image = stbi_load(filepath, width, height, channels, 0);

        if (image != null) {
            if (channels.get(0) == 3) {
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width.get(0), height.get(0), 0, GL_RGB, GL_UNSIGNED_BYTE, image);
            }
            else if(channels.get(0) == 4) {
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width.get(0), height.get(0), 0, GL_RGBA, GL_UNSIGNED_BYTE, image);
            }
            else {
                printError("Unknown number of channels in image (Texture)");
            }

            stbi_image_free(image);
        }
        else {
            printError("Could not load image for texture " + filepath);
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
