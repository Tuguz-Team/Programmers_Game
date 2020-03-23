package com.mygdx.game;

import com.badlogic.gdx.math.Vector3;

class Field {
    private Chunk[][] chunks;
    private static Vector3 center;
    private static Vector3 offset;

    Field(final int size) {
        center = (size % 2 == 0) ? new Vector3(0.5f, 0, 0.5f) : new Vector3();
        offset = new Vector3().set(center).add((float) (-size / 2), 0, (float) (-size / 2));
        chunks = new Chunk[size][size];
        generate();
    }

    static Vector3 getCenter() {
        return center;
    }

    static Vector3 getOffset() {
        return offset;
    }

    void render() {
        for (Chunk[] chunks : chunks) {
            for (Chunk chunk : chunks) {
                chunk.render();
            }
        }
    }

    private void generate() {
        for (int i = 0; i < chunks.length; i++) {
            for (int j = 0; j < chunks[i].length; j++) {
                chunks[i][j] = new Chunk(i, j, 0, null);
            }
        }
    }

    void dispose() {
        for (Chunk[] chunks : chunks) {
            for (Chunk chunk : chunks) {
                chunk.dispose();
            }
        }
    }
}
