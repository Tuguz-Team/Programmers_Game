package com.programmers.game;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.programmers.interfaces.Procedure;
import com.programmers.interfaces.SpecificCode;
import com.programmers.screens.GameScreen;
import com.programmers.screens.ScreenLoader;

public class OnlineGame extends GameScreen {

    private final SpecificCode.Room room;
    private final boolean sendSeed;
    private boolean loadGame;

    private final Dialog waitDialog;

    public OnlineGame(final ScreenLoader screenLoader, final SpecificCode.Room room, boolean sendSeed) {
        super(screenLoader, room.getDifficulty(), room.getPlayersCount());

        waitDialog = new Dialog("Waiting...", ScreenLoader.getDefaultGdxSkin());
        waitDialog.setMovable(false);

        this.sendSeed = sendSeed;
        this.room = room;
        if (sendSeed) {
            field = new Field(this);
            loadGame();
            // send data
        } else {
            screenLoader.specificCode.addListener(
                    room, new Procedure() {
                        @Override
                        public void call() {
                            waitDialog.show(OnlineGame.this);
                            new Thread() {
                                @Override
                                public void run() {
                                    // receive data
                                    loadGame = true;
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
            field = new Field(OnlineGame.this);
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
        //
    }

    @Override
    protected void loadModels() {
        field.loadModels();
    }

    @Override
    public void dispose() {
        if (sendSeed) {
            screenLoader.specificCode.deleteRoom(room.getName());
        } else {
            screenLoader.specificCode.removePlayerFromRoom(room);
        }
        super.dispose();
    }
}
