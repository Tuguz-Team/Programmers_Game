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

    private Chunk lift;
    private Color color;
    private Car car;
    private final Array<Life> lives = new Array<>();
    private final Field field;
    private String modelFileName;

    Chunk getLift() {
        return lift;
    }

    void setLift(final Chunk lift) {
        this.lift = lift;
    }

    void setColor(final Color color) {
        this.color = color;
    }

    Field getField() {
        return field;
    }

    Car getCar() {
        return car;
    }

    void setCar(final Car car) {
        this.car = car;
    }

    Array<Life> getLives() {
        return lives;
    }

    Chunk(final int x, final int y, final int z, final Color color, final Field field) {
        super(x, y, z);
        this.color = color;
        this.field = field;
    }

    @Override
    void loading() {
        if (lift != null && lift.getY() < getY()) {
            modelFileName = "Models/Terrain/Lift/Lift.obj";
        } else {
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
        ProgrammersGame.assetManager.load(modelFileName, Model.class);
        for (Life life : lives) {
            life.loading();
        }
    }

    @Override
    void doneLoading() {
        setModel(ProgrammersGame.assetManager.get(modelFileName, Model.class));
        setModelInstance(new ModelInstance(getModel()));
        getModelInstance().transform.setTranslation(new Vector3(
                getX() * width,
                getY() * height,
                getZ() * width
        ).add(field.getOffset()));
        getModelInstance().materials.get(0).set(ColorAttribute.createDiffuse(color));
        if (lift != null && lift.getY() < getY()) {
            if (lift.getX() < getX()) {
                getModelInstance().transform.rotate(Vector3.Y, -90f);
            } else if (lift.getX() > getX()) {
                getModelInstance().transform.rotate(Vector3.Y, 90f);
            } else if (lift.getZ() < getZ()) {
                getModelInstance().transform.rotate(Vector3.Y, 180f);
            }
        } else {
            getModelInstance().transform.rotate(Vector3.Y, 90f * random.nextInt(4));
        }
        ProgrammersGame.instances.add(getModelInstance());
        for (Life life : lives) {
            life.doneLoading();
        }
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
