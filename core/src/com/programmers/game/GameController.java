package com.programmers.game;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;

import com.programmers.enums.CardType;
import com.programmers.enums.Difficulty;
import com.programmers.game_objects.Chunk;
import com.programmers.ui_elements.AlgorithmCardWindow;
import com.programmers.ui_elements.Card;
import com.programmers.ui_elements.CardContainer;
import com.programmers.ui_elements.PlayerCardWindow;

import java.util.Stack;

import static com.badlogic.gdx.math.MathUtils.random;

public final class GameController {

    private Player thisPlayer;
    private final Player[] players;
    private final Field field;
    private final Difficulty difficulty;

    private final PlayerCardWindow playerCardWindow;
    private final AlgorithmCardWindow algorithmCardWindow;

    private final Array<GameCard> algorithmCards;
    private final Array<GameCard> discardPile;
    private final Stack<GameCard> talon = new Stack<>();

    public GameController(final Player[] players, final Field field) {
        this.players = players;
        this.field = field;
        thisPlayer = players[0];
        algorithmCards = new Array<>();
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
        //
        CardContainer playerCardContainer = new CardContainer(
                thisPlayer.getGameCards(),
                CardContainer.Content.All, true
        );
        playerCardWindow = new PlayerCardWindow(
                "Player cards", playerCardContainer, this
        );
        algorithmCardWindow = new AlgorithmCardWindow("Algorithm", this);
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

    public Array<GameCard> getAlgorithmCards() {
        return algorithmCards;
    }

    public Stack<GameCard> getTalon() {
        return talon;
    }

    public Player getThisPlayer() {
        return thisPlayer;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void toNextPlayer() {
        Array<GameCard> gameCards = thisPlayer.getGameCards();
        gameCards.clear();
        for (Actor actor : playerCardWindow.getCardContainer().getChildren()) {
            gameCards.add(((Card) actor).getGameCard());
        }
        while (gameCards.size < 5) {
            gameCards.add(talon.pop());
        }
        playerCardWindow.getCardContainer().clearChildren();
        //
        int i;
        for (i = 0; i < players.length; i++) {
            if (thisPlayer.equals(players[i]))
                break;
        }
        thisPlayer = players[(i + 1) % players.length];
        //
        for (GameCard gameCard : thisPlayer.getGameCards()) {
            playerCardWindow.getCardContainer().addCard(new Card(gameCard));
        }
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

    public PlayerCardWindow getPlayerCardWindow() {
        return playerCardWindow;
    }

    public AlgorithmCardWindow getAlgorithmCardWindow() {
        return algorithmCardWindow;
    }
}
