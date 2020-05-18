package com.programmers.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.programmers.interfaces.SpecificCode;
import com.programmers.game.GameAssets;
import com.programmers.game.SkyBox;

import static com.badlogic.gdx.math.MathUtils.random;

import java.util.Random;

public final class ScreenLoader extends Game {

    private MainMenuScreen mainMenu;
    private AssetManager assetManager;
    private SkyBox skyBox;

    private static Skin defaultGdxSkin;
    public static long seed = new Random().nextLong();
    public final SpecificCode specificCode;

    private Skin buttonSkin;
    private BitmapFont font;
    private static ImageTextButton.ImageTextButtonStyle buttonStyle;

    public ScreenLoader(final SpecificCode specificCode) {
        this.specificCode = specificCode;
    }

    @Override
    public void create() {
        Gdx.input.setCatchKey(Input.Keys.BACK, true);
        random.setSeed(seed);

        assetManager = new GameAssets();
        while (!assetManager.update());

        skyBox = new SkyBox(
                (Texture) assetManager.get("Sprites/SkyBox/Right.png"),
                (Texture) assetManager.get("Sprites/SkyBox/Left.png"),
                (Texture) assetManager.get("Sprites/SkyBox/Bottom.png"),
                (Texture) assetManager.get("Sprites/SkyBox/Front.png"),
                (Texture) assetManager.get("Sprites/SkyBox/Back.png")
        );

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

    public static ImageTextButton.ImageTextButtonStyle getButtonStyle() {
        return buttonStyle;
    }

    public SkyBox getSkyBox() {
        return skyBox;
    }
}
