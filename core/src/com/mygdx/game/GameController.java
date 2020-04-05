package com.mygdx.game;

class GameController {

    private Player[] players;

    Player[] getPlayers() {
        return players;
    }

    GameController(final Player[] players) {
        this.players = players;
    }
}
