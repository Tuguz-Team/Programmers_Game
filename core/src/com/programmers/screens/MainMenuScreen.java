package com.programmers.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.programmers.ui_elements.YesNoDialog;
import com.programmers.ui_elements.MyButton;

public final class MainMenuScreen extends Stage implements Screen, InputProcessor {

    private final YesNoDialog yesNoDialog;
    private boolean isYesNoDialogHidden = true;
    private OrthographicCamera camera;

    public MainMenuScreen(final ScreenLoader screenLoader) {
        Image main = new Image((Texture) screenLoader.getAssetManager().get("Sprites/Background/main.jpg"));
        main.setFillParent(true);
        addActor(main);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        setViewport(new FillViewport(1100, 1100, camera));

        yesNoDialog = new YesNoDialog("   Are you sure you want to exit the game?   ", ScreenLoader.getGameSkin()) {
            @Override
            public void call() {
                Gdx.app.exit();
            }

            @Override
            protected void result(Object object) {
                super.result(object);
                isYesNoDialogHidden = true;
            }
        };

        VerticalGroup mainButtons = new VerticalGroup();
        mainButtons.setFillParent(true);
        addActor(mainButtons);

        Image logo = new Image((Texture) screenLoader.getAssetManager().get("Sprites/Background/logo.png"));

        TextButton startButton =
                new MyButton("   START GAME   ", ScreenLoader.getGameSkin()) {
            @Override
            public void call() {
                screenLoader.setScreen(new ChooseGameScreen(
                        screenLoader, MainMenuScreen.this)
                );
            }
        };
        startButton.getLabel().setFontScale(2);

        TextButton exitButton =
                new MyButton("   EXIT   ", ScreenLoader.getGameSkin()) {
            @Override
            public void call() {
                if (isYesNoDialogHidden) {
                    yesNoDialog.show(MainMenuScreen.this);
                    isYesNoDialogHidden = false;
                }
            }
        };
        exitButton.getLabel().setFontScale(2);

        mainButtons.addActor(logo);
        mainButtons.addActor(startButton);
        mainButtons.addActor(exitButton);
        mainButtons.center().space(75);

        screenLoader.networkManager.registerAnon();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
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

    @Override
    public boolean keyDown(int keyCode) {
        if (keyCode == Input.Keys.BACK && isYesNoDialogHidden) {
            yesNoDialog.show(this);
            isYesNoDialogHidden = false;
            return true;
        }
        return false;
    }
}
