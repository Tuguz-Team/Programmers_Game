package com.programmers.game;

class Card {

    private final Type type;

    Card(final Type type) {
        this.type = type;
    }

    enum Type {
        StepForward,
        StepForwardToFloor,
        Jump,
        Turn90Left,
        Turn90Right,
        Turn180,
        Cycle2,
        Cycle3,
        Teleport
    }
}
