package ca.benbingham.engine.graphics.shaders;

import ca.benbingham.engine.util.FileReader;

import static ca.benbingham.engine.util.Printing.printError;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;

public class ShaderProgramGenerator {
    private ShaderProgram shaderProgram;

    public ShaderProgramGenerator(String vertexShaderPath, String fragmentShaderPath, String shaderName) {
        // vertex shader
        String vertexSource = "";
        try {
            vertexSource = FileReader.readFile(vertexShaderPath);
        }
        catch (Exception e) {
            printError(shaderName + " vertex shader could not be read from file");
        }

        Shader vertexShader = new Shader(vertexSource, GL_VERTEX_SHADER);

        if (!vertexShader.checkShaderStatus()) {
            printError(shaderName + " vertex shader failed to compile");
        }

        // fragment shader
        String fragmentSource = "";
        try {
            fragmentSource = FileReader.readFile(fragmentShaderPath);
        }
        catch (Exception e) {
            printError(shaderName + " fragment shader could not be read from file");
        }

        Shader fragmentShader = new Shader(fragmentSource, GL_FRAGMENT_SHADER);

        if (!fragmentShader.checkShaderStatus()) {
            printError(shaderName + " fragment shader failed to compile");
        }

        // skybox shader program
        shaderProgram = new ShaderProgram();
        shaderProgram.attachShader(vertexShader);
        shaderProgram.attachShader(fragmentShader);
        shaderProgram.linkProgram();

        if (!shaderProgram.checkProgramLinkStatus()) {
            printError(shaderName + " shader program failed to link");
        }

        shaderProgram.detachShader(vertexShader);
        shaderProgram.detachShader(fragmentShader);

        vertexShader.delete();
        fragmentShader.delete();
    }

    public ShaderProgram getShaderProgram() {
        return shaderProgram;
    }
}
