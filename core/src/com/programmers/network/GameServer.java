package com.programmers.network;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.programmers.game.Player;
import com.programmers.network.GameNetwork.TestMessage;

import java.io.IOException;

public class GameServer extends Server {

    public GameServer() throws IOException {
        GameNetwork.register(this);

        addListener(new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                if (object instanceof TestMessage) {
                    TestMessage testMessage = (TestMessage) object;
                    Gdx.app.log("GameServer", "Got message: " + testMessage.message);
                }
            }
        });

        bind(GameNetwork.PORT, GameNetwork.PORT);
    }

    @Override
    protected Connection newConnection() {
        return new PlayerConnection();
    }

    static class PlayerConnection extends Connection {
        public Player player;
    }
}
