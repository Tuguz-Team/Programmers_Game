package com.programmers.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.programmers.enums.Difficulty;
import com.programmers.game.Field;
import com.programmers.game.GameController;
import com.programmers.game.GameInputProcessor;
import com.programmers.game.Player;
import com.programmers.game_objects.Base;
import com.programmers.game_objects.Car;

import static com.badlogic.gdx.math.MathUtils.random;

public class GameScreen implements Screen {

	private int size;
	private int playersCount;

	ScreenLoader screenLoader;
	private Field field;
	private GameController gameController;
	private com.programmers.enums.Difficulty difficulty;

	private Array<ModelInstance> instances;
	private AssetManager assetManager;
	private boolean loading;

	private PerspectiveCamera camera;
	private Environment environment;
	private ModelBatch modelBatch;
	private GameInputProcessor gameInputProcessor;

	public GameScreen(ScreenLoader screenLoader, final com.programmers.enums.Difficulty difficulty, final int playersCount) {
		this.screenLoader = screenLoader;
		this.difficulty = difficulty;
		this.playersCount = playersCount;

		switch (difficulty) {
			case Hard:
				size = 9;
				break;
			case Easy:
				size = 6;
		}

		instances = new Array<>();
		assetManager = new AssetManager();
		modelBatch = new ModelBatch();

		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));

		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, 1f, 0.8f, 0.2f));

		camera = new PerspectiveCamera(67f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.position.set(-size, size, -size);
		camera.lookAt(0f,0f,0f);
		camera.near = 0.1f;
		camera.far = 100f;
		camera.update();

		gameInputProcessor = new GameInputProcessor(camera, this);
		Gdx.input.setInputProcessor(new GestureDetector(gameInputProcessor));

		field = new Field(this);
		Player[] players = new Player[playersCount];
		Array<Base> bases = new Array<>(new Base[] {
				(Base)field.getChunks()[0][0], (Base)field.getChunks()[0][size - 1],
				(Base)field.getChunks()[size - 1][0], (Base)field.getChunks()[size - 1][size - 1]
		});
		for (int i = 0; i < players.length; i++) {
			int index = random.nextInt(bases.size);
			players[i] = new Player(new Car(bases.get(index), players[i]));
			bases.removeIndex(index);
		}
		gameController = new GameController(players, field);

		loading();

		// X axis (red) is i, Y axis (green) is height, Z axis (blue) is j
		{
			Vector3 start = new Vector3(0f, 5f, 0f), end = new Vector3();

			ModelBuilder modelBuilder1 = new ModelBuilder();
			modelBuilder1.begin();
			MeshPartBuilder builder1 = modelBuilder1.part("Y", 1, 3, new Material());
			builder1.setColor(Color.GREEN);
			end.set(start).add(new Vector3(Vector3.Y).scl(3));
			builder1.line(start.x, start.y, start.z, end.x, end.y, end.z);
			instances.add(new ModelInstance(modelBuilder1.end()));

			ModelBuilder modelBuilder2 = new ModelBuilder();
			modelBuilder2.begin();
			MeshPartBuilder builder2 = modelBuilder2.part("X", 1, 3, new Material());
			builder2.setColor(Color.RED);
			end.set(start).add(new Vector3(Vector3.X).scl(3));
			builder2.line(start.x, start.y, start.z, end.x, end.y, end.z);
			instances.add(new ModelInstance(modelBuilder2.end()));

			ModelBuilder modelBuilder3 = new ModelBuilder();
			modelBuilder3.begin();
			MeshPartBuilder builder3 = modelBuilder3.part("Z", 1, 3, new Material());
			builder3.setColor(Color.BLUE);
			end.set(start).add(new Vector3(Vector3.Z).scl(3));
			builder3.line(start.x, start.y, start.z, end.x, end.y, end.z);
			instances.add(new ModelInstance(modelBuilder3.end()));
		}
	}

	public int getSize() {
		return size;
	}

	public Array<ModelInstance> getInstances() {
		return instances;
	}

	public int getPlayersCount() {
		return playersCount;
	}

	public Difficulty getDifficulty() {
		return difficulty;
	}

	public AssetManager getAssetManager() {
		return assetManager;
	}

	private void loading() {
		field.loading();
        for (Player player : gameController.getPlayers()) {
            player.getCar().loading();
        }
		loading = true;
	}

	private void doneLoading() {
		field.doneLoading();
		for (Player player : gameController.getPlayers()) {
			player.getCar().doneLoading();
		}
		loading = false;
	}

	@Override
	public void show() {

	}

	@Override
	public void render(float delta) {
		if (loading && assetManager.update()) {
			doneLoading();
			gameInputProcessor.unlockCamera();
		}

		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		gameInputProcessor.cameraPosChange();
		camera.update();

		modelBatch.begin(camera);
		modelBatch.render(instances, environment);
		modelBatch.end();
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose() {
		modelBatch.dispose();
		instances.clear();
		assetManager.dispose();
	}
}