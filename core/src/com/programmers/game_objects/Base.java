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
        switch (getGameScreen().getDifficulty()) {
            case Easy:
                labColors = new Array<>(4);
                labColors.addAll(Car.Color.Red, Car.Color.Green, Car.Color.Yellow, Car.Color.Blue);
                break;
            case Hard:
            default:
                labColors = new Array<>(2);
        }
        stringBuilder.append(getProgrammersGame().getDifficulty()).append("Mode/Bases/");
        stringBuilder.append(baseColor).append("Base/").append(baseColor).append("Base.obj");
        switch (baseColor) {
            case Red:
                labColors.addAll(Car.Color.Blue, Car.Color.Yellow);
                break;
            case Green:
                labColors.addAll(Car.Color.Red, Car.Color.Yellow);
                break;
            case Yellow:
                labColors.addAll(Car.Color.Blue, Car.Color.Green);
                break;
            case Blue:
                labColors.addAll(Car.Color.Red, Car.Color.Green);
        }
        setModelFileName(stringBuilder.toString());
    }

    @Override
    public void loading() {
        getGameScreen().getAssetManager().load(getModelFileName(), Model.class);
    }

    @Override
    public void doneLoading() {
        setModel(getGameScreen().getAssetManager().get(getModelFileName(), Model.class));
        setModelInstance(new ModelInstance(getModel()));
        getModelInstance().transform.setTranslation(new Vector3(
                getX() * width,
                getY() * height,
                getZ() * width
        ).add(getField().getOffset()));
        getGameScreen().getInstances().add(getModelInstance());
    }

    public Car.Color getBaseColor() {
        return baseColor;
    }

    public Array<Car.Color> getLabColors() {
        return labColors;
    }
}
