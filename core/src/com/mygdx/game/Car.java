package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;

public class Car {
    Model model;
    ModelInstance modelInstance;
    ModelBatch modelBatch;

    int x, y, height;
    GameColor color;

    Car(final int x, final int y, final int height, final GameColor color) {
        this.x = x;
        this.y = y;
        this.height = height;
        this.color = color;

        modelBatch = new ModelBatch();
        model = new ModelBuilder().createBox(1, 1, 1,
                new Material(ColorAttribute.createDiffuse(Color.RED)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        modelInstance = new ModelInstance(model);
        modelInstance.transform.translate(new Vector3(x, height, y).add(Field.getOffset()));
    }

    void render(PerspectiveCamera camera, Environment environment) {
        modelBatch.begin(camera);
        modelBatch.render(modelInstance, environment);
        modelBatch.end();
    }

    void dispose() {
        model.dispose();
        modelBatch.dispose();
    }
}
