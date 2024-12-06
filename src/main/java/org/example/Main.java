package org.example;

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Main {
    // Window handle
    private long window;

    public void run(){
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");

        init();
        loop();

        // Free window callbacks and destroy the window
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private void init() {
        //Setup error callback. Default will print in System.err
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions don't work before this.
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        // Configure GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // Window stays hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // Window is resizeable

        // Create window
        window = glfwCreateWindow(1080, 720, "Hello World", NULL, NULL);
        if (window == NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        // Set up a key callback. IT will be called every time a key is pressed, or anything.
        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                glfwSetWindowShouldClose(window, true); // Detected in rendering loop
            }
        });

        // Thread pushing the window!
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1);
            IntBuffer pHeight = stack.mallocInt(1);

            // Get the window size
            glfwGetWindowSize(window, pWidth, pHeight);

            // Get resolution of primary monitor
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // Center the window
            glfwSetWindowPos(window,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        } // Stack frame is popped automatically

        // Make OpenGL context current
        glfwMakeContextCurrent(window);
        // V-Sync
        glfwSwapInterval(1);

        // Show window
        glfwShowWindow(window);
    }

    private void loop() {
        // This line is critical for interoperation with GLFW, or any context.
        GL.createCapabilities();

        // Clear color
        glClearColor(1.0f, 0.0f, 0.0f, 0.0f);

        // Run rendering loop until user tries to close window
        while (!glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear framebuffer

            glfwSwapBuffers(window); // Swap color buffers

            // Poll for window events. Key callback above will only be called here
            glfwPollEvents();
        }
    }

    public static void main(String[] args) {
        new Main().run();
    }
}