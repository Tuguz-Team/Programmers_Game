package com.programmers.game.online;

import com.programmers.enums.Difficulty;
import com.programmers.network.GameClient;
import com.programmers.screens.GameScreen;
import com.programmers.screens.ScreenLoader;

public final class OnlineGameClient extends GameScreen {

    private final GameClient gameClient;

    public OnlineGameClient(final ScreenLoader screenLoader, final Difficulty difficulty,
                               final int playersCount, final GameClient gameClient) {
        super(screenLoader, difficulty, playersCount);

        this.gameClient = gameClient;

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
        gameClient.close();
        super.dispose();
    }
}
