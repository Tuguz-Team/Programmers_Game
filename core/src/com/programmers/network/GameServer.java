package com.programmers.network;

import com.esotericsoftware.kryonet.Server;

import java.io.IOException;

public final class GameServer extends Server {

    public GameServer() throws IOException {
        GameNetwork.register(this);
        bind(GameNetwork.TCP_PORT, GameNetwork.UDP_PORT);
    }
}
