package com.programmers.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.utils.Align;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.programmers.network.GameClient;
import com.programmers.ui_elements.MyButton;
import com.programmers.network.GameNetwork.PlayersCount;

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
                try {
                    gameClient = new GameClient();
                    gameClient.start();
                } catch (IOException ignored) { }
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
        gameClient.disconnect();
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
                Dialog waitingDialog = new Dialog("Waiting...", ScreenLoader.getDefaultGdxSkin()) {
                    @Override
                    protected void result(Object object) {
                        gameClient.stop();
                    }
                };
                waitingDialog.setMovable(false);
                waitingDialog.show(connectGameScreen);
                if (gameClient.connectByUDP()) {
                    final Dialog dialog = new Dialog("Connected! Waiting for other players...",
                            ScreenLoader.getDefaultGdxSkin()) {
                        @Override
                        protected void result(Object object) {
                            gameClient.disconnect();
                        }
                    };
                    dialog.setMovable(false);
                    dialog.button("Disconnect from server");

                    final Label label = new Label("Connected players: 1", ScreenLoader.getDefaultGdxSkin());
                    label.setWrap(true);
                    label.setAlignment(Align.center);
                    dialog.getContentTable().add(label);

                    final Listener listener = new Listener() {
                        @Override
                        public void received(Connection connection, Object object) {
                            if (object instanceof PlayersCount) {
                                PlayersCount playersCount = (PlayersCount) object;
                                label.setText("Connected players: " + playersCount.playersCount);
                            }
                        }

                        @Override
                        public void disconnected(Connection connection) {
                            dialog.hide();
                            label.setText("Connected players: 1");
                        }
                    };
                    gameClient.addListener(listener);
                    dialog.show(connectGameScreen);
                } else {
                    Dialog dialog = new Dialog("No games were found!", ScreenLoader.getDefaultGdxSkin());
                    dialog.setMovable(false);
                    dialog.button("OK");
                    dialog.show(connectGameScreen);
                }
                waitingDialog.hide();
            } catch (IOException ignored) { }
        }
    }
}
