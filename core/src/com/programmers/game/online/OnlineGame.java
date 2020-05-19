package com.programmers.game.online;

import com.programmers.interfaces.SpecificCode;
import com.programmers.screens.GameScreen;
import com.programmers.screens.ScreenLoader;

public class OnlineGame extends GameScreen {

    private final SpecificCode.Room room;

    public OnlineGame(ScreenLoader screenLoader, SpecificCode.Room room) {
        super(screenLoader, room.getDifficulty(), room.getPlayersCount());
        this.room = room;
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
