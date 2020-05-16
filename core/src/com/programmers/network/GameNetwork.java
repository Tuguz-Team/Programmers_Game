package com.programmers.network;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;
import com.programmers.enums.Difficulty;

public final class GameNetwork {

    public static final int TCP_PORT = 54555;
    public static final int UDP_PORT = 54777;
    public static final int BUF = (int)Math.pow(2, 16);

    static void register(EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();
        kryo.register(Seed.class);
        kryo.register(Disconnect.class);
        kryo.register(PlayersCount.class);
        kryo.register(Ready.class);
        kryo.register(LoadGame.class);
        kryo.register(Difficulty.class);
    }

    static public final class Seed {
        public long seed;
    }

    static public final class PlayersCount {
        public int playersCount;
    }

    static public final class Disconnect { }

    static public final class Ready { }

    static public final class LoadGame {
        public Difficulty difficulty;
        public int playersCount;
    }
}
