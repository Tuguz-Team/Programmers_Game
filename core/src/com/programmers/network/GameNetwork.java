package com.programmers.network;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

public class GameNetwork {
    public static final int PORT = 54555;

    static void register(EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();
        kryo.register(TestMessage.class);
    }

    static public class TestMessage {
        public String message;

        public TestMessage(String message) {
            this.message = message;
        }
    }
}
