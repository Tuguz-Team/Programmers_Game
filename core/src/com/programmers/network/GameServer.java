package com.programmers.network;

import com.esotericsoftware.kryonet.Server;

import java.io.IOException;

public final class GameServer extends Server {

    public GameServer() {
        super(GameNetwork.BUF, GameNetwork.BUF);
        GameNetwork.register(this);
        try {
            bind(GameNetwork.TCP_PORT, GameNetwork.UDP_PORT);
        } catch (IOException ignored) { }
    }
}
