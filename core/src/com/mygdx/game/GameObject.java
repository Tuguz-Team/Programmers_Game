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

    void dispose() {
        model.dispose();
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

    void setX(int x) {
        this.x = x;
        if (modelInstance != null) {
            Vector3 position = modelInstance.transform.getTranslation(new Vector3());
            modelInstance.transform.translate(x, position.y, position.z);
        }
    }

    void setY(int y) {
        this.y = y;
        if (modelInstance != null) {
            Vector3 position = modelInstance.transform.getTranslation(new Vector3());
            modelInstance.transform.translate(position.x, y, position.z);
        }
    }

    void setZ(int z) {
        this.z = z;
        if (modelInstance != null) {
            Vector3 position = modelInstance.transform.getTranslation(new Vector3());
            modelInstance.transform.translate(position.x, position.y, z);
        }
    }
}
