package com.programmers.game.hotseat;

import com.badlogic.gdx.utils.Array;
import com.programmers.game.GameCard;
import com.programmers.game_objects.Car;
import com.programmers.game_objects.Life;

public final class HotseatPlayer {

    private int score;
    private final Car car;
    private final Array<Life> lives;
    private final Array<GameCard> cards;

    public HotseatPlayer(final Car car) {
        this.car = car;
        car.setHotseatPlayer(this);
        lives = new Array<>(10);
        cards = new Array<>(5);
    }

    public int getScore() {
        return score;
    }

    public void addScore(final Life.Type type) {
        for (Life life : lives) {
            if (life.getType() == type) {
                score++;
                return;
            }
        }
        score += 2;
    }

    public void addCard(GameCard gameCard) {
        gameCard.setHotseatPlayer(this);
        cards.add(gameCard);
    }

    public Car getCar() {
        return car;
    }

    public Array<Life> getLives() {
        return lives;
    }

    public Array<GameCard> getGameCards() {
        return cards;
    }
}
