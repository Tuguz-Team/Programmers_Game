package com.mygdx.game;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.StringBuilder;

import static com.badlogic.gdx.math.MathUtils.random;

class Car extends GameObject implements ICards {

    private Field field;
    private String modelFileName;
    private Direction direction;

    private static final int size = ProgrammersGame.getSize();

    Array<Life> lives = new Array<>(3);
    private Chunk base;
    private boolean compensated;

    enum Color {
        RED,
        GREEN,
        YELLOW,
        BLUE
    }

    Car(final int x, final int y, final int z, final Color color, final Field field) {
        super(x, y, z);
        this.field = field;
        this.base = field.chunks[getX()][getZ()];
        field.chunks[getX()][getZ()].car = this;
        StringBuilder stringBuilder = new StringBuilder("Models/");
        switch (ProgrammersGame.difficulty) {
            case Hard:
                stringBuilder.append("Hard");
                break;
            case Easy:
                stringBuilder.append("Easy");
        }
        stringBuilder.append("Mode/Cars/");
        switch (color) {
            case RED:
                stringBuilder.append("RedCar/RedCar.obj");
                break;
            case GREEN:
                stringBuilder.append("GreenCar/GreenCar.obj");
                break;
            case YELLOW:
                stringBuilder.append("YellowCar/YellowCar.obj");
                break;
            case BLUE:
                stringBuilder.append("BlueCar/BlueCar.obj");
        }
        modelFileName = stringBuilder.toString();
    }

    @Override
    void setX(final int x) {
        super.setX(x);
        if (modelInstance != null) {
            modelInstance.transform.setTranslation(new Vector3(x * Chunk.width,
                    getY() * Chunk.height, getZ() * Chunk.width).add(field.getOffset()));
        }
    }

    @Override
    void setY(final int y) {
        super.setY(y);
        if (modelInstance != null) {
            modelInstance.transform.setTranslation(new Vector3(getX() * Chunk.width,
                    y * Chunk.height, getZ() * Chunk.width).add(field.getOffset()));
        }
    }

    @Override
    void setZ(final int z) {
        super.setZ(z);
        if (modelInstance != null) {
            modelInstance.transform.setTranslation(new Vector3(getX() * Chunk.width,
                    getY() * Chunk.height, z * Chunk.width).add(field.getOffset()));
        }
    }

    @Override
    void setPosition(final int x, final int y, final int z) {
        super.setPosition(x, y, z);
        if (modelInstance != null) {
            modelInstance.transform.setTranslation(new Vector3(
                    getX() * Chunk.width + 0.001f,
                    getY() * Chunk.height + 0.001f,
                    getZ() * Chunk.width + 0.001f
            ).add(field.getOffset()));
        }
    }

    @Override
    void loading() {
        ProgrammersGame.assetManager.load(modelFileName, Model.class);
    }

    @Override
    void doneLoading() {
        model = ProgrammersGame.assetManager.get(modelFileName, Model.class);
        modelInstance = new ModelInstance(model);
        modelInstance.transform.setTranslation(new Vector3(
                getX() * Chunk.width + 0.001f,
                getY() * Chunk.height + 0.001f,
                getZ() * Chunk.width + 0.001f
        ).add(field.getOffset()));
        if (getX() == 0 && getZ() == 0) {
            if (field.chunks[0][0].getY() != field.chunks[0][1].getY()) {
                direction = Direction.Left;
            } else if (field.chunks[0][0].getY() != field.chunks[1][0].getY()) {
                direction = Direction.Forward;
            } else {
                direction = random.nextBoolean() ? Direction.Forward : Direction.Left;
            }
        } else if (getX() == 0 && getZ() == size - 1) {
            if (field.chunks[0][size - 1].getY() != field.chunks[0][size - 2].getY()) {
                direction = Direction.Left;
            } else if (field.chunks[0][size - 1].getY() != field.chunks[1][size - 1].getY()) {
                direction = Direction.Back;
            } else {
                direction = random.nextBoolean() ? Direction.Back : Direction.Left;
            }
        } else if (getX() == size - 1 && getZ() == 0) {
            if (field.chunks[size - 1][0].getY() != field.chunks[size - 2][0].getY()) {
                direction = Direction.Forward;
            } else if (field.chunks[size - 1][0].getY() != field.chunks[size - 1][1].getY()) {
                direction = Direction.Right;
            } else {
                direction = random.nextBoolean() ? Direction.Forward : Direction.Right;
            }
        } else {
            if (field.chunks[size - 1][size - 1].getY()
                    != field.chunks[size - 2][size - 1].getY()) {
                direction = Direction.Back;
            } else if (field.chunks[size - 1][size - 1].getY()
                    != field.chunks[size - 1][size - 2].getY()) {
                direction = Direction.Right;
            } else {
                direction = random.nextBoolean() ? Direction.Back : Direction.Right;
            }
        }
        switch (direction) {
            case Forward:
                break;
            case Back:
                modelInstance.transform.rotate(Vector3.Y, 180f);
                break;
            case Left:
                modelInstance.transform.rotate(Vector3.Y, 90f);
                break;
            case Right:
                modelInstance.transform.rotate(Vector3.Y, -90f);
        }
        ProgrammersGame.instances.add(modelInstance);
    }

    @Override
    public boolean stepForward() {
        boolean isInBounds, isWall;
        Chunk thisChunk = field.chunks[getX()][getZ()], nextChunk;
        Procedure move;
        switch (direction) {
            case Forward:
                isInBounds = getZ() != size - 1;
                nextChunk = field.chunks[getX()][getZ() + 1];
                isWall = thisChunk.wallForward;
                move = new Procedure() {
                    @Override
                    public void call() {
                        setZ(getZ() + 1);
                    }
                };
                break;
            case Back:
                isInBounds = getZ() != 0;
                nextChunk = field.chunks[getX()][getZ() - 1];
                isWall = thisChunk.wallBack;
                move = new Procedure() {
                    @Override
                    public void call() {
                        setZ(getZ() - 1);
                    }
                };
                break;
            case Left:
                isInBounds = getX() != size - 1;
                nextChunk = field.chunks[getX() + 1][getZ()];
                isWall = thisChunk.wallLeft;
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
                nextChunk = field.chunks[getX() - 1][getZ()];
                isWall = thisChunk.wallRight;
                move = new Procedure() {
                    @Override
                    public void call() {
                        setX(getX() - 1);
                    }
                };
        }
        if (isInBounds && (thisChunk.getY() == nextChunk.getY()) && !isWall) {
            if (nextChunk.car == null) {
                compensated = false;
                thisChunk.car = null;
                move.call();
                nextChunk.car = this;
                thisChunk = nextChunk;
                if (thisChunk.lives.size > 0 && lives.size < 3) {
                    for (int i = lives.size; i <= 3 && !thisChunk.lives.isEmpty(); i++) {
                        lives.add(thisChunk.lives.get(thisChunk.lives.size - 1));
                        thisChunk.lives.removeIndex(thisChunk.lives.size - 1);
                        ProgrammersGame.instances.removeValue(lives.get(lives.size - 1).modelInstance, false);
                    }
                    return false;
                }
                return true;
            } else {
                compensation(nextChunk.car);
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
        Chunk thisChunk = field.chunks[getX()][getZ()], nextChunk;
        Procedure move;
        switch (direction) {
            case Forward:
                isInBounds = getZ() != size - 1;
                nextChunk = field.chunks[getX()][getZ() + 1];
                move = new Procedure() {
                    @Override
                    public void call() {
                        setZ(getZ() + 1);
                    }
                };
                break;
            case Back:
                isInBounds = getZ() != 0;
                nextChunk = field.chunks[getX()][getZ() - 1];
                move = new Procedure() {
                    @Override
                    public void call() {
                        setZ(getZ() - 1);
                    }
                };
                break;
            case Left:
                isInBounds = getX() != size - 1;
                nextChunk = field.chunks[getX() + 1][getZ()];
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
                nextChunk = field.chunks[getX() - 1][getZ()];
                move = new Procedure() {
                    @Override
                    public void call() {
                        setX(getX() - 1);
                    }
                };
        }
        if (isInBounds && Math.abs(nextChunk.getY() - thisChunk.getY()) != 2) {
            if (nextChunk.car == null) {
                compensated = false;
                thisChunk.car = null;
                move.call();
                nextChunk.car = this;
                thisChunk = nextChunk;
                setY(thisChunk.getY() + 1);
                if (thisChunk.lives.size > 0 && lives.size < 3) {
                    for (int i = lives.size; i <= 3 && !thisChunk.lives.isEmpty(); i++) {
                        lives.add(thisChunk.lives.get(thisChunk.lives.size - 1));
                        thisChunk.lives.removeIndex(thisChunk.lives.size - 1);
                        ProgrammersGame.instances.removeValue(lives.get(lives.size - 1).modelInstance, false);
                    }
                }
            } else {
                compensation(nextChunk.car);
            }
        }
    }

    @Override
    public void turn90Left() {
        modelInstance.transform.rotate(new Vector3(0, 1, 0), 90f);
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
        modelInstance.transform.rotate(new Vector3(0, 1, 0), -90f);
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
        modelInstance.transform.rotate(new Vector3(0, 1, 0), 180f);
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
                nextChunk = field.chunks[getX()][impulse];
                isInBounds = impulse < size;
                break;
            case Back:
                impulse = getZ() - 1;
                nextChunk = field.chunks[getX()][impulse];
                break;
            case Left:
                impulse = getX() + 1;
                nextChunk = field.chunks[impulse][getZ()];
                isInBounds = impulse < size;
                break;
            case Right:
            default:
                impulse = getX() - 1;
                nextChunk = field.chunks[impulse][getZ()];
        }
        while (isInBounds && nextChunk.getY() < height) {
            switch (direction) {
                case Forward:
                case Back:
                    nextChunk = field.chunks[getX()][impulse];
                    break;
                case Right:
                case Left:
                default:
                    nextChunk = field.chunks[impulse][getZ()];
            }
            if (nextChunk.getY() == height - 1) {
                if (nextChunk.car != null) {
                    if (nextChunk.car.base.car == null) {
                        Car car = nextChunk.car;
                        Chunk base = car.base;
                        for (Life life : car.lives) {
                            life.setPosition(nextChunk.getX(), nextChunk.getY(), nextChunk.getZ());
                            ProgrammersGame.instances.add(life.modelInstance);
                        }
                        nextChunk.lives.addAll(car.lives);
                        car.lives.clear();
                        car.setPosition(base.getX(), base.getY(), base.getZ());
                    }
                    break;
                } else if (!nextChunk.lives.isEmpty()) {
                    Chunk prevChunk;
                    switch (direction) {
                        case Forward:
                            prevChunk = field.chunks[getX()][impulse - 1];
                            break;
                        case Back:
                            prevChunk = field.chunks[getX()][impulse + 1];
                            break;
                        case Left:
                            prevChunk = field.chunks[impulse - 1][getZ()];
                            break;
                        case Right:
                        default:
                            prevChunk = field.chunks[impulse + 1][getZ()];
                    }
                    for (Life life : nextChunk.lives) {
                        life.setPosition(prevChunk.getX(), prevChunk.getY() + 1, prevChunk.getZ());
                    }
                    prevChunk.lives.addAll(nextChunk.lives);
                    nextChunk.lives.clear();
                    if (field.chunks[getX()][getZ()].lives.size != 0 && lives.size < 3) {
                        for (int i = lives.size; i <= 3 && !field.chunks[getX()][getZ()].lives.isEmpty(); i++) {
                            lives.add(field.chunks[getX()][getZ()].lives.get(field.chunks[getX()][getZ()].lives.size - 1));
                            field.chunks[getX()][getZ()].lives.removeIndex(field.chunks[getX()][getZ()].lives.size - 1);
                            ProgrammersGame.instances.removeValue(lives.get(lives.size - 1).modelInstance, false);
                        }
                    }
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

    private void compensation(Car other) {
        if (!compensated) {
            if (lives.size < 3 && other.lives.size > 0) {
                lives.add(other.lives.get(other.lives.size - 1));
                other.lives.removeIndex(other.lives.size - 1);
            }
            compensated = true;
        }
    }
}
