package com.programmers.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public final class GameAssets extends AssetManager {

    public GameAssets() {
        loadUI();
        loadModels();
    }

    private void loadUI() {
        load("uiskin.json", Skin.class);
        load("buttons.pack", TextureAtlas.class);
        load("CustomFont.png", Texture.class);
    }

    private void loadModels() {
        loadTerrain();
        loadLives();
        loadWalls();
        loadCars();
        loadLifts();
        loadBases();
    }

    private void loadTerrain() {
        load("Models/Terrain/Layer1/Layer1.g3db", Model.class);
        load("Models/Terrain/Layer2/Layer2.g3db", Model.class);
        load("Models/Terrain/Layer3/Layer3.g3db", Model.class);
        load("Models/Terrain/Layer4/Layer4.g3db", Model.class);
        load("Models/Terrain/Layer5/Layer5.g3db", Model.class);

        load("Models/Terrain/LayerBonus1/LayerBonus1.g3db", Model.class);
        load("Models/Terrain/LayerBonus2/LayerBonus2.g3db", Model.class);
        load("Models/Terrain/LayerBonus3/LayerBonus3.g3db", Model.class);
        load("Models/Terrain/LayerBonus4/LayerBonus4.g3db", Model.class);
        load("Models/Terrain/LayerBonus5/LayerBonus5.g3db", Model.class);
    }

    private void loadLives() {
        load("Models/LifeObjects/LifeObjectUnknown/LifeObjectUnknown.g3db", Model.class);
        load("Models/LifeObjects/LifeObjectBlue/LifeObjectBlue.g3db", Model.class);
        load("Models/LifeObjects/LifeObjectGreen/LifeObjectGreen.g3db", Model.class);
        load("Models/LifeObjects/LifeObjectPurple/LifeObjectPurple.g3db", Model.class);
        load("Models/LifeObjects/LifeObjectYellow/LifeObjectYellow.g3db", Model.class);
    }

    private void loadWalls() {
        load("Models/Terrain/Walls/Wall1/Wall1.obj", Model.class);
    }

    private void loadCars() {
        load("Models/EasyMode/Cars/BlueCar/BlueCar.g3db", Model.class);
        load("Models/EasyMode/Cars/GreenCar/GreenCar.g3db", Model.class);
        load("Models/EasyMode/Cars/RedCar/RedCar.g3db", Model.class);
        load("Models/EasyMode/Cars/YellowCar/YellowCar.g3db", Model.class);

        load("Models/HardMode/Cars/BlueCar/BlueCar.g3db", Model.class);
        load("Models/HardMode/Cars/GreenCar/GreenCar.g3db", Model.class);
        load("Models/HardMode/Cars/RedCar/RedCar.g3db", Model.class);
        load("Models/HardMode/Cars/YellowCar/YellowCar.g3db", Model.class);
    }

    private void loadLifts() {
        load("Models/Terrain/Lift/Lift.g3db", Model.class);
    }

    private void loadBases() {
        load("Models/EasyMode/Bases/BlueBase/BlueBase.g3db", Model.class);
        load("Models/EasyMode/Bases/GreenBase/GreenBase.g3db", Model.class);
        load("Models/EasyMode/Bases/RedBase/RedBase.g3db", Model.class);
        load("Models/EasyMode/Bases/YellowBase/YellowBase.g3db", Model.class);

        load("Models/HardMode/Bases/BlueBase/BlueBase.g3db", Model.class);
        load("Models/HardMode/Bases/GreenBase/GreenBase.g3db", Model.class);
        load("Models/HardMode/Bases/RedBase/RedBase.g3db", Model.class);
        load("Models/HardMode/Bases/YellowBase/YellowBase.g3db", Model.class);
    }
}
