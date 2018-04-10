package main.org.engine.items;

import org.joml.Vector3f;

import main.org.engine.graph.Mesh;

public class GameItem {

	private Mesh mesh;

	private final Vector3f position;

	private float scale;

	private final Vector3f rotation;

	public GameItem() {
		this.position = new Vector3f(0, 0, 0);
		this.scale = 1;
		this.rotation = new Vector3f(0, 0, 0);
	}

	public GameItem(Mesh mesh) {
		this();
		this.mesh = mesh;
	}

	public Vector3f getPosition() {
		return this.position;
	}

	public void setPosition(float x, float y, float z) {
		this.position.x = x;
		this.position.y = y;
		this.position.z = z;
	}

	public float getScale() {
		return this.scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

	public Vector3f getRotation() {
		return this.rotation;
	}

	public void setRotation(float x, float y, float z) {
		this.rotation.x = x;
		this.rotation.y = y;
		this.rotation.z = z;
	}

	public Mesh getMesh() {
		return mesh;
	}

	public void setMesh(Mesh mesh) {
		this.mesh = mesh;
	}
}