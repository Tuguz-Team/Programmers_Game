package com.programmers.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.utils.Align;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.programmers.enums.Difficulty;
import com.programmers.game.online.OnlineGameClient;
import com.programmers.network.GameClient;
import com.programmers.network.GameNetwork;
import com.programmers.ui_elements.MyButton;
import com.programmers.network.GameNetwork.PlayersCount;

import java.io.IOException;
import java.net.InetAddress;
import java.util.List;

import static com.programmers.network.GameNetwork.TCP_PORT;
import static com.programmers.network.GameNetwork.TIMEOUT;
import static com.programmers.network.GameNetwork.UDP_PORT;

public final class ConnectGameScreen extends ReturnableScreen {

    private GameClient gameClient;
    private boolean launchOnlineGame, connected;
    private Difficulty difficulty;
    private int playersCount;

    private final Dialog notFoundDialog, foundDialog, waitingDialog, connectionErrorDialog;
    private final Label label;
    private final VerticalGroup existingGames;
    private final Listener listener = new Listener() {
        @Override
        public void received(Connection connection, Object object) {
            if (object instanceof PlayersCount) {
                PlayersCount playersCount = (PlayersCount) object;
                label.setText("Connected players: " + playersCount.playersCount);
            } else if (object instanceof GameNetwork.LoadGame) {
                GameNetwork.LoadGame loadGame = (GameNetwork.LoadGame) object;
                gameClient.removeListener(this);
                difficulty = loadGame.difficulty;
                playersCount = loadGame.playersCount;
                launchOnlineGame = true;
            }
        }

        @Override
        public void disconnected(Connection connection) {
            foundDialog.hide();
            label.setText("Connected players: 1");
        }
    };

    public ConnectGameScreen(final ScreenLoader screenLoader, final Screen previousScreen) {
        super(screenLoader, previousScreen);

        gameClient = new GameClient();

        notFoundDialog = new Dialog("No games were found!", ScreenLoader.getDefaultGdxSkin());
        notFoundDialog.setMovable(false);
        notFoundDialog.button("OK");

        foundDialog = new Dialog("Connected! Waiting for other players...",
                ScreenLoader.getDefaultGdxSkin()) {
            @Override
            protected void result(Object object) {
                gameClient.disconnect();
            }
        };
        foundDialog.setMovable(false);
        foundDialog.button("Disconnect from server");

        waitingDialog = new Dialog("Connecting...", ScreenLoader.getDefaultGdxSkin());
        waitingDialog.setMovable(false);

        connectionErrorDialog = new Dialog("Connection error!", ScreenLoader.getDefaultGdxSkin());
        connectionErrorDialog.setMovable(false);
        connectionErrorDialog.button("OK");

        label = new Label("Connected players: 1", ScreenLoader.getDefaultGdxSkin());
        label.setWrap(true);
        label.setAlignment(Align.center);
        foundDialog.getContentTable().add(label);

        Table table = new Table();
        table.setFillParent(true);
        addActor(table);
        table.add(new Label(
                "GAMES TO CONNECT",
                ScreenLoader.getDefaultGdxSkin()
        )).spaceBottom(0.01f * Gdx.graphics.getHeight()).row();

        ImageTextButton updateGames = new MyButton("UPDATE", ScreenLoader.getButtonStyle()) {
            @Override
            public void call() {
                new GameFinder(ConnectGameScreen.this).start();
            }
        };
        table.add(updateGames).spaceBottom(0.1f * Gdx.graphics.getHeight()).row();

        existingGames = new VerticalGroup();
        existingGames.setFillParent(true);
        existingGames.space(0.05f * Gdx.graphics.getHeight());
        existingGames.center();

        table.add(existingGames);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        if (connected) {
            connected = false;
            gameClient.addListener(listener);
            foundDialog.show(this);
        }
        if (launchOnlineGame) {
            dispose();
            screenLoader.setScreen(new OnlineGameClient(
                    screenLoader, difficulty, playersCount, gameClient
            ));
        }
    }

    @Override
    public void dispose() {
        if (!launchOnlineGame) {
            gameClient.disconnect();
        }
        super.dispose();
    }

    private final class GamePanel extends MyButton {

        private GameClient gameClient;
        private final InetAddress inetAddress;

        private GamePanel(final InetAddress inetAddress) {
            super("", ScreenLoader.getButtonStyle());
            this.inetAddress = inetAddress;
            gameClient = new GameClient();
            gameClient.addListener(new com.esotericsoftware.kryonet.Listener() {
                @Override
                public void received(Connection connection, Object object) {
                    if (object instanceof GameNetwork.InfoResponse) {
                        GameNetwork.InfoResponse response = (GameNetwork.InfoResponse) object;
                        setText("COUNT OF PLAYERS: " + response.playersCount
                                + "\nDIFFICULTY: " + response.difficulty.toString().toUpperCase());
                        gameClient.disconnect();
                    }
                }
            });
            gameClient.start();
            try {
                gameClient.connect(TIMEOUT, inetAddress, TCP_PORT, UDP_PORT);
                gameClient.sendTCP(new GameNetwork.InfoRequest());
            } catch (IOException ignored) { }
        }

        @Override
        public void call() {
            new Thread() {
                @Override
                public void run() {
                    waitingDialog.show(ConnectGameScreen.this);
                    try {
                        gameClient.disconnect();
                        ConnectGameScreen.this.gameClient.connect(
                                TIMEOUT, inetAddress, TCP_PORT, UDP_PORT);
                        ConnectGameScreen.this.gameClient.sendTCP(new GameNetwork.Connect());
                        connected = true;
                    } catch (IOException ignored) {
                        connectionErrorDialog.show(ConnectGameScreen.this);
                        connected = false;
                    }
                    waitingDialog.hide();
                    interrupt();
                }
            }.start();
        }
    }

    private final class GameFinder extends Thread {

        private final GameClient gameClient;
        private final ConnectGameScreen connectGameScreen;

        private GameFinder(final ConnectGameScreen connectGameScreen) {
            super("ConnectGameScreen");
            this.connectGameScreen = connectGameScreen;
            gameClient = new GameClient();
            gameClient.start();
        }

        @Override
        public void run() {
            existingGames.clear();
            Dialog waitingDialog = new Dialog("Searching for games...", ScreenLoader.getDefaultGdxSkin());
            waitingDialog.setMovable(false);
            waitingDialog.show(connectGameScreen);
            List<InetAddress> inetAddresses = gameClient.discoverHosts(UDP_PORT, TIMEOUT);
            if (!inetAddresses.isEmpty()) {
                for (InetAddress inetAddress : inetAddresses) {
                    existingGames.addActor(new GamePanel(inetAddress));
                }
            } else {
                connected = false;
                notFoundDialog.show(connectGameScreen);
            }
            waitingDialog.hide();
            gameClient.disconnect();
            interrupt();
        }
    }
}
