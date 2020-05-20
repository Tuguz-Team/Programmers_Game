package com.programmers.game.online;

import com.badlogic.gdx.math.MathUtils;
import com.programmers.enums.Difficulty;
import com.programmers.game.Field;
import com.programmers.game.GameCard;
import com.programmers.game.GameController;
import com.programmers.game.Player;
import com.programmers.game.hotseat.HotseatGameController;
import com.programmers.game_objects.Base;
import com.programmers.game_objects.Car;
import com.programmers.interfaces.NetworkManager;
import com.programmers.ui_elements.AlgorithmCardWindow;
import com.programmers.ui_elements.Card;
import com.programmers.ui_elements.PlayerCardWindow;

public final class OnlineGameController extends GameController {

    private final NetworkManager networkManager;
    private final NetworkManager.Room room;
    private final Car[] cars;

    public OnlineGameController(NetworkManager networkManager, HotseatGameController hotseatGameController,
                                NetworkManager.Room room, OnlineGame onlineGame, Field field, Car[] cars) {
        super(onlineGame, field);
        this.networkManager = networkManager;
        this.room = room;
        this.cars = cars;
        thisPlayer = hotseatGameController
                .getPlayers()[MathUtils.random.nextInt(room.getPlayersCount())];
        algorithmCards.addAll(hotseatGameController.getAlgorithmCards());
        discardPile.addAll(hotseatGameController.getDiscardPile());
        talon.addAll(hotseatGameController.getTalon());
        initContainers();
    }

    public OnlineGameController(NetworkManager networkManager, NetworkManager.GameData gameData,
                                NetworkManager.Room room, OnlineGame onlineGame, Field field, Car[] cars) {
        super(onlineGame, field);
        this.networkManager = networkManager;
        this.room = room;
        this.cars = cars;
        getFromServer(gameData);
        initContainers();
    }

    @Override
    public void toNextPlayer() {
        // set data somewhere
        networkManager.toNextPlayer(room);
        sendToServer();
    }

    public void sendToServer() {
        networkManager.updateGameData(room, this, thisPlayer);
    }

    public void getFromServer(NetworkManager.GameData gameData) {
        NetworkManager.GameData.Player player = networkManager.getThisPlayerData(room);
        NetworkManager.FieldData.Base base = player.getCar().getBase();
        for (Car car : cars) {
            if (car.getBase().equals(field.getChunks()[base.getX()][base.getZ()])) {
                thisPlayer = new Player(car);
                break;
            }
        }
        algorithmCards.clear();
        //
        discardPile.clear();
        for (NetworkManager.GameData.GameCard gameCard : gameData.getCardsData().getDiscardPile()) {
            discardPile.add(new GameCard(gameCard.getCardType(), null));
        }
        talon.clear();
        for (NetworkManager.GameData.GameCard gameCard : gameData.getCardsData().getTalon()) {
            talon.add(new GameCard(gameCard.getCardType(), null));
        }
    }

    public PlayerCardWindow getPlayerCardWindow() {
        return playerCardWindow;
    }

    public AlgorithmCardWindow getAlgorithmCardWindow() {
        return algorithmCardWindow;
    }

    @Override
    public Difficulty getDifficulty() {
        return room.getDifficulty();
    }

    public Car[] getCars() {
        return cars;
    }
}
