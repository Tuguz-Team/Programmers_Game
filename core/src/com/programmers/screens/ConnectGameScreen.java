package com.programmers.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.utils.Align;
import com.programmers.enums.Difficulty;
import com.programmers.game.online.OnlineGameServer;
import com.programmers.network.GameClient;
import com.programmers.network.GameServer;
import com.programmers.ui_elements.MyButton;

import java.io.IOException;

public final class ConnectGameScreen extends ReturnableScreen {

    private GameClient gameClient;

    public ConnectGameScreen(final ScreenLoader screenLoader, final Screen previousScreen) {
        super(screenLoader, previousScreen);

        try {
            gameClient = new GameClient();
            gameClient.start();
        } catch (IOException ignored) { }

        Table table = new Table();
        table.setFillParent(true);
        addActor(table);
        table.add(new Label(
                "GAMES TO CONNECT",
                ScreenLoader.getDefaultGdxSkin()
        )).spaceBottom(0.01f * Gdx.graphics.getHeight()).row();

        ImageTextButton updateGames = new MyButton("UPDATE", screenLoader.getButtonStyle()) {
            @Override
            public void call() {
                new GameFinder(gameClient, ConnectGameScreen.this).start();
            }
        };
        table.add(updateGames).spaceBottom(0.1f * Gdx.graphics.getHeight()).row();

        VerticalGroup existingGames = new VerticalGroup();
        existingGames.setFillParent(true);

        existingGames.space(0.05f * Gdx.graphics.getHeight());
        existingGames.center();

        table.add(existingGames);
    }

    @Override
    public void dispose() {
        super.dispose();
        gameClient.stop();
    }

    private static class GameFinder extends Thread {

        private final GameClient gameClient;
        private final ConnectGameScreen connectGameScreen;

        GameFinder(final GameClient gameClient, final ConnectGameScreen connectGameScreen) {
            this.gameClient = gameClient;
            this.connectGameScreen = connectGameScreen;
        }

        @Override
        public void run() {
            try {
                Dialog waitingDialog = new Dialog("Waiting...", ScreenLoader.getDefaultGdxSkin());
                waitingDialog.setMovable(false);
                waitingDialog.show(connectGameScreen);
                if (gameClient.connectByUDP()) {
                    Dialog dialog = new Dialog("Connected!", ScreenLoader.getDefaultGdxSkin());
                    dialog.setMovable(false);
                    dialog.show(connectGameScreen);
                    sleep(1000);
                    dialog.hide();
                    // new dialog (in new thread) displaying count of connected players
                } else {
                    Dialog dialog = new Dialog("No games were found!", ScreenLoader.getDefaultGdxSkin());
                    dialog.setMovable(false);
                    dialog.button("OK");
                    dialog.show(connectGameScreen);
                }
                waitingDialog.hide();
            } catch (IOException | InterruptedException ignored) { }
        }
    }
}
