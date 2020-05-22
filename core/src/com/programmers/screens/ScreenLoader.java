package com.programmers.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.programmers.interfaces.NetworkManager;
import com.programmers.game.GameAssets;
import com.programmers.game.SkyBox;

public final class ScreenLoader extends Game {

    private MainMenuScreen mainMenu;
    private AssetManager assetManager;
    private SkyBox skyBox;

    private static MusicManager musicManager;
    private static Skin gameSkin;
    public final NetworkManager networkManager;

    public ScreenLoader(final NetworkManager networkManager) {
        this.networkManager = networkManager;
    }

    @Override
    public void create() {
        musicManager = new MusicManager();
        Gdx.input.setCatchKey(Input.Keys.BACK, true);

        assetManager = new GameAssets();
        while (!assetManager.update());

        skyBox = new SkyBox(
                (Texture) assetManager.get("Sprites/SkyBox/Right.png"),
                (Texture) assetManager.get("Sprites/SkyBox/Left.png"),
                (Texture) assetManager.get("Sprites/SkyBox/Bottom.png"),
                (Texture) assetManager.get("Sprites/SkyBox/Front.png"),
                (Texture) assetManager.get("Sprites/SkyBox/Back.png")
        );
        gameSkin = assetManager.get("GameSkin/GameSkin.json");

        mainMenu = new MainMenuScreen(this);
        setScreen(mainMenu);
    }

    @Override
    public void dispose() {
        mainMenu.dispose();
        assetManager.dispose();
        skyBox.dispose();
        super.dispose();
    }

    public static MusicManager getMusicManager() {
        return musicManager;
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }

    public static Skin getGameSkin() {
        return gameSkin;
    }

    public MainMenuScreen getMainMenu() {
        return mainMenu;
    }

    public SkyBox getSkyBox() {
        return skyBox;
    }
}
