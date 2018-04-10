package main.org.engine.graph;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import main.org.engine.items.GameItem;

public class Transformation {

	private final Matrix4f projectionMatrix;

	private final Matrix4f modelViewMatrix;

	private final Matrix4f modelMatrix;

	private final Matrix4f viewMatrix;

	private final Matrix4f orthoMatrix;

	private final Matrix4f orthoModelMatrix;

	public Transformation() {
		this.projectionMatrix = new Matrix4f();
		this.modelMatrix = new Matrix4f();
		this.modelViewMatrix = new Matrix4f();
		this.viewMatrix = new Matrix4f();
		this.orthoMatrix = new Matrix4f();
		this.orthoModelMatrix = new Matrix4f();
	}

	public Matrix4f getProjectionMatrix() {
		return this.projectionMatrix;
	}

	public Matrix4f updateProjectionMatrix(float fov, float width, float height, float zNear, float zFar) {
		float aspectRatio = width / height;
		this.projectionMatrix.identity();
		this.projectionMatrix.perspective(fov, aspectRatio, zNear, zFar);
		return this.projectionMatrix;
	}

	public Matrix4f getViewMatrix() {
		return this.viewMatrix;
	}

	public Matrix4f updateViewMatrix(Camera camera) {
		Vector3f cameraPos = camera.getPosition();
		Vector3f rotation = camera.getRotation();

		this.viewMatrix.identity();
		// First do the rotation so camera rotates over its position
		this.viewMatrix.rotate((float) Math.toRadians(rotation.x), new Vector3f(1, 0, 0)).rotate((float) Math.toRadians(rotation.y), new Vector3f(0, 1, 0));
		// Then do the translation
		this.viewMatrix.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);
		return this.viewMatrix;
	}

	public final Matrix4f getOrthoProjectionMatrix(float left, float right, float bottom, float top) {
		this.orthoMatrix.identity();
		this.orthoMatrix.setOrtho2D(left, right, bottom, top);
		return this.orthoMatrix;
	}

	public Matrix4f buildModelViewMatrix(GameItem gameItem, Matrix4f viewMatrix) {
		Vector3f rotation = gameItem.getRotation();
		this.modelMatrix.identity().translate(gameItem.getPosition()).rotateX((float) Math.toRadians(-rotation.x)).rotateY((float) Math.toRadians(-rotation.y)).rotateZ((float) Math.toRadians(-rotation.z)).scale(gameItem.getScale());
		this.modelViewMatrix.set(viewMatrix);
		return this.modelViewMatrix.mul(this.modelMatrix);
	}

	public Matrix4f buildOrtoProjModelMatrix(GameItem gameItem, Matrix4f orthoMatrix) {
		Vector3f rotation = gameItem.getRotation();
		this.modelMatrix.identity();
		this.modelMatrix.translate(gameItem.getPosition());
		this.modelMatrix.rotateX((float) Math.toRadians(-rotation.x));
		this.modelMatrix.rotateY((float) Math.toRadians(-rotation.y));
		this.modelMatrix.rotateZ((float) Math.toRadians(-rotation.z));
		this.modelMatrix.scale(gameItem.getScale());
		this.orthoModelMatrix.set(orthoMatrix);
		this.orthoModelMatrix.mul(this.modelMatrix);
		return this.orthoModelMatrix;
	}
}