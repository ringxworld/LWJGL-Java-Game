package main.org.engine.graph;

import org.joml.Vector3f;

public class DirectionalLight {

	private Vector3f color;

	private Vector3f direction;

	private float intensity;

	public DirectionalLight(Vector3f color, Vector3f direction, float intensity) {
		this.color = color;
		this.direction = direction;
		this.intensity = intensity;
	}

	public DirectionalLight(DirectionalLight light) {
		this(new Vector3f(light.getColor()), new Vector3f(light.getDirection()), light.getIntensity());
	}

	public float getIntensity() {
		return this.intensity;
	}

	public Vector3f getDirection() {
		return this.direction;
	}

	public Vector3f getColor() {
		return this.color;
	}

	public void setIntensity(float intensity) {
		this.intensity = intensity;
	}

	public void setDirection(Vector3f direction) {
		this.direction = direction;
	}

	public void setColor(Vector3f color) {
		this.color = color;
	}
}
