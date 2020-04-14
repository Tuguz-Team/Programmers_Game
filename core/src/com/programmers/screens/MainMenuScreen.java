package com.programmers.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.programmers.enums.Difficulty;

public class MainMenuScreen extends Stage implements Screen {

    private ScreenLoader screenLoader;
    private VerticalGroup mainButtons;
    Texture fontTexture;

    TextButton startButton, exitButton;
    Skin buttonSkin;
    BitmapFont font;

    private SpriteBatch batch; // объект для отрисовки спрайтов нашей игры
    private OrthographicCamera camera; // область просмотра нашей игры

    public MainMenuScreen(final ScreenLoader screenLoader) {
        this.screenLoader = screenLoader;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        batch = new SpriteBatch();

        buttonSkin = new Skin();
        buttonSkin.addRegions(new TextureAtlas("buttons.pack"));
        fontTexture = new Texture(Gdx.files.internal("CustomFont.png"));
        font = new BitmapFont(Gdx.files.internal("CustomFont.fnt"), new TextureRegion(fontTexture), false);

        Gdx.input.setInputProcessor(this);
        mainButtons = new VerticalGroup();
        mainButtons.setFillParent(true);
        addActor(mainButtons);
        mainButtons.setDebug(true);

        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.up = buttonSkin.getDrawable("start_button");
        style.down = buttonSkin.getDrawable("exit_button");
        style.font = font;

        startButton = new TextButton("START", style);
        exitButton = new TextButton("END", style);
        mainButtons.addActor(startButton);
        mainButtons.space(400f);
        mainButtons.addActor(exitButton);
        mainButtons.center();

        startButton.addListener(new InputListener() {
            boolean wasPressed;

            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return wasPressed = true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (wasPressed)
                    screenLoader.setScreen(new GameScreen(screenLoader, Difficulty.Hard, 4));
                //Gdx.app.exit();//Gdx.app.log("my app", "Pressed");
                wasPressed = false;
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                wasPressed = true;
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                wasPressed = false;
            }
        });

        exitButton.addListener(new InputListener() {
            boolean wasPressed;

            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return wasPressed = true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (wasPressed)
                    Gdx.app.exit();//Gdx.app.log("my app", "Pressed");
                wasPressed = false;
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                wasPressed = true;
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                wasPressed = false;
            }
        });
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        act(Gdx.graphics.getDeltaTime());
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        draw();
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        getViewport().update(width, height, true);
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
        batch.dispose();
        buttonSkin.dispose();
        font.dispose();
    }
}
