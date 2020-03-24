package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.math.Vector3;

import static com.badlogic.gdx.math.MathUtils.random;

class Chunk extends GameObject {

    final static float width = 1.5f;
    final static float height = 0.7f;

    boolean impulse, isBase, hasLife, hasLift;
    boolean northWall, southWall, eastWall, westWall;
    Car.Color labColor;

    private Color color;

    Chunk(final int x, final int y, final int z, final Color color) {
        super(x, y, z);
        this.color = color;
    }


    @Override
    void loading() {
        ProgrammersGame.assetManager.load("models/Layer0.1/Layer0.1.obj", Model.class);
    }

    @Override
    void doneLoading() {
        model = ProgrammersGame.assetManager.get("models/Layer0.1/Layer0.1.obj", Model.class);
        ModelInstance modelInstance = new ModelInstance(model);
        if (color == null) {
            color = new Color(random.nextFloat(), random.nextFloat(), random.nextFloat(), 1);
        }
        modelInstance.materials.get(0).set(ColorAttribute.createDiffuse(color));
        modelInstance.transform.translate(new Vector3(x * width, z * height, y * width).add(Field.getOffset()));
        ProgrammersGame.instances.add(modelInstance);
    }
}
