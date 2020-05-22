package com.programmers.ui_elements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.programmers.game.Player;
import com.programmers.game.online.OnlineGame;
import com.programmers.game.online.OnlineGameController;
import com.programmers.game_objects.Car;
import com.programmers.game_objects.Life;
import com.programmers.interfaces.NetworkManager;
import com.programmers.screens.ScreenLoader;

public final class OnlineGameInfo extends GameInfo {

    private final OnlineGameController onlineGameController;
    private final Label[] score;

    public OnlineGameInfo(NetworkManager.GameData gameData, OnlineGame onlineGame) {
        super();
        gameScreen = onlineGame;
        onlineGameController = onlineGame.getOnlineGameController();
        onlineGameController.setOnlineGameInfo(this);
        dialog = new Dialog(gameData);
        //
        for (NetworkManager.GameData.Player player : gameData.getPlayersData().getPlayers()) {
            Car.Color color = player.getCar().getBase().getBaseColor();
            add(new CarImage(color));
        }
        Label label = new Label("Score", ScreenLoader.getGameSkin());
        label.setFontScale(1.5f);
        add(label).spaceLeft(20).row();
        score = new Label[gameData.getPlayersData().getPlayers().size()];
        for (int i = 0; i < score.length; i++) {
            score[i] = new Label("", ScreenLoader.getGameSkin());
            score[i].setFontScale(1.5f);
            add(score[i]);
        }
        row();
        updateData();
    }

    @Override
    public void updateData() {
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                int index = onlineGameController.getPlayersData().getIndex();
                NetworkManager.GameData.Player thisPlayer
                        = onlineGameController.getPlayersData().getPlayers().get(index);
                for (int i = 0; i < score.length; i++) {
                    NetworkManager.GameData.Player player = onlineGameController.getPlayersData().getPlayers().get(i);
                    CarImage carImage = (CarImage) getCells().get(i).getActor();
                    carImage.setOn(carImage.color == thisPlayer.getCar().getBase().getBaseColor());
                    score[i].setText(player.getScore());
                    // set dialog data
                    dialog.getLivesUnknownCount()[i].setText(player.getCar().getLives().size());
                    int yellow = 0, purple = 0, green = 0, blue = 0;
                    for (NetworkManager.FieldData.Life life : player.getLives()) {
                        switch (life.getType()) {
                            case Yellow:
                                yellow++;
                                break;
                            case Purple:
                                purple++;
                                break;
                            case Green:
                                green++;
                                break;
                            case Blue:
                                blue++;
                        }
                    }
                    dialog.getLivesYellowCount()[i].setText(yellow);
                    dialog.getLivesPurpleCount()[i].setText(purple);
                    dialog.getLivesGreenCount()[i].setText(green);
                    dialog.getLivesBlueCount()[i].setText(blue);
                }
            }
        });
    }
}
