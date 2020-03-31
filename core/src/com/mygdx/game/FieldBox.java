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
        modelInstance.transform.translate(new Vector3(getX(), getZ(), getY()).add(field.getOffset()));
        ProgrammersGame.instances.add(modelInstance);
    }
}
