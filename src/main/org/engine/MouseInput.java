package main.org.engine;

import org.joml.Vector2d;
import org.joml.Vector2f;
import static org.lwjgl.glfw.GLFW.*;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWCursorEnterCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

public class MouseInput {

	private final Vector2d previousPos;

	private final Vector2d currentPos;

	private final Vector2f displVec;

	private boolean inWindow = false;

	private boolean leftButtonPressed = false;

	private boolean rightButtonPressed = false;

	private GLFWCursorPosCallback cursorPosCallback;

	private GLFWCursorEnterCallback cursorEnterCallback;

	private GLFWMouseButtonCallback mouseButtonCallback;

	public MouseInput() {
		this.previousPos = new Vector2d(-1, -1);
		this.currentPos = new Vector2d(0, 0);
		this.displVec = new Vector2f();
	}

	public void init(Window window) {
		glfwSetCursorPosCallback(window.getWindowHandle(), this.cursorPosCallback = new GLFWCursorPosCallback() {
			@Override
			public void invoke(long window, double xpos, double ypos) {
				MouseInput.this.currentPos.x = xpos;
				MouseInput.this.currentPos.y = ypos;
			}
		});
		glfwSetCursorEnterCallback(window.getWindowHandle(), this.cursorEnterCallback = new GLFWCursorEnterCallback() {
			@Override
			public void invoke(long window, boolean entered) {
				MouseInput.this.inWindow = entered;
			}
		});
		glfwSetMouseButtonCallback(window.getWindowHandle(), this.mouseButtonCallback = new GLFWMouseButtonCallback() {
			@Override
			public void invoke(long window, int button, int action, int mods) {
				MouseInput.this.leftButtonPressed = button == GLFW_MOUSE_BUTTON_1 && action == GLFW_PRESS;
				MouseInput.this.rightButtonPressed = button == GLFW_MOUSE_BUTTON_2 && action == GLFW_PRESS;
			}
		});
	}

	public Vector2f getDisplVec() {
		return this.displVec;
	}

	public void input(Window window) {
		this.displVec.x = 0;
		this.displVec.y = 0;
		if (this.previousPos.x > 0 && this.previousPos.y > 0 && this.inWindow) {
			double deltax = this.currentPos.x - this.previousPos.x;
			double deltay = this.currentPos.y - this.previousPos.y;
			boolean rotateX = deltax != 0;
			boolean rotateY = deltay != 0;
			if (rotateX) {
				this.displVec.y = (float) deltax;
			}
			if (rotateY) {
				this.displVec.x = (float) deltay;
			}
		}
		this.previousPos.x = this.currentPos.x;
		this.previousPos.y = this.currentPos.y;
	}

	public boolean isLeftButtonPressed() {
		return this.leftButtonPressed;
	}

	public boolean isRightButtonPressed() {
		return this.rightButtonPressed;
	}
}