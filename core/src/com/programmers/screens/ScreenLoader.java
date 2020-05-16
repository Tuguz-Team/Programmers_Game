package com.programmers.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.programmers.game.GameAssets;
import com.programmers.network.GameClient;
import com.programmers.network.GameServer;

import java.io.IOException;
import java.util.Random;

public final class ScreenLoader extends Game {

    private MainMenuScreen mainMenu;
    private AssetManager assetManager;

    private static Skin defaultGdxSkin;
    public static long seed = System.nanoTime();
    public final static Random random = new Random(seed);

    private Skin buttonSkin;
    private BitmapFont font;
    private ImageTextButton.ImageTextButtonStyle buttonStyle;

    @Override
    public void create() {
        Gdx.input.setCatchKey(Input.Keys.BACK, true);

        assetManager = new GameAssets();
        while (!assetManager.update());

        defaultGdxSkin = assetManager.get("uiskin.json");
        buttonSkin = new Skin();

        final TextureAtlas textureAtlas = assetManager.get("buttons.pack");
        buttonSkin.addRegions(textureAtlas);

        final Texture fontTexture = assetManager.get("CustomFont.png");
        font = new BitmapFont(Gdx.files.internal("CustomFont.fnt"),
                new TextureRegion(fontTexture), false);

        buttonStyle = new ImageTextButton.ImageTextButtonStyle();
        buttonStyle.up = buttonSkin.getDrawable("start_button");
        buttonStyle.down = buttonSkin.getDrawable("exit_button");
        buttonStyle.font = font;

        mainMenu = new MainMenuScreen(this);
        setScreen(mainMenu);
    }

    @Override
    public void dispose() {
        mainMenu.dispose();
        buttonSkin.dispose();
        font.dispose();
        assetManager.dispose();
        super.dispose();
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }

    public static Skin getDefaultGdxSkin() {
        return defaultGdxSkin;
    }

    public MainMenuScreen getMainMenu() {
        return mainMenu;
    }

    public ImageTextButton.ImageTextButtonStyle getButtonStyle() {
        return buttonStyle;
    }
}
