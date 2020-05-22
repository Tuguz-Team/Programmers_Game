package com.programmers.game_objects;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.programmers.screens.GameScreen;

public abstract class GameObject {

    private GameScreen gameScreen;
    private Model model;
    private ModelInstance modelInstance;
    private int x, y, z;
    private String modelFileName;

    GameObject(final int x, final int y, final int z, final GameScreen gameScreen) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.gameScreen = gameScreen;
    }

    abstract protected void loadModel();

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

    String getModelFileName() {
        return modelFileName;
    }

    void setModelFileName(String modelFileName) {
        this.modelFileName = modelFileName;
    }
}
