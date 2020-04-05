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

    boolean wallForward, wallBack, wallLeft, wallRight;

    Chunk lift;
    Color color;
    Car car;
    Array<Life> lives = new Array<>();
    Field field;
    private String modelFileName;

    Chunk(final int x, final int y, final int z, final Color color, final Field field) {
        super(x, y, z);
        this.color = color;
        this.field = field;
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
        modelInstance.transform.setTranslation(new Vector3(
                getX() * width,
                getY() * height,
                getZ() * width
        ).add(field.getOffset()));
        modelInstance.materials.get(0).set(ColorAttribute.createDiffuse(color));
        modelInstance.transform.rotate(Vector3.Y, 90f * random.nextInt(4));
        ProgrammersGame.instances.add(modelInstance);
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
}
