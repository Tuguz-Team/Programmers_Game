package com.mygdx.game;

import com.badlogic.gdx.utils.Array;

class Player {

    private int score;

    private final Car car;
    private final Array<Life> lives;
    private final Array<Card> cards;

    Player(final Car car) {
        this.car = car;
        lives = new Array<>(10);
        cards = new Array<>(5);
    }

    int getScore() {
        return score;
    }

    void addScore(final Life.Type type) {
        for (Life life : lives) {
            if (life.getType() == type) {
                score++;
                return;
            }
        }
        score += 2;
    }

    Car getCar() {
        return car;
    }

    Array<Life> getLives() {
        return lives;
    }

    Array<Card> getCards() {
        return cards;
    }
}
