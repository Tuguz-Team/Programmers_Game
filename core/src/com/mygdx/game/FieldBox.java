package com.mygdx.game;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;

public class FieldBox extends GameObject {

    private final Field field;

    FieldBox(final int x, final int y, final int z, final Field field) {
        super(x, y, z);
        this.field = field;
    }

    @Override
    void loading() {

    }

    @Override
    void doneLoading() {
        setModelInstance(new ModelInstance(getModel()));
        getModelInstance().transform.translate(new Vector3(getX(), getY(), getZ()).add(field.getOffset()));
        ProgrammersGame.instances.add(getModelInstance());
    }

    @Override
    void setX(final int x) {
        super.setX(x);
        if (getModelInstance() != null) {
            getModelInstance().transform.setTranslation(new Vector3(x * Chunk.width,
                    getY() * Chunk.height, getZ() * Chunk.width).add(field.getOffset()));
        }
    }

    @Override
    void setY(final int y) {
        super.setY(y);
        if (getModelInstance() != null) {
            getModelInstance().transform.setTranslation(new Vector3(getX() * Chunk.width,
                    y * Chunk.height, getZ() * Chunk.width).add(field.getOffset()));
        }
    }

    @Override
    void setZ(final int z) {
        super.setZ(z);
        if (getModelInstance() != null) {
            getModelInstance().transform.setTranslation(new Vector3(getX() * Chunk.width,
                    getY() * Chunk.height, z * Chunk.width).add(field.getOffset()));
        }
    }

    @Override
    void setPosition(final int x, final int y, final int z) {
        super.setPosition(x, y, z);
        if (getModelInstance() != null) {
            getModelInstance().transform.setTranslation(new Vector3(getX() * Chunk.width,
                    getY() * Chunk.height, getZ() * Chunk.width).add(field.getOffset()));
        }
    }
}
