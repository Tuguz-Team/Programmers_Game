package com.programmers.interfaces;

import com.programmers.enums.Difficulty;

import java.util.LinkedList;

public interface SpecificCode {

    void registerAnon();

    boolean createNewRoom(String name, int playersCount, Difficulty difficulty);

    void deleteRoom(String name);

    void addPlayerToRoom(Room room);

    void removePlayerFromRoom(Room room);

    LinkedList<Room> findRooms();

    void setListener(Room room, Procedure procedure);

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
}
