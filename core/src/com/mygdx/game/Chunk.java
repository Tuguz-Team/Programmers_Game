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

    private final static int[] chances = { 100, 100, 100, 100, 100, 2, 2, 2, 2, 2 };
    final static float width = 1.5f;
    final static float height = 0.6f;

    boolean impulse, hasLife, hasLift;
    boolean northWall, southWall, eastWall, westWall;

    private Car.Color baseColor;
    Color color;
    private String modelFileName;

    private int getRandomChunkNum() {
        Array<Integer> floats = new Array<>();
        int sum = 0;
        for (int i = 0; i < chances.length; i++) {
            floats.add(chances[i]);
            sum += floats.get(i);
        }
        int value = random.nextInt(sum);
        sum = 0;
        for (int i = 0; i < chances.length; i++) {
            sum += chances[i];
            if (value < sum) {
                return i + 1;
            }
        }
        return 10;
    }

    void setBaseColor(Car.Color baseColor) {
        this.baseColor = baseColor;
        StringBuilder stringBuilder = new StringBuilder("Models/");
        if (ProgrammersGame.difficulty == ProgrammersGame.Difficulty.Easy) {
            stringBuilder.append("Easy");
        } else {
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

    Chunk(final int x, final int y, final int z, final Color color) {
        super(x, y, z);
        this.color = color;
        if (this.color == null) {
            this.color = new Color(random.nextFloat(), random.nextFloat(), random.nextFloat(), 1);
        }
        StringBuilder stringBuilder = new StringBuilder("Models/Terrain/");
        int nextInt = getRandomChunkNum();
        switch (nextInt) {
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                    stringBuilder.append("Layer").append(nextInt).append("/Layer").append(nextInt).append(".obj");
                    break;
                default:
                    nextInt -= 5;
                    stringBuilder.append("LayerBonus").append(nextInt).append("/LayerBonus").append(nextInt).append(".obj");
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
        modelInstance.transform.translate(new Vector3(x * width, z * height, y * width).add(Field.getOffset()));
        if (baseColor == null) {
            modelInstance.materials.get(0).set(ColorAttribute.createDiffuse(color));
            modelInstance.transform.rotate(new Vector3(0, 1, 0), 90f * random.nextInt(4));
        }
        ProgrammersGame.instances.add(modelInstance);
    }

    public Car.Color getBaseColor() {
        return baseColor;
    }
}
