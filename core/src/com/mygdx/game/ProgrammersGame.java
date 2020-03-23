package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.math.RandomXS128;
import com.badlogic.gdx.math.Vector3;

public class ProgrammersGame extends ApplicationAdapter {
	public static RandomXS128 random = new RandomXS128();

	private static int size = 9;
	private Field field;
	private Car[] cars;

	static PerspectiveCamera camera;
	static Environment environment;
	static ModelBatch modelBatch;

	@Override
	public void create () {
		modelBatch = new ModelBatch();

		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

		camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.position.set(size, size, size);
		camera.lookAt(0,0,0);
		camera.near = 0.1f;
		camera.far = 300f;
		camera.update();

		field = new Field(size);
		cars = new Car[] {
				new Car(0, 0, 1, GameColor.RED),
				new Car(0, size - 1, 1, GameColor.GREEN),
				new Car(size - 1, 0, 1, GameColor.YELLOW),
				new Car(size - 1, size - 1, 1, GameColor.BLUE) };
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}

	@Override
	public void render () {
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		camera.rotateAround(new Vector3(), new Vector3(0, 1, 0),
				Gdx.graphics.getDeltaTime() * -Gdx.input.getDeltaX() * 5f);
		camera.update();

		field.render();
		for (Car car : cars) {
			car.render();
		}
	}

	@Override
	public void pause() {
		super.pause();
	}

	@Override
	public void resume() {
		super.resume();
	}

	@Override
	public void dispose () {
		field.dispose();
		for (Car car : cars) {
			car.dispose();
		}
		modelBatch.dispose();
	}
}
