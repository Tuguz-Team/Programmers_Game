package com.mygdx.game;

class GameController {

    private Player[] players;
    private Field field;

    GameController(final Player[] players, final Field field) {
        this.players = players;
        this.field = field;
    }

    Player[] getPlayers() {
        return players;
    }

    Player getWinner() {
        for (Player player : players) {
            if (player.getLives().size >= 7) {
                return player;
            }
        }
        for (Chunk[] chunks : field.getChunks()) {
            for (Chunk chunk : chunks) {
                if (!chunk.getLives().isEmpty()) {
                    return null;
                }
            }
        }
        for (Player player : players) {
            if (!player.getCar().getLives().isEmpty()) {
                return null;
            }
        }
        Player winner = players[0];
        for (int i = 1; i < players.length; i++) {
            if (players[i].getLives().size > winner.getLives().size) {
                winner = players[i];
            }
        }
        return winner;
    }
}
