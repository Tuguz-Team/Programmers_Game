package com.programmers.game.online;

import com.badlogic.gdx.Input;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.programmers.enums.Difficulty;
import com.programmers.game.Field;
import com.programmers.network.GameClient;
import com.programmers.network.GameNetwork;
import com.programmers.screens.GameScreen;
import com.programmers.screens.ScreenLoader;

import java.util.Random;

import static com.badlogic.gdx.math.MathUtils.random;
import static com.programmers.screens.ScreenLoader.seed;

public final class OnlineGameClient extends GameScreen {

    private final GameClient gameClient;

    public OnlineGameClient(final ScreenLoader screenLoader, final Difficulty difficulty,
                            final int playersCount, final GameClient gameClient) {
        super(screenLoader, difficulty, playersCount);
        this.gameClient = gameClient;

        gameClient.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                if (object instanceof GameNetwork.Seed) {
                    GameNetwork.Seed seedNet = (GameNetwork.Seed) object;
                    seed = seedNet.seed;
                    random.setSeed(seedNet.seed);
                    field = new Field(OnlineGameClient.this);
                    loadGame();
                }
            }

            @Override
            public void disconnected(Connection connection) {
                exit();
            }
        });
    }

    @Override
    protected void setCameraPosition() {
        camera.position.set(-size, size, -size);
        camera.update();
    }

    @Override
    protected void addCardWindows() {
        //
    }

    @Override
    protected void loadModels() {
        field.loadModels();
    }

    @Override
    public void dispose() {
        gameClient.disconnect();
        super.dispose();
    }

    @Override
    public boolean keyDown(int keyCode) {
        if (keyCode == Input.Keys.BACK) {
            gameClient.disconnect();
        }
        return super.keyDown(keyCode);
    }
}
