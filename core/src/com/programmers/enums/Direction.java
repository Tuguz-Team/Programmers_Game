package com.programmers.enums;

public enum Direction {
    Forward,
    Back,
    Left,
    Right;

    public static Direction getRandom() {
        int temp = com.badlogic.gdx.math.MathUtils.random.nextInt(4);
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
