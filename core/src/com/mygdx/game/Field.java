package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;

import static com.badlogic.gdx.math.MathUtils.random;

class Field {

    //private FieldBox[] fieldBoxes = new FieldBox[4];
    Chunk[][] chunks;
    private Vector3 offset;

    Field(final int size) {
        offset = new Vector3((1 - size) * Chunk.width / 2f, 0f, (1 - size) * Chunk.width / 2f);
        generate(size);
    }

    Vector3 getOffset() {
        return offset;
    }

    private void generate(final int size) {
        chunks = new Chunk[size][size];
        switch (ProgrammersGame.difficulty) {
            case Easy: {
                // Array initializing
                for (int i = 0; i < size; i++) {
                    for (int j = 0; j < size; j++)
                        chunks[i][j] = new Chunk(i, j, 0,
                                new Color(253 / 255f, 208 / 255f, 2 / 255f, 1f), this);
                }
                // Set position of square 2x2
                int i_ = random.nextInt(size - 1), j_ = random.nextInt(size - 1);
                for (int i = i_; i < i_ + 2; i++) {
                    for (int j = j_; j < j_ + 2; j++) {
                        chunks[i][j].setZ(chunks[i][j].getZ() + 1);
                        chunks[i][j].color = new Color(2 / 255f, 168 / 255f, 112 / 255f, 1f);
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
                            if (chunks[i][j].getZ() != 0) {
                                b = false;
                                break f;
                            }
                } while (!b);
                for (int i = i_; i < i_ + length; i++) {
                    for (int j = j_; j < j_ + width; j++) {
                        chunks[i][j].setZ(chunks[i][j].getZ() + 1);
                        chunks[i][j].color = color;
                    }
                }
                break;
            }
            case Hard: {
                // Array initializing
                for (int i = 0; i < size; i++) {
                    for (int j = 0; j < size; j++)
                        chunks[i][j] = new Chunk(i, j, 0,
                                new Color(247 / 255f, 64 / 255f, 103 / 255f, 1f), this);
                }
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
                        chunks[i][j].setZ(chunks[i][j].getZ() + 2);
                        chunks[i][j].color = new Color(230 / 255f, 96 / 255f, 201 / 255f, 1);
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
                            j2 = (i1 == 0 || i1 == 3)
                                    ? random.nextInt(width1 + 1 - j1) + j1 + width1
                                    : (random.nextBoolean() ? 3 : 6);
                        } else if (j1 == 3) {
                            j2 = random.nextBoolean() ? 0 : 6;
                        } else {
                            j2 = (i1 == 0 || i1 == 3)
                                    ? random.nextInt(j1 - width1 + 1)
                                    : (random.nextBoolean() ? 0 : 3);
                        }
                    } else {
                        if (i1 == 0) {
                            j2 = random.nextInt(size - width2 + 1);
                            if (j1 == 0) {
                                i2 = (j2 == 6)
                                        ? random.nextInt(size - length2 + 1)
                                        : 3;
                            } else if (j1 == 3) {
                                i2 = (j2 == 0)
                                        ? random.nextInt(size - length2 + 1)
                                        : 3;
                            } else {
                                i2 = 3;
                            }
                        } else if (i1 == 6) {
                            j2 = random.nextInt(size - width2 + 1);
                            if (j1 == 0) {
                                i2 = (j2 == 6)
                                        ? random.nextInt(size - length2 + 1)
                                        : 0;
                            } else if (j1 == 3) {
                                i2 = (j2 == 0)
                                        ? random.nextInt(size - length2 + 1)
                                        : 0;
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
                                j2 = (i2 == 6)
                                        ? random.nextInt(size - width2 + 1)
                                        : 3;
                            } else if (i1 == 3) {
                                j2 = (i2 == 0)
                                        ? random.nextInt(size - width2 + 1)
                                        : 3;
                            } else {
                                j2 = 3;
                            }
                        } else if (j1 == 6) {
                            i2 = random.nextInt(size - length2 + 1);
                            if (i1 == 0) {
                                j2 = (i2 == 6)
                                        ? random.nextInt(size - width2 + 1)
                                        : 0;
                            } else if (i1 == 3) {
                                j2 = (i2 == 0)
                                        ? random.nextInt(size - width2 + 1)
                                        : 0;
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
                            i2 = (j1 == 0 || j1 == 3)
                                    ? random.nextInt(length1 + 1 - i1) + i1 + length1
                                    : (random.nextBoolean() ? 3 : 6);
                        } else if (i1 == 3) {
                            i2 = random.nextBoolean() ? 0 : 6;
                        } else {
                            i2 = (j1 == 0 || j1 == 3)
                                    ? random.nextInt(i1 - length1 + 1)
                                    : (random.nextBoolean() ? 0 : 3);
                        }
                    }
                }
                for (int i = i2; i < i2 + length2; i++) {
                    for (int j = j2; j < j2 + width2; j++) {
                        chunks[i][j].setZ(chunks[i][j].getZ() + 1);
                        chunks[i][j].color = new Color(246 / 255f, 151 / 255f, 85 / 255f, 1);
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
                            if (chunks[i][j].getZ() != 0) {
                                b = false;
                                break f;
                            }
                } while (!b);
                for (int i = i3; i < i3 + 3; i++)
                    for (int j = j3; j < j3 + 3; j++) {
                        chunks[i][j].setZ(chunks[i][j].getZ() + 1);
                        chunks[i][j].color = new Color(240 / 255f, 203 / 255f, 90 / 255f, 1f);
                    }
            }
        }

        Car.Color[] colors = new Car.Color[4];
        boolean[] b = new boolean[4];
        int i = 0;
        while (!b[0] || !b[1] || !b[2] || !b[3]) {
            int temp = random.nextInt(4);
            switch (temp) {
                case 0:
                    if (!b[0]) {
                        colors[i] = Car.Color.RED;
                        i++;
                        b[0] = true;
                    }
                    break;
                case 1:
                    if (!b[1]) {
                        colors[i] = Car.Color.GREEN;
                        i++;
                        b[1] = true;
                    }
                    break;
                case 2:
                    if (!b[2]) {
                        colors[i] = Car.Color.YELLOW;
                        i++;
                        b[2] = true;
                    }
                    break;
                case 3:
                default:
                    if (!b[3]) {
                        colors[i] = Car.Color.BLUE;
                        i++;
                        b[3] = true;
                    }
            }
        }
        chunks[0][0].setBaseColor(colors[0]);
        chunks[0][size - 1].setBaseColor(colors[1]);
        chunks[size - 1][0].setBaseColor(colors[2]);
        chunks[size - 1][size - 1].setBaseColor(colors[3]);
        /*
        final float width = Chunk.width / 4;
        final float length = size * Chunk.width + 2 * width;
        final float height = Chunk.height * ProgrammersGame.maxHeight;
        fieldBoxes[0] = new FieldBox(-Chunk.width / 2 - width / 2, (size - 1) / 2f * Chunk.width, height / 2 - Chunk.height / 2,
                new ModelBuilder().createBox(width, height, length,
                        new Material(ColorAttribute.createDiffuse(Color.GRAY)),
                        VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal)
        );
        fieldBoxes[1] = new FieldBox((size - 1) / 2f * Chunk.width, -Chunk.width / 2 - width / 2, height / 2 - Chunk.height / 2,
                new ModelBuilder().createBox(length, height, width,
                        new Material(ColorAttribute.createDiffuse(Color.GRAY)),
                        VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal)
                );
        fieldBoxes[2] = new FieldBox(size * Chunk.width - Chunk.width / 2 + width / 2, (size - 1) / 2f * Chunk.width, height / 2 - Chunk.height / 2,
                new ModelBuilder().createBox(width, height, length,
                        new Material(ColorAttribute.createDiffuse(Color.GRAY)),
                        VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal)
        );
        fieldBoxes[3] = new FieldBox((size - 1) / 2f * Chunk.width, size * Chunk.width - Chunk.width / 2 + width / 2, height / 2 - Chunk.height / 2,
                new ModelBuilder().createBox(length, height, width,
                        new Material(ColorAttribute.createDiffuse(Color.GRAY)),
                        VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal)
        );
        */
    }

    void loading() {
        for (Chunk[] chunks : chunks) {
            for (Chunk chunk : chunks) {
                chunk.loading();
            }
        }
        /*
        for (FieldBox fieldBox : fieldBoxes) {
            fieldBox.loading();
        }
        */
    }

    void doneLoading() {
        for (Chunk[] chunks : chunks) {
            for (Chunk chunk : chunks) {
                chunk.doneLoading();
            }
        }
        /*
        for (FieldBox fieldBox : fieldBoxes) {
            fieldBox.doneLoading();
        }
        */
    }
}
