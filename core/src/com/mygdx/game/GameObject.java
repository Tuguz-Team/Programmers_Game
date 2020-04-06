package com.mygdx.game;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;

abstract class GameObject {

    private Model model;
    private ModelInstance modelInstance;
    private int x, y, z;

    GameObject(final int x, final int y, final int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    abstract void loading();

    abstract void doneLoading();

    void setPosition(final int x, final int y, final int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

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

    Model getModel() {
        return model;
    }

    void setModel(Model model) {
        this.model = model;
    }

    ModelInstance getModelInstance() {
        return modelInstance;
    }

    void setModelInstance(ModelInstance modelInstance) {
        this.modelInstance = modelInstance;
    }
}
