package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
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
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;

import static com.badlogic.gdx.math.MathUtils.random;

public class ProgrammersGame extends ApplicationAdapter {

	private static int size;
	private static int playersCount;

	private Field field;
	private GameController gameController;
	static Difficulty difficulty;

	static Array<ModelInstance> instances;
	static AssetManager assetManager;
	private boolean loading;

	private PerspectiveCamera camera;
	private Environment environment;
	private ModelBatch modelBatch;
	private UIController uiController;
	private MyGestureDetector myGestureDetector;

	public ProgrammersGame(final Difficulty difficulty, final int players) {
		ProgrammersGame.difficulty = difficulty;
		playersCount = players;
	}

	static int getSize() {
		return size;
	}

	@Override
	public void create() {
		switch (difficulty) {
			case Hard:
				size = 9;
				break;
			case Easy:
				size = 6;
		}

		instances = new Array<>();
		assetManager = new AssetManager();
		uiController = new UIController(new Skin());
		modelBatch = new ModelBatch();

		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

		camera = new PerspectiveCamera(67f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.position.set(-size, size, -size);
		camera.lookAt(0f,0f,0f);
		camera.near = 0.1f;
		camera.far = 100f;
		camera.update();

		myGestureDetector = new MyGestureDetector(camera);
		Gdx.input.setInputProcessor(new GestureDetector(myGestureDetector));

		field = new Field();
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
	public void render() {
		if (loading && assetManager.update()) {
			doneLoading();
			myGestureDetector.unlockCamera();
		}

		//Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		myGestureDetector.cameraPosChange();
		camera.update();

		modelBatch.begin(camera);
		modelBatch.render(instances, environment);
		modelBatch.end();

		uiController.draw();
	}

	@Override
	public void dispose() {
		modelBatch.dispose();
		instances.clear();
		assetManager.dispose();
		uiController.dispose();
	}
}
