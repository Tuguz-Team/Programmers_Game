package com.mygdx.game;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;

public class FieldBox extends GameObject {

    FieldBox(final float x, final float y, final float z, final Model model) {
        super(x, y, z);
        this.model = model;
    }

    @Override
    void loading() {

    }

    @Override
    void doneLoading() {
        ModelInstance modelInstance = new ModelInstance(model);
        modelInstance.transform.translate(new Vector3(x, z, y).add(Field.getOffset()));
        ProgrammersGame.instances.add(modelInstance);
    }
}
