package com.programmers.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.programmers.network.GameClient;
import com.programmers.network.GameNetwork;
import com.programmers.network.GameServer;
import com.programmers.ui_elements.MyButton;

import java.io.IOException;
import java.net.InetAddress;

public class ConnectGameScreen extends ReturnableScreen {

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
                try {
                    GameServer gameServer = new GameServer();
                    gameServer.start();
                    gameClient.connectByUDP();
                    gameServer.close();
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
        gameClient.stop();
    }
}
