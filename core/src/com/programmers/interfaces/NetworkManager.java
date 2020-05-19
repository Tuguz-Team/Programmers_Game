package com.programmers.interfaces;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.programmers.enums.CardType;
import com.programmers.enums.Difficulty;
import com.programmers.enums.Direction;
import com.programmers.game.Field;
import com.programmers.game.Player;
import com.programmers.game.online.OnlineGameController;
import com.programmers.game_objects.Base;
import com.programmers.game_objects.Car;
import com.programmers.game_objects.Life;
import com.programmers.game_objects.Lift;
import com.programmers.ui_elements.Card;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public interface NetworkManager {

    void registerAnon();

    boolean createNewRoom(String name, int playersCount, Difficulty difficulty);

    boolean roomExists(String name);

    void deleteRoom(String name);

    void launchRoom(Room room);

    void sendFieldData(Room room, com.programmers.game.Field field);

    FieldData getFieldData(Room room);

    void sendGameData(Room room, OnlineGameController onlineGameController);

    GameData getGameData(Room room);

    boolean addPlayerToRoom(Room room);

    boolean removePlayerFromRoom(Room room);

    LinkedList<Room> findRooms();

    void addRoomChangedListener(Room room, Procedure procedure);

    void removeListener(Room room);

    final class Room {
        private String name;
        private int nowPlayers = 1;
        private int playersCount;
        private Difficulty difficulty;
        private boolean launched;

        public Room() { }

        public Room(String name, int playersCount, Difficulty difficulty) {
            this.name = name;
            this.playersCount = playersCount;
            this.difficulty = difficulty;
        }

        public void set(Room room) {
            name = room.getName();
            playersCount = room.getPlayersCount();
            difficulty = room.getDifficulty();
            nowPlayers = room.getNowPlayers();
            launched = room.isLaunched();
        }

        public String getName() {
            return name;
        }

        public int getPlayersCount() {
            return playersCount;
        }

        public void setNowPlayers(int nowPlayers) {
            this.nowPlayers = nowPlayers;
        }

        public Difficulty getDifficulty() {
            return difficulty;
        }

        public int getNowPlayers() {
            return nowPlayers;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setPlayersCount(int playersCount) {
            this.playersCount = playersCount;
        }

        public void setDifficulty(Difficulty difficulty) {
            this.difficulty = difficulty;
        }

        public void setLaunched(boolean launched) {
            this.launched = launched;
        }

        public boolean isLaunched() {
            return launched;
        }
    }

    final class FieldData {
        private int size;
        private List<Chunk> chunks;
        private List<Life> lives;
        private List<Wall> walls;
        private List<Base> bases;
        private List<Lift> lifts;

        public FieldData() { }

        public FieldData(Field field) {
            size = field.getSize();
            chunks = new LinkedList<>();
            lives = new LinkedList<>();
            walls = new LinkedList<>();
            lifts = new LinkedList<>();
            for (com.programmers.game_objects.Chunk[] chunks : field.getChunks()) {
                for (com.programmers.game_objects.Chunk chunk : chunks) {
                    this.chunks.add(new Chunk(chunk));
                    for (com.programmers.game_objects.Life life : chunk.getLives()) {
                        this.lives.add(new Life(life));
                    }
                    if (chunk.getWallBack() != null) walls.add(new Wall(chunk.getWallBack()));
                    if (chunk.getWallForward() != null) walls.add(new Wall(chunk.getWallForward()));
                    if (chunk.getWallLeft() != null) walls.add(new Wall(chunk.getWallLeft()));
                    if (chunk.getWallRight() != null) walls.add(new Wall(chunk.getWallRight()));
                    if (chunk instanceof com.programmers.game_objects.Lift) {
                        com.programmers.game_objects.Lift lift =
                                (com.programmers.game_objects.Lift) chunk;
                        lifts.add(new Lift(lift));
                    }
                }
            }
            bases = Arrays.asList(new Base((com.programmers.game_objects.Base) field.getChunks()[0][0]),
                    new Base((com.programmers.game_objects.Base) field.getChunks()[0][size - 1]),
                    new Base((com.programmers.game_objects.Base) field.getChunks()[size - 1][0]),
                    new Base((com.programmers.game_objects.Base) field.getChunks()[size - 1][size - 1]));
        }

        public int getSize() {
            return size;
        }

        public List<Chunk> getChunks() {
            return chunks;
        }

        public List<Life> getLives() {
            return lives;
        }

        public List<Wall> getWalls() {
            return walls;
        }

        public List<Base> getBases() {
            return bases;
        }

        public List<Lift> getLifts() {
            return lifts;
        }

        public abstract static class GameObject {
            private int x, y, z;

            public GameObject() { }

            public GameObject(com.programmers.game_objects.GameObject gameObject) {
                x = gameObject.getX();
                y = gameObject.getY();
                z = gameObject.getZ();
            }

            public int getX() {
                return x;
            }

            public int getY() {
                return y;
            }

            public int getZ() {
                return z;
            }
        }

        public static class Chunk extends FieldData.GameObject {
            private Color color;

            public Chunk() { }

            public Chunk(com.programmers.game_objects.Chunk chunk) {
                super(chunk);
                color = chunk.getColor();
            }

            public Color getColor() {
                return color;
            }
        }

        public static class Base extends GameObject {
            private com.programmers.game_objects.Car.Color baseColor;

            public Base() { }

            public Base(com.programmers.game_objects.Base base) {
                super(base);
                baseColor = base.getBaseColor();
            }

            public com.programmers.game_objects.Car.Color getBaseColor() {
                return baseColor;
            }
        }

        public static class Life extends FieldData.GameObject {
            private com.programmers.game_objects.Life.Type type;

            public Life() { }

            public Life (com.programmers.game_objects.Life life) {
                super(life);
                type = life.getType();
            }

            public com.programmers.game_objects.Life.Type getType() {
                return type;
            }
        }

        public static class Lift extends Chunk {
            private Chunk to;

            public Lift() { }

            public Lift(com.programmers.game_objects.Lift lift) {
                super(lift);
                to = new Chunk(lift.getLift());
            }

            public Chunk getTo() {
                return to;
            }
        }

        public static class Wall extends GameObject {
            private Direction direction;

            public Wall() { }

            public Wall(com.programmers.game_objects.Wall wall) {
                super(wall);
                direction = wall.getDirection();
            }

            public Direction getDirection() {
                return direction;
            }
        }

        public static class Car extends GameObject {
            private Base base;
            private List<Life> lives;
            private Direction direction;
            private boolean compensated;

            public Car() { }

            public Car(com.programmers.game_objects.Car car) {
                base = new Base(car.getBase());
                lives = new LinkedList<>();
                for (com.programmers.game_objects.Life life : car.getLives()) {
                    lives.add(new Life(life));
                }
                direction = car.getDirection();
                compensated = car.isCompensated();
            }

            public Base getBase() {
                return base;
            }

            public List<Life> getLives() {
                return lives;
            }

            public Direction getDirection() {
                return direction;
            }

            public boolean isCompensated() {
                return compensated;
            }
        }
    }

    final class GameData {
        private PlayersData playersData;
        private CardsData cardsData;

        public GameData() { }

        public GameData(PlayersData playersData, CardsData cardsData) {
            this.playersData = playersData;
            this.cardsData = cardsData;
        }

        public GameData(OnlineGameController onlineGameController) {
            playersData = new PlayersData(onlineGameController);
            cardsData = new CardsData(onlineGameController);
        }

        public PlayersData getPlayersData() {
            return playersData;
        }

        public CardsData getCardsData() {
            return cardsData;
        }

        public com.programmers.game.Player[] getGamePlayers(Field field, Room room) {
            com.programmers.game.Player[] players =
                    new com.programmers.game.Player[room.getPlayersCount()];
            for (int i = 0; i < room.getPlayersCount(); i++) {
                FieldData.Car car = playersData.getPlayers().get(i).getCar();
                FieldData.Base base = car.base;
                players[i] = new com.programmers.game.Player(new Car(
                        (Base) field.getChunks()[base.getX()][base.getZ()],
                        car.direction
                ));
            }
            return players;
        }

        public static final class PlayersData {
            private List<Player> players;

            public PlayersData() { }

            public PlayersData(OnlineGameController onlineGameController) {
                players = new LinkedList<>();
                for (com.programmers.game.Player player : onlineGameController.getPlayers()) {
                    players.add(new Player(player));
                }
            }

            public List<Player> getPlayers() {
                return players;
            }
        }

        public static final class CardsData {
            private List<GameCard> playerCardWindow;
            private AlgorithmCardWindow algorithmCardWindow;
            private List<GameCard> discardPile;
            private List<GameCard> talon;

            public CardsData() { }

            public CardsData(OnlineGameController onlineGameController) {
                playerCardWindow = new LinkedList<>();
                for (Actor actor : onlineGameController.getPlayerCardWindow().getCardContainer().getChildren()) {
                    GameCard gameCard = new GameCard(((Card) actor).getGameCard());
                    playerCardWindow.add(gameCard);
                }
                algorithmCardWindow = new AlgorithmCardWindow(onlineGameController.getAlgorithmCardWindow());
                discardPile = new LinkedList<>();
                for (com.programmers.game.GameCard gameCard : onlineGameController.getDiscardPile()) {
                    discardPile.add(new GameCard(gameCard));
                }
                talon = new LinkedList<>();
                for (com.programmers.game.GameCard gameCard : onlineGameController.getTalon()) {
                    talon.add(new GameCard(gameCard));
                }
            }

            public List<GameCard> getPlayerCardWindow() {
                return playerCardWindow;
            }

            public AlgorithmCardWindow getAlgorithmCardWindow() {
                return algorithmCardWindow;
            }

            public List<GameCard> getDiscardPile() {
                return discardPile;
            }

            public List<GameCard> getTalon() {
                return talon;
            }
        }

        public static final class Player {
            private int score;
            private List<GameCard> cards;
            private List<FieldData.Life> lives;
            private FieldData.Car car;

            public Player() { }

            public Player(com.programmers.game.Player player) {
                score = player.getScore();
                cards = new LinkedList<>();
                for (com.programmers.game.GameCard gameCard : player.getGameCards()) {
                    cards.add(new GameCard(gameCard));
                }
                lives = new LinkedList<>();
                for (Life life : player.getLives()) {
                    lives.add(new FieldData.Life(life));
                }
                car = new FieldData.Car(player.getCar());
            }

            public int getScore() {
                return score;
            }

            public FieldData.Car getCar() {
                return car;
            }

            public List<GameCard> getCards() {
                return cards;
            }

            public List<FieldData.Life> getLives() {
                return lives;
            }
        }

        public static final class GameCard {
            private CardType cardType;

            public GameCard() { }

            public GameCard(com.programmers.game.GameCard gameCard) {
                if (gameCard != null)
                    cardType = gameCard.getType();
            }

            public CardType getCardType() {
                return cardType;
            }
        }

        public static class AlgorithmCardWindow {
            private List<GameCard> actions;
            private List<GameCard> cycles;

            public AlgorithmCardWindow() { }

            public AlgorithmCardWindow(com.programmers.ui_elements.AlgorithmCardWindow algorithmCardWindow) {
                actions = new LinkedList<>();
                for (Cell cell : algorithmCardWindow.getActionsCardContainer().getCells()) {
                    if (cell.getActor() != null) {
                        GameCard gameCard = new GameCard(((Card) cell.getActor()).getGameCard());
                        actions.add(gameCard);
                    } else {
                        actions.add(null);
                    }
                }
                cycles = new LinkedList<>();
                if (algorithmCardWindow.getCyclesCardContainer() != null) {
                    for (Cell cell : algorithmCardWindow.getCyclesCardContainer().getCells()) {
                        if (cell.getActor() != null) {
                            GameCard gameCard = new GameCard(((Card) cell.getActor()).getGameCard());
                            cycles.add(gameCard);
                        } else {
                            cycles.add(null);
                        }
                    }
                }
            }

            public List<GameCard> getActions() {
                return actions;
            }

            public List<GameCard> getCycles() {
                return cycles;
            }
        }
    }
}
