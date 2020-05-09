package com.programmers.game;

import com.badlogic.gdx.utils.Array;

import com.programmers.enums.CardType;
import com.programmers.enums.Difficulty;
import com.programmers.game_objects.Chunk;

import java.util.Stack;

import static com.badlogic.gdx.math.MathUtils.random;

public final class GameController {

    private Player thisPlayer;
    private final Player[] players;
    private final Field field;
    private final Difficulty difficulty;

    private final Array<GameCard> algorithm;
    private final Array<GameCard> discardPile;
    private final Stack<GameCard> talon = new Stack<>();

    public GameController(final Player[] players, final Field field) {
        this.players = players;
        this.field = field;
        thisPlayer = players[0];
        algorithm = new Array<>();
        // Use discardPile as place where cards are initializing
        difficulty = field.getGameScreen().getDifficulty();
        int cardsCount = difficulty == Difficulty.Easy ? 36 : 52;
        discardPile = new Array<>(cardsCount);
        for (int i = 0; i < 6; i++) {
            discardPile.add(new GameCard(CardType.StepForward, null));
            discardPile.add(new GameCard(CardType.StepForwardToFloor, null));
            discardPile.add(new GameCard(CardType.Jump, null));
            discardPile.add(new GameCard(CardType.Turn90Left, null));
            discardPile.add(new GameCard(CardType.Turn90Right, null));
            discardPile.add(new GameCard(CardType.Turn180, null));
        }
        // Add Cycles and Teleports
        if (difficulty == Difficulty.Hard) {
            for (int i = 0; i < 5; i++) {
                discardPile.add(new GameCard(CardType.Cycle2, null));
                discardPile.add(new GameCard(CardType.Cycle3, null));
            }
            for (int i = 0; i < 6; i++)
                discardPile.add(new GameCard(CardType.Teleport, null));
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

    public void makeTalon() {
        while (!discardPile.isEmpty()) {
            final int i = random.nextInt(discardPile.size);
            talon.push(discardPile.get(i));
            discardPile.removeIndex(i);
        }
    }

    public Array<GameCard> getDiscardPile() {
        return discardPile;
    }

    public Array<GameCard> getAlgorithm() {
        return algorithm;
    }

    public Player getThisPlayer() {
        return thisPlayer;
    }

    public Difficulty getDifficulty() {
        return difficulty;
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
            if ((difficulty == Difficulty.Easy && player.getScore() >= 7)
                    || (difficulty == Difficulty.Hard && player.getScore() >= 9)) {
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
