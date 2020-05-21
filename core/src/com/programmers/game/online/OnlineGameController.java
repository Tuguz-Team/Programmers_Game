package com.programmers.game.online;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.programmers.enums.Difficulty;
import com.programmers.game.Field;
import com.programmers.game.GameCard;
import com.programmers.game.GameController;
import com.programmers.game.Player;
import com.programmers.game.hotseat.HotseatGameController;
import com.programmers.game_objects.Car;
import com.programmers.interfaces.NetworkManager;
import com.programmers.interfaces.Procedure;
import com.programmers.ui_elements.AlgorithmCardWindow;
import com.programmers.ui_elements.Card;
import com.programmers.ui_elements.PlayerCardWindow;

public final class OnlineGameController extends GameController {

    private final NetworkManager networkManager;
    private final NetworkManager.Room room;
    private NetworkManager.GameData.PlayersData playersData = new NetworkManager.GameData.PlayersData();
    private NetworkManager.GameData.CardsData cardsData = new NetworkManager.GameData.CardsData();

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
        initDataListeners();
    }

    public OnlineGameController(NetworkManager networkManager, NetworkManager.GameData gameData,
                                NetworkManager.Room room, OnlineGame onlineGame, Field field, Car[] cars) {
        super(onlineGame, field);
        this.networkManager = networkManager;
        this.room = room;
        this.cars = cars;
        NetworkManager.GameData.Player player = networkManager.getThisPlayerData(gameData.getPlayersData());
        NetworkManager.FieldData.Base base = player.getCar().getBase();
        for (Car car : cars) {
            if (car.getBase().equals(field.getChunks()[base.getX()][base.getZ()])) {
                thisPlayer = new Player(car);
                break;
            }
        }
        initContainers();
        initDataListeners();
    }

    @Override
    public void toNextPlayer() {
        // set data somewhere
        sendToServer();
        networkManager.toNextPlayer(room);
    }

    public void sendToServer() {
        networkManager.updateGameData(room, this, thisPlayer);
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

    private void initDataListeners() {
        Gdx.app.log("OGC", thisPlayer.getCar().getBase().getBaseColor().toString());
        networkManager.addPlayersDataChangedListener(
                room, playersData, new Procedure() {
                    @Override
                    public void call() {
                        if (networkManager.getThisPlayerData(playersData) != null) {
                            playerCardWindow.getCardContainer().clearChildren();
                            for (NetworkManager.GameData.GameCard gameCard
                                    : networkManager.getThisPlayerData(playersData).getCards()) {
                                playerCardWindow.getCardContainer().addCard(new Card(
                                        new GameCard(gameCard.getCardType(), thisPlayer),
                                        gameScreen.getGameInputProcessor(),
                                        gameScreen.getAssetManager()), 0, 0
                                );
                            }
                        }
                        if (networkManager.isThisPlayerTurn(playersData)) {
                            playerCardWindow.getCardContainer().setTouchable(Touchable.enabled);
                            playerCardWindow.enableButton();
                            algorithmCardWindow.enable();
                        } else {
                            playerCardWindow.getCardContainer().setTouchable(Touchable.disabled);
                            playerCardWindow.disableButton();
                            algorithmCardWindow.disable();
                        }
                        if (algorithmCardWindow.getActionsCardContainer() != null) {
                            algorithmCardWindow.getActionsCardContainer().discardMode = false;
                        }
                        if (algorithmCardWindow.getCyclesCardContainer() != null) {
                            algorithmCardWindow.getCyclesCardContainer().discardMode = false;
                        }
                    }
                }, new Procedure() {
                    @Override
                    public void call() { }
                }
        );
        networkManager.addCardsDataChangedListener(
                room, cardsData, new Procedure() {
                    @Override
                    public void call() {
                        updateNonPlayerCards(cardsData);
                    }
                }, new Procedure() {
                    @Override
                    public void call() { }
                }
        );
    }

    private void updateNonPlayerCards(NetworkManager.GameData.CardsData cardsData) {
        discardPile.clear();
        for (NetworkManager.GameData.GameCard gameCard : cardsData.getDiscardPile()) {
            discardPile.add(new GameCard(gameCard.getCardType(), null));
        }
        talon.clear();
        for (NetworkManager.GameData.GameCard gameCard : cardsData.getTalon()) {
            talon.add(new GameCard(gameCard.getCardType(), null));
        }
    }
}
