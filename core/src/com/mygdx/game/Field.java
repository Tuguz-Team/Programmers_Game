package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
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

    void render(PerspectiveCamera camera, Environment environment) {
        for (Chunk[] chunk : chunks) {
            for (Chunk value : chunk) {
                value.render(camera, environment);
            }
        }
    }

    private void generate() {
        for (int i = 0; i < chunks.length; i++) {
            for (int j = 0; j < chunks[i].length; j++) {
                chunks[i][j] = new Chunk(i, j, 0);
            }
        }
    }

    void dispose() {
        for (Chunk[] chunk : chunks) {
            for (Chunk value : chunk) {
                value.model.dispose();
                value.modelBatch.dispose();
            }
        }
    }

    public class Chunk {
        Model model;
        ModelInstance modelInstance;
        ModelBatch modelBatch;

        int x, y, height;
        boolean impulse, isBase, hasLife, hasLift;
        boolean northWall, southWall, eastWall, westWall;
        GameColor color, labColor;

        Chunk(final int x, final int y, final int height) {
            this.x = x;
            this.y = y;
            this.height = height;

            modelBatch = new ModelBatch();
            model = new ModelBuilder().createBox(1, 1, 1,
                    new Material(ColorAttribute.createDiffuse(Color.GREEN)),
                    VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
            modelInstance = new ModelInstance(model);
            modelInstance.transform.translate(new Vector3(x, height, y).add(offset));
        }

        void render(PerspectiveCamera camera, Environment environment) {
            modelBatch.begin(camera);
            modelBatch.render(modelInstance, environment);
            modelBatch.end();
        }
    }
}
