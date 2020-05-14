package com.programmers.game.online;

import com.programmers.enums.Difficulty;
import com.programmers.network.GameServer;
import com.programmers.screens.GameScreen;
import com.programmers.screens.ScreenLoader;

public final class OnlineGameServer extends GameScreen {

    private final GameServer gameServer;

    public OnlineGameServer(final ScreenLoader screenLoader, final Difficulty difficulty,
                               final int playersCount, final GameServer gameServer) {
        super(screenLoader, difficulty, playersCount);

        this.gameServer = gameServer;

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

    @Override
    public void dispose() {
        gameServer.close();
        super.dispose();
    }
}
