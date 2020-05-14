package com.programmers.game.online;

import com.programmers.enums.Difficulty;
import com.programmers.screens.GameScreen;
import com.programmers.screens.ScreenLoader;

public class OnlineGameServer extends GameScreen {

    protected OnlineGameServer(ScreenLoader screenLoader, Difficulty difficulty, int playersCount) {
        super(screenLoader, difficulty, playersCount);

        constructorEnd();
    }

    @Override
    protected void setCameraPosition() {

    }

    @Override
    protected void addCardWindows() {

    }

    @Override
    protected void loadModels() {

    }
}
