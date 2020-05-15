package com.programmers.network;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

public final class GameNetwork {

    public static final int TCP_PORT = 54555, UDP_PORT = 54777;

    static void register(EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();
        kryo.register(Disconnect.class);
        kryo.register(PlayersCount.class);
        kryo.register(GameObjectMessage.class);
    }

    static public class PlayersCount {
        public int playersCount;
    }

    static public class Disconnect { }

    static public class GameObjectMessage {
        public int x, y, z;
        public Vector3 position;
        public String modelFileName;
        public Color diffuseColor;
    }
}
