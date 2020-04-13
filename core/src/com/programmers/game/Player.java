package com.programmers.game;

import com.badlogic.gdx.utils.Array;
import com.programmers.game_objects.Car;
import com.programmers.game_objects.Life;
import com.programmers.interfaces.ICard;
import com.programmers.interfaces.Procedure;

public final class Player implements ICard {

    private int score;
    private final Car car;
    private final Array<Life> lives;
    private final Array<Card> cards;

    public Player(final Car car) {
        this.car = car;
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

    public Car getCar() {
        return car;
    }

    public Array<Life> getLives() {
        return lives;
    }

    public Array<Card> getCards() {
        return cards;
    }

    @Override
    public boolean stepForward() {
        return car.stepForward();
    }

    @Override
    public void stepForwardToFloor() {
        car.stepForwardToFloor();
    }

    @Override
    public void jump() {
        car.jump();
    }

    @Override
    public void turn90Left() {
        car.turn90Left();
    }

    @Override
    public void turn90Right() {
        car.turn90Right();
    }

    @Override
    public void turn180() {
        car.turn180();
    }

    @Override
    public void cycle2(Procedure[] procedures) {
        car.cycle2(procedures);
    }

    @Override
    public void cycle3(Procedure[] procedures) {
        car.cycle3(procedures);
    }

    @Override
    public void teleport() {
        car.teleport();
    }
}
