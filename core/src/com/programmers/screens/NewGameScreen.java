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
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.Align;
import com.programmers.enums.Difficulty;
import com.programmers.game.HotseatGame;
import com.programmers.game.OnlineGame;
import com.programmers.interfaces.Procedure;
import com.programmers.interfaces.SpecificCode;
import com.programmers.ui_elements.MyButton;
import com.programmers.ui_elements.OKDialog;

public final class NewGameScreen extends ReturnableScreen {

    private SpecificCode.Room room;
    private boolean launchOnline;

    private static final Skin skin = ScreenLoader.getDefaultGdxSkin();
    private final Dialog waitDialog;

    public NewGameScreen(final ScreenLoader screenLoader, final Screen previousScreen, final boolean isHotseat) {
        super(screenLoader, previousScreen);

        Table ui = new Table();
        ui.setFillParent(true);
        addActor(ui);

        waitDialog = new Dialog("Waiting...", skin);
        waitDialog.setMovable(false);

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
        sliderDisplay.space(0.47f * playerCountSlider.getPrefWidth());

        ui.add(sliderDisplay).
                spaceBottom(0.05f * Gdx.graphics.getHeight()).row();

        ui.add(new Label("Choose game difficulty in new room :", skin))
                .spaceBottom(0.025f * Gdx.graphics.getHeight()).row();

        CheckBox EasyButton = new CheckBox("EASY", skin);
        CheckBox HardButton = new CheckBox("HARD", skin);

        final HorizontalGroup difficultyRadioButton = new HorizontalGroup();
        difficultyRadioButton.addActor(EasyButton);
        difficultyRadioButton.addActor(HardButton);
        difficultyRadioButton.space(0.05f * Gdx.graphics.getWidth());

        final ButtonGroup<CheckBox> radioButtonController = new ButtonGroup<>(EasyButton, HardButton);
        radioButtonController.setChecked("EASY");

        ui.add(difficultyRadioButton).spaceBottom(0.025f * Gdx.graphics.getHeight()).row();

        final TextField textField = new TextField("", ScreenLoader.getDefaultGdxSkin());
        if (!isHotseat) {
            ui.add(new Label("Choose name of the room :", skin)).row();
            ui.add(textField).row();
        }

        final NewRoomDialog newRoomDialog = new NewRoomDialog("Waiting for players...", skin);
        ui.add(new MyButton("START PLAYING !", ScreenLoader.getButtonStyle()) {
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
                            if (screenLoader.specificCode.createNewRoom(name, playersCount, difficulty)) {
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
        }).space(0.1f * Gdx.graphics.getHeight());

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
            screenLoader.specificCode.deleteRoom(room.getName());
        }
        super.dispose();
    }

    private class NewRoomDialog extends Dialog {

        final Label label;

        private NewRoomDialog(final String title, final Skin skin) {
            super(title, skin);
            setMovable(false);
            button("Close room");

            label = new Label("", skin);
            label.setWrap(true);
            label.setAlignment(Align.center);
            getContentTable().add(label);
        }

        private void show(final String name, final int playersCount, final Difficulty difficulty) {
            room = new SpecificCode.Room(name, playersCount, difficulty);
            show(NewGameScreen.this);
            screenLoader.specificCode.addRoomChangedListener(
                    room, new Procedure() {
                        @Override
                        public void call() {
                            label.setText("Players : " + room.getNowPlayers() + "/" + room.getPlayersCount());
                            if (room.getNowPlayers() == room.getPlayersCount()) {
                                launchOnline = true;
                                screenLoader.specificCode.removeListener(room);
                            }
                        }
                    }
            );
        }

        @Override
        protected void result(Object object) {
            screenLoader.specificCode.deleteRoom(room.getName());
        }
    }
}
