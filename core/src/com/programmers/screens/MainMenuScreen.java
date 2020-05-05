package com.programmers.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.programmers.enums.Difficulty;
import com.programmers.ui_elements.MyButton;

public class MainMenuScreen extends Stage implements Screen {

    private OrthographicCamera camera;

    public MainMenuScreen(final ScreenLoader screenLoader) {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        VerticalGroup mainButtons = new VerticalGroup();
        mainButtons.setFillParent(true);
        addActor(mainButtons);

        ImageTextButton startButton = new MyButton("START", screenLoader.getButtonStyle()) {
            @Override
            public void call() {
                screenLoader.setScreen(new GameScreen(screenLoader, Difficulty.Hard, 4));
            }
        };
        ImageTextButton exitButton = new MyButton("END", screenLoader.getButtonStyle()) {
            @Override
            public void call() {
                Gdx.app.exit();
            }
        };

        mainButtons.addActor(startButton);
        mainButtons.space(0.2f * Gdx.graphics.getHeight());
        mainButtons.addActor(exitButton);
        mainButtons.center();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        act(Gdx.graphics.getDeltaTime());
        getBatch().setProjectionMatrix(camera.combined);
        draw();
    }

    @Override
    public void resize(int width, int height) {
        getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
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
