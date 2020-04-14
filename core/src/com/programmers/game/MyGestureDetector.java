package com.programmers.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import com.programmers.game_objects.Chunk;
import com.programmers.screens.GameScreen;

public class MyGestureDetector implements GestureDetector.GestureListener {

    private final int size;

    private final float MIN_ZOOM;
    private final float MAX_ZOOM;

    private final static float MIN_ANGLE = 30f;
    private final static float MAX_ANGLE = 60f;

    private final static float MAX_VELOCITY = 100f;
    private final static float MIN_VELOCITY = -MAX_VELOCITY;

    private final PerspectiveCamera camera;
    private boolean locked;

    public MyGestureDetector(final PerspectiveCamera camera, final GameScreen gameScreen) {
        this.camera = camera;
        size = gameScreen.getSize();
        MIN_ZOOM = size * Chunk.width;
        MAX_ZOOM = (size + 2) * Chunk.width;
        lockCamera();
    }

    public void lockCamera() {
        locked = true;
    }

    public void unlockCamera() {
        locked = false;
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        if (!locked) {
            velocityX = 0;
            velocityY = 0;
        }
        return true;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        if (!locked) {
            velocityX *= Gdx.graphics.getDeltaTime();
            velocityX = MathUtils.clamp(velocityX, MIN_VELOCITY, MAX_VELOCITY);
            this.velocityX = velocityX;

            velocityY *= Gdx.graphics.getDeltaTime();
            velocityY = MathUtils.clamp(velocityY, MIN_VELOCITY, MAX_VELOCITY);
            this.velocityY = velocityY;
        }
        return (velocityX != 0) && (velocityY != 0);
    }

    private float velocityX, velocityY;
    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        if (!locked) {
            velocityX = MathUtils.clamp(deltaX, MIN_VELOCITY, MAX_VELOCITY);
            camera.rotateAround(new Vector3(),
                    new Vector3(0f, 1f, 0f),
                    Gdx.graphics.getDeltaTime() * -velocityX * 2f);

            velocityY = MathUtils.clamp(deltaY, MIN_VELOCITY, MAX_VELOCITY);
            camera.rotateAround(new Vector3(),
                    new Vector3(0f, 1f, 0f).crs(camera.direction),
                    Gdx.graphics.getDeltaTime() * velocityY * 2f);
        }
        return (deltaX != 0) && (deltaY != 0);
    }

    private boolean isVelXPositive, isVelYPositive;
    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        if (!locked) {
            isVelXPositive = velocityX > 0;
            isVelYPositive = velocityY > 0;
        }
        return true;
    }

    public void cameraPosChange() {
        if (!locked) {
            if ((isVelXPositive && velocityX > 0) || (!isVelXPositive && velocityX < 0)) {
                camera.rotateAround(new Vector3(),
                        new Vector3(0f, 1f, 0f),
                        Gdx.graphics.getDeltaTime() * -velocityX * 2f);
                velocityX += isVelXPositive
                        ? -Gdx.graphics.getDeltaTime() * MAX_VELOCITY / 2
                        : Gdx.graphics.getDeltaTime() * MAX_VELOCITY / 2;
            }

            if ((isVelYPositive && velocityY > 0) || (!isVelYPositive && velocityY < 0)) {
                camera.rotateAround(new Vector3(),
                        new Vector3(0f, 1f, 0f).crs(camera.direction),
                        Gdx.graphics.getDeltaTime() * velocityY * 2f);
                velocityY += isVelYPositive
                        ? -Gdx.graphics.getDeltaTime() * MAX_VELOCITY / 2
                        : Gdx.graphics.getDeltaTime() * MAX_VELOCITY / 2;
            }

            float vectorAngle = VectorAngle(new Vector3(0, 1, 0), camera.position);
            if (vectorAngle < MIN_ANGLE) {
                camera.rotateAround(new Vector3(),
                        new Vector3(0f, 1f, 0f).crs(camera.direction),
                        vectorAngle - MIN_ANGLE);
            } else if (vectorAngle > MAX_ANGLE) {
                camera.rotateAround(new Vector3(),
                        new Vector3(0f, 1f, 0f).crs(camera.direction),
                        vectorAngle - MAX_ANGLE);
            }

            if (camera.up.y < 0) {
                camera.rotateAround(new Vector3(),
                        new Vector3(0f, 1f, 0f).crs(camera.direction),
                        VectorAngle(new Vector3(0, 1, 0), camera.position) * 2);
            }

            camera.lookAt(0f, 0f, 0f);
        }
    }

    private float VectorAngle(final Vector3 first, final Vector3 second) {
        return (float)(Math.acos(first.dot(second) / first.len() / second.len()) * 180f / Math.PI);
    }

    private float distanceOld;
    @Override
    public boolean zoom(float initialDistance, float distance) {
        if (!locked) {
            float deltaZoomDistance = distance - distanceOld;
            deltaZoomDistance = MathUtils.clamp(deltaZoomDistance, -1f, 1f);
            camera.translate(new Vector3().set(camera.direction).scl(
                    Gdx.graphics.getDeltaTime() * deltaZoomDistance * 7f
            ));

            float vectorDistance = camera.position.dst(new Vector3());
            vectorDistance = MathUtils.clamp(vectorDistance, MIN_ZOOM, MAX_ZOOM);
            camera.position.nor().scl(vectorDistance);

            distanceOld = distance;
        }
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
