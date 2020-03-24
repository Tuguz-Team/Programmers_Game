package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class MyGestureDetector implements GestureDetector.GestureListener {

    private final float MIN_ZOOM = ProgrammersGame.size * Chunk.width + 1;
    private final float MAX_ZOOM = MIN_ZOOM + 4;

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        velocityX = 0;
        velocityY = 0;
        return true;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        velocityX *= Gdx.graphics.getDeltaTime();
        velocityX = MathUtils.clamp(velocityX, -100f, 100f);
        this.velocityX = velocityX;

        velocityY *= Gdx.graphics.getDeltaTime();
        velocityY = MathUtils.clamp(velocityY, -100f, 100f);
        this.velocityY = velocityY;

        return (velocityX != 0) && (velocityY != 0);
    }

    private float velocityX, velocityY;
    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        deltaX = MathUtils.clamp(deltaX, -100f, 100f);
        ProgrammersGame.camera.rotateAround(new Vector3(), new Vector3(0f, 1f, 0f),
                Gdx.graphics.getDeltaTime() * -deltaX * 3f);

        deltaY = MathUtils.clamp(deltaY, -100f, 100f);
        ProgrammersGame.camera.rotateAround(new Vector3(), new Vector3(0f, 1f, 0f).crs(ProgrammersGame.camera.direction),
                Gdx.graphics.getDeltaTime() * deltaY * 3f);

        velocityX = deltaX;
        velocityY = deltaY;
        return (deltaX != 0) && (deltaY != 0);
    }

    private boolean isVelXPositive, isVelYPositive;
    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        isVelXPositive = velocityX > 0;
        isVelYPositive = velocityY > 0;
        return true;
    }

    void cameraPosChange() {
        if ((isVelXPositive && velocityX > 0) || (!isVelXPositive && velocityX < 0)) {
            ProgrammersGame.camera.rotateAround(new Vector3(), new Vector3(0f, 1f, 0f),
                    Gdx.graphics.getDeltaTime() * -velocityX * 3f);
            velocityX += isVelXPositive
                    ? -Gdx.graphics.getDeltaTime() * 50f
                    : Gdx.graphics.getDeltaTime() * 50f;
        }

        if ((isVelYPositive && velocityY > 0) || (!isVelYPositive && velocityY < 0)) {
            ProgrammersGame.camera.rotateAround(new Vector3(), new Vector3(0f, 1f, 0f).crs(ProgrammersGame.camera.direction),
                    Gdx.graphics.getDeltaTime() * velocityY * 3f);
            velocityY += isVelYPositive
                    ? -Gdx.graphics.getDeltaTime() * 50f
                    : Gdx.graphics.getDeltaTime() * 50f;
        }
    }

    private float distanceOld;
    @Override
    public boolean zoom(float initialDistance, float distance) {
        float deltaZoomDistance = distance - distanceOld;
        deltaZoomDistance = MathUtils.clamp(deltaZoomDistance, -1f, 1f);
        ProgrammersGame.camera.translate(new Vector3().set(ProgrammersGame.camera.direction).scl(
                    Gdx.graphics.getDeltaTime() * deltaZoomDistance * 7f
            ));

        float vectorDistance = ProgrammersGame.camera.position.dst(new Vector3());
        vectorDistance = MathUtils.clamp(vectorDistance, MIN_ZOOM, MAX_ZOOM);
        ProgrammersGame.camera.position.nor().scl(vectorDistance);

        distanceOld = distance;
        return true;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return true;
    }

    @Override
    public void pinchStop() {

    }
}
