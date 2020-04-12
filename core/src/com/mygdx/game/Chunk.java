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

    private Wall wallForward;
    private Wall wallBack;
    private Wall wallLeft;
    private Wall wallRight;

    private Chunk lift;
    private Color color;
    private Car car;
    private final Array<Life> lives = new Array<>();
    private final Field field;

    Chunk(final int x, final int y, final int z, final Color color, final Field field) {
        super(x, y, z);
        this.color = color;
        this.field = field;
    }

    Chunk getLift() {
        return lift;
    }

    void setLift(final Chunk lift) {
        this.lift = lift;
    }

    Color getColor() {
        return color;
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

    Wall getWallForward() {
        return wallForward;
    }

    void setWallForward(Wall wallForward) {
        this.wallForward = wallForward;
    }

    Wall getWallBack() {
        return wallBack;
    }

    void setWallBack(Wall wallBack) {
        this.wallBack = wallBack;
    }

    Wall getWallLeft() {
        return wallLeft;
    }

    void setWallLeft(Wall wallLeft) {
        this.wallLeft = wallLeft;
    }

    Wall getWallRight() {
        return wallRight;
    }

    void setWallRight(Wall wallRight) {
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

    int getLivesCount() {
        int temp = 0;
        for (int i = getX() - 1; i <= getX() + 1; i++) {
            for (int j = getZ() - 1; j <= getZ() + 1; j++) {
                if (i >= 0 && i < field.getSize()
                        && j >= 0 && j < field.getSize()) {
                    temp += field.getChunks()[i][j].getLives().size;
                }
            }
        }
        return temp;
    }

    boolean canPlaceWall(final Direction direction) {
        Chunk other;
        switch (direction) {
            case Forward:
                if (getZ() < field.getSize() - 1) {
                    other = field.getChunks()[getX()][getZ() + 1];
                    break;
                } else return false;
            case Back:
                if (getZ() > 0) {
                    other = field.getChunks()[getX()][getZ() - 1];
                    break;
                } else return false;
            case Left:
                if (getX() < field.getSize() - 1) {
                    other = field.getChunks()[getX() + 1][getZ()];
                    break;
                } else return false;
            case Right:
            default:
                if (getX() > 0) {
                    other = field.getChunks()[getX() - 1][getZ()];
                } else return false;
        }
        return !(this instanceof Base)
                && getLift() == null
                && !hasWall(direction)
                && getWallCount() < 2
                && !other.hasWall(Direction.Left)
                && !(other instanceof Base)
                && (other.getY() == getY())
                && other.getWallCount() < 2;
    }

    @Override
    void loading() {
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
        ProgrammersGame.assetManager.load(getModelFileName(), Model.class);
        for (Life life : lives) {
            life.loading();
        }
        if (wallForward != null) wallForward.loading();
        if (wallBack != null) wallBack.loading();
        if (wallLeft != null) wallLeft.loading();
        if (wallRight != null) wallRight.loading();
    }

    @Override
    void doneLoading() {
        setModel(ProgrammersGame.assetManager.get(getModelFileName(), Model.class));
        setModelInstance(new ModelInstance(getModel()));
        getModelInstance().transform.setTranslation(new Vector3(
                getX() * width,
                getY() * height,
                getZ() * width
        ).add(field.getOffset()));
        getModelInstance().materials.get(0).set(ColorAttribute.createDiffuse(color));
        getModelInstance().transform.rotate(Vector3.Y, 90f * random.nextInt(4));
        ProgrammersGame.instances.add(getModelInstance());
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
