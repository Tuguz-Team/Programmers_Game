package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.utils.Array;

public class ProgrammersGame extends ApplicationAdapter {

	final static int size = 9;
	private static Field field;
	private static Car[] cars;

	static Array<ModelInstance> instances;
	static AssetManager assetManager;
	private static boolean loading;

	static PerspectiveCamera camera;
	private static Environment environment;
	private static ModelBatch modelBatch;
	private static MyGestureDetector myGestureDetector;

	@Override
	public void create () {
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
		camera.near = 1f;
		camera.far = 100f;
		myGestureDetector.zoom(Float.MAX_VALUE, Float.MAX_VALUE);
		camera.update();

		field = new Field(size);
		cars = new Car[] {
				new Car(0, 0, 1, Car.Color.RED),
				new Car(0, size - 1, 1, Car.Color.GREEN),
				new Car(size - 1, 0, 1, Car.Color.YELLOW),
				new Car(size - 1, size - 1, 1, Car.Color.BLUE) };

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
}
