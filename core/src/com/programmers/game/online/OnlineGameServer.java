package com.programmers.game.online;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.programmers.enums.Difficulty;
import com.programmers.game.hotseat.HotseatGame;
import com.programmers.network.GameNetwork;
import com.programmers.network.GameNetwork.Disconnect;
import com.programmers.network.GameServer;
import com.programmers.screens.ScreenLoader;

import java.io.IOException;

import static com.programmers.screens.ScreenLoader.seed;

public final class OnlineGameServer extends HotseatGame {

    private final GameServer gameServer;

    public OnlineGameServer(final ScreenLoader screenLoader, final Difficulty difficulty,
                            final int playersCount, final GameServer gameServer) {
        super(screenLoader, difficulty, playersCount);
        this.gameServer = gameServer;

        gameServer.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                if (object instanceof Disconnect) {
                    try {
                        gameServer.update(0);
                    } catch (IOException ignored) { }
                    if (gameServer.getConnections().length == 0) {
                        exit();
                    }
                }
            }
        });

        GameNetwork.Seed seedNet = new GameNetwork.Seed();
        seedNet.seed = seed;
        gameServer.sendToAllTCP(seed);
    }

    @Override
    public void dispose() {
        gameServer.stop();
        super.dispose();
    }
}
