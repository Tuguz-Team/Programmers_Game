package com.programmers.network;

import com.esotericsoftware.kryonet.Server;

import java.io.IOException;

import static com.programmers.network.GameNetwork.BUF;

public final class GameServer extends Server {

    public GameServer() throws IOException {
        super(BUF, BUF);
        GameNetwork.register(this);
        bind(GameNetwork.TCP_PORT, GameNetwork.UDP_PORT);
    }
}
