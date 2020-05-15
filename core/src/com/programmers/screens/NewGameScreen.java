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
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.programmers.enums.Difficulty;
import com.programmers.game.hotseat.HotseatGame;
import com.programmers.game.online.OnlineGameServer;
import com.programmers.network.GameNetwork;
import com.programmers.network.GameNetwork.PlayersCount;
import com.programmers.network.GameServer;
import com.programmers.ui_elements.MyButton;

import java.io.IOException;

public final class NewGameScreen extends ReturnableScreen {

    private final ScreenLoader screenLoader;
    private GameServer gameServer;

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

        final WaitDialog waitDialog = new WaitDialog("Waiting for players...", ScreenLoader.getDefaultGdxSkin());
        ui.add(new MyButton("START PLAYING !", screenLoader.getButtonStyle()) {
            @Override
            public void call() {
                final Difficulty difficulty = radioButtonController.
                        getChecked().getText().toString().equals("EASY") ? Difficulty.Easy : Difficulty.Hard;
                if (isHotseat) {
                    dispose();
                    screenLoader.setScreen(new HotseatGame(
                            screenLoader, difficulty,
                            (int)playerCountSlider.getValue()
                    ));
                } else {
                    waitDialog.show(
                            (int)playerCountSlider.getValue(),
                            NewGameScreen.this,
                            difficulty
                    );
                }
            }
        }).space(0.1f * Gdx.graphics.getHeight());

        ui.center();
    }

    @Override
    public void dispose() {
        if (gameServer != null)
            gameServer.stop();
        super.dispose();
    }

    private class WaitDialog extends Dialog {

        private int playersCount;
        private NewGameScreen newGameScreen;
        private Difficulty difficulty;
        private final Listener listener;

        private WaitDialog(final String title, final Skin skin) {
            super(title, skin);
            setMovable(false);
            button("Close server");

            final Label label = new Label("Connected players: 0", ScreenLoader.getDefaultGdxSkin());
            label.setWrap(true);
            label.setAlignment(Align.center);
            getContentTable().add(label);

            listener = new Listener() {
                @Override
                public void connected(Connection connection) {
                    doStuff();
                }

                @Override
                public void received(Connection connection, Object object) {
                    if (object instanceof GameNetwork.Disconnect) {
                        try {
                            gameServer.update(0);
                            doStuff();
                        } catch (IOException ignored) { }
                    }
                }

                private void doStuff() {
                    label.setText("Connected players: " + gameServer.getConnections().length);
                    if (gameServer.getConnections().length == playersCount - 1) {
                        newGameScreen.dispose();
                        gameServer.removeListener(this);
                        newGameScreen.screenLoader.setScreen(new OnlineGameServer(
                                newGameScreen.screenLoader, difficulty, playersCount, gameServer
                        ));
                    }
                    PlayersCount playersCount = new PlayersCount();
                    playersCount.playersCount = gameServer.getConnections().length;
                    gameServer.sendToAllTCP(playersCount);
                }
            };
        }

        private void show(final int playersCount, final NewGameScreen newGameScreen, final Difficulty difficulty) {
            this.playersCount = playersCount;
            this.newGameScreen = newGameScreen;
            this.difficulty = difficulty;
            try {
                gameServer = new GameServer();
            } catch (IOException ignored) { }
            gameServer.addListener(listener);
            gameServer.start();
            show(newGameScreen);
        }

        @Override
        protected void result(Object object) {
            gameServer.stop();
        }
    }
}
