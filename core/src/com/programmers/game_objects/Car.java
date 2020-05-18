package com.programmers.game_objects;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.StringBuilder;
import com.programmers.enums.Direction;
import com.programmers.game.Player;
import com.programmers.interfaces.ICard;
import com.programmers.interfaces.Procedure;

import static com.badlogic.gdx.math.MathUtils.random;

public final class Car extends GameObject implements ICard {

    private final int size;

    private Player player;
    private final Array<Life> lives = new Array<>(3);
    private final Base base;

    private Direction direction;
    private boolean compensated;

    public enum Color {
        Red,
        Green,
        Yellow,
        Blue;

        public static Color fromInt(int i) {
            switch (i % 4) {
                case 0:
                    return Red;
                case 1:
                    return Green;
                case 2:
                    return Yellow;
                default:
                    return Blue;
            }
        }
    }

    public Car(final Base base) {
        super(base.getX(), base.getY() + 1, base.getZ(), base.getGameScreen());
        this.base = base;
        this.size = getGameScreen().getSize();
        base.setCar(this);
        StringBuilder stringBuilder = new StringBuilder("Models/");
        stringBuilder.append(getGameScreen().getDifficulty()).append("Mode/Cars/");
        final Color color = base.getBaseColor();
        stringBuilder.append(color).append("Car/").append(color).append("Car.g3db");
        setModelFileName(stringBuilder.toString());
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Array<Life> getLives() {
        return lives;
    }

    public Chunk getChunk() {
        return base.getField().getChunks()[getX()][getZ()];
    }

    @Override
    public void setX(final int x) {
        super.setX(x);
        if (getModelInstance() != null) {
            getModelInstance().transform.setTranslation(new Vector3(x * Chunk.width,
                    getY() * Chunk.height, getZ() * Chunk.width).add(base.getField().getOffset()));
        }
    }

    @Override
    public void setY(final int y) {
        super.setY(y);
        if (getModelInstance() != null) {
            getModelInstance().transform.setTranslation(new Vector3(getX() * Chunk.width,
                    y * Chunk.height, getZ() * Chunk.width).add(base.getField().getOffset()));
        }
    }

    @Override
    public void setZ(final int z) {
        super.setZ(z);
        if (getModelInstance() != null) {
            getModelInstance().transform.setTranslation(new Vector3(getX() * Chunk.width,
                    getY() * Chunk.height, z * Chunk.width).add(base.getField().getOffset()));
        }
    }

    @Override
    public void setPosition(final int x, final int y, final int z) {
        super.setPosition(x, y, z);
        if (getModelInstance() != null) {
            getModelInstance().transform.setTranslation(new Vector3(
                    getX() * Chunk.width + 0.001f,
                    getY() * Chunk.height + 0.001f,
                    getZ() * Chunk.width + 0.001f
            ).add(base.getField().getOffset()));
        }
    }

    @Override
    public void setPosition(GameObject other) {
        super.setPosition(other);
        setY(getY() + 1);
        if (getModelInstance() != null) {
            getModelInstance().transform.setTranslation(new Vector3(
                    getX() * Chunk.width + 0.001f,
                    getY() * Chunk.height + 0.001f,
                    getZ() * Chunk.width + 0.001f
            ).add(base.getField().getOffset()));
        }
    }

    @Override
    public void loadModel() {
        setModel(getGameScreen().getAssetManager().get(getModelFileName(), Model.class));
        setModelInstance(new ModelInstance(getModel()));
        getModelInstance().transform.setTranslation(new Vector3(
                getX() * Chunk.width + 0.001f,
                getY() * Chunk.height + 0.001f,
                getZ() * Chunk.width + 0.001f
        ).add(base.getField().getOffset()));
        if (getX() == 0 && getZ() == 0) {
            if (base.getField().getChunks()[0][0].getY() != base.getField().getChunks()[0][1].getY()) {
                direction = Direction.Left;
            } else if (base.getField().getChunks()[0][0].getY() != base.getField().getChunks()[1][0].getY()) {
                direction = Direction.Forward;
            } else {
                direction = random.nextBoolean() ? Direction.Forward : Direction.Left;
            }
        } else if (getX() == 0 && getZ() == size - 1) {
            if (base.getField().getChunks()[0][size - 1].getY() != base.getField().getChunks()[0][size - 2].getY()) {
                direction = Direction.Left;
            } else if (base.getField().getChunks()[0][size - 1].getY() != base.getField().getChunks()[1][size - 1].getY()) {
                direction = Direction.Back;
            } else {
                direction = random.nextBoolean() ? Direction.Back : Direction.Left;
            }
        } else if (getX() == size - 1 && getZ() == 0) {
            if (base.getField().getChunks()[size - 1][0].getY() != base.getField().getChunks()[size - 2][0].getY()) {
                direction = Direction.Forward;
            } else if (base.getField().getChunks()[size - 1][0].getY() != base.getField().getChunks()[size - 1][1].getY()) {
                direction = Direction.Right;
            } else {
                direction = random.nextBoolean() ? Direction.Forward : Direction.Right;
            }
        } else {
            if (base.getField().getChunks()[size - 1][size - 1].getY()
                    != base.getField().getChunks()[size - 2][size - 1].getY()) {
                direction = Direction.Back;
            } else if (base.getField().getChunks()[size - 1][size - 1].getY()
                    != base.getField().getChunks()[size - 1][size - 2].getY()) {
                direction = Direction.Right;
            } else {
                direction = random.nextBoolean() ? Direction.Back : Direction.Right;
            }
        }
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
                getModelInstance().transform.rotate(Vector3.Y, -90f);
        }
        getGameScreen().getInstances().add(getModelInstance());
    }

    @Override
    public boolean stepForward() {
        boolean isInBounds, isWall;
        Chunk thisChunk = base.getField().getChunks()[getX()][getZ()], nextChunk;
        Procedure move;
        switch (direction) {
            case Forward:
                isInBounds = getZ() != size - 1;
                if (!isInBounds)
                    return false;
                nextChunk = base.getField().getChunks()[getX()][getZ() + 1];
                isWall = thisChunk.getWallForward() != null;
                move = new Procedure() {
                    @Override
                    public void call() {
                        setZ(getZ() + 1);
                    }
                };
                break;
            case Back:
                isInBounds = getZ() != 0;
                if (!isInBounds)
                    return false;
                nextChunk = base.getField().getChunks()[getX()][getZ() - 1];
                isWall = thisChunk.getWallBack() != null;
                move = new Procedure() {
                    @Override
                    public void call() {
                        setZ(getZ() - 1);
                    }
                };
                break;
            case Left:
                isInBounds = getX() != size - 1;
                if (!isInBounds)
                    return false;
                nextChunk = base.getField().getChunks()[getX() + 1][getZ()];
                isWall = thisChunk.getWallLeft() != null;
                move = new Procedure() {
                    @Override
                    public void call() {
                        setX(getX() + 1);
                    }
                };
                break;
            case Right:
            default:
                isInBounds = getX() != 0;
                if (!isInBounds)
                    return false;
                nextChunk = base.getField().getChunks()[getX() - 1][getZ()];
                isWall = thisChunk.getWallRight() != null;
                move = new Procedure() {
                    @Override
                    public void call() {
                        setX(getX() - 1);
                    }
                };
        }
        if (isInBounds && (thisChunk.getY() == nextChunk.getY()) && !isWall) {
            if (nextChunk.getCar() == null) {
                compensated = false;
                thisChunk.setCar(null);
                move.call();
                nextChunk.setCar(this);
                thisChunk = nextChunk;
                if (!thisChunk.getLives().isEmpty() && lives.size < 3) {
                    addLivesFrom(base.getField().getChunks()[getX()][getZ()]);
                    return false;
                }
                return true;
            } else {
                compensation(nextChunk.getCar());
                return false;
            }
        }
        return false;
    }

    @Override
    public void stepForwardToFloor() {
        while (stepForward()) ;
    }

    @Override
    public void jump() {
        boolean isInBounds;
        Chunk thisChunk = base.getField().getChunks()[getX()][getZ()], nextChunk;
        Procedure move;
        switch (direction) {
            case Forward:
                isInBounds = getZ() != size - 1;
                if (!isInBounds)
                    return;
                nextChunk = base.getField().getChunks()[getX()][getZ() + 1];
                move = new Procedure() {
                    @Override
                    public void call() {
                        setZ(getZ() + 1);
                    }
                };
                break;
            case Back:
                isInBounds = getZ() != 0;
                if (!isInBounds)
                    return;
                nextChunk = base.getField().getChunks()[getX()][getZ() - 1];
                move = new Procedure() {
                    @Override
                    public void call() {
                        setZ(getZ() - 1);
                    }
                };
                break;
            case Left:
                isInBounds = getX() != size - 1;
                if (!isInBounds)
                    return;
                nextChunk = base.getField().getChunks()[getX() + 1][getZ()];
                move = new Procedure() {
                    @Override
                    public void call() {
                        setX(getX() + 1);
                    }
                };
                break;
            case Right:
            default:
                isInBounds = getX() != 0;
                if (!isInBounds)
                    return;
                nextChunk = base.getField().getChunks()[getX() - 1][getZ()];
                move = new Procedure() {
                    @Override
                    public void call() {
                        setX(getX() - 1);
                    }
                };
        }
        if (isInBounds && Math.abs(nextChunk.getY() - thisChunk.getY()) != 2) {
            if (nextChunk.getCar() == null) {
                compensated = false;
                thisChunk.setCar(null);
                move.call();
                nextChunk.setCar(this);
                thisChunk = nextChunk;
                setY(thisChunk.getY() + 1);
                addLivesFrom(thisChunk);
            } else {
                compensation(nextChunk.getCar());
            }
        }
    }

    @Override
    public void turn90Left() {
        getModelInstance().transform.rotate(new Vector3(0, 1, 0), 90f);
        switch (direction) {
            case Forward:
                direction = Direction.Left;
                break;
            case Back:
                direction = Direction.Right;
                break;
            case Left:
                direction = Direction.Back;
                break;
            case Right:
                direction = Direction.Forward;
        }
    }

    @Override
    public void turn90Right() {
        getModelInstance().transform.rotate(new Vector3(0, 1, 0), -90f);
        switch (direction) {
            case Forward:
                direction = Direction.Right;
                break;
            case Back:
                direction = Direction.Left;
                break;
            case Left:
                direction = Direction.Forward;
                break;
            case Right:
                direction = Direction.Back;
        }
    }

    @Override
    public void turn180() {
        getModelInstance().transform.rotate(new Vector3(0, 1, 0), 180f);
        switch (direction) {
            case Forward:
                direction = Direction.Back;
                break;
            case Back:
                direction = Direction.Forward;
                break;
            case Left:
                direction = Direction.Right;
                break;
            case Right:
                direction = Direction.Left;
        }
    }

    @Override
    public void cycle2(Procedure[] procedures) {
        for (int i = 0; i < 2; i++) {
            for (Procedure procedure : procedures) {
                procedure.call();
            }
        }
    }

    @Override
    public void cycle3(Procedure[] procedures) {
        for (int i = 0; i < 3; i++) {
            for (Procedure procedure : procedures) {
                procedure.call();
            }
        }
    }

    @Override
    public void teleport() {
        int impulse;
        final int height = getY();
        boolean isInBounds = true;
        Chunk nextChunk;
        switch (direction) {
            case Forward:
                impulse = getZ() + 1;
                nextChunk = base.getField().getChunks()[getX()][impulse];
                isInBounds = impulse < size;
                break;
            case Back:
                impulse = getZ() - 1;
                nextChunk = base.getField().getChunks()[getX()][impulse];
                break;
            case Left:
                impulse = getX() + 1;
                nextChunk = base.getField().getChunks()[impulse][getZ()];
                isInBounds = impulse < size;
                break;
            case Right:
            default:
                impulse = getX() - 1;
                nextChunk = base.getField().getChunks()[impulse][getZ()];
        }
        while (isInBounds && nextChunk.getY() < height) {
            switch (direction) {
                case Forward:
                case Back:
                    nextChunk = base.getField().getChunks()[getX()][impulse];
                    break;
                case Right:
                case Left:
                default:
                    nextChunk = base.getField().getChunks()[impulse][getZ()];
            }
            if (nextChunk.getY() == height - 1) {
                if (nextChunk.getCar() != null) {
                    if (nextChunk.getCar().base.getCar() == null) {
                        Car car = nextChunk.getCar();
                        Chunk base = car.base;
                        for (Life life : car.lives) {
                            life.setPosition(nextChunk);
                            getGameScreen().getInstances().add(life.getModelInstance());
                        }
                        nextChunk.getLives().addAll(car.lives);
                        car.lives.clear();
                        car.setPosition(base);
                    }
                    break;
                } else if (!nextChunk.getLives().isEmpty()) {
                    Chunk prevChunk;
                    switch (direction) {
                        case Forward:
                            prevChunk = base.getField().getChunks()[getX()][impulse - 1];
                            break;
                        case Back:
                            prevChunk = base.getField().getChunks()[getX()][impulse + 1];
                            break;
                        case Left:
                            prevChunk = base.getField().getChunks()[impulse - 1][getZ()];
                            break;
                        case Right:
                        default:
                            prevChunk = base.getField().getChunks()[impulse + 1][getZ()];
                    }
                    for (Life life : nextChunk.getLives()) {
                        life.setPosition(prevChunk);
                    }
                    prevChunk.getLives().addAll(nextChunk.getLives());
                    nextChunk.getLives().clear();
                    addLivesFrom(base.getField().getChunks()[getX()][getZ()]);
                    break;
                }
            }
            switch (direction) {
                case Forward:
                case Left:
                    impulse++;
                    isInBounds = impulse < size;
                    break;
                case Back:
                case Right:
                default:
                    impulse--;
                    isInBounds = impulse >= 0;
            }
        }
    }

    public void compensation(final Car other) {
        if (!compensated) {
            if (lives.size < 3 && !other.lives.isEmpty()) {
                lives.add(other.lives.get(other.lives.size - 1));
                other.lives.removeIndex(other.lives.size - 1);
            }
            other.compensated = true;
            compensated = true;
        }
    }

    private void addLivesFrom(final Chunk chunk) {
        for (int i = lives.size; i < 3 && !chunk.getLives().isEmpty(); i++) {
            lives.add(chunk.getLives().get(chunk.getLives().size - 1));
            chunk.getLives().removeIndex(chunk.getLives().size - 1);
            getGameScreen().getInstances().removeValue(lives.get(lives.size - 1).getModelInstance(), false);
        }
        // if [chunk] is base:
        if (chunk instanceof Base
                && ((Base)chunk).getLabColors().contains(base.getBaseColor(), true)) {
            player.getLives().addAll(lives);
            for (Life life : lives) {
                player.addScore(life.getType());
            }
            lives.clear();
        }
    }
}
