package com.programmers.screens;

import com.badlogic.gdx.Game;

public class ScreenLoader extends Game {

    private MainMenuScreen mainMenu;

    @Override
    public void create() {
        mainMenu = new MainMenuScreen(this);
        setScreen(mainMenu);
    }

    @Override
    public void dispose() {
        mainMenu.dispose();
        super.dispose();
    }

    public MainMenuScreen getMainMenu() {
        return mainMenu;
    }
}
