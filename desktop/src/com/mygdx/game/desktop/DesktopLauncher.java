package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.programmers.enums.Difficulty;
import com.programmers.game.Field;
import com.programmers.game.Player;
import com.programmers.game.hotseat.HotseatGameController;
import com.programmers.game.online.OnlineGameController;
import com.programmers.interfaces.NetworkManager;
import com.programmers.interfaces.Procedure;
import com.programmers.screens.ScreenLoader;

import java.util.LinkedList;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new ScreenLoader(new NetworkManager() {
			@Override
			public void registerAnon() {

			}

			@Override
			public boolean createNewRoom(String name, int playersCount, Difficulty difficulty) {
				return false;
			}

			@Override
			public void deleteRoom(Room room) {

			}

			@Override
			public void launchRoom(Room room) {

			}

			@Override
			public void setPlayersOrder(Room room) {

			}

			@Override
			public void setFieldData(Room room, Field field) {

			}

			@Override
			public void toNextPlayer(Room room) {

			}

			@Override
			public boolean isThisPlayerTurn(GameData.PlayersData playersData) {
				return false;
			}

			@Override
			public GameData.Player getThisPlayerData(GameData.PlayersData playersData) {
				return null;
			}

			@Override
			public FieldData getFieldData(Room room) {
				return null;
			}

			@Override
			public void sendGameData(Room room, HotseatGameController hotseatGameController) {

			}

			@Override
			public void updateGameData(Room room, OnlineGameController onlineGameController, Player player) {

			}

			@Override
			public GameData getGameData(Room room) {
				return null;
			}

			@Override
			public boolean addPlayerToRoom(Room room) {
				return false;
			}

			@Override
			public boolean removePlayerFromRoom(Room room) {
				return false;
			}

			@Override
			public LinkedList<Room> findRooms() {
				return null;
			}

			@Override
			public void addRoomChangedListener(Room room, Procedure exists, Procedure doesNotExist) {

			}

			@Override
			public void removeRoomChangedListener() {

			}

			@Override
			public void addPlayersDataChangedListener(Room room, GameData.PlayersData playersData, Procedure exists, Procedure doesNotExist) {

			}

			@Override
			public void addCardsDataChangedListener(Room room, GameData.CardsData cardsData, Procedure exists, Procedure doesNotExist) {

			}
		}), config);
	}
}
