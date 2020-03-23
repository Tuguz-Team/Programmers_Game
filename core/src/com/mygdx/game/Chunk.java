package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;

import static com.badlogic.gdx.math.MathUtils.random;

class Chunk extends GameObject {
    boolean impulse, isBase, hasLife, hasLift;
    boolean northWall, southWall, eastWall, westWall;
    GameColor labColor;

    Chunk(final int x, final int y, final int z, final Color color) {
        super(x, y, z);

        model = new ModelBuilder().createBox(1, 1, 1,
                new Material(ColorAttribute.createDiffuse(
                        color == null
                                ? new Color(random.nextFloat(), random.nextFloat(), random.nextFloat(), 1)
                                : color)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        modelInstance = new ModelInstance(model);
        modelInstance.transform.translate(new Vector3(x, z, y).add(Field.getOffset()));
    }
}