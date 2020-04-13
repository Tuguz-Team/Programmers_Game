package com.programmers.game_objects;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.StringBuilder;
import com.programmers.game.Field;

public final class Base extends Chunk {

    private final Array<Car.Color> labColors;
    private final Car.Color baseColor;

    public Base(final int x, final int y, final int z, final Field field, final Car.Color baseColor) {
        super(x, y, z, null, field);
        this.baseColor = baseColor;
        StringBuilder stringBuilder = new StringBuilder("Models/");
        switch (getProgrammersGame().getDifficulty()) {
            case Easy:
                stringBuilder.append("Easy");
                labColors = new Array<>(4);
                labColors.addAll(Car.Color.RED, Car.Color.GREEN, Car.Color.YELLOW, Car.Color.BLUE);
                break;
            case Hard:
            default:
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
        setModelFileName(stringBuilder.toString());
    }

    @Override
    public void loading() {
        getProgrammersGame().getAssetManager().load(getModelFileName(), Model.class);
    }

    @Override
    public void doneLoading() {
        setModel(getProgrammersGame().getAssetManager().get(getModelFileName(), Model.class));
        setModelInstance(new ModelInstance(getModel()));
        getModelInstance().transform.setTranslation(new Vector3(
                getX() * width,
                getY() * height,
                getZ() * width
        ).add(getField().getOffset()));
        getProgrammersGame().getInstances().add(getModelInstance());
    }

    public Car.Color getBaseColor() {
        return baseColor;
    }

    public Array<Car.Color> getLabColors() {
        return labColors;
    }
}
