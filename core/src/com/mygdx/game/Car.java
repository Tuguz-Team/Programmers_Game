package com.mygdx.game;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.StringBuilder;

import static com.badlogic.gdx.math.MathUtils.random;

class Car extends GameObject {

    private Field field;
    private String modelFileName;

    public enum Color {
        RED,
        GREEN,
        YELLOW,
        BLUE
    }

    Car(final int x, final int y, final int z, final Color color, final Field field) {
        super(x, y, z);
        this.field = field;
        StringBuilder stringBuilder = new StringBuilder("Models/");
        switch (ProgrammersGame.difficulty) {
            case Hard:
                stringBuilder.append("Hard");
                break;
            case Easy:
                stringBuilder.append("Easy");
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
        modelInstance.transform.translate(new Vector3(getX() * Chunk.width, getZ() * Chunk.height, getY() * Chunk.width)
                .add(field.getOffset()));
        modelInstance.transform.rotate(new Vector3(0, 1, 0), 90f * random.nextInt(4));
        ProgrammersGame.instances.add(modelInstance);
    }
}
