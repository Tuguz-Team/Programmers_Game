package com.mygdx.game;

import com.badlogic.gdx.utils.Array;

import static com.badlogic.gdx.math.MathUtils.random;

class GameController {

    private Player thisPlayer;
    private Player[] players;
    private Field field;
    private Array<Card> cards;

    GameController(final Player[] players, final Field field) {
        this.players = players;
        this.field = field;
        thisPlayer = players[0];
        cards = new Array<>(36);
        for (int i = 0; i < 6; i++)
            cards.add(new Card(Card.Type.StepForward));
        for (int i = 0; i < 6; i++)
            cards.add(new Card(Card.Type.StepForwardToFloor));
        for (int i = 0; i < 6; i++)
            cards.add(new Card(Card.Type.Jump));
        for (int i = 0; i < 6; i++)
            cards.add(new Card(Card.Type.Turn90Left));
        for (int i = 0; i < 6; i++)
            cards.add(new Card(Card.Type.Turn90Right));
        for (int i = 0; i < 6; i++)
            cards.add(new Card(Card.Type.Turn180));
        // Add Cycles and Teleports
        if (ProgrammersGame.difficulty == Difficulty.Hard) {
            cards.ensureCapacity(16);
            for (int i = 0; i < 5; i++)
                cards.add(new Card(Card.Type.Cycle2));
            for (int i = 0; i < 5; i++)
                cards.add(new Card(Card.Type.Cycle3));
            for (int i = 0; i < 6; i++)
                cards.add(new Card(Card.Type.Teleport));
        }
        // Add 5 cards to each Player
        for (Player player : players) {
            for (int i = 0; i < 5; i++) {
                int index = random.nextInt(players.length);
                player.getCards().add(cards.get(index));
                cards.removeIndex(index);
            }
        }
    }

    Player nextPlayer() {
        int i;
        for (i = 0; i < players.length; i++) {
            if (thisPlayer.equals(players[i]))
                break;
        }
        return players[(i + 1) % players.length];
    }

    Player[] getPlayers() {
        return players;
    }

    Player getWinner() {
        for (Player player : players) {
            if ((ProgrammersGame.difficulty == Difficulty.Easy && player.getScore() >= 7)
                    || (ProgrammersGame.difficulty == Difficulty.Hard && player.getScore() >= 9)) {
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
            if (players[i].getScore() > winner.getScore()) {
                winner = players[i];
            }
        }
        return winner;
    }
}
