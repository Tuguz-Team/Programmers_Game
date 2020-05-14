package com.programmers.network;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.programmers.network.GameNetwork.TestMessage;

import java.io.IOException;

public final class GameServer extends Server {

    public GameServer() throws IOException {
        GameNetwork.register(this);

        addListener(new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                if (object instanceof TestMessage) {
                    TestMessage testMessage = (TestMessage) object;
                    Gdx.app.log("GameServer", "Got message: " + testMessage.getMessage());
                }
            }
        });

        bind(GameNetwork.TCP_PORT, GameNetwork.UDP_PORT);
    }

    @Override
    protected Connection newConnection() {
        return new PlayerConnection();
    }

    static class PlayerConnection extends Connection { }
}
