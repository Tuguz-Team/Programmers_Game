package com.programmers.game.online;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.programmers.enums.Difficulty;
import com.programmers.game.Field;
import com.programmers.game_objects.Chunk;
import com.programmers.network.GameClient;
import com.programmers.network.GameNetwork;
import com.programmers.screens.GameScreen;
import com.programmers.screens.ScreenLoader;

public final class OnlineGameClient extends GameScreen {

    private final GameClient gameClient;

    public OnlineGameClient(final ScreenLoader screenLoader, final Difficulty difficulty,
                            final int playersCount, final GameClient gameClient) {
        super(screenLoader, difficulty, playersCount);
        this.gameClient = gameClient;

        final Chunk[][] chunks = new Chunk[size][size];
        field = new Field(this, chunks);
        gameClient.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                if (object instanceof GameNetwork.Ready) {
                    loadGame();
                    Gdx.app.log("OnlineGameClient", "Load Game!");
                } else if (object instanceof GameNetwork.Chunk[][]) {
                    Gdx.app.log("OnlineGameClient", "Got data!");
                    for (int i = 0; i < size; i++) {
                        for (int j = 0; j < size; j++) {
                            GameNetwork.Chunk chunk = ((GameNetwork.Chunk[][]) object)[i][j];
                            chunks[i][j] = new Chunk(chunk.x, chunk.y, chunk.z, chunk.color, field);
                        }
                    }
                }
                gameClient.sendTCP(new GameNetwork.Ready());
                Gdx.app.log("OnlineGameClient", "Send ready message");
            }

            @Override
            public void disconnected(Connection connection) {
                dispose();
                screenLoader.setScreen(screenLoader.getMainMenu());
            }
        });
    }

    @Override
    protected void setCameraPosition() {
        //
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
}
