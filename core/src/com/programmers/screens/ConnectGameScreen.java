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
import com.programmers.game.OnlineGame;
import com.programmers.interfaces.Procedure;
import com.programmers.interfaces.SpecificCode;
import com.programmers.ui_elements.MyButton;
import com.programmers.ui_elements.OKDialog;

import java.util.LinkedList;

public final class ConnectGameScreen extends ReturnableScreen {

    private boolean launchOnline;
    private SpecificCode.Room room;

    private final Dialog foundDialog, waitingDialog;
    private final OKDialog notFoundDialog, roomNoExistDialog;
    private final Label label;
    private final VerticalGroup existingGames;

    public ConnectGameScreen(final ScreenLoader screenLoader, final Screen previousScreen) {
        super(screenLoader, previousScreen);

        final Skin skin = ScreenLoader.getDefaultGdxSkin();

        notFoundDialog = new OKDialog("No games were found!", skin);

        roomNoExistDialog = new OKDialog("This room doesn't exist now!", skin);

        waitingDialog = new Dialog("Searching for games...", skin);
        waitingDialog.setMovable(false);

        label = new Label("", skin);
        label.setWrap(true);
        label.setAlignment(Align.center);
        foundDialog = new Dialog("Connected! Waiting for other players...", skin) {
            @Override
            protected void result(Object object) {
                if (!screenLoader.specificCode.removePlayerFromRoom(room)) {
                    roomNoExistDialog.show(ConnectGameScreen.this);
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
                new Thread() {
                    @Override
                    public void run() {
                        LinkedList<SpecificCode.Room> rooms = screenLoader.specificCode.findRooms();
                        if (!rooms.isEmpty()) {
                            for (SpecificCode.Room room : rooms)
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

        private GameRoom(final SpecificCode.Room room, final ImageTextButtonStyle style) {
            super("ROOM NAME: " + room.getName().toUpperCase()
                    + "\nPLAYERS: " + room.getNowPlayers() + "/" + room.getPlayersCount()
                    + "\nDIFFICULTY: " + room.getDifficulty().toString().toUpperCase(), style);
            ConnectGameScreen.this.room = room;
            screenLoader.specificCode.addRoomChangedListener(
                    room, new Procedure() {
                        @Override
                        public void call() {
                            setText("ROOM NAME: " + room.getName().toUpperCase()
                                    + "\nPLAYERS: " + room.getNowPlayers() + "/" + room.getPlayersCount()
                                    + "\nDIFFICULTY: " + room.getDifficulty().toString().toUpperCase());
                        }
                    }
            );
        }

        @Override
        public void call() {
            if (room.getNowPlayers() <= room.getPlayersCount()) {
                if (screenLoader.specificCode.addPlayerToRoom(room)) {
                    foundDialog.show(ConnectGameScreen.this);
                    screenLoader.specificCode.addRoomChangedListener(
                            room, new Procedure() {
                                @Override
                                public void call() {
                                    label.setText("Players : " + room.getNowPlayers() + "/" + room.getPlayersCount());
                                    if (room.getPlayersCount() == room.getNowPlayers()) {
                                        launchOnline = true;
                                        screenLoader.specificCode.removeListener(room);
                                    }
                                }
                            }
                    );
                } else {
                    screenLoader.specificCode.removeListener(room);
                    roomNoExistDialog.show(ConnectGameScreen.this);
                }
            }
        }
    }
}
