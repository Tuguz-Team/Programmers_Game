package com.programmers.network;

import com.badlogic.gdx.math.Vector3;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;
import com.programmers.game_objects.GameObject;

public final class GameNetwork {

    public static final int TCP_PORT = 54555, UDP_PORT = 54777;

    static void register(EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();
        kryo.register(TestMessage.class);
        kryo.register(GameObjectMessage.class);
    }

    static public class TestMessage {
        private String message;

        public TestMessage(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

    static public class GameObjectMessage {

        private final int x, y, z;
        private final Vector3 position;
        private final String modelFileName;

        public GameObjectMessage(GameObject gameObject) {
            x = gameObject.getX();
            y = gameObject.getY();
            z = gameObject.getZ();
            position = gameObject.getModelInstance().transform.getTranslation(new Vector3());
            modelFileName = gameObject.getModelFileName();
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getZ() {
            return z;
        }

        public Vector3 getPosition() {
            return position;
        }

        public String getModelFileName() {
            return modelFileName;
        }
    }
}
