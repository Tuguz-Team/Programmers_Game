package com.programmers.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class ScreenLoader extends Game {

    private MainMenuScreen mainMenu;

    private Skin buttonSkin;
    private BitmapFont font;
    private ImageTextButton.ImageTextButtonStyle buttonStyle;

    @Override
    public void create() {
        buttonSkin = new Skin();
        buttonSkin.addRegions(new TextureAtlas("buttons.pack"));
        final Texture fontTexture = new Texture(Gdx.files.internal("CustomFont.png"));
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
        super.dispose();
    }

    public MainMenuScreen getMainMenu() {
        return mainMenu;
    }

    public ImageTextButton.ImageTextButtonStyle getButtonStyle() {
        return buttonStyle;
    }
}
