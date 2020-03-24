package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;

class Field {

    private FieldBox[] fieldBoxes = new FieldBox[4];
    private Chunk[][] chunks;
    private static Vector3 offset;

    Field(final int size) {
        offset = new Vector3((1 - size) * Chunk.width / 2f, 0f, (1 - size) * Chunk.width / 2f);
        generate(size);
    }

    static Vector3 getOffset() {
        return offset;
    }

    private void generate(final int size) {
        chunks = new Chunk[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                chunks[i][j] = new Chunk(i, j, 0, null);
            }
        }
        final float width = Chunk.width / 4, length = size * Chunk.width + 2 * width, height = Chunk.height * 2f;
        fieldBoxes[0] = new FieldBox(-Chunk.width / 2 - width / 2, (size >> 1) * Chunk.width, height / 2 - Chunk.height / 2,
                new ModelBuilder().createBox(width, height, length,
                        new Material(ColorAttribute.createDiffuse(Color.GRAY)),
                        VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal)
        );
        fieldBoxes[1] = new FieldBox((size >> 1) * Chunk.width, -Chunk.width / 2 - width / 2, height / 2 - Chunk.height / 2,
                new ModelBuilder().createBox(length, height, width,
                        new Material(ColorAttribute.createDiffuse(Color.GRAY)),
                        VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal)
                );
        fieldBoxes[2] = new FieldBox(size * Chunk.width - Chunk.width / 2 + width / 2, (size >> 1) * Chunk.width, height / 2 - Chunk.height / 2,
                new ModelBuilder().createBox(width, height, length,
                        new Material(ColorAttribute.createDiffuse(Color.GRAY)),
                        VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal)
        );
        fieldBoxes[3] = new FieldBox((size >> 1) * Chunk.width, size * Chunk.width - Chunk.width / 2 + width / 2, height / 2 - Chunk.height / 2,
                new ModelBuilder().createBox(length, height, width,
                        new Material(ColorAttribute.createDiffuse(Color.GRAY)),
                        VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal)
        );
    }

    void loading() {
        for (Chunk[] chunks : chunks) {
            for (Chunk chunk : chunks) {
                chunk.loading();
            }
        }
        for (FieldBox fieldBox : fieldBoxes) {
            fieldBox.loading();
        }
    }

    void doneLoading() {
        for (Chunk[] chunks : chunks) {
            for (Chunk chunk : chunks) {
                chunk.doneLoading();
            }
        }
        for (FieldBox fieldBox : fieldBoxes) {
            fieldBox.doneLoading();
        }
    }

    void dispose() {
        for (Chunk[] chunks : chunks) {
            for (Chunk chunk : chunks) {
                chunk.dispose();
            }
        }
        for (FieldBox fieldBox : fieldBoxes) {
            fieldBox.dispose();
        }
    }
}
