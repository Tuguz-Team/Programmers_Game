package com.programmers.game;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.programmers.interfaces.Procedure;
import com.programmers.interfaces.SpecificCode;
import com.programmers.screens.GameScreen;
import com.programmers.screens.ScreenLoader;

public class OnlineGame extends GameScreen {

    private final SpecificCode.Room room;
    private SpecificCode.Field fieldData;
    private final boolean sendData;
    private boolean loadGame;

    private final Dialog waitDialog;

    public OnlineGame(final ScreenLoader screenLoader, final SpecificCode.Room room, boolean sendData) {
        super(screenLoader, room.getDifficulty(), room.getPlayersCount());

        waitDialog = new Dialog("Waiting...", ScreenLoader.getDefaultGdxSkin());
        waitDialog.setMovable(false);

        this.sendData = sendData;
        this.room = room;
        if (sendData) {
            field = new Field(this);
            loadGame();
            screenLoader.specificCode.sendFieldData(room, field);
        } else {
            screenLoader.specificCode.addRoomChangedListener(
                    room, new Procedure() {
                        @Override
                        public void call() {
                            waitDialog.show(OnlineGame.this);
                            new Thread() {
                                @Override
                                public void run() {
                                    try {
                                        sleep(2000);
                                        fieldData = screenLoader.specificCode.getFieldData(room);
                                        loadGame = true;
                                        screenLoader.specificCode.removeListener(room);
                                    } catch (InterruptedException ignored) { }
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
            field = new Field(this, fieldData);
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
        if (sendData) {
            screenLoader.specificCode.deleteRoom(room.getName());
        } else {
            screenLoader.specificCode.removePlayerFromRoom(room);
        }
        screenLoader.specificCode.removeListener(room);
        super.dispose();
    }
}
