package ca.benbingham.engine.graphics.shaders;

import static ca.benbingham.engine.util.Printing.printError;
import static org.lwjgl.opengl.GL20.*;

public class Shader {
    private String shaderSource;
    private final int shader;

    public Shader(String shaderSource, int shaderType) {
        shader = glCreateShader(shaderType);
        glShaderSource(shader, shaderSource);
        glCompileShader(shader);
        this.shaderSource = shaderSource;
    }

    public boolean checkShaderStatus() {
        if(glGetShaderi(shader, GL_COMPILE_STATUS) == GL_FALSE) {
            printError(glGetShaderInfoLog(shader, 1024));

            glDeleteShader(shader);
            return false;
        }
        return true;
    }

    public int getShader() {
        return shader;
    }

    public void delete() {
        glDeleteShader(shader);
    }

    public String getShaderSource() {
        return shaderSource;
    }
}
