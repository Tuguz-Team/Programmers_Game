package com.programmers.game_objects;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.programmers.screens.GameScreen;

public abstract class GameObject {

    private GameScreen gameScreen;
    private Model model;
    private ModelInstance modelInstance;
    private int x, y, z;
    private String modelFileName;

    protected GameObject(final int x, final int y, final int z, final GameScreen gameScreen) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.gameScreen = gameScreen;
    }

    abstract protected void loading();

    abstract protected void doneLoading();

    protected GameScreen getGameScreen() {
        return gameScreen;
    }

    protected void setPosition(final int x, final int y, final int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    protected void setPosition(final GameObject other) {
        setPosition(other.x, other.y, other.z);
    }

    public final void lookAt(final float x, final float y, final float z) {
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

    public final int getX() {
        return x;
    }

    public final int getY() {
        return y;
    }

    public final int getZ() {
        return z;
    }

    public void setX(final int x) {
        this.x = x;
    }

    public void setY(final int y) {
        this.y = y;
    }

    public void setZ(final int z) {
        this.z = z;
    }

    protected Model getModel() {
        return model;
    }

    protected void setModel(Model model) {
        this.model = model;
    }

    protected ModelInstance getModelInstance() {
        return modelInstance;
    }

    protected void setModelInstance(ModelInstance modelInstance) {
        this.modelInstance = modelInstance;
    }

    private static float VectorAngle(final Vector3 first, final Vector3 second) {
        return (float)(Math.acos(first.dot(second) / first.len() / second.len()) * 180f / Math.PI);
    }

    protected String getModelFileName() {
        return modelFileName;
    }

    protected void setModelFileName(String modelFileName) {
        this.modelFileName = modelFileName;
    }
}
