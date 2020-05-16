package com.programmers.network;

import com.esotericsoftware.kryonet.Client;
import com.programmers.network.GameNetwork.Disconnect;

import java.io.IOException;
import java.net.InetAddress;

public final class GameClient extends Client {

    public GameClient() {
        super(GameNetwork.BUF, GameNetwork.BUF);
        GameNetwork.register(this);
    }

    public void disconnect() {
        sendTCP(new Disconnect());
        stop();
    }
}
