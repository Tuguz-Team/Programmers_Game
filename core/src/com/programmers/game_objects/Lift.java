package com.programmers.game_objects;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.math.Vector3;

public final class Lift extends Chunk {

    public Lift(Chunk from, Chunk lift) {
        super(from.getX(), from.getY(), from.getZ(), from.getColor(), from.getField());
        this.setLift(lift);
        lift.setLift(this);
        getLives().addAll(from.getLives());
    }

    @Override
    public void loadModel() {
        setModelFileName("Models/Terrain/Lift/Lift.g3db");
        //
        setModel(getGameScreen().getAssetManager().get(getModelFileName(), Model.class));
        setModelInstance(new ModelInstance(getModel()));
        getModelInstance().transform.setTranslation(new Vector3(
                getX() * width,
                getY() * height,
                getZ() * width
        ).add(getField().getOffset()));
        getModelInstance().materials.get(0).set(ColorAttribute.createDiffuse(getColor()));
        if (getLift().getX() < getX()) {
            getModelInstance().transform.rotate(Vector3.Y, -90f);
        } else if (getLift().getX() > getX()) {
            getModelInstance().transform.rotate(Vector3.Y, 90f);
        } else if (getLift().getZ() < getZ()) {
            getModelInstance().transform.rotate(Vector3.Y, 180f);
        }
        getGameScreen().getInstances().add(getModelInstance());
        for (Life life : getLives()) {
            life.loadModel();
        }
    }
}
