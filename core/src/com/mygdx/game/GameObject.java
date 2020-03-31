package com.mygdx.game;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;

abstract class GameObject {
    Model model;
    ModelInstance modelInstance;
    private int x, y, z;

    GameObject(final int x, final int y, final int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    abstract void loading();

    abstract void doneLoading();

    int getX() {
        return x;
    }

    int getY() {
        return y;
    }

    int getZ() {
        return z;
    }

    void setX(final int x) {
        this.x = x;
    }

    void setY(final int y) {
        this.y = y;
    }

    void setZ(final int z) {
        this.z = z;
    }
}
