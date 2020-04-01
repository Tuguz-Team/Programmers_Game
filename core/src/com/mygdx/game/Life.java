package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import static com.badlogic.gdx.math.MathUtils.random;

class Life extends GameObject {

    final static float width = 0.9f, height = 0.2f;

    private Type type;
    private Field field;

    Life(final int x, final int y, final int z, final Type type, final Field field) {
        super(x, y, z);
        this.type = type;
        this.field = field;
        field.chunks[getX()][getZ()].lives.add(this);
    }

    @Override
    void loading() {
        ProgrammersGame.assetManager.load("Models/LifeObjects/LifeObject0/LifeObject0.obj", Model.class);
    }

    @Override
    void doneLoading() {
        model = ProgrammersGame.assetManager.get("Models/LifeObjects/LifeObject0/LifeObject0.obj", Model.class);
        modelInstance = new ModelInstance(model);
        modelInstance.transform.setTranslation(new Vector3(
                getX() * Chunk.width + 0.001f,
                getY() * Chunk.height + 0.001f,
                getZ() * Chunk.width + 0.001f
        ).add(field.getOffset()));
        ProgrammersGame.instances.add(modelInstance);
    }

    void translate(final int x, final int y, final int z) {
        if (modelInstance != null) {
            modelInstance.transform.translate(new Vector3(x * Chunk.width, y, z * Chunk.width));
        }
    }

    enum Type {
        Yellow,
        Purple,
        Green,
        Blue
    }
}
