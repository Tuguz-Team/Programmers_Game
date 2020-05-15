package com.programmers.network;

import com.esotericsoftware.kryonet.Client;
import com.programmers.network.GameNetwork.Disconnect;

import java.io.IOException;
import java.net.InetAddress;

import static com.programmers.network.GameNetwork.BUF;

public final class GameClient extends Client {

    public GameClient() throws IOException {
        GameNetwork.register(this);
    }

    public boolean connectByUDP() throws IOException {
        InetAddress inetAddress = discoverHost(GameNetwork.UDP_PORT, 1000);
        if (inetAddress != null) {
            connect(1000, inetAddress.getHostAddress(), GameNetwork.TCP_PORT, GameNetwork.UDP_PORT);
            return true;
        }
        return false;
    }

    public void disconnect() {
        sendTCP(new Disconnect());
        stop();
    }
}
