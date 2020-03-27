package com.mygdx.game;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.StringBuilder;

import static com.badlogic.gdx.math.MathUtils.random;

class Car extends GameObject {

    private Car.Color color;

    private static boolean loaded = false;

    public enum Color {
        RED,
        GREEN,
        YELLOW,
        BLUE
    }

    Car(final int x, final int y, final int z, final Color color) {
        super(x, y, z);
        this.color = color;
    }

    @Override
    void loading() {
        if (!loaded) {
            switch (ProgrammersGame.difficulty) {
                case Easy:
                    ProgrammersGame.assetManager.load("Models/EasyMode/Cars/RedCar/RedCar.obj", Model.class);
                    ProgrammersGame.assetManager.load("Models/EasyMode/Cars/GreenCar/GreenCar.obj", Model.class);
                    ProgrammersGame.assetManager.load("Models/EasyMode/Cars/YellowCar/YellowCar.obj", Model.class);
                    ProgrammersGame.assetManager.load("Models/EasyMode/Cars/BlueCar/BlueCar.obj", Model.class);
                    break;
                case Hard:
                    ProgrammersGame.assetManager.load("Models/HardMode/Cars/RedCar/RedCar.obj", Model.class);
                    ProgrammersGame.assetManager.load("Models/HardMode/Cars/GreenCar/GreenCar.obj", Model.class);
                    ProgrammersGame.assetManager.load("Models/HardMode/Cars/YellowCar/YellowCar.obj", Model.class);
                    ProgrammersGame.assetManager.load("Models/HardMode/Cars/BlueCar/BlueCar.obj", Model.class);
            }
            loaded = true;
        }
    }

    @Override
    void doneLoading() {
        // Get model name and set model
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
        model = ProgrammersGame.assetManager.get(stringBuilder.toString(), Model.class);
        // Set modelInstance
        modelInstance = new ModelInstance(model);
        modelInstance.transform.translate(new Vector3(getX() * Chunk.width, getZ() * Chunk.height, getY() * Chunk.width)
                .add(Field.getOffset()));
        modelInstance.transform.rotate(new Vector3(0, 1, 0), 90f * random.nextInt(4));
        ProgrammersGame.instances.add(modelInstance);
    }
}
