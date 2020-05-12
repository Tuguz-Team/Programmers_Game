package com.programmers.network;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.programmers.network.GameNetwork.TestMessage;

import java.io.IOException;
import java.net.InetAddress;

public class GameClient extends Client {

    public GameClient() throws IOException {
        GameNetwork.register(this);

        addListener(new Listener() {
            @Override
            public void connected(Connection connection) {
                Gdx.app.log("GameClient", "Connected with TCP port: " + GameNetwork.PORT);
            }

            @Override
            public void received(Connection connection, Object object) {
                if (object instanceof TestMessage) {
                    TestMessage testMessage = (TestMessage) object;
                    Gdx.app.log("GameClient", "Got message: " + testMessage.message);
                }
            }

            @Override
            public void disconnected(Connection connection) {
                Gdx.app.log("GameClient", "Disconnected!");
            }
        });
    }

    public void connectByUDP() throws IOException {
        InetAddress inetAddress = discoverHost(GameNetwork.PORT, 1000);
        if (inetAddress != null)
            connect(1000, inetAddress.getHostAddress(), GameNetwork.PORT);
    }
}
