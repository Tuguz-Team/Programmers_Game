package com.programmers.network;

import com.badlogic.gdx.math.Vector3;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

public final class GameNetwork {

    public static final int TCP_PORT = 54555, UDP_PORT = 54777;

    static void register(EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();
        kryo.register(TestMessage.class);
        kryo.register(GameObjectMessage.class);
    }

    static public class TestMessage {
        public String message;
    }

    static public class GameObjectMessage {
        public int x, y, z;
        public Vector3 position;
        public String modelFileName;
    }
}
