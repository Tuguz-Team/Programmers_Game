package com.mygdx.game;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;

abstract class GameObject {

    private Model model;
    private ModelInstance modelInstance;
    private int x, y, z;
    private String modelFileName;

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

    void setPosition(final GameObject other) {
        setPosition(other.x, other.y, other.z);
    }

    final void lookAt(final float x, final float y, final float z) {
        if (modelInstance != null) {
            Vector3 position = modelInstance.transform.getTranslation(new Vector3());
            Vector3 direction = new Vector3(x, y, z).sub(position);
            modelInstance.transform.idt().rotate(
                    Vector3.Z.cpy().crs(direction),
                    VectorAngle(Vector3.Z, direction)
            ).setTranslation(position);
            //modelInstance.transform.rotate(Vector3.Z, 0);
            //System.out.println(VectorAngle(position, new Vector3(x, y, z)));
        }
    }

    final void lookAt(final Vector3 target) {
        lookAt(target.x, target.y, target.z);
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

    private static float VectorAngle(final Vector3 first, final Vector3 second) {
        return (float)(Math.acos(first.dot(second) / first.len() / second.len()) * 180f / Math.PI);
    }

    String getModelFileName() {
        return modelFileName;
    }

    void setModelFileName(String modelFileName) {
        this.modelFileName = modelFileName;
    }
}
