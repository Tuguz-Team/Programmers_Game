package com.programmers.ui_elements;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.programmers.game.Player;
import com.programmers.game.hotseat.HotseatGameController;
import com.programmers.game_objects.Car;
import com.programmers.game_objects.Life;
import com.programmers.screens.ScreenLoader;

public final class HotseatGameInfo extends GameInfo {

    private final HotseatGameController hotseatGameController;
    private final Label[] score;

    public HotseatGameInfo(final HotseatGameController hotseatGameController) {
        super();
        this.hotseatGameController = hotseatGameController;
        hotseatGameController.setHotseatGameInfo(this);
        gameScreen = hotseatGameController.getGameScreen();
        dialog = new Dialog(hotseatGameController);
        //
        for (Player player : hotseatGameController.getPlayers()) {
            Car.Color color = player.getCar().getBase().getBaseColor();
            add(new CarImage(color));
        }
        Label label = new Label("Score", ScreenLoader.getGameSkin());
        label.setFontScale(1.5f);
        add(label).spaceLeft(20).row();
        score = new Label[hotseatGameController.getPlayers().length];
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
        for (int i = 0; i < score.length; i++) {
            Player player = hotseatGameController.getPlayers()[i];
            CarImage carImage = (CarImage) getCells().get(i).getActor();
            carImage.setOn(carImage.color == hotseatGameController.getThisPlayer().getCar()
                    .getBase().getBaseColor());
            score[i].setText(player.getScore());
            // set dialog data
            dialog.getLivesUnknownCount()[i].setText(player.getCar().getLives().size);
            int yellow = 0, purple = 0, green = 0, blue = 0;
            for (Life life : player.getLives()) {
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
}
