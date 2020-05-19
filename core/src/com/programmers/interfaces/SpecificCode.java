package com.programmers.interfaces;

import com.badlogic.gdx.graphics.Color;
import com.programmers.enums.Difficulty;
import com.programmers.enums.Direction;
import com.programmers.game_objects.Base;
import com.programmers.game_objects.Car;
import com.programmers.game_objects.Chunk;
import com.programmers.game_objects.Wall;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public interface SpecificCode {

    void registerAnon();

    boolean createNewRoom(String name, int playersCount, Difficulty difficulty);

    boolean roomExists(String name);

    void deleteRoom(String name);

    void sendFieldData(Room room, com.programmers.game.Field field);

    Field getFieldData(Room room);

    boolean addPlayerToRoom(Room room);

    boolean removePlayerFromRoom(Room room);

    LinkedList<Room> findRooms();

    void addRoomChangedListener(Room room, Procedure procedure);

    void removeListener(Room room);

    final class Room {
        private String name;
        private int nowPlayers = 1;
        private int playersCount;
        private Difficulty difficulty;

        public Room() { }

        public Room(String name, int playersCount, Difficulty difficulty) {
            this.name = name;
            this.playersCount = playersCount;
            this.difficulty = difficulty;
        }

        public String getName() {
            return name;
        }

        public int getPlayersCount() {
            return playersCount;
        }

        public void setNowPlayers(int nowPlayers) {
            this.nowPlayers = nowPlayers;
        }

        public Difficulty getDifficulty() {
            return difficulty;
        }

        public int getNowPlayers() {
            return nowPlayers;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setPlayersCount(int playersCount) {
            this.playersCount = playersCount;
        }

        public void setDifficulty(Difficulty difficulty) {
            this.difficulty = difficulty;
        }
    }

    final class Field {
        private int size;
        private List<Chunk> chunks;
        private List<Life> lives;
        private List<Wall> walls;
        private List<Base> bases;

        public Field() { }

        public Field(com.programmers.game.Field field) {
            size = field.getSize();
            chunks = new LinkedList<>();
            lives = new LinkedList<>();
            walls = new LinkedList<>();
            for (com.programmers.game_objects.Chunk[] chunks : field.getChunks()) {
                for (com.programmers.game_objects.Chunk chunk : chunks) {
                    this.chunks.add(new Chunk(chunk));
                    for (com.programmers.game_objects.Life life : chunk.getLives()) {
                        this.lives.add(new Life(life));
                    }
                    if (chunk.getWallBack() != null) walls.add(new Wall(chunk.getWallBack()));
                    if (chunk.getWallForward() != null) walls.add(new Wall(chunk.getWallForward()));
                    if (chunk.getWallLeft() != null) walls.add(new Wall(chunk.getWallLeft()));
                    if (chunk.getWallRight() != null) walls.add(new Wall(chunk.getWallRight()));
                }
            }
            bases = Arrays.asList(new Base((com.programmers.game_objects.Base) field.getChunks()[0][0]),
                    new Base((com.programmers.game_objects.Base) field.getChunks()[0][size - 1]),
                    new Base((com.programmers.game_objects.Base) field.getChunks()[size - 1][0]),
                    new Base((com.programmers.game_objects.Base) field.getChunks()[size - 1][size - 1]));
        }

        public int getSize() {
            return size;
        }

        public List<Chunk> getChunks() {
            return chunks;
        }

        public List<Life> getLives() {
            return lives;
        }

        public List<Wall> getWalls() {
            return walls;
        }

        public List<Base> getBases() {
            return bases;
        }

        public abstract static class GameObject {
            private int x, y, z;

            public GameObject() { }

            public GameObject(com.programmers.game_objects.GameObject gameObject) {
                x = gameObject.getX();
                y = gameObject.getY();
                z = gameObject.getZ();
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
        }

        public static class Chunk extends Field.GameObject {
            private Color color;

            public Chunk() { }

            public Chunk(com.programmers.game_objects.Chunk chunk) {
                super(chunk);
                color = chunk.getColor();
            }

            public Color getColor() {
                return color;
            }
        }

        public static class Base extends GameObject {
            private Car.Color baseColor;

            public Base() { }

            public Base(com.programmers.game_objects.Base base) {
                super(base);
                baseColor = base.getBaseColor();
            }

            public Car.Color getBaseColor() {
                return baseColor;
            }
        }

        public static class Life extends Field.GameObject {
            private com.programmers.game_objects.Life.Type type;

            public Life() { }

            public Life (com.programmers.game_objects.Life life) {
                super(life);
                type = life.getType();
            }

            public com.programmers.game_objects.Life.Type getType() {
                return type;
            }
        }

        public static class Wall extends GameObject {
            private Direction direction;

            public Wall() { }

            public Wall(com.programmers.game_objects.Wall wall) {
                super(wall);
                direction = wall.getDirection();
            }

            public Direction getDirection() {
                return direction;
            }
        }
    }
}
