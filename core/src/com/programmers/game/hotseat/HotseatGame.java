package com.programmers.game.hotseat;

import com.badlogic.gdx.utils.Array;
import com.programmers.enums.Difficulty;
import com.programmers.game.Field;
import com.programmers.game.Player;
import com.programmers.game_objects.Base;
import com.programmers.game_objects.Car;
import com.programmers.screens.GameScreen;
import com.programmers.screens.ScreenLoader;

import static com.badlogic.gdx.math.MathUtils.random;

public class HotseatGame extends GameScreen {

    private final HotseatGameController hotseatGameController;

    public HotseatGame(final ScreenLoader screenLoader, final Difficulty difficulty, final int playersCount) {
        super(screenLoader, difficulty, playersCount);

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
        hotseatGameController = new HotseatGameController(players, field, this);

        loadGame();
    }

    @Override
    protected void setCameraPosition() {
        int x, z;
        Car car = hotseatGameController.getThisPlayer().getCar();
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
        camera.position.set(x, size, z);
        camera.update();
    }

    @Override
    protected void addCardWindows() {
        addActor(hotseatGameController.getPlayerCardWindow());
        addActor(hotseatGameController.getAlgorithmCardWindow());
    }

    @Override
    protected void loadModels() {
        field.loadModels();
        for (Player player : hotseatGameController.getPlayers())
            player.getCar().loadModel();
    }
}
