package com.programmers.network;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;
import com.programmers.enums.Difficulty;
import com.programmers.enums.Direction;
import com.programmers.game_objects.Car;
import com.programmers.game_objects.Chunk;
import com.programmers.game_objects.Life;

public final class GameNetwork {

    public static final int TCP_PORT = 54555;
    public static final int UDP_PORT = 54777;
    public static final int BUF = (int)Math.pow(2, 20);

    static void register(EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();
        kryo.register(Disconnect.class);
        kryo.register(PlayersCount.class);
        kryo.register(Ready.class);
        kryo.register(LoadGame.class);
        //
        kryo.register(GameObject.class);
        kryo.register(Chunk.class);
        kryo.register(Chunk[].class);
        kryo.register(Chunk[][].class);
        kryo.register(Base.class);
        kryo.register(Life.class);
        kryo.register(Lift.class);
        kryo.register(Wall.class);
        //
        kryo.register(Color.class);
        kryo.register(Car.Color.class);
        kryo.register(Difficulty.class);
        kryo.register(com.programmers.game_objects.Life.Type.class);
        kryo.register(Vector3.class);
        kryo.register(Direction.class);
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

    static abstract private class GameObject {
        public int x, y, z;
        public String modelFileName;
    }

    static public class Chunk extends GameObject {
        public Color color;
    }

    static public final class Base extends Chunk {
        public Car.Color baseColor;
    }

    static public final class Life extends GameObject {
        public com.programmers.game_objects.Life.Type type;
    }

    static public final class Lift extends Chunk { }

    static public final class Wall extends GameObject {
        public Vector3 offset;
        public Direction direction;
    }
}
