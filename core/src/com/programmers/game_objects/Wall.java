package com.programmers.game_objects;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.programmers.enums.Direction;

public final class Wall extends GameObject {

    private final static int[] chances = {};

    private final Vector3 offset;
    private final Chunk chunk;
    private final Direction direction;

    public Wall(final Chunk chunk, final Direction direction) {
        super(chunk.getX(), chunk.getY() + 1, chunk.getZ(), chunk.getGameScreen());
        this.chunk = chunk;
        this.direction = direction;
        switch (direction) {
            case Left:
                offset = new Vector3(Chunk.width / 2 + 0.001f, 0.001f, 0.001f);
                break;
            case Right:
                offset = new Vector3(-Chunk.width / 2 + 0.001f, 0.001f, 0.001f);
                break;
            case Forward:
                offset = new Vector3(0.001f, 0.001f, Chunk.width / 2 + 0.001f);
                break;
            case Back:
            default:
                offset = new Vector3(0.001f, 0.001f, -Chunk.width / 2 + 0.001f);
        }
        setModelFileName("Models/Terrain/Walls/Wall1/Wall1.obj");
    }

    @Override
    public void loadModel() {
        setModel(getGameScreen().getAssetManager().get(getModelFileName(), Model.class));
        setModelInstance(new ModelInstance(getModel()));
        getModelInstance().transform.setTranslation(new Vector3(
                getX() * Chunk.width,
                getY() * Chunk.height,
                getZ() * Chunk.width
        ).add(chunk.getField().getOffset()).add(offset));
        switch (direction) {
            case Forward:
                break;
            case Back:
                getModelInstance().transform.rotate(Vector3.Y, 180f);
                break;
            case Left:
                getModelInstance().transform.rotate(Vector3.Y, 90f);
                break;
            case Right:
            default:
                getModelInstance().transform.rotate(Vector3.Y, -90f);
        }
        getGameScreen().getInstances().add(getModelInstance());
    }
}
