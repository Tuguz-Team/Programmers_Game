package com.programmers.game.hotseat;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;

import com.programmers.enums.CardType;
import com.programmers.enums.Difficulty;
import com.programmers.game.Field;
import com.programmers.game.GameCard;
import com.programmers.game_objects.Chunk;
import com.programmers.ui_elements.AlgorithmCardWindow;
import com.programmers.ui_elements.Card;
import com.programmers.ui_elements.CardContainer;
import com.programmers.ui_elements.PlayerCardWindow;

import java.util.Stack;

import static com.badlogic.gdx.math.MathUtils.random;

public final class HotseatGameController {

    private HotseatPlayer thisHotseatPlayer;
    private final HotseatPlayer[] hotseatPlayers;
    private final Field field;
    private final Difficulty difficulty;

    private final PlayerCardWindow playerCardWindow;
    private final AlgorithmCardWindow algorithmCardWindow;

    private final Array<GameCard> algorithmCards;
    private final Array<GameCard> discardPile;
    private final Stack<GameCard> talon = new Stack<>();

    public HotseatGameController(final HotseatPlayer[] hotseatPlayers, final Field field) {
        this.hotseatPlayers = hotseatPlayers;
        this.field = field;
        thisHotseatPlayer = hotseatPlayers[0];
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
        // Add 5 cards from talon to each player from talon
        for (HotseatPlayer hotseatPlayer : hotseatPlayers) {
            for (int i = 0; i < 5; i++) {
                GameCard gameCard = talon.pop();
                hotseatPlayer.addCard(gameCard);
                gameCard.setHotseatPlayer(hotseatPlayer);
            }
        }
        // add UI objects that are necessary for the game
        CardContainer playerCardContainer = new CardContainer(
                thisHotseatPlayer.getGameCards(),
                CardContainer.Content.All, true
        );
        playerCardWindow = new PlayerCardWindow(
                "HotseatPlayer cards", playerCardContainer, this
        );
        algorithmCardWindow = new AlgorithmCardWindow("Algorithm", this);
    }

    public void makeTalon() {
        while (!discardPile.isEmpty()) {
            final int i = random.nextInt(discardPile.size);
            final GameCard gameCard = discardPile.get(i);
            talon.push(gameCard);
            gameCard.setHotseatPlayer(null);
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

    public HotseatPlayer getThisHotseatPlayer() {
        return thisHotseatPlayer;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void toNextPlayer() {
        // add new cards to the player and clear playerCardWindow
        final Array<GameCard> gameCards = thisHotseatPlayer.getGameCards();
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
                gameCard.setHotseatPlayer(thisHotseatPlayer);
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
                gameCard.setHotseatPlayer(thisHotseatPlayer);
            }
        }
        // set this player
        int i;
        for (i = 0; i < hotseatPlayers.length; i++) {
            if (thisHotseatPlayer.equals(hotseatPlayers[i]))
                break;
        }
        thisHotseatPlayer = hotseatPlayers[(i + 1) % hotseatPlayers.length];
        // add this player cards to the playerCardWindow
        for (GameCard gameCard : thisHotseatPlayer.getGameCards()) {
            Card card = new Card(gameCard);
            playerCardContainer.addCard(card, 0, 0);
            gameCard.setHotseatPlayer(thisHotseatPlayer);
        }
    }

    public HotseatPlayer[] getHotseatPlayers() {
        return hotseatPlayers;
    }

    public HotseatPlayer getWinner() {
        for (HotseatPlayer hotseatPlayer : hotseatPlayers) {
            if ((difficulty == Difficulty.Easy && hotseatPlayer.getScore() >= 7)
                    || (difficulty == Difficulty.Hard && hotseatPlayer.getScore() >= 9)) {
                return hotseatPlayer;
            }
        }
        for (Chunk[] chunks : field.getChunks()) {
            for (Chunk chunk : chunks) {
                if (!chunk.getLives().isEmpty()) {
                    return null;
                }
            }
        }
        for (HotseatPlayer hotseatPlayer : hotseatPlayers) {
            if (!hotseatPlayer.getCar().getLives().isEmpty()) {
                return null;
            }
        }
        HotseatPlayer winner = hotseatPlayers[0];
        for (int i = 1; i < hotseatPlayers.length; i++) {
            if (hotseatPlayers[i].getScore() > winner.getScore()) {
                winner = hotseatPlayers[i];
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
