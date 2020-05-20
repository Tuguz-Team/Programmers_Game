package com.programmers.game.hotseat;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;

import com.programmers.enums.CardType;
import com.programmers.enums.Difficulty;
import com.programmers.game.Field;
import com.programmers.game.GameCard;
import com.programmers.game.GameController;
import com.programmers.game.Player;
import com.programmers.screens.GameScreen;
import com.programmers.ui_elements.Card;
import com.programmers.ui_elements.CardContainer;

import static com.badlogic.gdx.math.MathUtils.random;

public final class HotseatGameController extends GameController {

    private final Player[] players;
    private final Difficulty difficulty;

    protected HotseatGameController(final Player[] players, final Field field,
                                    final GameScreen gameScreen, final Void v) {
        super(gameScreen, field);
        this.players = players;
        difficulty = field.getGameScreen().getDifficulty();
    }

    public HotseatGameController(final Player[] players, final Field field, final GameScreen gameScreen) {
        this(players, field, gameScreen, null);
        thisPlayer = players[0];
        // Use discardPile as place where cards are initializing
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
        // Add 5 cards from talon to each player from talon
        for (Player player : players) {
            for (int i = 0; i < 5; i++) {
                GameCard gameCard = talon.pop();
                player.addCard(gameCard);
                gameCard.setPlayer(player);
            }
        }
        initContainers();
    }

    @Override
    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void toNextPlayer() {
        // add new cards to the player and clear playerCardWindow
        final Array<GameCard> gameCards = thisPlayer.getGameCards();
        final CardContainer playerCardContainer = playerCardWindow.getCardContainer();
        if (((Card) playerCardContainer.getChild(0)).getGameCard() != null) {
            gameCards.clear();
            for (Actor actor : playerCardContainer.getChildren()) {
                gameCards.add(((Card) actor).getGameCard());
            }
            while (gameCards.size < 5) {
                if (talon.empty()) {
                    makeTalon();
                }
                GameCard gameCard = talon.pop();
                gameCards.add(gameCard);
                gameCard.setPlayer(thisPlayer);
            }
            playerCardContainer.clearChildren();
        } else {
            gameCards.clear();
            while (gameCards.size < 5) {
                if (talon.empty()) {
                    makeTalon();
                }
                GameCard gameCard = talon.pop();
                gameCards.add(gameCard);
                gameCard.setPlayer(thisPlayer);
            }
        }
        // set this player
        int i;
        for (i = 0; i < players.length; i++) {
            if (thisPlayer.equals(players[i]))
                break;
        }
        thisPlayer = players[(i + 1) % players.length];
        // add this player cards to the playerCardWindow
        for (GameCard gameCard : thisPlayer.getGameCards()) {
            Card card = new Card(gameCard, gameScreen.getGameInputProcessor());
            playerCardContainer.addCard(card, 0, 0);
            gameCard.setPlayer(thisPlayer);
        }
    }

    public Player[] getPlayers() {
        return players;
    }
}
