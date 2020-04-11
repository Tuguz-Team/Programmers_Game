package com.mygdx.game;

interface ICard {

    boolean stepForward();

    void stepForwardToFloor();

    void jump();

    void turn90Left();

    void turn90Right();

    void turn180();

    void cycle2(Procedure[] procedures);

    void cycle3(Procedure[] procedures);

    void teleport();

}
