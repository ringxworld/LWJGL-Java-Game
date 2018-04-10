package main.org.engine.graph;

import org.joml.Vector3f;

public class Material {

	private static final Vector3f DEFAULT_COLOUR = new Vector3f(1.0f, 1.0f, 1.0f);

	private Vector3f colour;

	private float reflectance;

	private Texture texture;

	public Material() {
		this.colour = DEFAULT_COLOUR;
		this.reflectance = 0;
	}

	public Material(Vector3f colour, float reflectance) {
		this();
		this.colour = colour;
		this.reflectance = reflectance;
	}

	public Material(Texture texture) {
		this();
		this.texture = texture;
	}

	public Material(Texture texture, float reflectance) {
		this();
		this.texture = texture;
		this.reflectance = reflectance;
	}

	public Vector3f getColour() {
		return this.colour;
	}

	public void setColour(Vector3f colour) {
		this.colour = colour;
	}

	public float getReflectance() {
		return this.reflectance;
	}

	public void setReflectance(float reflectance) {
		this.reflectance = reflectance;
	}

	public boolean isTextured() {
		return this.texture != null;
	}

	public Texture getTexture() {
		return this.texture;
	}

	public void setTexture(Texture texture) {
		this.texture = texture;
	}
}