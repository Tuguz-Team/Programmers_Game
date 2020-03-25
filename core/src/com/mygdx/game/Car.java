package com.mygdx.game;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.StringBuilder;

class Car extends GameObject {

    private Color color;
    private String modelFileName;

    public enum Color {
        RED,
        GREEN,
        YELLOW,
        BLUE
    }

    Car(final int x, final int y, final int z, final Color color) {
        super(x, y, z);
        this.color = color;
        StringBuilder stringBuilder = new StringBuilder("Models/");
        switch (ProgrammersGame.difficulty) {
            case Hard:
                stringBuilder.append("Hard");
                break;
            case Easy:
                stringBuilder.append("Easy");
                break;
        }
        stringBuilder.append("Mode/Cars/");
        switch (color) {
            case RED:
                stringBuilder.append("RedCar/RedCar.obj");
                break;
            case GREEN:
                stringBuilder.append("GreenCar/GreenCar.obj");
                break;
            case YELLOW:
                stringBuilder.append("YellowCar/YellowCar.obj");
                break;
            case BLUE:
                stringBuilder.append("BlueCar/BlueCar.obj");
        }
        modelFileName = stringBuilder.toString();
    }

    @Override
    void loading() {
        ProgrammersGame.assetManager.load(modelFileName, Model.class);
    }

    @Override
    void doneLoading() {
        model = ProgrammersGame.assetManager.get(modelFileName, Model.class);
        modelInstance = new ModelInstance(model);
        modelInstance.transform.translate(new Vector3(x * Chunk.width, z * Chunk.height, y * Chunk.width).add(Field.getOffset()));
        //modelInstance.transform.rotate(new Vector3(0, 1, 0), 90);
        ProgrammersGame.instances.add(modelInstance);
    }
}
