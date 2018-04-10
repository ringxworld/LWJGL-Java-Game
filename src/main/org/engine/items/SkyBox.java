package main.org.engine.items;

import main.org.engine.graph.Material;
import main.org.engine.graph.Mesh;
import main.org.engine.graph.OBJLoader;
import main.org.engine.graph.Texture;

public class SkyBox extends GameItem {

	public SkyBox(String objModel, String textureFile) throws Exception {
		super();
		Mesh skyBoxMesh = OBJLoader.loadMesh(objModel);
		Texture skyBoxtexture = new Texture(textureFile);
		skyBoxMesh.setMaterial(new Material(skyBoxtexture, 0.0f));
		setMesh(skyBoxMesh);
		setPosition(0, 0, 0);
	}
}