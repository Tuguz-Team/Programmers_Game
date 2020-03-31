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
        modelInstance = new ModelInstance(model);
        modelInstance.transform.translate(new Vector3(getX(), getY(), getZ()).add(field.getOffset()));
        ProgrammersGame.instances.add(modelInstance);
    }

    @Override
    void setX(final int x) {
        super.setX(x);
        if (modelInstance != null) {
            modelInstance.transform.setTranslation(new Vector3(x * Chunk.width,
                    getY() * Chunk.height, getZ() * Chunk.width).add(field.getOffset()));
        }
    }

    @Override
    void setY(final int y) {
        super.setY(y);
        if (modelInstance != null) {
            modelInstance.transform.setTranslation(new Vector3(getX() * Chunk.width,
                    y * Chunk.height, getZ() * Chunk.width).add(field.getOffset()));
        }
    }

    @Override
    void setZ(final int z) {
        super.setZ(z);
        if (modelInstance != null) {
            modelInstance.transform.setTranslation(new Vector3(getX() * Chunk.width,
                    getY() * Chunk.height, z * Chunk.width).add(field.getOffset()));
        }
    }
}
