package com.programmers.game.online;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.utils.Array;
import com.programmers.game.Field;
import com.programmers.game.Player;
import com.programmers.game.hotseat.HotseatGameController;
import com.programmers.game_objects.Base;
import com.programmers.game_objects.Car;
import com.programmers.interfaces.Procedure;
import com.programmers.interfaces.NetworkManager;
import com.programmers.screens.GameScreen;
import com.programmers.screens.ScreenLoader;
import com.programmers.ui_elements.OKDialog;

import java.util.List;

import static com.badlogic.gdx.math.MathUtils.random;

public class OnlineGame extends GameScreen {

    private OnlineGameController onlineGameController;
    private final NetworkManager.Room room;
    private NetworkManager.FieldData fieldData;
    private NetworkManager.GameData gameData;

    private boolean loadGame;
    private boolean exit;

    private final Dialog waitDialog;
    private final OKDialog notExistDialog;

    private final Car[] cars;

    public OnlineGame(final ScreenLoader screenLoader, final NetworkManager.Room room, boolean sendData) {
        super(screenLoader, room.getDifficulty(), room.getPlayersCount());
        cars = new Car[room.getPlayersCount()];

        waitDialog = new Dialog("Waiting...", ScreenLoader.getDefaultGdxSkin());
        waitDialog.setMovable(false);

        notExistDialog = new OKDialog("Error! Room doesn't exist!", ScreenLoader.getDefaultGdxSkin()) {
            @Override
            protected void result(Object object) {
                dispose();
                screenLoader.setScreen(screenLoader.getMainMenu());
            }
        };

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
            HotseatGameController hotseatGameController =
                    new HotseatGameController(players, field, this);
            for (int i = 0; i < hotseatGameController.getPlayers().length; i++) {
                cars[i] = hotseatGameController.getPlayers()[i].getCar();
                cars[i].loadModel();
            }
            onlineGameController = new OnlineGameController(
                    screenLoader.networkManager, hotseatGameController,
                    room, this, field, cars
            );
            loadGame();
            screenLoader.networkManager.sendFieldData(room, field);
            screenLoader.networkManager.sendGameData(room, hotseatGameController);
            screenLoader.networkManager.setPlayersID(room);
            screenLoader.networkManager.launchRoom(room);
            initDataListener();
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
                    }, new Procedure() {
                        @Override
                        public void call() {
                            notExistDialog.show(OnlineGame.this);
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
            List<NetworkManager.GameData.Player> players = gameData.getPlayersData().getPlayers();
            for (int i = 0; i < players.size(); i++) {
                NetworkManager.FieldData.Car car = players.get(i).getCar();
                cars[i] = new Car(
                        (Base) field.getChunks()[car.getBase().getX()][car.getBase().getZ()],
                        car.getDirection()
                );
                cars[i].loadModel();
            }
            onlineGameController = new OnlineGameController(
                    screenLoader.networkManager, gameData, room, this, field, cars
            );
            loadGame();
            waitDialog.hide();
            initDataListener();
        }
        if (exit) {
            dispose();
            screenLoader.setScreen(screenLoader.getMainMenu());
            if (room.getPlayers().size() == 0) {
                screenLoader.networkManager.deleteRoom(room.getName());
            }
        }
    }

    @Override
    protected void setCameraPosition() {
        perspectiveCamera.position.set(-size, size, -size);
        perspectiveCamera.update();
    }

    @Override
    protected void addCardWindows() {
        addActor(onlineGameController.getPlayerCardWindow());
        addActor(onlineGameController.getAlgorithmCardWindow());
    }

    @Override
    protected void loadModels() {
        field.loadModels();
    }

    @Override
    public void dispose() {
        screenLoader.networkManager.removePlayerFromRoom(room);
        screenLoader.networkManager.removeListener(room);
        super.dispose();
    }

    public void initDataListener() {
        screenLoader.networkManager.addRoomChangedListener(
                room, new Procedure() {
                    @Override
                    public void call() {
                        if (room.getPlayers().size() != room.getPlayersCount()) {
                            exit = true;
                            return;
                        }
                        waitDialog.show(OnlineGame.this);
                        new Thread() {
                            @Override
                            public void run() {
                                onlineGameController.getFromServer(
                                        screenLoader.networkManager.getGameData(room)
                                );
                                interrupt();
                            }
                        }.start();
                        waitDialog.hide();
                    }
                }, new Procedure() {
                    @Override
                    public void call() { }
                }
        );
    }
}
