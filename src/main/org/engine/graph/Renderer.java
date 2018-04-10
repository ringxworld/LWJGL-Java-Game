package main.org.engine.graph;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import main.org.engine.IHud;
import main.org.engine.Scene;
import main.org.engine.SceneLight;
import main.org.engine.Utils;
import main.org.engine.Window;
import main.org.engine.items.GameItem;
import main.org.engine.items.SkyBox;

import static org.lwjgl.opengl.GL11.*;

import java.util.List;
import java.util.Map;

public class Renderer {

	/**
	 * Field of View in Radians
	 */
	private static final float FOV = (float) Math.toRadians(60.0f);

	private static final float Z_NEAR = 0.01f;

	private static final float Z_FAR = 1000.f;

	private static final int MAX_POINT_LIGHTS = 5;

	private static final int MAX_SPOT_LIGHTS = 5;

	private final Transformation transformation;

	private Shader sceneShaderProgram;

	private Shader hudShaderProgram;

	private final float specularPower;

	private Shader skyBoxShaderProgram;

	public Renderer() {
		this.transformation = new Transformation();
		this.specularPower = 10f;
	}

	public void init(Window window) throws Exception {
		setupSceneShader();
		setupHudShader();
		setupSkyBoxShader();
	}

	private void setupSceneShader() throws Exception {
		// Create shader
		this.sceneShaderProgram = new Shader();
		this.sceneShaderProgram.createVertexShader(Utils.loadResource("/shaders/scene_vertex.vs"));
		this.sceneShaderProgram.createFragmentShader(Utils.loadResource("/shaders/scene_fragment.fs"));
		this.sceneShaderProgram.link();

		// Create uniforms for modelView and projection matrices and texture
		this.sceneShaderProgram.createUniform("projectionMatrix");
		this.sceneShaderProgram.createUniform("modelViewMatrix");
		this.sceneShaderProgram.createUniform("texture_sampler");
		// Create uniform for material
		this.sceneShaderProgram.createMaterialUniform("material");
		// Create lighting related uniforms
		this.sceneShaderProgram.createUniform("specularPower");
		this.sceneShaderProgram.createUniform("ambientLight");
		this.sceneShaderProgram.createPointLightListUniform("pointLights", MAX_POINT_LIGHTS);
		this.sceneShaderProgram.createSpotLightListUniform("spotLights", MAX_SPOT_LIGHTS);
		this.sceneShaderProgram.createDirectionalLightUniform("directionalLight");
	}

	private void setupHudShader() throws Exception {
		this.hudShaderProgram = new Shader();
		this.hudShaderProgram.createVertexShader(Utils.loadResource("/shaders/hud_vertex.vs"));
		this.hudShaderProgram.createFragmentShader(Utils.loadResource("/shaders/hud_fragment.fs"));
		this.hudShaderProgram.link();

		// Create uniforms for Ortographic-model projection matrix and base
		// colour
		this.hudShaderProgram.createUniform("projModelMatrix");
		this.hudShaderProgram.createUniform("colour");
		this.hudShaderProgram.createUniform("hasTexture");
	}

	private void setupSkyBoxShader() throws Exception {
		this.skyBoxShaderProgram = new Shader();
		this.skyBoxShaderProgram.createVertexShader(Utils.loadResource("/shaders/sb_vertex.vs"));
		this.skyBoxShaderProgram.createFragmentShader(Utils.loadResource("/shaders/sb_fragment.fs"));
		this.skyBoxShaderProgram.link();

		this.skyBoxShaderProgram.createUniform("projectionMatrix");
		this.skyBoxShaderProgram.createUniform("modelViewMatrix");
		this.skyBoxShaderProgram.createUniform("texture_sampler");
		this.skyBoxShaderProgram.createUniform("ambientLight");
	}

	public void clear() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}

	public void render(Window window, Camera camera, Scene scene, IHud hud) {
		clear();

		if (window.isResized()) {
			glViewport(0, 0, window.getWidth(), window.getHeight());
			window.setResized(false);
		}

		// Update projection and view atrices once per render cycle
		this.transformation.updateProjectionMatrix(FOV, window.getWidth(), window.getHeight(), Z_NEAR, Z_FAR);
		this.transformation.updateViewMatrix(camera);

		//System.out.println(this.transformation.getViewMatrix());
		renderScene(window, camera, scene);

		renderSkyBox(window, camera, scene);

		renderHud(window, hud);
	}

	private void renderSkyBox(Window window, Camera camera, Scene scene) {
		this.skyBoxShaderProgram.bind();

		this.skyBoxShaderProgram.setUniform("texture_sampler", 0);

		Matrix4f projectionMatrix = this.transformation.getProjectionMatrix();
		this.skyBoxShaderProgram.setUniform("projectionMatrix", projectionMatrix);
		SkyBox skyBox = scene.getSkyBox();
		Matrix4f viewMatrix = this.transformation.getViewMatrix();
		viewMatrix.m30(0);
		viewMatrix.m31(0);
		viewMatrix.m32(0);
		Matrix4f modelViewMatrix = this.transformation.buildModelViewMatrix(skyBox, viewMatrix);
		this.skyBoxShaderProgram.setUniform("modelViewMatrix", modelViewMatrix);
		this.skyBoxShaderProgram.setUniform("ambientLight", scene.getSceneLight().getAmbientLight());

		scene.getSkyBox().getMesh().render();

		this.skyBoxShaderProgram.unbind();
	}

	public void renderScene(Window window, Camera camera, Scene scene) {
		this.sceneShaderProgram.bind();

		Matrix4f projectionMatrix = this.transformation.getProjectionMatrix();
		this.sceneShaderProgram.setUniform("projectionMatrix", projectionMatrix);

		Matrix4f viewMatrix = this.transformation.getViewMatrix();

		SceneLight sceneLight = scene.getSceneLight();
		renderLights(viewMatrix, sceneLight);

		this.sceneShaderProgram.setUniform("texture_sampler", 0);
		// Render each mesh with the associated game Items
		Map<Mesh, List<GameItem>> mapMeshes = scene.getGameMeshes();
		for (Mesh mesh : mapMeshes.keySet()) {
			this.sceneShaderProgram.setUniform("material", mesh.getMaterial());
			mesh.renderList(mapMeshes.get(mesh), (GameItem gameItem) -> {
				Matrix4f modelViewMatrix = this.transformation.buildModelViewMatrix(gameItem, viewMatrix);
				this.sceneShaderProgram.setUniform("modelViewMatrix", modelViewMatrix);
			});
		}

		this.sceneShaderProgram.unbind();
	}

	private void renderLights(Matrix4f viewMatrix, SceneLight sceneLight) {

		this.sceneShaderProgram.setUniform("ambientLight", sceneLight.getAmbientLight());
		this.sceneShaderProgram.setUniform("specularPower", this.specularPower);

		// Process Point Lights
		PointLight[] pointLightList = sceneLight.getPointLightList();
		int numLights = pointLightList != null ? pointLightList.length : 0;
		for (int i = 0; i < numLights; i++) {
			// Get a copy of the point light object and transform its position
			// to view coordinates
			PointLight currPointLight = new PointLight(pointLightList[i]);
			Vector3f lightPos = currPointLight.getPosition();
			Vector4f aux = new Vector4f(lightPos, 1);
			aux.mul(viewMatrix);
			lightPos.x = aux.x;
			lightPos.y = aux.y;
			lightPos.z = aux.z;
			this.sceneShaderProgram.setUniform("pointLights", currPointLight, i);
		}

		// Process Spot Ligths
		SpotLight[] spotLightList = sceneLight.getSpotLightList();
		numLights = spotLightList != null ? spotLightList.length : 0;
		for (int i = 0; i < numLights; i++) {
			// Get a copy of the spot light object and transform its position
			// and cone direction to view coordinates
			SpotLight currSpotLight = new SpotLight(spotLightList[i]);
			Vector4f dir = new Vector4f(currSpotLight.getConeDirection(), 0);
			dir.mul(viewMatrix);
			currSpotLight.setConeDirection(new Vector3f(dir.x, dir.y, dir.z));

			Vector3f lightPos = currSpotLight.getPointLight().getPosition();
			Vector4f aux = new Vector4f(lightPos, 1);
			aux.mul(viewMatrix);
			lightPos.x = aux.x;
			lightPos.y = aux.y;
			lightPos.z = aux.z;

			this.sceneShaderProgram.setUniform("spotLights", currSpotLight, i);
		}

		// Get a copy of the directional light object and transform its position
		// to view coordinates
		DirectionalLight currDirLight = new DirectionalLight(sceneLight.getDirectionalLight());
		Vector4f dir = new Vector4f(currDirLight.getDirection(), 0);
		dir.mul(viewMatrix);
		currDirLight.setDirection(new Vector3f(dir.x, dir.y, dir.z));
		this.sceneShaderProgram.setUniform("directionalLight", currDirLight);
	}

	private void renderHud(Window window, IHud hud) {
		this.hudShaderProgram.bind();

		Matrix4f ortho = this.transformation.getOrthoProjectionMatrix(0, window.getWidth(), window.getHeight(), 0);
		for (GameItem gameItem : hud.getGameItems()) {
			Mesh mesh = gameItem.getMesh();
			// Set ortohtaphic and model matrix for this HUD item
			Matrix4f projModelMatrix = this.transformation.buildOrtoProjModelMatrix(gameItem, ortho);
			this.hudShaderProgram.setUniform("projModelMatrix", projModelMatrix);
			this.hudShaderProgram.setUniform("colour", gameItem.getMesh().getMaterial().getColour());
			this.hudShaderProgram.setUniform("hasTexture", gameItem.getMesh().getMaterial().isTextured() ? 1 : 0);

			// Render the mesh for this HUD item
			mesh.render();
		}

		this.hudShaderProgram.unbind();
	}

	public void cleanup() {
		if (this.skyBoxShaderProgram != null) {
			this.skyBoxShaderProgram.cleanup();
		}
		if (this.sceneShaderProgram != null) {
			this.sceneShaderProgram.cleanup();
		}
		if (this.hudShaderProgram != null) {
			this.hudShaderProgram.cleanup();
		}
	}
}