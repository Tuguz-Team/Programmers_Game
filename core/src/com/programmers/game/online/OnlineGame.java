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
import com.programmers.ui_elements.GameInfo;
import com.programmers.ui_elements.OKDialog;
import com.programmers.ui_elements.OnlineGameInfo;

import java.util.List;

import static com.badlogic.gdx.math.MathUtils.random;

public final class OnlineGame extends GameScreen {

    private OnlineGameController onlineGameController;
    private final NetworkManager.Room room;
    private NetworkManager.FieldData fieldData = new NetworkManager.FieldData();
    private NetworkManager.GameData gameData = new NetworkManager.GameData();

    private boolean loadGame;
    private boolean exit;

    private final Dialog waitDialog;
    private final OKDialog notExistDialog;

    public OnlineGame(final ScreenLoader screenLoader, final NetworkManager.Room room, boolean sendData) {
        super(screenLoader, room.getDifficulty(), room.getPlayersCount());

        waitDialog = new Dialog("   Waiting...   ", ScreenLoader.getGameSkin());
        waitDialog.setMovable(false);

        notExistDialog = new OKDialog("   Error! Room doesn't exist!   ", ScreenLoader.getGameSkin()) {
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
            Car[] cars = new Car[room.getPlayersCount()];
            for (int i = 0; i < hotseatGameController.getPlayers().length; i++) {
                cars[i] = hotseatGameController.getPlayers()[i].getCar();
                cars[i].loadModel();
            }
            onlineGameController = new OnlineGameController(
                    screenLoader.networkManager, hotseatGameController,
                    room, this, field, cars
            );
            gameData = new NetworkManager.GameData(hotseatGameController);
            loadGame();
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
                                        screenLoader.networkManager.removeRoomChangedListener();
                                        loadGame = true;
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
            Car[] cars = new Car[room.getPlayersCount()];
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
                screenLoader.networkManager.deleteRoom(room);
            }
        }
    }

    @Override
    protected void setCameraPosition() {
        int x, z;
        Car car = onlineGameController.getThisPlayer().getCar();
        if (car.getX() == 0 && car.getZ() == 0) {
            x = z = -size;
        } else if (car.getX() == 0 && car.getZ() == size - 1) {
            x = -size;
            z = size;
        } else if (car.getX() == size - 1 && car.getZ() == 0) {
            x = size;
            z = -size;
        } else {
            x = z = size;
        }
        perspectiveCamera.position.set(x, size, z);
        perspectiveCamera.update();
    }

    @Override
    protected void addUI() {
        super.addUI();
        GameInfo gameInfo = new OnlineGameInfo(gameData, this);
        addActor(gameInfo);
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

    public OnlineGameController getOnlineGameController() {
        return onlineGameController;
    }

    @Override
    public void dispose() {
        screenLoader.networkManager.removePlayerFromRoom(room);
        screenLoader.networkManager.removeRoomChangedListener();
        super.dispose();
    }

    private void initDataListener() {
        screenLoader.networkManager.addRoomChangedListener(
                room, new Procedure() {
                    @Override
                    public void call() {
                        if (room.getPlayers().size() != room.getPlayersCount()) {
                            exit = true;
                        }
                    }
                }, new Procedure() {
                    @Override
                    public void call() { }
                }
        );
    }
}
