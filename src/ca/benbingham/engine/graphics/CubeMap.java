package ca.benbingham.engine.graphics;

import ca.benbingham.engine.graphics.images.Image;

import java.nio.ByteBuffer;

import static ca.benbingham.engine.util.Printing.printError;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_TEXTURE_WRAP_R;
import static org.lwjgl.opengl.GL13.GL_TEXTURE_CUBE_MAP;
import static org.lwjgl.opengl.GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;
import static org.lwjgl.stb.STBImage.*;

public class CubeMap{
    private Image[] images;
    private final int cubeMap;

    public CubeMap() {
        cubeMap = glGenTextures();
        bind();
    }

    public void bindImageData(Image[] images) {
        for (int i = 0; i < images.length; i++) {
            this.images = images;

            ByteBuffer byteBufferImage = images[i].getByteBufferImage();

            if (byteBufferImage != null) {
                if (images[i].getChannels() == 3) {
                    glTexImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL_RGB, images[i].getWidth(), images[i].getHeight(), 0, GL_RGB, GL_UNSIGNED_BYTE, byteBufferImage);
                }
                else if(images[i].getChannels() == 4) {
                    glTexImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL_RGBA, images[i].getWidth(), images[i].getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, byteBufferImage);
                }
                else {
                    printError("Unknown number of channels in image (Cube map)");
                }

                stbi_image_free(byteBufferImage);
            }
            else {
                printError("Could not load image for texture " + images[i].getPath());
            }
        }
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_CUBE_MAP, cubeMap);
    }

    public void unbind() {
        glBindTexture(GL_TEXTURE_CUBE_MAP, cubeMap);
    }

    public void setStretchMode(int stretchMode) {
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MIN_FILTER, stretchMode);
    }

    public void setShrinkMode(int shrinkMode) {
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MAG_FILTER, shrinkMode);
    }

    public void setWrapSettings(int wrapMode) {
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, wrapMode);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, wrapMode);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_R, wrapMode);
    }

    public void generateMipmaps() {
        glGenerateMipmap(GL_TEXTURE_CUBE_MAP);
    }

    public int getCubeMap() {
        return cubeMap;
    }

    public void delete() { glDeleteTextures(cubeMap);}

}
