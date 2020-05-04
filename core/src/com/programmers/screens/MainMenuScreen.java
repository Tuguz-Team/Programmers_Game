package com.programmers.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.programmers.enums.Difficulty;
import com.programmers.ui_elements.MyButton;

public class MainMenuScreen extends Stage implements Screen {

    private ScreenLoader screenLoader;
    private VerticalGroup mainButtons;

    private Texture fontTexture;
    private ImageTextButton startButton, exitButton;
    private Skin buttonSkin;
    private BitmapFont font;

    private OrthographicCamera camera; // область просмотра нашей игры

    public MainMenuScreen(final ScreenLoader screenLoader) {
        this.screenLoader = screenLoader;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        buttonSkin = new Skin();
        buttonSkin.addRegions(new TextureAtlas("buttons.pack"));
        fontTexture = new Texture(Gdx.files.internal("CustomFont.png"));
        font = new BitmapFont(Gdx.files.internal("CustomFont.fnt"), new TextureRegion(fontTexture), false);

        mainButtons = new VerticalGroup();
        mainButtons.setFillParent(true);
        addActor(mainButtons);

        ImageTextButton.ImageTextButtonStyle style = new ImageTextButton.ImageTextButtonStyle();
        style.up = buttonSkin.getDrawable("start_button");
        style.down = buttonSkin.getDrawable("exit_button");
        style.font = font;

        startButton = new MyButton("START", style) {
            @Override
            public void call() {
                screenLoader.setScreen(new GameScreen(screenLoader, Difficulty.Hard, 4));
            }
        };
        exitButton = new MyButton("END", style) {
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

    @Override
    public void dispose() {
        buttonSkin.dispose();
        font.dispose();
        super.dispose();
    }
}
