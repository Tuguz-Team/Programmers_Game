package com.programmers.game_objects;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.programmers.game.Field;

public final class Life extends GameObject {

    private final Type type;
    private final Field field;

    private Model typeModel;
    private String typeModelFileName;

    public Life(final Chunk chunk, final Type type) {
        super(chunk.getX(), chunk.getY() + 1, chunk.getZ(), chunk.getProgrammersGame());
        this.type = type;
        this.field = chunk.getField();
        chunk.getLives().add(this);
        typeModelFileName = "Models/LifeObjects/LifeObject" + type + "/LifeObject" + type + ".obj";
        setModelFileName("Models/LifeObjects/LifeObjectUnknown/LifeObjectUnknown.obj");
    }

    @Override
    public void loading() {
        getProgrammersGame().getAssetManager().load(getModelFileName(), Model.class);
        getProgrammersGame().getAssetManager().load(typeModelFileName, Model.class);
    }

    @Override
    public void doneLoading() {
        setModel(getProgrammersGame().getAssetManager().get(getModelFileName(), Model.class));
        typeModel = getProgrammersGame().getAssetManager().get(typeModelFileName, Model.class);
        setModelInstance(new ModelInstance(getModel()));
        getModelInstance().transform.setTranslation(new Vector3(
                getX() * Chunk.width + 0.002f,
                getY() * Chunk.height + 0.002f,
                getZ() * Chunk.width + 0.002f
        ).add(field.getOffset()));
        getProgrammersGame().getInstances().add(getModelInstance());
    }

    @Override
    public void setPosition(int x, int y, int z) {
        super.setPosition(x, y, z);
        if (getModelInstance() != null) {
            getModelInstance().transform.setTranslation(new Vector3(
                    getX() * Chunk.width + 0.002f,
                    getY() * Chunk.height + 0.002f,
                    getZ() * Chunk.width + 0.002f
            ).add(field.getOffset()));
        }
    }

    @Override
    public void setPosition(GameObject other) {
        super.setPosition(other);
        setY(getY() + 1);
        if (getModelInstance() != null) {
            getModelInstance().transform.setTranslation(new Vector3(
                    getX() * Chunk.width + 0.002f,
                    getY() * Chunk.height + 0.002f,
                    getZ() * Chunk.width + 0.002f
            ).add(field.getOffset()));
        }
    }

    public Type getType() {
        return type;
    }

    public enum Type {
        Yellow,
        Purple,
        Green,
        Blue;

        public static Type fromInt(final int num) {
            switch (num % 4) {
                case 0:
                    return Yellow;
                case 1:
                    return Purple;
                case 2:
                    return Green;
                default:
                    return Blue;
            }
        }
    }
}
