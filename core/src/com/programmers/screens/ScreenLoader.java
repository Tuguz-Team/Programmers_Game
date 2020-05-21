package com.programmers.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.programmers.interfaces.NetworkManager;
import com.programmers.game.GameAssets;
import com.programmers.game.SkyBox;

public final class ScreenLoader extends Game {

    private MainMenuScreen mainMenu;
    private AssetManager assetManager;
    private SkyBox skyBox;

    private static Skin defaultGdxSkin;
    public final NetworkManager networkManager;

    private Skin buttonSkin;
    private BitmapFont font;
    private static ImageTextButton.ImageTextButtonStyle buttonStyle;

    public ScreenLoader(final NetworkManager networkManager) {
        this.networkManager = networkManager;
    }

    @Override
    public void create() {
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

        defaultGdxSkin = assetManager.get("GameSkin/GameSkin.json");
        buttonSkin = new Skin();

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("GameFont.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter
                = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 36;
        parameter.color = new Color(50 / 255f, 195 / 255f, 195 / 255f, 1f);
        parameter.borderColor = new Color(40 / 255f, 140 / 255f, 140 / 255f, 1f);
        parameter.borderWidth = parameter.shadowOffsetX = parameter.shadowOffsetY = 3;
        font = generator.generateFont(parameter);

        final TextureAtlas textureAtlas = assetManager.get("buttons.pack");
        buttonSkin.addRegions(textureAtlas);

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
        skyBox.dispose();
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

    public SkyBox getSkyBox() {
        return skyBox;
    }
}
