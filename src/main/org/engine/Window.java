package main.org.engine;

import static org.lwjgl.glfw.GLFW.*;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

import static org.lwjgl.glfw.GLFW.*;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {

	private final String title;

	private int width;

	private int height;

	private long windowHandle;

	private GLFWErrorCallback errorCallback;

	private GLFWKeyCallback keyCallback;

	private GLFWWindowSizeCallback windowSizeCallback;

	private boolean resized;

	private boolean vSync;

	public Window(String title, int width, int height, boolean vSync) {
		this.title = title;
		this.width = width;
		this.height = height;
		this.vSync = vSync;
		this.resized = false;
	}

	public void init() {
		// Setup an error callback. The default implementation
		// will print the error message in System.err.
		glfwSetErrorCallback(this.errorCallback = GLFWErrorCallback.createPrint(System.err));

		// Initialize GLFW. Most GLFW functions will not work before doing this.
		if (!glfwInit()) {
			throw new IllegalStateException("Unable to initialize GLFW");
		}

		glfwDefaultWindowHints(); // optional, the current window hints are
									// already the default
		glfwWindowHint(GLFW_VISIBLE, GL_FALSE); // the window will stay hidden
												// after creation
		glfwWindowHint(GLFW_RESIZABLE, GL_TRUE); // the window will be resizable
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);

		// Create the window
		this.windowHandle = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
		if (this.windowHandle == NULL) {
			throw new RuntimeException("Failed to create the GLFW window");
		}

		// Setup resize callback
		glfwSetWindowSizeCallback(this.windowHandle, this.windowSizeCallback = new GLFWWindowSizeCallback() {
			@Override
			public void invoke(long window, int width, int height) {
				Window.this.width = width;
				Window.this.height = height;
				Window.this.setResized(true);
			}
		});

		// Setup a key callback. It will be called every time a key is pressed,
		// repeated or released.
		glfwSetKeyCallback(this.windowHandle, this.keyCallback = new GLFWKeyCallback() {
			@Override
			public void invoke(long window, int key, int scancode, int action, int mods) {
				if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
					glfwSetWindowShouldClose(window, true);
				}
			}
		});

		// Get the resolution of the primary monitor
		GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		// Center our window
		glfwSetWindowPos(this.windowHandle, (vidmode.width() - this.width) / 2, (vidmode.height() - this.height) / 2);

		// Make the OpenGL context current
		glfwMakeContextCurrent(this.windowHandle);

		if (isvSync()) {
			// Enable v-sync
			glfwSwapInterval(1);
		}

		// Make the window visible
		glfwShowWindow(this.windowHandle);

		GL.createCapabilities();

		// Set the clear color
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		glEnable(GL_DEPTH_TEST);
		// glPolygonMode( GL_FRONT_AND_BACK, GL_LINE );

		// Support for transparencies
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);
	}

	public long getWindowHandle() {
		return this.windowHandle;
	}

	public void setClearColor(float r, float g, float b, float alpha) {
		glClearColor(r, g, b, alpha);
	}

	public boolean isKeyPressed(int keyCode) {
		return glfwGetKey(this.windowHandle, keyCode) == GLFW_PRESS;
	}

	public boolean windowShouldClose() {
		return glfwWindowShouldClose(this.windowHandle);
	}

	public String getTitle() {
		return this.title;
	}

	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}

	public boolean isResized() {
		return this.resized;
	}

	public void setResized(boolean resized) {
		this.resized = resized;
	}

	public boolean isvSync() {
		return this.vSync;
	}

	public void setvSync(boolean vSync) {
		this.vSync = vSync;
	}

	public void update() {
		glfwSwapBuffers(this.windowHandle);
		glfwPollEvents();
	}
}