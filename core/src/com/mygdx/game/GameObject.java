package com.mygdx.game;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;

abstract class GameObject {
    Model model;
    float x, y, z;

    GameObject(final float x, final float y, final float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    abstract void loading();

    abstract void doneLoading();

    void dispose() {
        model.dispose();
    }
}
