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
    private ProgrammersGame.Direction direction;

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
            modelInstance.transform.setTranslation(new Vector3(getX() * Chunk.width,
                    getY() * Chunk.height, getZ() * Chunk.width).add(field.getOffset()));
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
                direction = ProgrammersGame.Direction.Left;
            } else if (field.chunks[0][0].getY() != field.chunks[1][0].getY()) {
                direction = ProgrammersGame.Direction.Forward;
            } else {
                direction = random.nextBoolean() ? ProgrammersGame.Direction.Forward : ProgrammersGame.Direction.Left;
            }
        } else if (getX() == 0 && getZ() == ProgrammersGame.size - 1) {
            if (field.chunks[0][ProgrammersGame.size - 1].getY() != field.chunks[0][ProgrammersGame.size - 2].getY()) {
                direction = ProgrammersGame.Direction.Left;
            } else if (field.chunks[0][ProgrammersGame.size - 1].getY() != field.chunks[1][ProgrammersGame.size - 1].getY()) {
                direction = ProgrammersGame.Direction.Back;
            } else {
                direction = random.nextBoolean() ? ProgrammersGame.Direction.Back : ProgrammersGame.Direction.Left;
            }
        } else if (getX() == ProgrammersGame.size - 1 && getZ() == 0) {
            if (field.chunks[ProgrammersGame.size - 1][0].getY() != field.chunks[ProgrammersGame.size - 2][0].getY()) {
                direction = ProgrammersGame.Direction.Forward;
            } else if (field.chunks[ProgrammersGame.size - 1][0].getY() != field.chunks[ProgrammersGame.size - 1][1].getY()) {
                direction = ProgrammersGame.Direction.Right;
            } else {
                direction = random.nextBoolean() ? ProgrammersGame.Direction.Forward : ProgrammersGame.Direction.Right;
            }
        } else {
            if (field.chunks[ProgrammersGame.size - 1][ProgrammersGame.size - 1].getY()
                    != field.chunks[ProgrammersGame.size - 2][ProgrammersGame.size - 1].getY()) {
                direction = ProgrammersGame.Direction.Back;
            } else if (field.chunks[ProgrammersGame.size - 1][ProgrammersGame.size - 1].getY()
                    != field.chunks[ProgrammersGame.size - 1][ProgrammersGame.size - 2].getY()) {
                direction = ProgrammersGame.Direction.Right;
            } else {
                direction = random.nextBoolean() ? ProgrammersGame.Direction.Back : ProgrammersGame.Direction.Right;
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
        switch (direction) {
            case Forward:
                if ((getZ() != ProgrammersGame.size - 1)
                        && (field.chunks[getX()][getZ()].getY() == field.chunks[getX()][getZ() + 1].getY())
                        && !field.chunks[getX()][getZ()].wallForward) {
                    if (field.chunks[getX()][getZ() + 1].car == null) {
                        compensated = false;
                        field.chunks[getX()][getZ()].car = null;
                        setZ(getZ() + 1);
                        field.chunks[getX()][getZ()].car = this;
                        if (field.chunks[getX()][getZ()].lives.size > 0 && lives.size < 3) {
                            for (int i = lives.size; i <= 3 && field.chunks[getX()][getZ()].lives.size > 0; i++) {
                                lives.add(field.chunks[getX()][getZ()].lives.get(field.chunks[getX()][getZ()].lives.size - 1));
                                field.chunks[getX()][getZ()].lives.removeIndex(field.chunks[getX()][getZ()].lives.size - 1);
                                ProgrammersGame.instances.removeValue(lives.get(lives.size - 1).modelInstance, false);
                            }
                            return false;
                        }
                        return true;
                    } else {
                        compensation(field.chunks[getX()][getZ() + 1].car);
                        return false;
                    }
                }
                break;
            case Back:
                if ((getZ() != 0)
                        && (field.chunks[getX()][getZ()].getY() == field.chunks[getX()][getZ() - 1].getY())
                        && !field.chunks[getX()][getZ()].wallBack) {
                    if (field.chunks[getX()][getZ() - 1].car == null) {
                        compensated = false;
                        field.chunks[getX()][getZ()].car = null;
                        setZ(getZ() - 1);
                        field.chunks[getX()][getZ()].car = this;
                        if (field.chunks[getX()][getZ()].lives.size != 0 && lives.size < 3) {
                            for (int i = lives.size; i <= 3 && field.chunks[getX()][getZ()].lives.size > 0; i++) {
                                lives.add(field.chunks[getX()][getZ()].lives.get(field.chunks[getX()][getZ()].lives.size - 1));
                                field.chunks[getX()][getZ()].lives.removeIndex(field.chunks[getX()][getZ()].lives.size - 1);
                                ProgrammersGame.instances.removeValue(lives.get(lives.size - 1).modelInstance, false);
                            }
                            return false;
                        }
                        return true;
                    } else {
                        compensation(field.chunks[getX()][getZ() - 1].car);
                        return false;
                    }
                }
                break;
            case Left:
                if ((getX() != ProgrammersGame.size - 1)
                        && (field.chunks[getX()][getZ()].getY() == field.chunks[getX() + 1][getZ()].getY())
                        && !field.chunks[getX()][getZ()].wallLeft) {
                    if (field.chunks[getX() + 1][getZ()].car == null) {
                        compensated = false;
                        field.chunks[getX()][getZ()].car = null;
                        setX(getX() + 1);
                        field.chunks[getX()][getZ()].car = this;
                        if (field.chunks[getX()][getZ()].lives.size != 0 && lives.size < 3) {
                            for (int i = lives.size; i <= 3 && field.chunks[getX()][getZ()].lives.size > 0; i++) {
                                lives.add(field.chunks[getX()][getZ()].lives.get(field.chunks[getX()][getZ()].lives.size - 1));
                                field.chunks[getX()][getZ()].lives.removeIndex(field.chunks[getX()][getZ()].lives.size - 1);
                                ProgrammersGame.instances.removeValue(lives.get(lives.size - 1).modelInstance, false);
                            }
                            return false;
                        }
                        return true;
                    } else {
                        compensation(field.chunks[getX() + 1][getZ()].car);
                        return false;
                    }
                }
                break;
            case Right:
                if ((getX() != 0)
                        && (field.chunks[getX()][getZ()].getY() == field.chunks[getX() - 1][getZ()].getY())
                        && !field.chunks[getX()][getZ()].wallRight) {
                    if (field.chunks[getX() - 1][getZ()].car == null) {
                        compensated = false;
                        field.chunks[getX()][getZ()].car = null;
                        setX(getX() - 1);
                        field.chunks[getX()][getZ()].car = this;
                        if (field.chunks[getX()][getZ()].lives.size != 0 && lives.size < 3) {
                            for (int i = lives.size; i <= 3 && field.chunks[getX()][getZ()].lives.size > 0; i++) {
                                lives.add(field.chunks[getX()][getZ()].lives.get(field.chunks[getX()][getZ()].lives.size - 1));
                                field.chunks[getX()][getZ()].lives.removeIndex(field.chunks[getX()][getZ()].lives.size - 1);
                                ProgrammersGame.instances.removeValue(lives.get(lives.size - 1).modelInstance, false);
                            }
                            return false;
                        }
                        return true;
                    } else {
                        compensation(field.chunks[getX() - 1][getZ()].car);
                        return false;
                    }
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
        switch (direction) {
            case Forward:
                if (getZ() != ProgrammersGame.size - 1 &&
                        Math.abs(field.chunks[getX()][getZ() + 1].getY() - field.chunks[getX()][getZ()].getY()) != 2) {
                    if (field.chunks[getX()][getZ() + 1].car == null) {
                        compensated = false;
                        field.chunks[getX()][getZ()].car = null;
                        setZ(getZ() + 1);
                        field.chunks[getX()][getZ()].car = this;
                        setY(field.chunks[getX()][getZ()].getY() + 1);
                    } else {
                        compensation(field.chunks[getX()][getZ() + 1].car);
                    }
                }
                break;
            case Back:
                if (getZ() != 0 &&
                        Math.abs(field.chunks[getX()][getZ() - 1].getY() - field.chunks[getX()][getZ()].getY()) != 2) {
                    if (field.chunks[getX()][getZ() - 1].car == null) {
                        compensated = false;
                        field.chunks[getX()][getZ()].car = null;
                        setZ(getZ() - 1);
                        field.chunks[getX()][getZ()].car = this;
                        setY(field.chunks[getX()][getZ()].getY() + 1);
                    } else {
                        compensation(field.chunks[getX()][getZ() - 1].car);
                    }
                }
                break;
            case Left:
                if (getX() != ProgrammersGame.size - 1
                        && Math.abs(field.chunks[getX() + 1][getZ()].getY() - field.chunks[getX()][getZ()].getY()) != 2) {
                    if (field.chunks[getX() + 1][getZ()].car == null) {
                        compensated = false;
                        field.chunks[getX()][getZ()].car = null;
                        setX(getX() + 1);
                        field.chunks[getX()][getZ()].car = this;
                        setY(field.chunks[getX()][getZ()].getY() + 1);
                    } else {
                        compensation(field.chunks[getX() + 1][getZ()].car);
                    }
                }
                break;
            case Right:
                if (getX() != 0
                        && Math.abs(field.chunks[getX() - 1][getZ()].getY() - field.chunks[getX()][getZ()].getY()) != 2) {
                    if (field.chunks[getX() - 1][getZ()].car == null) {
                        compensated = false;
                        field.chunks[getX()][getZ()].car = null;
                        setX(getX() - 1);
                        field.chunks[getX()][getZ()].car = this;
                        setY(field.chunks[getX()][getZ()].getY() + 1);
                    } else {
                        compensation(field.chunks[getX() - 1][getZ()].car);
                    }
                }
        }
    }

    @Override
    public void turn90Left() {
        modelInstance.transform.rotate(new Vector3(0, 1, 0), 90f);
        switch (direction) {
            case Forward:
                direction = ProgrammersGame.Direction.Left;
                break;
            case Back:
                direction = ProgrammersGame.Direction.Right;
                break;
            case Left:
                direction = ProgrammersGame.Direction.Back;
                break;
            case Right:
                direction = ProgrammersGame.Direction.Forward;
        }
    }

    @Override
    public void turn90Right() {
        modelInstance.transform.rotate(new Vector3(0, 1, 0), -90f);
        switch (direction) {
            case Forward:
                direction = ProgrammersGame.Direction.Right;
                break;
            case Back:
                direction = ProgrammersGame.Direction.Left;
                break;
            case Left:
                direction = ProgrammersGame.Direction.Forward;
                break;
            case Right:
                direction = ProgrammersGame.Direction.Back;
        }
    }

    @Override
    public void turn180() {
        modelInstance.transform.rotate(new Vector3(0, 1, 0), 180f);
        switch (direction) {
            case Forward:
                direction = ProgrammersGame.Direction.Back;
                break;
            case Back:
                direction = ProgrammersGame.Direction.Forward;
                break;
            case Left:
                direction = ProgrammersGame.Direction.Right;
                break;
            case Right:
                direction = ProgrammersGame.Direction.Left;
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
        int height = getY();
        switch (direction) {
            case Forward:
                impulse = getZ() + 1;
                while (impulse < ProgrammersGame.size && field.chunks[getX()][impulse].getY() > height) {
                    if (field.chunks[getX()][impulse].getY() == height) {
                        if (field.chunks[getX()][impulse].car != null) {
                            if (field.chunks[getX()][impulse].car.base.car == null) {
                                Car car = field.chunks[getX()][impulse].car;
                                Chunk base = field.chunks[getX()][impulse].car.base;
                                field.chunks[getX()][impulse].lives.addAll(car.lives);
                                car.lives = new Array<>();
                                car.setPosition(base.getX(), base.getY(), base.getZ());
                            }
                            break;
                        } else if (field.chunks[getX()][impulse].lives.size != 0) {
                            for (Life life : field.chunks[getX()][impulse].lives) {
                                life.translate(0, 0, -1);
                            }
                            break;
                        }
                    }
                    impulse++;
                }
                break;
            case Back:
                impulse = getZ() - 1;
                while (impulse >= 0 && field.chunks[getX()][impulse].getY() > height) {
                    if (field.chunks[getX()][impulse].getY() == height) {
                        if (field.chunks[getX()][impulse].car != null) {
                            if (field.chunks[getX()][impulse].car.base.car == null) {
                                Car car = field.chunks[getX()][impulse].car;
                                Chunk base = field.chunks[getX()][impulse].car.base;
                                field.chunks[getX()][impulse].lives.addAll(car.lives);
                                car.lives = new Array<>();
                                car.setPosition(base.getX(), base.getY(), base.getZ());
                            }
                            break;
                        } else if (field.chunks[getX()][impulse].lives.size != 0) {
                            for (Life life : field.chunks[getX()][impulse].lives) {
                                life.translate(0, 0, 1);
                            }
                            break;
                        }
                    }
                    impulse--;
                }
                break;
            case Left:
                impulse = getX() + 1;
                while (impulse < ProgrammersGame.size && field.chunks[impulse][getZ()].getY() > height) {
                    if (field.chunks[impulse][getZ()].getY() == height) {
                        if (field.chunks[impulse][getZ()].car != null) {
                            if (field.chunks[impulse][getZ()].car.base.car == null) {
                                Car car = field.chunks[impulse][getZ()].car;
                                Chunk base = field.chunks[impulse][getZ()].car.base;
                                field.chunks[impulse][getZ()].lives.addAll(car.lives);
                                car.lives = new Array<>();
                                car.setPosition(base.getX(), base.getY(), base.getZ());
                            }
                            break;
                        } else if (field.chunks[impulse][getZ()].lives.size != 0) {
                            for (Life life : field.chunks[impulse][getZ()].lives) {
                                life.translate(-1, 0, 0);
                            }
                            break;
                        }
                    }
                    impulse++;
                }
                break;
            case Right:
                impulse = getX() - 1;
                while (impulse >= 0 && field.chunks[impulse][getZ()].getY() > height) {
                    if (field.chunks[impulse][getZ()].getY() == height) {
                        if (field.chunks[impulse][getZ()].car != null) {
                            if (field.chunks[impulse][getZ()].car.base.car == null) {
                                Car car = field.chunks[impulse][getZ()].car;
                                Chunk base = field.chunks[impulse][getZ()].car.base;
                                field.chunks[impulse][getZ()].lives.addAll(car.lives);
                                car.lives = new Array<>();
                                car.setPosition(base.getX(), base.getY(), base.getZ());
                            }
                            break;
                        } else if (field.chunks[impulse][getZ()].lives.size != 0) {
                            for (Life life : field.chunks[impulse][getZ()].lives) {
                                life.translate(1, 0, 0);
                            }
                            break;
                        }
                    }
                    impulse--;
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
