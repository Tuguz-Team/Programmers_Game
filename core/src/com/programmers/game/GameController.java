package com.programmers.game;

import com.badlogic.gdx.utils.Array;

import com.programmers.enums.Difficulty;
import com.programmers.game_objects.Chunk;

import static com.badlogic.gdx.math.MathUtils.random;

public final class GameController {

    private Player thisPlayer;
    private final Player[] players;
    private final Field field;
    private final Array<Card> cards;

    public GameController(final Player[] players, final Field field) {
        this.players = players;
        this.field = field;
        thisPlayer = players[0];
        cards = new Array<>(36);
        for (int i = 0; i < 6; i++)
            cards.add(new Card(Card.Type.StepForward, null));
        for (int i = 0; i < 6; i++)
            cards.add(new Card(Card.Type.StepForwardToFloor, null));
        for (int i = 0; i < 6; i++)
            cards.add(new Card(Card.Type.Jump, null));
        for (int i = 0; i < 6; i++)
            cards.add(new Card(Card.Type.Turn90Left, null));
        for (int i = 0; i < 6; i++)
            cards.add(new Card(Card.Type.Turn90Right, null));
        for (int i = 0; i < 6; i++)
            cards.add(new Card(Card.Type.Turn180, null));
        // Add Cycles and Teleports
        if (field.getGameScreen().getDifficulty() == Difficulty.Hard) {
            cards.ensureCapacity(16);
            for (int i = 0; i < 5; i++)
                cards.add(new Card(Card.Type.Cycle2, null));
            for (int i = 0; i < 5; i++)
                cards.add(new Card(Card.Type.Cycle3, null));
            for (int i = 0; i < 6; i++)
                cards.add(new Card(Card.Type.Teleport, null));
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

    public Player nextPlayer() {
        int i;
        for (i = 0; i < players.length; i++) {
            if (thisPlayer.equals(players[i]))
                break;
        }
        return players[(i + 1) % players.length];
    }

    public Player[] getPlayers() {
        return players;
    }

    public Player getWinner() {
        for (Player player : players) {
            if ((field.getGameScreen().getDifficulty() == Difficulty.Easy && player.getScore() >= 7)
                    || (field.getGameScreen().getDifficulty() == Difficulty.Hard && player.getScore() >= 9)) {
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
