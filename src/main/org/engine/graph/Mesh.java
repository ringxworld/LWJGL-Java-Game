package main.org.engine.graph;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.lwjgl.BufferUtils;

import main.org.engine.items.GameItem;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class Mesh {

	private final int vaoId;

	private final List<Integer> vboIdList;

	private final int vertexCount;

	private Material material;

	public Mesh(float[] positions, float[] textCoords, float[] normals, int[] indices) {
		this.vertexCount = indices.length;
		this.vboIdList = new ArrayList();

		this.vaoId = glGenVertexArrays();
		glBindVertexArray(this.vaoId);

		// Position VBO
		int vboId = glGenBuffers();
		this.vboIdList.add(vboId);
		FloatBuffer posBuffer = BufferUtils.createFloatBuffer(positions.length);
		posBuffer.put(positions).flip();
		glBindBuffer(GL_ARRAY_BUFFER, vboId);
		glBufferData(GL_ARRAY_BUFFER, posBuffer, GL_STATIC_DRAW);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

		// Texture coordinates VBO
		vboId = glGenBuffers();
		this.vboIdList.add(vboId);
		FloatBuffer textCoordsBuffer = BufferUtils.createFloatBuffer(textCoords.length);
		textCoordsBuffer.put(textCoords).flip();
		glBindBuffer(GL_ARRAY_BUFFER, vboId);
		glBufferData(GL_ARRAY_BUFFER, textCoordsBuffer, GL_STATIC_DRAW);
		glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);

		// Vertex normals VBO
		vboId = glGenBuffers();
		this.vboIdList.add(vboId);
		FloatBuffer vecNormalsBuffer = BufferUtils.createFloatBuffer(normals.length);
		vecNormalsBuffer.put(normals).flip();
		glBindBuffer(GL_ARRAY_BUFFER, vboId);
		glBufferData(GL_ARRAY_BUFFER, vecNormalsBuffer, GL_STATIC_DRAW);
		glVertexAttribPointer(2, 3, GL_FLOAT, false, 0, 0);

		// Index VBO
		vboId = glGenBuffers();
		this.vboIdList.add(vboId);
		IntBuffer indicesBuffer = BufferUtils.createIntBuffer(indices.length);
		indicesBuffer.put(indices).flip();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboId);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);

		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindVertexArray(0);
	}

	public Material getMaterial() {
		return this.material;
	}

	public void setMaterial(Material material) {
		this.material = material;
	}

	public int getVaoId() {
		return this.vaoId;
	}

	public int getVertexCount() {
		return this.vertexCount;
	}

	public void render() {
		initRender();

		glDrawElements(GL_TRIANGLES, getVertexCount(), GL_UNSIGNED_INT, 0);

		endRender();
	}

	public void cleanUp() {
		glDisableVertexAttribArray(0);

		// Delete the VBOs
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		for (int vboId : this.vboIdList) {
			glDeleteBuffers(vboId);
		}

		// Delete the texture
		Texture texture = this.material.getTexture();
		if (texture != null) {
			texture.cleanup();
		}

		// Delete the VAO
		glBindVertexArray(0);
		glDeleteVertexArrays(this.vaoId);
	}

	public void deleteBuffers() {
		glDisableVertexAttribArray(0);

		// Delete the VBOs
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		for (int vboId : this.vboIdList) {
			glDeleteBuffers(vboId);
		}

		// Delete the VAO
		glBindVertexArray(0);
		glDeleteVertexArrays(this.vaoId);
	}

	public void renderList(List<GameItem> gameItems, Consumer<GameItem> consumer) {
		initRender();

		for (GameItem gameItem : gameItems) {
			// Set up data requiered by gameItem
			consumer.accept(gameItem);
			// Render this game item
			glDrawElements(GL_TRIANGLES, getVertexCount(), GL_UNSIGNED_INT, 0);
		}

		endRender();
	}

	private void initRender() {
		Texture texture = this.material.getTexture();
		if (texture != null) {
			// Activate firs texture bank
			glActiveTexture(GL_TEXTURE0);
			// Bind the texture
			glBindTexture(GL_TEXTURE_2D, texture.getId());
		}

		// Draw the mesh
		glBindVertexArray(getVaoId());
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glEnableVertexAttribArray(2);
	}

	private void endRender() {
		// Restore state
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(2);
		glBindVertexArray(0);

		glBindTexture(GL_TEXTURE_2D, 0);
	}
}