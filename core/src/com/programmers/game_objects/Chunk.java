package com.programmers.game_objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.StringBuilder;
import com.programmers.enums.Direction;
import com.programmers.game.Field;

import static com.badlogic.gdx.math.MathUtils.random;

public class Chunk extends GameObject {

    private final static int[] chances = { 100, 2, 100, 2, 100, 2, 100, 2, 100, 2 };
    public final static float width = 1.5f, height = 0.6f;

    private Wall wallForward, wallBack, wallLeft, wallRight;
    private Chunk lift;
    private Color color;
    private Car car;
    private final Array<Life> lives = new Array<>();
    private final Field field;

    public Chunk(final int x, final int y, final int z, final Color color, final Field field) {
        super(x, y, z, field.getGameScreen());
        this.color = color;
        this.field = field;
    }

    public Chunk getLift() {
        return lift;
    }

    public void setLift(final Chunk lift) {
        this.lift = lift;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(final Color color) {
        this.color = color;
    }

    public Field getField() {
        return field;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(final Car car) {
        this.car = car;
    }

    public Array<Life> getLives() {
        return lives;
    }

    public Wall getWallForward() {
        return wallForward;
    }

    public void setWallForward(Wall wallForward) {
        this.wallForward = wallForward;
    }

    public Wall getWallBack() {
        return wallBack;
    }

    public void setWallBack(Wall wallBack) {
        this.wallBack = wallBack;
    }

    public Wall getWallLeft() {
        return wallLeft;
    }

    public void setWallLeft(Wall wallLeft) {
        this.wallLeft = wallLeft;
    }

    public Wall getWallRight() {
        return wallRight;
    }

    public void setWallRight(Wall wallRight) {
        this.wallRight = wallRight;
    }

    private boolean hasWall(final Direction direction) {
        switch (direction) {
            case Forward:
                return wallForward != null;
            case Back:
                return wallBack != null;
            case Left:
                return wallLeft != null;
            case Right:
            default:
                return wallRight != null;
        }
    }

    private int getWallCount() {
        int temp = 0;
        if (wallRight != null) temp++;
        if (wallLeft != null) temp++;
        if (wallBack != null) temp++;
        if (wallForward != null) temp++;
        return temp;
    }

    public boolean canPlaceWall(final Direction direction) {
        Chunk other;
        switch (direction) {
            case Forward:
                if (hasWall(Direction.Back))
                    return false;
                if (getZ() < field.getSize() - 1) {
                    other = field.getChunks()[getX()][getZ() + 1];
                    break;
                } else return false;
            case Back:
                if (hasWall(Direction.Forward))
                    return false;
                if (getZ() > 0) {
                    other = field.getChunks()[getX()][getZ() - 1];
                    break;
                } else return false;
            case Left:
                if (hasWall(Direction.Right))
                    return false;
                if (getX() < field.getSize() - 1) {
                    other = field.getChunks()[getX() + 1][getZ()];
                    break;
                } else return false;
            case Right:
            default:
                if (hasWall(Direction.Left))
                    return false;
                if (getX() > 0) {
                    other = field.getChunks()[getX() - 1][getZ()];
                } else return false;
        }
        return !(this instanceof Base)
                && getLift() == null
                && !hasWall(direction)
                && getWallCount() < 2
                && !other.hasWall(direction)
                && !(other instanceof Base)
                && (other.getY() == getY())
                && other.getWallCount() < 2;
    }

    @Override
    public void loading() {
        StringBuilder stringBuilder = new StringBuilder("Models/Terrain/Layer");
        int nextInt = getRandomChunkIndex();
        if (nextInt % 2 == 0) {
            nextInt = (nextInt >> 1) + 1;
            stringBuilder.append(nextInt).append("/Layer").append(nextInt).append(".obj");
        } else {
            nextInt = (nextInt >> 1) + 1;
            stringBuilder.append("Bonus").append(nextInt).append("/LayerBonus").append(nextInt).append(".obj");
        }
        setModelFileName(stringBuilder.toString());
        getGameScreen().getAssetManager().load(getModelFileName(), Model.class);
        for (Life life : lives) {
            life.loading();
        }
        if (wallForward != null) wallForward.loading();
        if (wallBack != null) wallBack.loading();
        if (wallLeft != null) wallLeft.loading();
        if (wallRight != null) wallRight.loading();
    }

    @Override
    public void doneLoading() {
        setModel(getGameScreen().getAssetManager().get(getModelFileName(), Model.class));
        setModelInstance(new ModelInstance(getModel()));
        getModelInstance().transform.setTranslation(new Vector3(
                getX() * width,
                getY() * height,
                getZ() * width
        ).add(field.getOffset()));
        getModelInstance().materials.get(0).set(ColorAttribute.createDiffuse(color));
        getModelInstance().transform.rotate(Vector3.Y, 90f * random.nextInt(4));
        getGameScreen().getInstances().add(getModelInstance());
        for (Life life : lives) {
            life.doneLoading();
        }
        if (wallForward != null) wallForward.doneLoading();
        if (wallBack != null) wallBack.doneLoading();
        if (wallLeft != null) wallLeft.doneLoading();
        if (wallRight != null) wallRight.doneLoading();
    }

    private int getRandomChunkIndex() {
        Array<Integer> integers = new Array<>();
        int sum = 0;
        for (int i = 0; i < chances.length; i++) {
            integers.add(chances[i]);
            sum += integers.get(i);
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
