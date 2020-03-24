package com.mygdx.game;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;

class Car extends GameObject {

    private Car.Color color;

    public enum Color {
        RED,
        GREEN,
        YELLOW,
        BLUE
    }

    Car(final int x, final int y, final int z, final Car.Color color) {
        super(x, y, z);
        this.color = color;
    }

    @Override
    void loading() {
        switch (color) {
            case RED:
                ProgrammersGame.assetManager.load("models/RedCarHard/RedCarHard.obj", Model.class);
                break;
            case GREEN:
                ProgrammersGame.assetManager.load("models/GreenCarHard/GreenCarHard.obj", Model.class);
                break;
            case YELLOW:
                ProgrammersGame.assetManager.load("models/YellowCarHard/YellowCarHard.obj", Model.class);
                break;
            case BLUE:
            default:
                ProgrammersGame.assetManager.load("models/BlueCarHard/BlueCarHard.obj", Model.class);
        }
    }

    @Override
    void doneLoading() {
        switch (color) {
            case RED:
                model = ProgrammersGame.assetManager.get("models/RedCarHard/RedCarHard.obj", Model.class);
                break;
            case GREEN:
                model = ProgrammersGame.assetManager.get("models/GreenCarHard/GreenCarHard.obj", Model.class);
                break;
            case YELLOW:
                model = ProgrammersGame.assetManager.get("models/YellowCarHard/YellowCarHard.obj", Model.class);
                break;
            case BLUE:
            default:
                model = ProgrammersGame.assetManager.get("models/BlueCarHard/BlueCarHard.obj", Model.class);
        }
        ModelInstance modelInstance = new ModelInstance(model);
        modelInstance.transform.translate(new Vector3(x * Chunk.width, z * Chunk.height, y * Chunk.width).add(Field.getOffset()));
        //modelInstance.transform.rotate(new Vector3(0, 1, 0), 90);
        ProgrammersGame.instances.add(modelInstance);
    }
}
