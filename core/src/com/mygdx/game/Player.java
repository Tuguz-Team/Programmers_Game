package com.mygdx.game;

import com.badlogic.gdx.utils.Array;

class Player implements ICards {

    private Car car;
    private Array<Life> lives;

    Player(final Car car) {
        this.car = car;
        lives = new Array<>();
    }

    Car getCar() {
        return car;
    }

    Array<Life> getLives() {
        return lives;
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
