package com.programmers.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.utils.Align;
import com.programmers.game.online.OnlineGame;
import com.programmers.interfaces.Procedure;
import com.programmers.interfaces.NetworkManager;
import com.programmers.ui_elements.MyButton;
import com.programmers.ui_elements.OKDialog;

import java.util.LinkedList;

public final class ConnectGameScreen extends ReturnableScreen {

    private boolean launchOnline;
    private NetworkManager.Room room;

    private final Dialog foundDialog, waitingDialog;
    private final OKDialog notFoundDialog, notExistDialog, unableToConnectDialog;
    private final Label label;
    private final VerticalGroup existingGames;

    public ConnectGameScreen(final ScreenLoader screenLoader, final Screen previousScreen) {
        super(screenLoader, previousScreen);

        final Skin skin = ScreenLoader.getDefaultGdxSkin();

        notFoundDialog = new OKDialog("No games were found!", skin);

        notExistDialog = new OKDialog("This room doesn't exist now!", skin) {
            @Override
            public Dialog show(Stage stage) {
                screenLoader.networkManager.removeListener(room);
                foundDialog.hide();
                room = null;
                return super.show(stage);
            }
        };

        waitingDialog = new Dialog("Searching for games...", skin);
        waitingDialog.setMovable(false);

        unableToConnectDialog = new OKDialog("You cannot connect to room: game was started!", skin);

        label = new Label("", skin);
        label.setWrap(true);
        label.setAlignment(Align.center);
        foundDialog = new Dialog("Connected! Waiting for other players...", skin) {
            @Override
            protected void result(Object object) {
                if (room != null && !screenLoader.networkManager.removePlayerFromRoom(room)) {
                    notExistDialog.show(ConnectGameScreen.this);
                }
            }
        };
        foundDialog.setMovable(false);
        foundDialog.button("Disconnect from the room");
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
                waitingDialog.show(ConnectGameScreen.this);
                existingGames.clearChildren();
                room = null;
                new Thread() {
                    @Override
                    public void run() {
                        LinkedList<NetworkManager.Room> rooms = screenLoader.networkManager.findRooms();
                        if (!rooms.isEmpty()) {
                            for (NetworkManager.Room room : rooms)
                                existingGames.addActor(new GameRoom(room, ScreenLoader.getButtonStyle()));
                        } else
                            notFoundDialog.show(ConnectGameScreen.this);
                        waitingDialog.hide();
                        interrupt();
                    }
                }.start();
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
        if (launchOnline) {
            screenLoader.setScreen(new OnlineGame(screenLoader, room, false));
        }
    }

    private final class GameRoom extends MyButton {

        private GameRoom(final NetworkManager.Room room, final ImageTextButtonStyle style) {
            super("ROOM NAME: " + room.getName().toUpperCase()
                    + "\nPLAYERS: " + room.getPlayers().size() + "/" + room.getPlayersCount()
                    + "\nDIFFICULTY: " + room.getDifficulty().toString().toUpperCase(), style);
            ConnectGameScreen.this.room = room;
            screenLoader.networkManager.addRoomChangedListener(
                    room, new Procedure() {
                        @Override
                        public void call() {
                            setText("ROOM NAME: " + room.getName().toUpperCase()
                                    + "\nPLAYERS: " + room.getPlayers().size() + "/" + room.getPlayersCount()
                                    + "\nDIFFICULTY: " + room.getDifficulty().toString().toUpperCase());
                        }
                    }, new Procedure() {
                        @Override
                        public void call() { }
                    }
            );
        }

        @Override
        public void call() {
            if (room != null && room.getPlayers().size() <= room.getPlayersCount()) {
                if (!room.isLaunched()) {
                    if (screenLoader.networkManager.addPlayerToRoom(room)) {
                        foundDialog.show(ConnectGameScreen.this);
                        screenLoader.networkManager.addRoomChangedListener(
                                room, new Procedure() {
                                    @Override
                                    public void call() {
                                        if (room != null) {
                                            label.setText("Players : " + (room.getPlayers().size() + 1) + "/" + room.getPlayersCount());
                                            if (room.getPlayersCount() == room.getPlayers().size()) {
                                                launchOnline = true;
                                                screenLoader.networkManager.removeListener(room);
                                            }
                                        }
                                    }
                                }, new Procedure() {
                                    @Override
                                    public void call() {
                                        if (room != null) {
                                            notExistDialog.show(ConnectGameScreen.this);
                                        }
                                    }
                                }
                        );
                    } else if (room != null) {
                        notExistDialog.show(ConnectGameScreen.this);
                    }
                } else {
                    unableToConnectDialog.show(ConnectGameScreen.this);
                }
            } else {
                notExistDialog.show(ConnectGameScreen.this);
            }
        }
    }
}
