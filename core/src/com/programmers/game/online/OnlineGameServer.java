package com.programmers.game.online;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.programmers.enums.Difficulty;
import com.programmers.game.hotseat.HotseatGame;
import com.programmers.game_objects.Chunk;
import com.programmers.network.GameNetwork;
import com.programmers.network.GameNetwork.Disconnect;
import com.programmers.network.GameServer;
import com.programmers.screens.ScreenLoader;

public final class OnlineGameServer extends HotseatGame {

    private final GameServer gameServer;

    public OnlineGameServer(final ScreenLoader screenLoader, final Difficulty difficulty,
                            final int playersCount, final GameServer gameServer) {
        super(screenLoader, difficulty, playersCount);
        this.gameServer = gameServer;
        gameServer.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                if (object instanceof Disconnect) {
                    // stop game and wait for this player
                } else if (object instanceof GameNetwork.Ready) {
                    gameServer.sendToTCP(connection.getID(), new GameNetwork.Ready());
                }
            }
        });
        loadGame();

        GameNetwork.Chunk[][] chunksNet = new GameNetwork.Chunk[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Chunk chunk = field.getChunks()[i][j];
                GameNetwork.Chunk chunkNet = new GameNetwork.Chunk();
                chunkNet.x = chunk.getX();
                chunkNet.y = chunk.getY();
                chunkNet.z = chunk.getZ();
                chunkNet.color = chunk.getColor();
                chunkNet.modelFileName = chunk.getModelFileName();
                chunksNet[i][j] = chunkNet;
            }
        }
        gameServer.sendToAllTCP(chunksNet);
        Gdx.app.log("OnlineGameServer", "Data was sent!");
    }

    @Override
    public void dispose() {
        gameServer.stop();
        super.dispose();
    }
}
