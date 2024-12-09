package org.digitalcow;

import static org.lwjgl.opengl.GL46C.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.joml.Matrix4f;
import org.lwjgl.system.MemoryStack;

public class Shader {
    public int programHandle;

    public Shader(String vertexPath, String fragmentPath) {
        String vertexSource, fragmentSource;
        
        try {
            vertexSource = Files.readString(Path.of(vertexPath));
            fragmentSource = Files.readString(Path.of(fragmentPath));
        } catch (IOException exception) {
            vertexSource = "";
            fragmentSource = "";

            System.err.println("Failed to read shader file\n" + exception.getMessage());
        }

        int vertexShaderHandle, fragmentShaderHandle;

        vertexShaderHandle = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexShaderHandle, vertexSource);
        glCompileShader(vertexShaderHandle);
        logShaderCompileErrors(vertexShaderHandle);
        
        fragmentShaderHandle = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentShaderHandle, fragmentSource);
        glCompileShader(fragmentShaderHandle);
        logShaderCompileErrors(fragmentShaderHandle);

        programHandle = glCreateProgram();
        glAttachShader(programHandle, vertexShaderHandle);
        glAttachShader(programHandle, fragmentShaderHandle);

        glLinkProgram(programHandle);

        glDetachShader(programHandle, vertexShaderHandle);
        glDetachShader(programHandle, fragmentShaderHandle);

        glDeleteShader(vertexShaderHandle);
        glDeleteShader(fragmentShaderHandle);
    }

    public void bind() {
        glUseProgram(programHandle);
    }

    private void logShaderCompileErrors(int shaderHandle) {
        String message = glGetShaderInfoLog(shaderHandle);
        if (!message.isEmpty()) {
            System.err.println("Could not compile shader:\n" + message);
        }
    }

    public void setUniform(String name, Matrix4f value) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            glUniformMatrix4fv(glGetUniformLocation(programHandle, name), false, value.get(stack.mallocFloat(16)));
        }
    }
}
