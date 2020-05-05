package com.programmers.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.programmers.enums.Difficulty;
import com.programmers.ui_elements.MyButton;

public final class PreGameScreen extends ReturnableScreen {

    public PreGameScreen(final ScreenLoader screenLoader, Screen previousScreen) {
        super(screenLoader, previousScreen);

        Table ui = new Table();
        ui.setFillParent(true);
        addActor(ui);

        final Skin skin = new Skin(Gdx.files.internal("uiskin.json"));

        final Slider playerCountSlider = new Slider(2, 4, 1,
                false, skin) {
            @Override
            public float getPrefWidth() {
                return 0.4f * Gdx.graphics.getWidth();
            }
        };
        playerCountSlider.setValue(playerCountSlider.getMaxValue());

        ui.add(new Label("Choose number of players in new room :", skin))
                .space(0.025f * Gdx.graphics.getHeight());
        ui.row();

        ui.add(playerCountSlider);
        ui.row();

        HorizontalGroup sliderDisplay = new HorizontalGroup();
        sliderDisplay.addActor(new Label("2", skin));
        sliderDisplay.addActor(new Label("3", skin));
        sliderDisplay.addActor(new Label("4", skin));
        sliderDisplay.space(0.47f * playerCountSlider.getPrefWidth());

        ui.add(sliderDisplay).
                spaceBottom(0.05f * Gdx.graphics.getHeight());
        ui.row();

        ui.add(new Label("Choose game difficulty in new room :", skin))
                .spaceBottom(0.025f * Gdx.graphics.getHeight());
        ui.row();

        CheckBox EasyButton = new CheckBox("EASY", skin);
        CheckBox HardButton = new CheckBox("HARD", skin);

        HorizontalGroup difficultyRadioButton = new HorizontalGroup();
        difficultyRadioButton.addActor(EasyButton);
        difficultyRadioButton.addActor(HardButton);
        difficultyRadioButton.space(0.05f * Gdx.graphics.getWidth());

        final ButtonGroup<CheckBox> radioButtonController = new ButtonGroup(EasyButton, HardButton);
        radioButtonController.setChecked("EASY");

        ui.add(difficultyRadioButton)
                .spaceBottom(0.025f * Gdx.graphics.getHeight());
        ui.row();

        ui.add(new MyButton("START PLAYING !", screenLoader.getButtonStyle()) {
            @Override
            public void call() {
                dispose();
                screenLoader.setScreen(new GameScreen(screenLoader, radioButtonController.
                        getChecked().getText().toString().equals("EASY") ? Difficulty.Easy : Difficulty.Hard,
                        (int)playerCountSlider.getValue()));
            }
        })
                .space(0.1f * Gdx.graphics.getHeight());

        ui.center();
    }
}
