package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.StringBuilder;

import static com.badlogic.gdx.math.MathUtils.random;

class Chunk extends GameObject {

    private final static int[] chances = { 100, 2, 100, 2, 100, 2, 100, 2, 100, 2 };
    final static float width = 1.5f;
    final static float height = 0.6f;

    boolean impulse, hasLife, hasLift;
    boolean northWall, southWall, eastWall, westWall;

    private Car.Color baseColor;
    Color color;
    private Field field;
    private String modelFileName;

    Chunk(final int x, final int y, final int z, final Color color, final Field field) {
        super(x, y, z);
        this.color = color;
        this.field = field;
        if (this.color == null) {
            this.color = new Color(random.nextFloat(), random.nextFloat(), random.nextFloat(), 1);
        }
        StringBuilder stringBuilder = new StringBuilder("Models/Terrain/Layer");
        int nextInt = getRandomChunkIndex();
        if (nextInt % 2 == 0) {
            nextInt = (nextInt >> 1) + 1;
            stringBuilder.append(nextInt).append("/Layer").append(nextInt).append(".obj");
        } else {
            nextInt = (nextInt >> 1) + 1;
            stringBuilder.append("Bonus").append(nextInt).append("/LayerBonus").append(nextInt).append(".obj");
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
        modelInstance.transform.translate(new Vector3(getX() * width, getZ() * height, getY() * width)
                .add(field.getOffset()));
        if (baseColor == null) {
            modelInstance.materials.get(0).set(ColorAttribute.createDiffuse(color));
            modelInstance.transform.rotate(new Vector3(0, 1, 0), 90f * random.nextInt(4));
        }
        ProgrammersGame.instances.add(modelInstance);
    }

    Car.Color getBaseColor() {
        return baseColor;
    }

    private int getRandomChunkIndex() {
        Array<Integer> floats = new Array<>();
        int sum = 0;
        for (int i = 0; i < chances.length; i++) {
            floats.add(chances[i]);
            sum += floats.get(i);
        }
        int value = random.nextInt(sum + 1);
        sum = 0;
        for (int i = 0; i < chances.length; i++) {
            sum += chances[i];
            if (value <= sum) {
                return i;
            }
        }
        return 9;
    }

    void setBaseColor(final Car.Color baseColor) {
        this.baseColor = baseColor;
        StringBuilder stringBuilder = new StringBuilder("Models/");
        switch (ProgrammersGame.difficulty) {
            case Easy:
                stringBuilder.append("Easy");
                break;
            case Hard:
                stringBuilder.append("Hard");
        }
        stringBuilder.append("Mode/Bases/");
        switch (baseColor) {
            case RED:
                stringBuilder.append("RedBase/RedBase.obj");
                break;
            case GREEN:
                stringBuilder.append("GreenBase/GreenBase.obj");
                break;
            case YELLOW:
                stringBuilder.append("YellowBase/YellowBase.obj");
                break;
            case BLUE:
                stringBuilder.append("BlueBase/BlueBase.obj");
        }
        modelFileName = stringBuilder.toString();
    }
}
