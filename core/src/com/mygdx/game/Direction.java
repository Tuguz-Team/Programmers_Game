package com.mygdx.game;

import static com.badlogic.gdx.math.MathUtils.random;

enum Direction {
    Forward,
    Back,
    Left,
    Right;

    static Direction getRandom() {
        int temp = random.nextInt(4);
        switch (temp) {
            case 0:
                return Forward;
            case 1:
                return Back;
            case 2:
                return Left;
            default:
                return Right;
        }
    }
}
