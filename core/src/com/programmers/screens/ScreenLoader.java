package com.programmers.screens;

import com.badlogic.gdx.Game;

public class ScreenLoader extends Game {

    public MainMenuScreen mainMenu;

    @Override
    public void create() {
        mainMenu = new MainMenuScreen(this);

        setScreen(mainMenu);
    }
}
