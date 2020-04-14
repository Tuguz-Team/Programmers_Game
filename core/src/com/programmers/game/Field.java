package com.programmers.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector3;
import com.programmers.enums.Direction;
import com.programmers.game_objects.Base;
import com.programmers.game_objects.Car;
import com.programmers.game_objects.Chunk;
import com.programmers.game_objects.Life;
import com.programmers.game_objects.Lift;
import com.programmers.game_objects.Wall;
import com.programmers.screens.GameScreen;

import static com.badlogic.gdx.math.MathUtils.random;

public final class Field {

    private final int size;
    private GameScreen gameScreen;
    private final Chunk[][] chunks;
    private final Vector3 offset;

    public Field(final GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        size = gameScreen.getSize();
        offset = new Vector3(
                (1 - size) * Chunk.width / 2f,
                0f,
                (1 - size) * Chunk.width / 2f);
        chunks = new Chunk[size][size];
        generateField();
    }

    public Vector3 getOffset() {
        return offset;
    }

    public Chunk[][] getChunks() {
        return chunks;
    }

    public GameScreen getGameScreen() {
        return gameScreen;
    }

    public int getSize() {
        return size;
    }

    private void generateField() {
        switch (gameScreen.getDifficulty()) {
            case Easy: {
                initializing(253 / 255f, 208 / 255f, 2 / 255f);
                // Set position of square 2x2
                int i_ = random.nextInt(size - 1), j_ = random.nextInt(size - 1);
                for (int i = i_; i < i_ + 2; i++) {
                    for (int j = j_; j < j_ + 2; j++) {
                        chunks[i][j].setY(chunks[i][j].getY() + 1);
                        chunks[i][j].setColor(new Color(2 / 255f, 168 / 255f, 112 / 255f, 1f));
                    }
                }
                // Set position of rectangle 2x4
                int length, width;
                Color color = new Color(172 / 255f, 199 / 255f, 44 / 255f, 1f);
                boolean b;
                do {
                    b = true;
                    length = random.nextBoolean() ? 2 : 4;
                    width = 6 - length;
                    i_ = random.nextInt(size - length + 1);
                    j_ = random.nextInt(size - width + 1);
                    f:
                    for (int i = i_; i < i_ + length; i++)
                        for (int j = j_; j < j_ + width; j++)
                            if (chunks[i][j].getY() != 0) {
                                b = false;
                                break f;
                            }
                } while (!b);
                for (int i = i_; i < i_ + length; i++) {
                    for (int j = j_; j < j_ + width; j++) {
                        chunks[i][j].setY(chunks[i][j].getY() + 1);
                        chunks[i][j].setColor(color);
                    }
                }
                // Generate game objects
                generateBases();
                generateLives();
                generateWalls();
                break;
            }
            case Hard: {
                initializing(247 / 255f, 64 / 255f, 103 / 255f);
                // Set position of rectangle 3x6 (height is 2)
                int i1, j1, length1, width1;
                do {
                    length1 = random.nextBoolean() ? 6 : 3;
                    width1 = 9 - length1;
                    i1 = random.nextInt(size - length1 + 1);
                    j1 = random.nextInt(size - width1 + 1);
                } while (i1 != 0 && j1 != 0 && i1 != size - length1 && j1 != size - width1);
                for (int i = i1; i < i1 + length1; i++) {
                    for (int j = j1; j < j1 + width1; j++) {
                        chunks[i][j].setY(chunks[i][j].getY() + 2);
                        chunks[i][j].setColor(new Color(230 / 255f, 96 / 255f, 201 / 255f, 1));
                    }
                }
                // Set position of rectangle 3x6 (height is 1)
                int length2 = random.nextBoolean() ? 6 : 3;
                int width2 = 9 - length2;
                int i2, j2;
                if (length2 > width2) {
                    if (length1 > width1) {
                        i2 = random.nextInt(size - length2 + 1);
                        if (j1 < 3) {
                            j2 = (i1 == 0 || i1 == 3) ? j1 + width1 : 3;
                        } else if (j1 == 3) {
                            j2 = random.nextBoolean() ? 0 : 6;
                        } else {
                            j2 = (i1 == 0 || i1 == 3) ? j1 - width1 : 3;
                        }
                    } else {
                        if (i1 == 0) {
                            j2 = random.nextInt(size - width2 + 1);
                            if (j1 == 0) {
                                i2 = (j2 == 6) ? random.nextInt(size - length2) : 3;
                            } else if (j1 == 3) {
                                i2 = (j2 == 0) ? random.nextInt(size - length2) : 3;
                            } else {
                                i2 = 3;
                            }
                        } else if (i1 == 6) {
                            j2 = random.nextInt(size - width2 + 1);
                            if (j1 == 0) {
                                i2 = (j2 == 6) ? random.nextInt(size - length2) : 0;
                            } else if (j1 == 3) {
                                i2 = (j2 == 0) ? random.nextInt(size - length2) : 0;
                            } else {
                                i2 = 0;
                            }
                        } else {
                            i2 = random.nextInt(size - length2 + 1);
                            j2 = (j1 == 0) ? 6 : 0;
                        }
                    }
                } else {
                    if (length1 > width1) {
                        if (j1 == 0) {
                            i2 = random.nextInt(size - length2 + 1);
                            if (i1 == 0) {
                                j2 = (i2 == 6) ? random.nextInt(size - width2) : 3;
                            } else if (i1 == 3) {
                                j2 = (i2 == 0) ? random.nextInt(size - width2) : 3;
                            } else {
                                j2 = 3;
                            }
                        } else if (j1 == 6) {
                            i2 = random.nextInt(size - length2 + 1);
                            if (i1 == 0) {
                                j2 = (i2 == 6) ? random.nextInt(size - width2) : 0;
                            } else if (i1 == 3) {
                                j2 = (i2 == 0) ? random.nextInt(size - width2) : 0;
                            } else {
                                j2 = 0;
                            }
                        } else {
                            j2 = random.nextInt(size - width2 + 1);
                            i2 = (i1 == 0) ? 6 : 0;
                        }
                    } else {
                        j2 = random.nextInt(size - width2 + 1);
                        if (i1 < 3) {
                            i2 = (j1 == 0 || j1 == 3) ? i1 + length1 : 3;
                        } else if (i1 == 3) {
                            i2 = random.nextBoolean() ? 0 : 6;
                        } else {
                            i2 = (j1 == 0 || j1 == 3) ? i1 - length1 : 3;
                        }
                    }
                }
                for (int i = i2; i < i2 + length2; i++) {
                    for (int j = j2; j < j2 + width2; j++) {
                        chunks[i][j].setY(chunks[i][j].getY() + 1);
                        chunks[i][j].setColor(new Color(246 / 255f, 151 / 255f, 85 / 255f, 1));
                    }
                }
                // Set position of square 3x3
                int i3, j3;
                boolean b;
                do {
                    b = true;
                    i3 = random.nextInt(size - 2);
                    j3 = random.nextInt(size - 2);
                    f:
                    for (int i = i3; i < i3 + 3; i++)
                        for (int j = j3; j < j3 + 3; j++)
                            if (chunks[i][j].getY() != 0) {
                                b = false;
                                break f;
                            }
                } while (!b);
                for (int i = i3; i < i3 + 3; i++) {
                    for (int j = j3; j < j3 + 3; j++) {
                        chunks[i][j].setY(chunks[i][j].getY() + 1);
                        chunks[i][j].setColor(new Color(240 / 255f, 203 / 255f, 90 / 255f, 1f));
                    }
                }
                // Generate game objects
                generateBases();
                generateLives();
                for (int i = 0; i < 2; i++) {
                    generateLifts(i1, j1, length1, width1);
                    generateLifts(i2, j2, length2, width2);
                    generateLifts(i3, j3, 3, 3);
                }
                generateWalls();
                break;
            }
        }
    }

    private void initializing(float r, float g, float b) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++)
                chunks[i][j] = new Chunk(i, 0, j,
                        new Color(r, g, b, 1f), this);
        }
    }

    private void generateBases() {
        Car.Color[] colors = new Car.Color[4];
        boolean[] b = new boolean[4];
        int i = 0;
        while (!b[0] || !b[1] || !b[2] || !b[3]) {
            int temp = random.nextInt(4);
            if (!b[temp]) {
                colors[i] = Car.Color.fromInt(temp);
                i++;
                b[temp] = true;
            }
        }
        chunks[0][0] = new Base(0, chunks[0][0].getY(), 0, this, colors[0]);
        chunks[0][size - 1] = new Base(0, chunks[0][size - 1].getY(), size - 1, this, colors[1]);
        chunks[size - 1][0] = new Base(size - 1, chunks[size - 1][0].getY(), 0, this, colors[2]);
        chunks[size - 1][size - 1] = new Base(size - 1, chunks[size - 1][size - 1].getY(), size - 1,
                this, colors[3]);
    }

    private void generateLifts(final int i, final int j, final int length, final int width) {
        com.programmers.enums.Direction direction;
        int temp = 0;
        do direction = com.programmers.enums.Direction.getRandom();
        while (liftNotGenerated(i, j, length, width, direction) && (temp++ < 10));
    }

    private boolean liftNotGenerated(final int i, final int j, final int length, final int width,
                                     final com.programmers.enums.Direction direction) {
        int temp = 0;
        switch (direction) {
            case Forward:
                if (j + width < size) {
                    for (int k = i; k < i + length; k++) {
                        if (chunks[k][j + width - 1] instanceof Lift)
                            return true;
                    }
                    int iTemp;
                    do {
                        if (++temp > 10)
                            return true;
                        iTemp = random.nextInt(length) + i;
                    }
                    while (chunks[iTemp][j + width - 1].getY() <= chunks[iTemp][j + width].getY()
                            || chunks[iTemp][j + width].getLift() != null
                            || (j + width == 8 && (iTemp == 8 || iTemp == 0)));
                    chunks[iTemp][j + width - 1] = new Lift(
                            chunks[iTemp][j + width - 1],
                            chunks[iTemp][j + width]
                    );
                    return false;
                }
                return true;
            case Back:
                if (j > 0) {
                    for (int k = i; k < i + length; k++) {
                        if (chunks[k][j] instanceof Lift)
                            return true;
                    }
                    int iTemp;
                    do {
                        if (++temp > 10)
                            return true;
                        iTemp = random.nextInt(length) + i;
                    }
                    while (chunks[iTemp][j].getY() <= chunks[iTemp][j - 1].getY()
                            || chunks[iTemp][j - 1].getLift() != null
                            || (j == 1 && (iTemp == 8 || iTemp == 0)));
                    chunks[iTemp][j] = new Lift(
                            chunks[iTemp][j],
                            chunks[iTemp][j - 1]
                    );
                    return false;
                }
                return true;
            case Left:
                if (i + length < size) {
                    for (int k = j; k < j + width; k++) {
                        if (chunks[i + length - 1][k] instanceof Lift)
                            return true;
                    }
                    int jTemp;
                    do {
                        if (++temp > 10)
                            return true;
                        jTemp = random.nextInt(width) + j;
                    }
                    while (chunks[i + length - 1][jTemp].getY() <= chunks[i + length][jTemp].getY()
                            || chunks[i + length][jTemp].getLift() != null
                            || (i + length == 8 && (jTemp == 8 || jTemp == 0)));
                    chunks[i + length - 1][jTemp] = new Lift(
                            chunks[i + length - 1][jTemp],
                            chunks[i + length][jTemp]
                    );
                    return false;
                }
                return true;
            case Right:
            default:
                if (i > 0) {
                    for (int k = j; k < j + width; k++) {
                        if (chunks[i][k] instanceof Lift)
                            return true;
                    }
                    int jTemp;
                    do {
                        if (++temp > 10)
                            return true;
                        jTemp = random.nextInt(width) + j;
                    }
                    while (chunks[i][jTemp].getY() <= chunks[i - 1][jTemp].getY()
                            || chunks[i - 1][jTemp].getLift() != null
                            || (i == 1 && (jTemp == 8 || jTemp == 0)));
                    chunks[i][jTemp] = new Lift(
                            chunks[i][jTemp],
                            chunks[i - 1][jTemp]
                    );
                    return false;
                }
                return true;
        }
    }

    private void generateLives() {
        GridPoint2[] gridPoint2;
        int[] typeCount;
        switch (gameScreen.getDifficulty()) {
            case Easy:
                typeCount = new int[]{ 3, 3, 3, 3 };
                gridPoint2 = new GridPoint2[] {
                        new GridPoint2(0, 1),
                        new GridPoint2(0, 3),
                        new GridPoint2(1, 2),
                        new GridPoint2(1, 4),
                        new GridPoint2(2, 0),
                        new GridPoint2(2, 3),
                        new GridPoint2(3, 2),
                        new GridPoint2(3, 5),
                        new GridPoint2(4, 1),
                        new GridPoint2(4, 3),
                        new GridPoint2(4, 4),
                        new GridPoint2(5, 2)
                };
                break;
            case Hard:
            default:
                typeCount = new int[]{ 5, 5, 5, 5 };
                gridPoint2 = new GridPoint2[] {
                        new GridPoint2(0, 2),
                        new GridPoint2(1, 3),
                        new GridPoint2(1, 6),
                        new GridPoint2(2, 1),
                        new GridPoint2(2, 4),
                        new GridPoint2(2, 7),
                        new GridPoint2(3, 0),
                        new GridPoint2(3, 2),
                        new GridPoint2(3, 5),
                        new GridPoint2(3, 7),
                        new GridPoint2(4, 4),
                        new GridPoint2(5, 1),
                        new GridPoint2(5, 3),
                        new GridPoint2(5, 8),
                        new GridPoint2(6, 6),
                        new GridPoint2(6, 8),
                        new GridPoint2(7, 3),
                        new GridPoint2(7, 6),
                        new GridPoint2(8, 2),
                        new GridPoint2(8, 4)
                };
                break;
        }
        if (random.nextBoolean()) {
            // Transpose the matrix
            for (GridPoint2 point : gridPoint2) {
                int temp = point.x;
                point.x = point.y;
                point.y = temp;
            }
        }
        // Mirror the matrix
        boolean swapX = random.nextBoolean(),
                swapY = random.nextBoolean();
        for (GridPoint2 point : gridPoint2) {
            if (swapX)
                point.x = size - 1 - point.x;
            if (swapY)
                point.y = size - 1 - point.y;
        }
        int k;
        for (GridPoint2 point : gridPoint2) {
            do {
                k = random.nextInt(4);
            } while (typeCount[k] == 0);
            typeCount[k]--;
            new Life(chunks[point.x][point.y], Life.Type.fromInt(k));
        }
    }

    private void generateWalls() {
        int count;
        switch (gameScreen.getDifficulty()) {
            case Easy:
                count = random.nextInt(3) + 12;
                break;
            case Hard:
            default:
                count = random.nextInt(8) + 23;
        }
        int x, z;
        com.programmers.enums.Direction direction;
        Wall wall;
        while (count-- > 0) {
            do {
                x = random.nextInt(size);
                z = random.nextInt(size);
                direction = Direction.getRandom();
            } while (!chunks[x][z].canPlaceWall(direction));
            wall = new Wall(chunks[x][z], direction);
            switch (direction) {
                case Forward:
                    chunks[x][z].setWallForward(wall);
                    if (z + 1 < size) chunks[x][z + 1].setWallBack(wall);
                    break;
                case Back:
                    chunks[x][z].setWallBack(wall);
                    if (z - 1 > 0) chunks[x][z - 1].setWallForward(wall);
                    break;
                case Left:
                    chunks[x][z].setWallLeft(wall);
                    if (x + 1 < size) chunks[x + 1][z].setWallRight(wall);
                    break;
                case Right:
                default:
                    chunks[x][z].setWallRight(wall);
                    if (x - 1 > 0) chunks[x - 1][z].setWallLeft(wall);
            }
        }
    }

    public void loading() {
        for (Chunk[] chunks : chunks) {
            for (Chunk chunk : chunks) {
                chunk.loading();
            }
        }
    }

    public void doneLoading() {
        for (Chunk[] chunks : chunks) {
            for (Chunk chunk : chunks) {
                chunk.doneLoading();
            }
        }
    }
}
