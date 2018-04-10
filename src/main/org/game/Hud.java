package main.org.game;

import java.awt.Font;

import org.joml.Vector3f;

import main.org.engine.IHud;
import main.org.engine.Window;
import main.org.engine.graph.FontTexture;
import main.org.engine.graph.Material;
import main.org.engine.graph.Mesh;
import main.org.engine.graph.OBJLoader;
import main.org.engine.items.GameItem;
import main.org.engine.items.TextItem;

public class Hud implements IHud {

	private static final Font FONT = new Font("Arial", Font.PLAIN, 20);

	private static final String CHARSET = "ISO-8859-1";

	private final GameItem[] gameItems;

	private final TextItem statusTextItem;

	private final GameItem compassItem;

	public Hud(String statusText) throws Exception {
		FontTexture fontTexture = new FontTexture(FONT, CHARSET);
		this.statusTextItem = new TextItem(statusText, fontTexture);
		this.statusTextItem.getMesh().getMaterial().setColour(new Vector3f(1, 1, 1));

		// Create compass
		Mesh mesh = OBJLoader.loadMesh("/models/compass.obj");
		Material material = new Material();
		material.setColour(new Vector3f(1, 0, 0));
		mesh.setMaterial(material);
		this.compassItem = new GameItem(mesh);
		this.compassItem.setScale(40.0f);
		// Rotate to transform it to screen coordinates
		this.compassItem.setRotation(0f, 0f, 180f);

		// Create list that holds the items that compose the HUD
		this.gameItems = new GameItem[] { this.statusTextItem, this.compassItem };
	}

	public void setStatusText(String statusText) {
		this.statusTextItem.setText(statusText);
	}

	public void rotateCompass(float angle) {
		this.compassItem.setRotation(0, 0, 180 + angle);
	}

	@Override
	public GameItem[] getGameItems() {
		return this.gameItems;
	}

	public void updateSize(Window window) {
		this.statusTextItem.setPosition(10f, window.getHeight() - 50f, 0);
		this.compassItem.setPosition(window.getWidth() - 40f, 50f, 0);
	}
}