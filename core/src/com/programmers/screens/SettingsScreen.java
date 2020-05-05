package com.programmers.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.utils.Align;
import com.programmers.ui_elements.MyButton;

public class SettingsScreen extends Stage implements Screen {

    private final OrthographicCamera camera;

    SettingsScreen(final ScreenLoader screenLoader, final Screen previousScreen) {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        ImageTextButton returnButton = new MyButton("RETURN", screenLoader.getButtonStyle()) {
            @Override
            public void call() {
                dispose();
                screenLoader.setScreen(previousScreen);
            }
        };
        addActor(returnButton);
        returnButton.setPosition(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f,
                Align.center);
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        act(Gdx.graphics.getDeltaTime());
        getBatch().setProjectionMatrix(camera.combined);
        draw();
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
}