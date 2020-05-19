package com.programmers.game.online;

import com.programmers.game.Field;
import com.programmers.game.GameCard;
import com.programmers.game.Player;
import com.programmers.game.hotseat.GameController;
import com.programmers.interfaces.NetworkManager;
import com.programmers.screens.GameScreen;
import com.programmers.ui_elements.AlgorithmCardWindow;
import com.programmers.ui_elements.CardContainer;
import com.programmers.ui_elements.PlayerCardWindow;

import java.util.List;

public final class OnlineGameController extends GameController {

    private NetworkManager networkManager;
    private final NetworkManager.Room room;

    public OnlineGameController(Player[] players, Field field,
                                NetworkManager.Room room, GameScreen gameScreen) {
        super(players, field, gameScreen);
        this.room = room;
    }

    public OnlineGameController(NetworkManager.GameData gameData, Field field,
                                NetworkManager.Room room, GameScreen gameScreen) {
        super(gameData.getGamePlayers(field, room), field, gameScreen, null);
        this.room = room;
        thisPlayer = players[0];
        for (NetworkManager.GameData.GameCard gameCard : gameData.getCardsData().getDiscardPile()) {
            discardPile.add(new GameCard(gameCard.getCardType(), null));
        }
        for (NetworkManager.GameData.GameCard gameCard : gameData.getCardsData().getTalon()) {
            talon.add(new GameCard(gameCard.getCardType(), null));
        }
        for (int i = 0; i < players.length; i++) {
            List<NetworkManager.GameData.GameCard> gameCards =
                    gameData.getPlayersData().getPlayers().get(i).getCards();
            for (NetworkManager.GameData.GameCard gameCard : gameCards) {
                players[i].getGameCards().add(new GameCard(gameCard.getCardType(), players[i]));
            }
        }
        // add UI objects that are necessary for the game
        CardContainer playerCardContainer = new CardContainer(thisPlayer.getGameCards(),
                difficulty, CardContainer.Content.All, this
        );
        playerCardWindow = new PlayerCardWindow(
                "Player cards", playerCardContainer, this
        );
        algorithmCardWindow = new AlgorithmCardWindow("Algorithm", this);
    }
}
