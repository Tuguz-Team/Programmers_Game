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

        ImageTextButton createNewGame = new MyButton("CREATE NEW GAME !", screenLoader.getButtonStyle()) {
            @Override
            public void call() {
                screenLoader.setScreen(new NewGameScreen(screenLoader, ChooseGameScreen.this));
            }
        };
        ImageTextButton connectToGame = new MyButton("CONNECT TO EXISTING GAME !", screenLoader.getButtonStyle()) {
            @Override
            public void call() {
                screenLoader.setScreen(new ConnectGameScreen(screenLoader, ChooseGameScreen.this));
            }
        };
        buttons.addActor(createNewGame);
        buttons.addActor(connectToGame);

        buttons.space(0.2f * Gdx.graphics.getHeight());
        buttons.center();
    }
}
