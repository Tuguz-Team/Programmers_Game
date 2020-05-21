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
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.Align;
import com.programmers.enums.Difficulty;
import com.programmers.game.hotseat.HotseatGame;
import com.programmers.game.online.OnlineGame;
import com.programmers.interfaces.Procedure;
import com.programmers.interfaces.NetworkManager;
import com.programmers.ui_elements.MyButton;
import com.programmers.ui_elements.OKDialog;

public final class NewGameScreen extends ReturnableScreen {

    private NetworkManager.Room room;
    private boolean launchOnline;

    private static final Skin skin = ScreenLoader.getGameSkin();
    private final Dialog waitDialog;

    public NewGameScreen(final ScreenLoader screenLoader, final Screen previousScreen, final boolean isHotseat) {
        super(screenLoader, previousScreen);

        Table ui = new Table();
        ui.setFillParent(true);
        addActor(ui);

        waitDialog = new Dialog("   Waiting...   ", skin);
        waitDialog.setMovable(false);

        final TextField textField = new TextField("", ScreenLoader.getGameSkin());
        textField.setMaxLength(30);
        textField.setAlignment(Align.center);

        if (!isHotseat) {
            ui.add(new Label("Choose name of the room :", skin)).space(20).row();
            ui.add(textField).width(1000).row();
        }

        final Slider playerCountSlider = new Slider(2, 4, 1,
                false, skin) {
            @Override
            public float getPrefWidth() {
                return 0.4f * Gdx.graphics.getWidth();
            }
        };
        playerCountSlider.setValue(playerCountSlider.getMaxValue());

        ui.add(new Label("Choose number of players in new room :", skin))
                .space(0.025f * Gdx.graphics.getHeight()).row();

        ui.add(playerCountSlider).row();

        HorizontalGroup sliderDisplay = new HorizontalGroup();
        sliderDisplay.addActor(new Label("2", skin));
        sliderDisplay.addActor(new Label("3", skin));
        sliderDisplay.addActor(new Label("4", skin));
        sliderDisplay.space(0.4525f * playerCountSlider.getPrefWidth());

        ui.add(sliderDisplay).
                spaceBottom(0.05f * Gdx.graphics.getHeight()).row();

        ui.add(new Label("Choose game difficulty in new room :", skin))
                .spaceBottom(0.025f * Gdx.graphics.getHeight()).row();

        CheckBox EasyButton = new CheckBox("   EASY   ", skin);
        CheckBox HardButton = new CheckBox("   HARD   ", skin);

        final HorizontalGroup difficultyRadioButton = new HorizontalGroup();
        difficultyRadioButton.addActor(EasyButton);
        difficultyRadioButton.addActor(HardButton);
        difficultyRadioButton.space(0.05f * Gdx.graphics.getWidth());

        final ButtonGroup<CheckBox> radioButtonController = new ButtonGroup<>(EasyButton, HardButton);
        radioButtonController.setChecked("EASY");

        ui.add(difficultyRadioButton).spaceBottom(0.025f * Gdx.graphics.getHeight()).row();

        final NewRoomDialog newRoomDialog = new NewRoomDialog("Waiting for players...", skin);
        TextButton start = new MyButton("   START PLAYING !   ", ScreenLoader.getGameSkin()) {
            @Override
            public void call() {
                final Difficulty difficulty = radioButtonController.getChecked().getText()
                        .toString().equals("EASY") ? Difficulty.Easy : Difficulty.Hard;
                final int playersCount = (int)playerCountSlider.getValue();
                if (isHotseat) {
                    dispose();
                    screenLoader.setScreen(new HotseatGame(screenLoader, difficulty, playersCount));
                } else {
                    final String name = textField.getText(), trimmed = name.trim();
                    if (trimmed.isEmpty()) {
                        OKDialog dialog = new OKDialog("Name cannot be empty!", skin);
                        dialog.show(NewGameScreen.this);
                        return;
                    }
                    waitDialog.show(NewGameScreen.this);
                    new Thread() {
                        @Override
                        public void run() {
                            if (screenLoader.networkManager.createNewRoom(name, playersCount, difficulty)) {
                                newRoomDialog.show(name, playersCount, difficulty);
                            } else {
                                OKDialog dialog = new OKDialog(
                                        "Room with this name is already exists!", skin);
                                dialog.show(NewGameScreen.this);
                            }
                            waitDialog.hide();
                            interrupt();
                        }
                    }.start();
                }
            }
        };
        start.getLabel().setFontScale(2);
        ui.add(start).space(0.1f * Gdx.graphics.getHeight());

        ui.center();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        if (launchOnline) {
            dispose();
            screenLoader.setScreen(new OnlineGame(screenLoader, room, true));
        }
    }

    @Override
    public void dispose() {
        if (room != null && !launchOnline) {
            screenLoader.networkManager.deleteRoom(room);
        }
        super.dispose();
    }

    private class NewRoomDialog extends Dialog {

        final Label label;

        private NewRoomDialog(final String title, final Skin skin) {
            super(title, skin);
            setMovable(false);
            button("   Close room   ");

            label = new Label("", skin);
            label.setWrap(true);
            label.setAlignment(Align.center);
            getContentTable().add(label);
        }

        private void show(final String name, final int playersCount, final Difficulty difficulty) {
            room = new NetworkManager.Room(name, playersCount, difficulty);
            show(NewGameScreen.this);
            screenLoader.networkManager.addRoomChangedListener(
                    room, new Procedure() {
                        @Override
                        public void call() {
                            label.setText("   Players : " + room.getPlayers().size() + "/" + room.getPlayersCount() + "   ");
                            if (room.getPlayers().size() == room.getPlayersCount()) {
                                launchOnline = true;
                                screenLoader.networkManager.removeRoomChangedListener();
                            }
                        }
                    }, new Procedure() {
                        @Override
                        public void call() { }
                    }
            );
        }

        @Override
        protected void result(Object object) {
            screenLoader.networkManager.deleteRoom(room);
        }
    }
}
