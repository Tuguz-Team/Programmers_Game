package com.programmers.game;

import com.badlogic.gdx.utils.Array;

import com.programmers.enums.Difficulty;
import com.programmers.game_objects.Chunk;

import java.util.Stack;

import static com.badlogic.gdx.math.MathUtils.random;

final class GameController {

    private Player thisPlayer;
    private final Player[] players;
    private final Field field;

    private final int cardsCount;
    private final Array<Card> discardPile;
    private final Stack<Card> talon = new Stack<>();

    GameController(final Player[] players, final Field field) {
        this.players = players;
        this.field = field;
        thisPlayer = players[0];
        // Use discardPile as place where cards are initializing
        cardsCount = field.getProgrammersGame().getDifficulty() == Difficulty.Easy ? 36 : 52;
        discardPile = new Array<>(cardsCount);
        for (int i = 0; i < 6; i++)
            discardPile.add(new Card(Card.Type.StepForward, null));
        for (int i = 0; i < 6; i++)
            discardPile.add(new Card(Card.Type.StepForwardToFloor, null));
        for (int i = 0; i < 6; i++)
            discardPile.add(new Card(Card.Type.Jump, null));
        for (int i = 0; i < 6; i++)
            discardPile.add(new Card(Card.Type.Turn90Left, null));
        for (int i = 0; i < 6; i++)
            discardPile.add(new Card(Card.Type.Turn90Right, null));
        for (int i = 0; i < 6; i++)
            discardPile.add(new Card(Card.Type.Turn180, null));
        // Add Cycles and Teleports
        if (field.getProgrammersGame().getDifficulty() == Difficulty.Hard) {
            for (int i = 0; i < 5; i++)
                discardPile.add(new Card(Card.Type.Cycle2, null));
            for (int i = 0; i < 5; i++)
                discardPile.add(new Card(Card.Type.Cycle3, null));
            for (int i = 0; i < 6; i++)
                discardPile.add(new Card(Card.Type.Teleport, null));
        }
        // Make a talon of cards
        makeTalon();
        // Add 5 cards from talon to each player
        for (Player player : players) {
            for (int i = 0; i < 5; i++) {
                player.addCard(talon.pop());
            }
        }
    }

    private void makeTalon() {
        while (!discardPile.isEmpty()) {
            final int i = random.nextInt(discardPile.size);
            talon.push(discardPile.get(i));
            discardPile.removeIndex(i);
        }
    }

    private void discardThisPlayerCards() {
        for (Card card : thisPlayer.getCards()) {
            card.setPlayer(null);
            discardPile.add(card);
        }
        thisPlayer.getCards().clear();
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
            if ((field.getProgrammersGame().getDifficulty() == Difficulty.Easy && player.getScore() >= 7)
                    || (field.getProgrammersGame().getDifficulty() == Difficulty.Hard && player.getScore() >= 9)) {
                return player;
            }
        }
        for (Chunk[] chunks : field.getChunks()) {
            for (Chunk chunk : chunks) {
                if (!chunk.getLives().isEmpty())
                    return null;
            }
        }
        for (Player player : players) {
            if (!player.getCar().getLives().isEmpty())
                return null;
        }
        Player winner = players[0];
        for (int i = 1; i < players.length; i++) {
            if (players[i].getScore() > winner.getScore())
                winner = players[i];
        }
        return winner;
    }
}
