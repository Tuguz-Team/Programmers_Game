package com.programmers.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.programmers.ui_elements.MyButton;

public class ChooseGameScreen extends ReturnableScreen {

    public ChooseGameScreen(final ScreenLoader screenLoader, Screen previousScreen) {
        super(screenLoader, previousScreen);

        VerticalGroup buttons = new VerticalGroup();
        buttons.setFillParent(true);
        addActor(buttons);

        ImageTextButton hotseatGame = new MyButton("CREATE HOTSEAT GAME", screenLoader.getButtonStyle()) {
            @Override
            public void call() {
                screenLoader.setScreen(new NewGameScreen(screenLoader, ChooseGameScreen.this, true));
            }
        };
        ImageTextButton createNewGame = new MyButton("CREATE NEW SERVER GAME !", screenLoader.getButtonStyle()) {
            @Override
            public void call() {
                screenLoader.setScreen(new NewGameScreen(screenLoader, ChooseGameScreen.this, false));
            }
        };
        ImageTextButton connectToGame = new MyButton("CONNECT TO EXISTING GAME !", screenLoader.getButtonStyle()) {
            @Override
            public void call() {
                screenLoader.setScreen(new ConnectGameScreen(screenLoader, ChooseGameScreen.this));
            }
        };
        buttons.addActor(hotseatGame);
        buttons.addActor(createNewGame);
        buttons.addActor(connectToGame);

        buttons.space(0.05f * Gdx.graphics.getHeight());
        buttons.center();
    }
}
