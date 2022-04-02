package ca.benbingham.engine.graphics;

import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static ca.benbingham.engine.util.Printing.printError;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_TEXTURE_WRAP_R;
import static org.lwjgl.opengl.GL13.GL_TEXTURE_CUBE_MAP;
import static org.lwjgl.opengl.GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;
import static org.lwjgl.stb.STBImage.*;

public class CubeMap{
    private String[] filePaths;
    private final int cubemap;

    public CubeMap() {
        cubemap = glGenTextures();
        bind();
    }

    public void bindImageData(String[] filePaths, boolean flipImages) {
        for (int i = 0; i < filePaths.length; i++) {
            String filepath = filePaths[i];

            this.filePaths = filePaths;

            stbi_set_flip_vertically_on_load(flipImages);

            IntBuffer width = BufferUtils.createIntBuffer(1);
            IntBuffer height = BufferUtils.createIntBuffer(1);
            IntBuffer channels = BufferUtils.createIntBuffer(1);
            ByteBuffer image = stbi_load(filePaths[i], width, height, channels, 0);

            if (image != null) {
                if (channels.get(0) == 3) {
                    glTexImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL_RGB, width.get(0), height.get(0), 0, GL_RGB, GL_UNSIGNED_BYTE, image);
                }
                else if(channels.get(0) == 4) {
                    glTexImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL_RGBA, width.get(0), height.get(0), 0, GL_RGBA, GL_UNSIGNED_BYTE, image);
                }
                else {
                    printError("Unknown number of channels in image (Cube map)");
                }

                stbi_image_free(image);
            }
            else {
                printError("Could not load image for texture " + filepath);
            }
        }
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_CUBE_MAP, cubemap);
    }

    public void unbind() {
        glBindTexture(GL_TEXTURE_CUBE_MAP, cubemap);
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

    public int getCubemap() {
        return cubemap;
    }

    public void delete() { glDeleteTextures(cubemap);}

}
