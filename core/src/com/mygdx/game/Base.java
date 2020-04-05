package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.StringBuilder;

class Base extends Chunk {

    Array<Car.Color> labColors;
    private String modelFileName;
    private Car.Color baseColor;

    Base(final int x, final int y, final int z, final Color color, final Field field, final Car.Color baseColor) {
        super(x, y, z, color, field);
        this.baseColor = baseColor;
        StringBuilder stringBuilder = new StringBuilder("Models/");
        switch (ProgrammersGame.difficulty) {
            case Easy:
                stringBuilder.append("Easy");
                labColors = new Array<>(4);
                labColors.addAll(Car.Color.RED, Car.Color.GREEN, Car.Color.YELLOW, Car.Color.BLUE);
                break;
            case Hard:
                stringBuilder.append("Hard");
                labColors = new Array<>(2);
        }
        stringBuilder.append("Mode/Bases/");
        switch (baseColor) {
            case RED:
                stringBuilder.append("RedBase/RedBase.obj");
                labColors.addAll(Car.Color.BLUE, Car.Color.YELLOW);
                break;
            case GREEN:
                stringBuilder.append("GreenBase/GreenBase.obj");
                labColors.addAll(Car.Color.RED, Car.Color.YELLOW);
                break;
            case YELLOW:
                stringBuilder.append("YellowBase/YellowBase.obj");
                labColors.addAll(Car.Color.BLUE, Car.Color.GREEN);
                break;
            case BLUE:
                stringBuilder.append("BlueBase/BlueBase.obj");
                labColors.addAll(Car.Color.RED, Car.Color.GREEN);
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
        modelInstance.transform.setTranslation(new Vector3(
                getX() * width,
                getY() * height,
                getZ() * width
        ).add(field.getOffset()));
        ProgrammersGame.instances.add(modelInstance);
    }

    Car.Color getBaseColor() {
        return baseColor;
    }
}
