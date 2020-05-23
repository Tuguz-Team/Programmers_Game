package com.programmers.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.programmers.game.GameAssets;
import com.programmers.game.SkyBox;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;

public class SplashScreen extends Stage implements Screen {

    private Sprite icon, back, loading;
    private ScreenLoader screenLoader;

    private TweenManager tweenManager;
    private OrthographicCamera camera;
    private final long ratio = Gdx.graphics.getWidth() / Gdx.graphics.getHeight();

    SplashScreen (ScreenLoader screenLoader) {
        this.screenLoader = screenLoader;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        setViewport(new FillViewport(1280, 720, camera));
    }

    @Override
    public void show() {
        tweenManager = new TweenManager();
        Tween.registerAccessor(Sprite.class, new SpriteAccessor());

        back = new Sprite(new Texture(Gdx.files.internal("Sprites/Background/splash_back.png")));
        back.setSize(1280, 720);

        icon = new Sprite(new Texture(Gdx.files.internal("Sprites/Background/icon.png")));
        icon.setPosition((1280 - icon.getTexture().getWidth()) / 2f,
                (720 - icon.getTexture().getHeight()) / 2f + 100
        );

        loading = new Sprite(new Texture(Gdx.files.internal("Sprites/Background/loading.png")));
        loading.setPosition((1280 - loading.getTexture().getWidth()) / 2f,
                (720 - loading.getTexture().getHeight() - icon.getTexture().getHeight()) / 2f
        );

        Tween.set(icon, SpriteAccessor.ALPHA).target(0).start(tweenManager);
        Tween.set(loading, SpriteAccessor.ALPHA).target(0).start(tweenManager);

        Tween.to(icon, SpriteAccessor.ALPHA, 2.5f).target(1).start(tweenManager);
        Tween.to(loading, SpriteAccessor.ALPHA, 2.5f).target(1).start(tweenManager);

        GameAssets assetManager = new GameAssets();
        screenLoader.setAssetManager(assetManager);
    }

    @Override
    public void render(float delta) {
        tweenManager.update(delta);
        getBatch().setProjectionMatrix(camera.combined);

        getBatch().begin();
        back.draw(getBatch());
        icon.draw(getBatch());
        loading.draw(getBatch());
        getBatch().end();

        if (screenLoader.getAssetManager().update()) {
            MusicManager musicManager = new MusicManager();
            ScreenLoader.setMusicManager(musicManager);

            Gdx.input.setCatchKey(Input.Keys.BACK, true);

            SkyBox skyBox = new SkyBox(
                    (Texture) screenLoader.getAssetManager().get("Sprites/SkyBox/Right.png"),
                    (Texture) screenLoader.getAssetManager().get("Sprites/SkyBox/Left.png"),
                    (Texture) screenLoader.getAssetManager().get("Sprites/SkyBox/Bottom.png"),
                    (Texture) screenLoader.getAssetManager().get("Sprites/SkyBox/Front.png"),
                    (Texture) screenLoader.getAssetManager().get("Sprites/SkyBox/Back.png")
            );
            screenLoader.setSkyBox(skyBox);

            Skin gameSkin = screenLoader.getAssetManager().get("GameSkin/GameSkin.json");
            ScreenLoader.setGameSkin(gameSkin);

            MainMenuScreen mainMenu = new MainMenuScreen(screenLoader);
            screenLoader.setMainMenu(mainMenu);

            screenLoader.setScreen(new MainMenuScreen(screenLoader));
        }
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
        back.getTexture().dispose();
        icon.getTexture().dispose();
        loading.getTexture().dispose();
    }
}
