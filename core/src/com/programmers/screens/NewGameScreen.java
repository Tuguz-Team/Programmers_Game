package com.programmers.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.programmers.enums.Difficulty;
import com.programmers.game.hotseat.HotseatGame;
import com.programmers.game.online.OnlineGameServer;
import com.programmers.network.GameServer;
import com.programmers.ui_elements.MyButton;

import java.io.IOException;

public final class NewGameScreen extends ReturnableScreen {

    private final ScreenLoader screenLoader;

    public NewGameScreen(final ScreenLoader screenLoader, final Screen previousScreen, final boolean isHotseat) {
        super(screenLoader, previousScreen);
        this.screenLoader = screenLoader;

        Table ui = new Table();
        ui.setFillParent(true);
        addActor(ui);

        final Skin skin = ScreenLoader.getDefaultGdxSkin();

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

        final HorizontalGroup difficultyRadioButton = new HorizontalGroup();
        difficultyRadioButton.addActor(EasyButton);
        difficultyRadioButton.addActor(HardButton);
        difficultyRadioButton.space(0.05f * Gdx.graphics.getWidth());

        final ButtonGroup<CheckBox> radioButtonController = new ButtonGroup<>(EasyButton, HardButton);
        radioButtonController.setChecked("EASY");

        ui.add(difficultyRadioButton)
                .spaceBottom(0.025f * Gdx.graphics.getHeight());
        ui.row();

        ui.add(new MyButton("START PLAYING !", screenLoader.getButtonStyle()) {
            @Override
            public void call() {
                if (isHotseat) {
                    dispose();
                    screenLoader.setScreen(new HotseatGame(screenLoader, radioButtonController.
                            getChecked().getText().toString().equals("EASY") ? Difficulty.Easy : Difficulty.Hard,
                            (int)playerCountSlider.getValue())
                    );
                } else {
                    Difficulty difficulty = radioButtonController
                            .getChecked().getText().toString().equals("EASY") ? Difficulty.Easy : Difficulty.Hard;
                    new WaitDialog(
                            "Waiting for players...", ScreenLoader.getDefaultGdxSkin(),
                            (int)playerCountSlider.getValue(),
                            NewGameScreen.this, difficulty
                    );
                }
            }
        }).space(0.1f * Gdx.graphics.getHeight());

        ui.center();
    }

    private static class WaitDialog extends Dialog {

        private Thread thread;
        private GameServer gameServer;

        private WaitDialog(final String title, final Skin skin,
                           final int playerCount, final NewGameScreen newGameScreen,
                           final Difficulty difficulty) {
            super(title, skin);
            setMovable(false);
            button("OK");
            final Label label = new Label("Connected players: 0", ScreenLoader.getDefaultGdxSkin());
            label.setWrap(true);
            label.setAlignment(Align.center);
            getContentTable().add(label);
            show(newGameScreen);
            try {
                gameServer = new GameServer();
            } catch (IOException ignored) { }
            gameServer.start();
            thread = new Thread() {
                @Override
                public void run() {
                    while (!isInterrupted()) {
                        try {
                            if (gameServer.getConnections().length < playerCount - 1) {
                                Gdx.app.log("ServerWait", "Connected players: " + gameServer.getConnections().length);
                                label.setText("Connected players: " + gameServer.getConnections().length);
                                sleep(1000);
                            } else {
                                newGameScreen.dispose();
                                newGameScreen.screenLoader.setScreen(new OnlineGameServer(
                                        newGameScreen.screenLoader, difficulty, playerCount, gameServer
                                ));
                                interrupt();
                            }
                        } catch (InterruptedException e) {
                            return;
                        }
                    }
                }
            };
            thread.start();
        }

        @Override
        protected void result(Object object) {
            gameServer.close();
            thread.interrupt();
        }
    }
}
