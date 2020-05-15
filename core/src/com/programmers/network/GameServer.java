package com.programmers.network;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;

import java.io.IOException;

public final class GameServer extends Server {

    public GameServer() throws IOException {
        GameNetwork.register(this);

        //addListener(new Listener());

        bind(GameNetwork.TCP_PORT, GameNetwork.UDP_PORT);
    }

    @Override
    protected Connection newConnection() {
        return new PlayerConnection();
    }

    static class PlayerConnection extends Connection { }
}
