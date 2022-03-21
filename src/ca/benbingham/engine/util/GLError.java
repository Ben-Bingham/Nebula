package ca.benbingham.engine.util;

import static ca.benbingham.engine.util.Printing.print;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.GL_INVALID_FRAMEBUFFER_OPERATION;

public class GLError {
    public static int getOpenGLError(Throwable throwable) {
        int errorCode;
        while ((errorCode = glGetError()) != GL_NO_ERROR) {
            String error = "";
            switch (errorCode) {
                case GL_INVALID_ENUM -> error = "INVALID_ENUM";
                case GL_INVALID_VALUE -> error = "INVALID_VALUE";
                case GL_INVALID_OPERATION -> error = "INVALID_OPERATION";
                case GL_STACK_OVERFLOW -> error = "STACK_OVERFLOW";
                case GL_STACK_UNDERFLOW -> error = "STACK_UNDERFLOW";
                case GL_OUT_OF_MEMORY -> error = "OUT_OF_MEMORY";
                case GL_INVALID_FRAMEBUFFER_OPERATION -> error = "INVALID_FRAMEBUFFER_OPERATION";
            }
            print(error + " " + throwable.getStackTrace()[0].getFileName() + " java:" + throwable.getStackTrace()[0].getLineNumber());
        }
        return errorCode;
    }
}
