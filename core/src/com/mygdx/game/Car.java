package com.mygdx.game;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.StringBuilder;

import static com.badlogic.gdx.math.MathUtils.random;

class Car extends GameObject implements ICard {

    private String modelFileName;
    private Direction direction;

    private static final int size = ProgrammersGame.getSize();

    private Field field;
    private Player player;
    private Array<Life> lives = new Array<>(3);
    private Chunk base;
    private boolean compensated;

    enum Color {
        RED,
        GREEN,
        YELLOW,
        BLUE
    }

    Car(final Base base, final Player player) {
        super(base.getX(), base.getY() + 1, base.getZ());
        this.field = base.getField();
        this.base = base;
        this.player = player;
        base.setCar(this);
        StringBuilder stringBuilder = new StringBuilder("Models/");
        switch (ProgrammersGame.difficulty) {
            case Hard:
                stringBuilder.append("Hard");
                break;
            case Easy:
                stringBuilder.append("Easy");
        }
        stringBuilder.append("Mode/Cars/");
        switch (base.getBaseColor()) {
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

    Player getPlayer() {
        return player;
    }

    Array<Life> getLives() {
        return lives;
    }

    @Override
    void setX(final int x) {
        super.setX(x);
        if (getModelInstance() != null) {
            getModelInstance().transform.setTranslation(new Vector3(x * Chunk.width,
                    getY() * Chunk.height, getZ() * Chunk.width).add(field.getOffset()));
        }
    }

    @Override
    void setY(final int y) {
        super.setY(y);
        if (getModelInstance() != null) {
            getModelInstance().transform.setTranslation(new Vector3(getX() * Chunk.width,
                    y * Chunk.height, getZ() * Chunk.width).add(field.getOffset()));
        }
    }

    @Override
    void setZ(final int z) {
        super.setZ(z);
        if (getModelInstance() != null) {
            getModelInstance().transform.setTranslation(new Vector3(getX() * Chunk.width,
                    getY() * Chunk.height, z * Chunk.width).add(field.getOffset()));
        }
    }

    @Override
    void setPosition(final int x, final int y, final int z) {
        super.setPosition(x, y, z);
        if (getModelInstance() != null) {
            getModelInstance().transform.setTranslation(new Vector3(
                    getX() * Chunk.width + 0.001f,
                    getY() * Chunk.height + 0.001f,
                    getZ() * Chunk.width + 0.001f
            ).add(field.getOffset()));
        }
    }

    @Override
    void setPosition(GameObject other) {
        super.setPosition(other);
        setY(getY() + 1);
        if (getModelInstance() != null) {
            getModelInstance().transform.setTranslation(new Vector3(
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
        setModel(ProgrammersGame.assetManager.get(modelFileName, Model.class));
        setModelInstance(new ModelInstance(getModel()));
        getModelInstance().transform.setTranslation(new Vector3(
                getX() * Chunk.width + 0.001f,
                getY() * Chunk.height + 0.001f,
                getZ() * Chunk.width + 0.001f
        ).add(field.getOffset()));
        if (getX() == 0 && getZ() == 0) {
            if (field.getChunks()[0][0].getY() != field.getChunks()[0][1].getY()) {
                direction = Direction.Left;
            } else if (field.getChunks()[0][0].getY() != field.getChunks()[1][0].getY()) {
                direction = Direction.Forward;
            } else {
                direction = random.nextBoolean() ? Direction.Forward : Direction.Left;
            }
        } else if (getX() == 0 && getZ() == size - 1) {
            if (field.getChunks()[0][size - 1].getY() != field.getChunks()[0][size - 2].getY()) {
                direction = Direction.Left;
            } else if (field.getChunks()[0][size - 1].getY() != field.getChunks()[1][size - 1].getY()) {
                direction = Direction.Back;
            } else {
                direction = random.nextBoolean() ? Direction.Back : Direction.Left;
            }
        } else if (getX() == size - 1 && getZ() == 0) {
            if (field.getChunks()[size - 1][0].getY() != field.getChunks()[size - 2][0].getY()) {
                direction = Direction.Forward;
            } else if (field.getChunks()[size - 1][0].getY() != field.getChunks()[size - 1][1].getY()) {
                direction = Direction.Right;
            } else {
                direction = random.nextBoolean() ? Direction.Forward : Direction.Right;
            }
        } else {
            if (field.getChunks()[size - 1][size - 1].getY()
                    != field.getChunks()[size - 2][size - 1].getY()) {
                direction = Direction.Back;
            } else if (field.getChunks()[size - 1][size - 1].getY()
                    != field.getChunks()[size - 1][size - 2].getY()) {
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
        ProgrammersGame.instances.add(getModelInstance());
    }

    @Override
    public boolean stepForward() {
        boolean isInBounds, isWall;
        Chunk thisChunk = field.getChunks()[getX()][getZ()], nextChunk;
        Procedure move;
        switch (direction) {
            case Forward:
                isInBounds = getZ() != size - 1;
                nextChunk = field.getChunks()[getX()][getZ() + 1];
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
                nextChunk = field.getChunks()[getX()][getZ() - 1];
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
                nextChunk = field.getChunks()[getX() + 1][getZ()];
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
                nextChunk = field.getChunks()[getX() - 1][getZ()];
                isWall = thisChunk.wallRight;
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
                    addLivesFrom(field.getChunks()[getX()][getZ()]);
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
        Chunk thisChunk = field.getChunks()[getX()][getZ()], nextChunk;
        Procedure move;
        switch (direction) {
            case Forward:
                isInBounds = getZ() != size - 1;
                nextChunk = field.getChunks()[getX()][getZ() + 1];
                move = new Procedure() {
                    @Override
                    public void call() {
                        setZ(getZ() + 1);
                    }
                };
                break;
            case Back:
                isInBounds = getZ() != 0;
                nextChunk = field.getChunks()[getX()][getZ() - 1];
                move = new Procedure() {
                    @Override
                    public void call() {
                        setZ(getZ() - 1);
                    }
                };
                break;
            case Left:
                isInBounds = getX() != size - 1;
                nextChunk = field.getChunks()[getX() + 1][getZ()];
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
                nextChunk = field.getChunks()[getX() - 1][getZ()];
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
                addLivesFrom(field.getChunks()[getX()][getZ()]);
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
                nextChunk = field.getChunks()[getX()][impulse];
                isInBounds = impulse < size;
                break;
            case Back:
                impulse = getZ() - 1;
                nextChunk = field.getChunks()[getX()][impulse];
                break;
            case Left:
                impulse = getX() + 1;
                nextChunk = field.getChunks()[impulse][getZ()];
                isInBounds = impulse < size;
                break;
            case Right:
            default:
                impulse = getX() - 1;
                nextChunk = field.getChunks()[impulse][getZ()];
        }
        while (isInBounds && nextChunk.getY() < height) {
            switch (direction) {
                case Forward:
                case Back:
                    nextChunk = field.getChunks()[getX()][impulse];
                    break;
                case Right:
                case Left:
                default:
                    nextChunk = field.getChunks()[impulse][getZ()];
            }
            if (nextChunk.getY() == height - 1) {
                if (nextChunk.getCar() != null) {
                    if (nextChunk.getCar().base.getCar() == null) {
                        Car car = nextChunk.getCar();
                        Chunk base = car.base;
                        for (Life life : car.lives) {
                            life.setPosition(nextChunk);
                            ProgrammersGame.instances.add(life.getModelInstance());
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
                            prevChunk = field.getChunks()[getX()][impulse - 1];
                            break;
                        case Back:
                            prevChunk = field.getChunks()[getX()][impulse + 1];
                            break;
                        case Left:
                            prevChunk = field.getChunks()[impulse - 1][getZ()];
                            break;
                        case Right:
                        default:
                            prevChunk = field.getChunks()[impulse + 1][getZ()];
                    }
                    for (Life life : nextChunk.getLives()) {
                        life.setPosition(prevChunk);
                    }
                    prevChunk.getLives().addAll(nextChunk.getLives());
                    nextChunk.getLives().clear();
                    addLivesFrom(field.getChunks()[getX()][getZ()]);
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

    void compensation(Car other) {
        if (!compensated) {
            if (lives.size < 3 && other.lives.size > 0) {
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
            ProgrammersGame.instances.removeValue(lives.get(lives.size - 1).getModelInstance(), false);
        }
        // if [chunk] is base:
        if ((chunk.getX() == 0 && chunk.getZ() == 0)
                || (chunk.getX() == ProgrammersGame.getSize() - 1 && chunk.getZ() == 0)
                || (chunk.getX() == 0 && chunk.getZ() == ProgrammersGame.getSize() - 1)
                || (chunk.getX() == ProgrammersGame.getSize() - 1 && chunk.getZ() == ProgrammersGame.getSize() - 1)) {
            player.getLives().addAll(lives);
            for (Life life : lives) {
                player.addScore(life.getType());
            }
            lives.clear();
        }
    }
}
