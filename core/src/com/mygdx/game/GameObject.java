package com.mygdx.game;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;

abstract class GameObject {
    Model model;
    ModelInstance modelInstance;
    int x, y, z;

    GameObject(final int x, final int y, final int z) {
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
