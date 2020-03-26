package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.utils.Array;

public class ProgrammersGame extends ApplicationAdapter {

	static int size;
	private static Field field;
	private static Car[] cars;
	static Difficulty difficulty = Difficulty.Hard;
	static int maxHeight;

	static Array<ModelInstance> instances;
	static AssetManager assetManager;
	private static boolean loading;

	static PerspectiveCamera camera;
	private static Environment environment;
	private static ModelBatch modelBatch;
	private static MyGestureDetector myGestureDetector;

	@Override
	public void create () {
		switch (difficulty) {
			case Hard:
				maxHeight = 3;
				size = 9;
				break;
			case Easy:
			default:
				maxHeight = 2;
				size = 6;
		}

		instances = new Array<>();
		assetManager = new AssetManager();

		myGestureDetector = new MyGestureDetector();
		Gdx.input.setInputProcessor(new GestureDetector(myGestureDetector));

		modelBatch = new ModelBatch();

		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

		camera = new PerspectiveCamera(67f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.position.set(size, size, size);
		camera.lookAt(0f,0f,0f);
		camera.near = 0.1f;
		camera.far = 100f;
		camera.update();

		field = new Field(size);
		cars = new Car[] {
				new Car(0, 0, Field.chunks[0][0].z + 1, Field.chunks[0][0].getBaseColor()),
				new Car(0, size - 1, Field.chunks[0][size - 1].z + 1, Field.chunks[0][size - 1].getBaseColor()),
				new Car(size - 1, 0, Field.chunks[size - 1][0].z + 1, Field.chunks[size - 1][0].getBaseColor()),
				new Car(size - 1, size - 1, Field.chunks[size - 1][size - 1].z + 1, Field.chunks[size - 1][size - 1].getBaseColor()) };

		loading();
	}

	private void loading() {
		field.loading();
		for (Car car : cars) {
			car.loading();
		}
		loading = true;
	}

	private void doneLoading() {
		field.doneLoading();
		for (Car car : cars) {
			car.doneLoading();
		}
		loading = false;
	}

	@Override
	public void render () {
		if (loading && assetManager.update())
			doneLoading();

		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		myGestureDetector.cameraPosChange();
		camera.update();

		modelBatch.begin(camera);
		modelBatch.render(instances, environment);
		modelBatch.end();
	}

	@Override
	public void dispose () {
		field.dispose();
		for (Car car : cars) {
			car.dispose();
		}
		modelBatch.dispose();
		instances.clear();
		assetManager.dispose();
	}

	public enum Difficulty {
		Easy,
		Hard
	}
}
