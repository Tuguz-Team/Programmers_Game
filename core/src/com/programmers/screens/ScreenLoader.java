package com.programmers.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.programmers.interfaces.NetworkManager;
import com.programmers.game.SkyBox;

public final class ScreenLoader extends Game {

    private MainMenuScreen mainMenu;
    private AssetManager assetManager;
    private SkyBox skyBox;
    private SplashScreen splashScreen;

    private static MusicManager musicManager;
    private static Skin gameSkin;
    public final NetworkManager networkManager;

    public ScreenLoader(final NetworkManager networkManager) {
        this.networkManager = networkManager;
    }

    @Override
    public void create() {
        splashScreen = new SplashScreen(this);
        setScreen(splashScreen);
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

    SkyBox getSkyBox() {
        return skyBox;
    }

    static void setMusicManager(MusicManager musicManager) {
        ScreenLoader.musicManager = musicManager;
    }

    void setAssetManager(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    static void setGameSkin(Skin gameSkin) {
        ScreenLoader.gameSkin = gameSkin;
    }

    void setMainMenu(MainMenuScreen mainMenu) {
        this.mainMenu = mainMenu;
    }

    void setSkyBox(SkyBox skyBox) {
        this.skyBox = skyBox;
    }
}
