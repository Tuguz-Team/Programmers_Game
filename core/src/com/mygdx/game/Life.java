package com.mygdx.game;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;

class Life extends GameObject {

    final static float width = 0.9f, height = 0.2f;

    private final Field field;
    private final Type type;

    private Model typeModel;
    private String typeModelFileName;

    Life(final Chunk chunk, final Type type) {
        super(chunk.getX(), chunk.getY() + 1, chunk.getZ());
        this.field = chunk.getField();
        this.type = type;
        chunk.getLives().add(this);
        StringBuilder stringBuilder = new StringBuilder("Models/LifeObjects/LifeObject");
        int number;
        switch (type) {
            case Yellow:
                number = 1;
                break;
            case Purple:
                number = 3;
                break;
            case Green:
                number = 2;
                break;
            case Blue:
            default:
                number = 4;
        }
        stringBuilder.append(number).append("/LifeObject").append(number).append(".obj");
        typeModelFileName = stringBuilder.toString();
        setModelFileName("Models/LifeObjects/LifeObject0/LifeObject0.obj");
    }

    @Override
    void loading() {
        ProgrammersGame.assetManager.load(getModelFileName(), Model.class);
        ProgrammersGame.assetManager.load(typeModelFileName, Model.class);
    }

    @Override
    void doneLoading() {
        setModel(ProgrammersGame.assetManager.get(getModelFileName(), Model.class));
        typeModel = ProgrammersGame.assetManager.get(typeModelFileName, Model.class);
        setModelInstance(new ModelInstance(getModel()));
        getModelInstance().transform.setTranslation(new Vector3(
                getX() * Chunk.width + 0.002f,
                getY() * Chunk.height + 0.002f,
                getZ() * Chunk.width + 0.002f
        ).add(field.getOffset()));
        ProgrammersGame.instances.add(getModelInstance());
    }

    @Override
    void setPosition(int x, int y, int z) {
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
    void setPosition(GameObject other) {
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

    Type getType() {
        return type;
    }

    enum Type {
        Yellow,
        Purple,
        Green,
        Blue;

        static Type fromInt(final int num) {
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
