package com.programmers.game.online;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.utils.Array;
import com.programmers.game.Field;
import com.programmers.game.Player;
import com.programmers.game_objects.Base;
import com.programmers.game_objects.Car;
import com.programmers.interfaces.Procedure;
import com.programmers.interfaces.NetworkManager;
import com.programmers.screens.GameScreen;
import com.programmers.screens.ScreenLoader;

import static com.badlogic.gdx.math.MathUtils.random;

public class OnlineGame extends GameScreen {

    private OnlineGameController onlineGameController;
    private final NetworkManager.Room room;
    private NetworkManager.FieldData fieldData;
    private NetworkManager.GameData gameData;

    private final boolean sendData;
    private boolean loadGame;

    private final Dialog waitDialog;

    public OnlineGame(final ScreenLoader screenLoader, final NetworkManager.Room room, boolean sendData) {
        super(screenLoader, room.getDifficulty(), room.getPlayersCount());

        waitDialog = new Dialog("Waiting...", ScreenLoader.getDefaultGdxSkin());
        waitDialog.setMovable(false);

        this.sendData = sendData;
        this.room = room;
        if (sendData) {
            field = new Field(this);
            Player[] players = new Player[playersCount];
            Array<Base> bases = new Array<>(new Base[] {
                    (Base)field.getChunks()[0][0], (Base)field.getChunks()[0][size - 1],
                    (Base)field.getChunks()[size - 1][0], (Base)field.getChunks()[size - 1][size - 1]
            });
            for (int i = 0; i < players.length; i++) {
                int index = random.nextInt(bases.size);
                players[i] = new Player(new Car(bases.get(index)));
                bases.removeIndex(index);
            }
            onlineGameController = new OnlineGameController(players, field, room, this);
            loadGame();
            screenLoader.networkManager.sendFieldData(room, field);
            screenLoader.networkManager.sendGameData(room, onlineGameController);
            screenLoader.networkManager.launchRoom(room);
        } else {
            waitDialog.show(OnlineGame.this);
            screenLoader.networkManager.addRoomChangedListener(
                    room, new Procedure() {
                        @Override
                        public void call() {
                            new Thread() {
                                @Override
                                public void run() {
                                    if (room.isLaunched()) {
                                        fieldData = screenLoader.networkManager.getFieldData(room);
                                        gameData = screenLoader.networkManager.getGameData(room);
                                        loadGame = true;
                                        screenLoader.networkManager.removeListener(room);
                                    }
                                    interrupt();
                                }
                            }.start();
                        }
                    });
        }
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        if (loadGame) {
            loadGame = false;
            field = new Field(this, fieldData);
            onlineGameController = new OnlineGameController(gameData, field, room, this);
            loadGame();
            waitDialog.hide();
        }
    }

    @Override
    protected void setCameraPosition() {
        camera.position.set(-size, size, -size);
        camera.update();
    }

    @Override
    protected void addCardWindows() {
        addActor(onlineGameController.getPlayerCardWindow());
        addActor(onlineGameController.getAlgorithmCardWindow());
    }

    @Override
    protected void loadModels() {
        field.loadModels();
        for (Player player : onlineGameController.getPlayers())
            player.getCar().loadModel();
    }

    @Override
    public void dispose() {
        if (sendData) {
            screenLoader.networkManager.deleteRoom(room.getName());
        } else {
            screenLoader.networkManager.removePlayerFromRoom(room);
        }
        screenLoader.networkManager.removeListener(room);
        super.dispose();
    }
}
