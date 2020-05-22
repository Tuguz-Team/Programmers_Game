package com.programmers.game.online;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.programmers.enums.Difficulty;
import com.programmers.game.Field;
import com.programmers.game.GameCard;
import com.programmers.game.GameController;
import com.programmers.game.Player;
import com.programmers.game.hotseat.HotseatGameController;
import com.programmers.game_objects.Car;
import com.programmers.game_objects.Chunk;
import com.programmers.interfaces.NetworkManager;
import com.programmers.interfaces.Procedure;
import com.programmers.screens.ScreenLoader;
import com.programmers.ui_elements.AlgorithmCardWindow;
import com.programmers.ui_elements.Card;
import com.programmers.ui_elements.CardContainer;
import com.programmers.ui_elements.CycleCardContainer;
import com.programmers.ui_elements.LabelDisappear;
import com.programmers.ui_elements.OKDialog;
import com.programmers.ui_elements.OnlineGameInfo;
import com.programmers.ui_elements.PlayerCardWindow;

import java.util.Collections;
import java.util.List;

public final class OnlineGameController extends GameController {

    private final NetworkManager networkManager;
    private final NetworkManager.Room room;
    private NetworkManager.GameData.PlayersData playersData = new NetworkManager.GameData.PlayersData();
    private NetworkManager.GameData.CardsData cardsData = new NetworkManager.GameData.CardsData();

    private final Car[] cars;
    private OnlineGameInfo onlineGameInfo;

    OnlineGameController(NetworkManager networkManager, HotseatGameController hotseatGameController,
                         NetworkManager.Room room, OnlineGame onlineGame, Field field, Car[] cars) {
        super(onlineGame, field);
        this.networkManager = networkManager;
        this.room = room;
        this.cars = cars;
        //
        networkManager.setFieldData(room, field);
        networkManager.sendGameData(room, hotseatGameController);
        networkManager.setPlayersOrder(room);
        //
        NetworkManager.GameData gameData = networkManager.getGameData(room);
        NetworkManager.GameData.Player player = networkManager.getThisPlayerData(gameData.getPlayersData());
        NetworkManager.FieldData.Base base = player.getCar().getBase();
        for (Car car : cars) {
            if (car.getBase().getBaseColor() == base.getBaseColor()) {
                thisPlayer = new Player(car);
                break;
            }
        }
        algorithmToDo.addAll(hotseatGameController.getAlgorithmToDo());
        discardPile.addAll(hotseatGameController.getDiscardPile());
        talon.addAll(hotseatGameController.getTalon());
        initContainers();
        initDataListeners();
        //
        networkManager.launchRoom(room);
    }

    OnlineGameController(NetworkManager networkManager, NetworkManager.GameData gameData,
                         NetworkManager.Room room, OnlineGame onlineGame, Field field, Car[] cars) {
        super(onlineGame, field);
        this.networkManager = networkManager;
        this.room = room;
        this.cars = cars;
        NetworkManager.GameData.Player player = networkManager.getThisPlayerData(gameData.getPlayersData());
        NetworkManager.FieldData.Base base = player.getCar().getBase();
        for (Car car : cars) {
            if (car.getBase().getBaseColor() == base.getBaseColor()) {
                thisPlayer = new Player(car);
                break;
            }
        }
        initContainers();
        initDataListeners();
    }

    @Override
    public void toNextPlayer() {
        // add new cards to the player and clear playerCardWindow
        final Array<GameCard> gameCards = thisPlayer.getGameCards();
        final CardContainer playerCardContainer = playerCardWindow.getCardContainer();
        if (((Card) playerCardContainer.getChild(0)).getGameCard() != null) {
            gameCards.clear();
            for (Actor actor : playerCardContainer.getChildren()) {
                gameCards.add(((Card) actor).getGameCard());
            }
            while (gameCards.size < 5) {
                if (talon.empty()) {
                    makeTalon();
                }
                GameCard gameCard = talon.pop();
                gameCards.add(gameCard);
                gameCard.setPlayer(thisPlayer);
            }
            playerCardContainer.clearChildren();
        } else {
            gameCards.clear();
            while (gameCards.size < 5) {
                if (talon.empty()) {
                    makeTalon();
                }
                GameCard gameCard = talon.pop();
                gameCards.add(gameCard);
                gameCard.setPlayer(thisPlayer);
            }
        }
        // add this player cards to the playerCardWindow
        for (GameCard gameCard : thisPlayer.getGameCards()) {
            Card card = new Card(gameCard, gameScreen.getAssetManager());
            playerCardContainer.addCard(card, 0, 0);
            gameCard.setPlayer(thisPlayer);
        }
        // set data to server
        networkManager.updateGameData(room, this, thisPlayer);
        networkManager.toNextPlayer(room);
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

    public NetworkManager.GameData.PlayersData getPlayersData() {
        return playersData;
    }

    public void setOnlineGameInfo(OnlineGameInfo onlineGameInfo) {
        this.onlineGameInfo = onlineGameInfo;
    }

    private Car.Color getWinnerColor(NetworkManager.GameData.PlayersData playersData) {
        List<NetworkManager.GameData.Player> players = playersData.getPlayers();
        for (NetworkManager.GameData.Player player : players) {
            if ((getDifficulty() == Difficulty.Easy && player.getScore() >= 7)
                    || (getDifficulty() == Difficulty.Hard && player.getScore() >= 9)) {
                return player.getCar().getBase().getBaseColor();
            }
        }
        for (Chunk[] chunks : field.getChunks()) {
            for (Chunk chunk : chunks) {
                if (!chunk.getLives().isEmpty()) {
                    return null;
                }
            }
        }
        int maxI = 0;
        for (int i = 1; i < players.size(); i++) {
            if (players.get(i).getScore() > players.get(maxI).getScore()) {
                maxI = i;
            }
        }
        return players.get(maxI).getCar().getBase().getBaseColor();
    }

    private void initDataListeners() {
        networkManager.addPlayersDataChangedListener(
                room, playersData, new Procedure() {
                    @Override
                    public void call() {
                        new Thread() {
                            @Override
                            public void run() {
                                try {
                                    sleep(500);
                                } catch (InterruptedException e) { }
                                Table table = new Table();
                                table.setFillParent(true);
                                table.center();
                                LabelDisappear label = new LabelDisappear(networkManager.isThisPlayerTurn(playersData)
                                        ? "Your turn!"
                                        : "Turn of player with " + playersData.getPlayers().get(playersData.getIndex())
                                        .getCar().getBase().getBaseColor() + " car!", 2000);
                                table.add(label).center();
                                gameScreen.addActor(table);
                            }
                        }.start();
                        Gdx.app.postRunnable(new Runnable() {
                            @Override
                            public void run() {
                                // update data
                                if (networkManager.getThisPlayerData(playersData) != null) {
                                    playerCardWindow.getCardContainer().clearChildren();
                                    for (NetworkManager.GameData.GameCard gameCard
                                            : networkManager.getThisPlayerData(playersData).getCards()) {
                                        playerCardWindow.getCardContainer().addCard(new Card(
                                                new GameCard(gameCard.getCardType(), thisPlayer),
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
                                // update data
                                if (onlineGameInfo != null)
                                    onlineGameInfo.updateData();
                                // if we got the winner
                                Car.Color winnerColor = getWinnerColor(playersData);
                                if (winnerColor != null) {
                                    OKDialog winnerDialog = new OKDialog("Winner of the game is "
                                            + winnerColor.toString().toUpperCase() + " car!",
                                            ScreenLoader.getGameSkin()
                                    );
                                    winnerDialog.show(gameScreen);
                                    playerCardWindow.getCardContainer().setTouchable(Touchable.disabled);
                                    playerCardWindow.disableButton();
                                    algorithmCardWindow.disable();
                                }
                            }
                        });
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
                        // do algorithmToDo
                        if (!networkManager.isThisPlayerTurn(playersData)) {
                            NetworkManager.GameData.Player playerData = playersData.getPlayers()
                                    .get(playersData.getIndex());
                            Car car = null;
                            for (Car item : cars) {
                                if (item.getBase().getBaseColor() == playerData.getCar().getBase().getBaseColor()) {
                                    car = item;
                                    break;
                                }
                            }
                            Player player = new Player(car);
                            //
                            StringBuilder stringBuilder = new StringBuilder();
                            for (NetworkManager.GameData.GameCard gameCard : cardsData.getAlgorithmToDo()) {
                                for (NetworkManager.GameData.GameCard gameCard1 : gameCard.getCards()) {
                                    stringBuilder.append(gameCard1.getCardType()).append(' ');
                                }
                                stringBuilder.append(gameCard.getCardType()).append(' ');
                            }
                            Gdx.app.error("ACW", stringBuilder.toString());
                            //
                            for (NetworkManager.GameData.GameCard gameCardData : cardsData.getAlgorithmToDo()) {
                                GameCard gameCard = new GameCard(gameCardData.getCardType(), player);
                                for (NetworkManager.GameData.GameCard item : gameCardData.getCards()) {
                                    gameCard.getCards().add(new GameCard(item.getCardType(), player));
                                }
                                player.addCard(gameCard);
                                gameCard.apply();
                            }
                            if (car.getChunk().getLift() != null) {
                                car.setCompensated(false);
                                car.setPosition(car.getChunk().getLift());
                                car.addLivesFrom(car.getChunk());
                            }
                        }
                        //
                        Gdx.app.postRunnable(new Runnable() {
                            @Override
                            public void run() {
                                updateNonPlayerCards(cardsData);
                                // update algorithmCardWindow
                                algorithmCardWindow.getActionsCardContainer().clearChildren();
                                Collections.reverse(cardsData.getAlgorithmCardWindow().getActions());
                                for (int i = cardsData.getAlgorithmCardWindow().getActions().size() - 1; i >= 0; i--) {
                                    NetworkManager.GameData.GameCard gameCard =
                                            cardsData.getAlgorithmCardWindow().getActions().get(i);
                                    if (gameCard != null && gameCard.getCardType() != null) {
                                        Card card = new Card(new GameCard(
                                                gameCard.getCardType(), thisPlayer),
                                                gameScreen.getAssetManager()
                                        );
                                        algorithmCardWindow.getActionsCardContainer().addCard(card, 0, 0);
                                        card.setActionToPrevious(algorithmCardWindow.getActionsCardContainer());
                                    }
                                }
                            }
                        });
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
