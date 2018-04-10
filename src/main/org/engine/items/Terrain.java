package main.org.engine.items;

import java.awt.geom.Rectangle2D;

import org.joml.Vector3f;

import main.org.engine.graph.HeightMapMesh;

public class Terrain {

	private final GameItem[] gameItems;

	public Terrain(int blocksPerRow, float scale, float minY, float maxY, String heightMap, String textureFile, int textInc) throws Exception {
		this.gameItems = new GameItem[blocksPerRow * blocksPerRow];
		HeightMapMesh heightMapMesh = new HeightMapMesh(minY, maxY, heightMap, textureFile, textInc);
		for (int row = 0; row < blocksPerRow; row++) {
			for (int col = 0; col < blocksPerRow; col++) {
				float xDisplacement = (col - ((float) blocksPerRow - 1) / (float) 2) * scale * HeightMapMesh.getXLength();
				float zDisplacement = (row - ((float) blocksPerRow - 1) / (float) 2) * scale * HeightMapMesh.getZLength();

				GameItem terrainBlock = new GameItem(heightMapMesh.getMesh());
				terrainBlock.setScale(scale);
				terrainBlock.setPosition(xDisplacement, 0, zDisplacement);
				this.gameItems[row * blocksPerRow + col] = terrainBlock;
			}
		}
	}

	public GameItem[] getGameItems() {
		return this.gameItems;
	}

	private Rectangle2D.Float getBoundingBox(GameItem terrainBlock) {
		float scale = terrainBlock.getScale();
		Vector3f position = terrainBlock.getPosition();

		float topLeftX = HeightMapMesh.STARTX * scale + position.x;
		float topLeftZ = HeightMapMesh.STARTZ * scale + position.z;
		float width = Math.abs(HeightMapMesh.STARTX * 2) * scale;
		float height = Math.abs(HeightMapMesh.STARTZ * 2) * scale;
		Rectangle2D.Float boundingBox = new Rectangle2D.Float(topLeftX, topLeftZ, width, height);
		return boundingBox;
	}
}