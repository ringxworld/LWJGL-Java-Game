package main.org.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.org.engine.graph.Mesh;
import main.org.engine.items.GameItem;
import main.org.engine.items.SkyBox;

public class Scene {

	private GameItem[] gameItems;

	private SkyBox skyBox;

	private SceneLight sceneLight;

	private Map<Mesh, List<GameItem>> meshMap;

	public Scene() {
		this.meshMap = new HashMap<>();
	}

	public GameItem[] getGameItems() {
		return this.gameItems;
	}

	public void setGameItems(GameItem[] gameItems) {
		int numGameItems = gameItems != null ? gameItems.length : 0;
		for (int i = 0; i < numGameItems; i++) {
			GameItem gameItem = gameItems[i];
			Mesh mesh = gameItem.getMesh();
			List<GameItem> list = this.meshMap.get(mesh);
			if (list == null) {
				list = new ArrayList<>();
				this.meshMap.put(mesh, list);
			}
			list.add(gameItem);
		}
	}

	public SkyBox getSkyBox() {
		return this.skyBox;
	}

	public void setSkyBox(SkyBox skyBox) {
		this.skyBox = skyBox;
	}

	public SceneLight getSceneLight() {
		return this.sceneLight;
	}

	public void setSceneLight(SceneLight sceneLight) {
		this.sceneLight = sceneLight;
	}

	public Map<Mesh, List<GameItem>> getGameMeshes() {
		return this.meshMap;
	}

	public void cleanup() {
		for (Mesh mesh : this.meshMap.keySet()) {
			mesh.cleanUp();
		}
	}
}