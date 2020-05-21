package com.programmers.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.programmers.ui_elements.MyButton;

public abstract class ReturnableScreen extends Stage implements Screen, InputProcessor {

    protected final ScreenLoader screenLoader;
    private final Screen previousScreen;

    private final OrthographicCamera camera;

    public ReturnableScreen(final ScreenLoader screenLoader, final Screen previousScreen) {
        this.screenLoader = screenLoader;
        this.previousScreen = previousScreen;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        setViewport(new FillViewport(1280, 720, camera));

        TextButton returnButton =
                new MyButton("   BACK   ", ScreenLoader.getGameSkin()) {
            @Override
            public void call() {
                returnToPreviousScreen();
            }
        };
        //returnButton.getLabel().setFontScale(2);

        addActor(returnButton);
        returnButton.setPosition(1270, 710, Align.topRight);
    }

    public void returnToPreviousScreen() {
        dispose();
        screenLoader.setScreen(previousScreen);
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

    @Override
    public boolean keyDown(int keyCode) {
        if (keyCode == Input.Keys.BACK) {
            returnToPreviousScreen();
            return true;
        }
        return false;
    }
}
