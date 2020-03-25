package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Collections;

import java.util.Arrays;

import static com.badlogic.gdx.math.MathUtils.degRad;
import static com.badlogic.gdx.math.MathUtils.random;

class Field {

    //private FieldBox[] fieldBoxes = new FieldBox[4];
    static Chunk[][] chunks;
    private static Vector3 offset;

    Field(final int size) {
        offset = new Vector3((1 - size) * Chunk.width / 2f, 0f, (1 - size) * Chunk.width / 2f);
        generate(size);
    }

    static Vector3 getOffset() {
        return offset;
    }

    private void generate(final int size) {
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

        chunks = new Chunk[size][size];
        for (i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                chunks[i][j] = new Chunk(i, j, 0, Color.FIREBRICK);
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

    void dispose() {
        for (Chunk[] chunks : chunks) {
            for (Chunk chunk : chunks) {
                chunk.dispose();
            }
        }
        /*
        for (FieldBox fieldBox : fieldBoxes) {
            fieldBox.dispose();
        }
        */
    }
}
