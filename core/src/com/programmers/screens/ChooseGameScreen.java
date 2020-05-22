package com.programmers.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.programmers.ui_elements.MyButton;

final class ChooseGameScreen extends ReturnableScreen {

    ChooseGameScreen(final ScreenLoader screenLoader, Screen previousScreen) {
        super(screenLoader, previousScreen);

        VerticalGroup buttons = new VerticalGroup();
        buttons.setFillParent(true);
        addActor(buttons);

        TextButton hotseatGame = new MyButton("   HOTSEAT GAME   ", ScreenLoader.getGameSkin()) {
            @Override
            public void call() {
                screenLoader.setScreen(new NewGameScreen(screenLoader, ChooseGameScreen.this, true));
            }
        };
        hotseatGame.getLabel().setFontScale(2);

        TextButton createNewGame = new MyButton("   NEW ROOM   ", ScreenLoader.getGameSkin()) {
            @Override
            public void call() {
                screenLoader.setScreen(new NewGameScreen(screenLoader, ChooseGameScreen.this, false));
            }
        };
        createNewGame.getLabel().setFontScale(2);

        TextButton connectToGame = new MyButton("   CONNECT TO ROOM   ", ScreenLoader.getGameSkin()) {
            @Override
            public void call() {
                screenLoader.setScreen(new ConnectGameScreen(screenLoader, ChooseGameScreen.this));
            }
        };
        connectToGame.getLabel().setFontScale(2);

        buttons.addActor(hotseatGame);
        buttons.addActor(createNewGame);
        buttons.addActor(connectToGame);

        buttons.space(0.05f * Gdx.graphics.getHeight());
        buttons.center();
    }
}
