package com.programmers.game;

import com.badlogic.gdx.utils.Array;
import com.programmers.enums.Difficulty;
import com.programmers.game_objects.Chunk;
import com.programmers.screens.GameScreen;
import com.programmers.ui_elements.AlgorithmCardWindow;
import com.programmers.ui_elements.CardContainer;
import com.programmers.ui_elements.PlayerCardWindow;

import java.util.Stack;

import static com.badlogic.gdx.math.MathUtils.random;

public abstract class GameController {

    protected Player thisPlayer;
    protected PlayerCardWindow playerCardWindow;
    protected AlgorithmCardWindow algorithmCardWindow;
    protected final Field field;

    protected final Array<GameCard> algorithmCards = new Array<>();
    protected final Array<GameCard> discardPile = new Array<>(52);
    protected final Stack<GameCard> talon = new Stack<>();

    protected final GameScreen gameScreen;

    protected GameController(GameScreen gameScreen, Field field) {
        this.gameScreen = gameScreen;
        this.field = field;
    }

    public GameScreen getGameScreen() {
        return gameScreen;
    }

    public AlgorithmCardWindow getAlgorithmCardWindow() {
        return algorithmCardWindow;
    }

    public Array<GameCard> getAlgorithmCards() {
        return algorithmCards;
    }

    public Array<GameCard> getDiscardPile() {
        return discardPile;
    }

    public Stack<GameCard> getTalon() {
        return talon;
    }

    public PlayerCardWindow getPlayerCardWindow() {
        return playerCardWindow;
    }

    public Player getThisPlayer() {
        return thisPlayer;
    }

    public abstract void toNextPlayer();

    public void initContainers() {
        CardContainer playerCardContainer = new CardContainer(
                thisPlayer.getGameCards(),
                getDifficulty(), CardContainer.Content.All,
                this
        );
        playerCardWindow = new PlayerCardWindow(
                "  Player cards  ", playerCardContainer, this
        );
        algorithmCardWindow = new AlgorithmCardWindow("  Algorithm  ", this);
    }

    public abstract Difficulty getDifficulty();

    public void makeTalon() {
        while (!discardPile.isEmpty()) {
            final int i = random.nextInt(discardPile.size);
            final GameCard gameCard = discardPile.get(i);
            talon.push(gameCard);
            gameCard.setPlayer(null);
            discardPile.removeIndex(i);
        }
    }

    public boolean isWinner() {
        if ((getDifficulty() == Difficulty.Easy && thisPlayer.getScore() >= 7)
                || (getDifficulty() == Difficulty.Hard && thisPlayer.getScore() >= 9)
                && thisPlayer.getCar().getLives().isEmpty()) {
            for (Chunk[] chunks : field.getChunks()) {
                for (Chunk chunk : chunks) {
                    if (!chunk.getLives().isEmpty()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
