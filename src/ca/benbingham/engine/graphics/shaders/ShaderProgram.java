package ca.benbingham.engine.graphics.shaders;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import static ca.benbingham.engine.util.Printing.printError;
import static org.lwjgl.opengl.GL20.*;

public class ShaderProgram {
    private int shaderProgram;
    private List<Shader> linkedShaders = new ArrayList<>();
    private FloatBuffer buffer = BufferUtils.createFloatBuffer(16);

    public ShaderProgram() {
        shaderProgram = glCreateProgram();
    }

    public void attachShader(ca.benbingham.engine.graphics.shaders.Shader shader) {
        glAttachShader(shaderProgram, shader.getShader());
        linkedShaders.add(shader);
    }

    public void detachShader(ca.benbingham.engine.graphics.shaders.Shader shader) {
        glDeleteShader(shader.getShader());
    }

    public void linkProgram() {
        glLinkProgram(shaderProgram);
    }

    public boolean checkProgramLinkStatus() {
        if(glGetProgrami(shaderProgram, GL_LINK_STATUS) == GL_FALSE) {
            printError("Shader program could not be linked");
            printError(glGetShaderInfoLog(shaderProgram, 1024));

            glDeleteProgram(shaderProgram);

            for (ca.benbingham.engine.graphics.shaders.Shader shader: linkedShaders) {
                shader.delete();
            }

            return false;
        }
        return true;
    }

    public void use() {
        glUseProgram(shaderProgram);
    }

    public int getShaderProgram() {
        return shaderProgram;
    }

    public void uploadUniform(String variableName, float value) {
        glUniform1f(glGetUniformLocation(shaderProgram, variableName), value);
    }

    public void uploadUniform(String variableName, Vector3f value) {
        glUniform3f(glGetUniformLocation(shaderProgram, variableName), value.x, value.y, value.z);
    }

    public void uploadUniform(String variableName, float x, float y, float z) {
        glUniform3f(glGetUniformLocation(shaderProgram, variableName), x, y, z);
    }

    public void uploadUniform(String variableName, Vector4f value) {
        glUniform4f(glGetUniformLocation(shaderProgram, variableName), value.x, value.y, value.z, value.w);
    }

    public void uploadUniform(String variableName, int value) {
        glUniform1i(glGetUniformLocation(shaderProgram, variableName), value);
    }

    public void uploadUniform(String variableName, boolean value) {
        if (value) {
            glUniform1i(glGetUniformLocation(shaderProgram, variableName), 1);
        }
        else {
            glUniform1i(glGetUniformLocation(shaderProgram, variableName), 0);
        }
    }

    public void uploadUniform(String variableName, Matrix4f value) {
//        FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
        value.get(buffer);
        glUniformMatrix4fv(glGetUniformLocation(shaderProgram, variableName), false, buffer);
    }

    public void delete() {
        glDeleteProgram(shaderProgram);
        buffer.clear();
    }
}
