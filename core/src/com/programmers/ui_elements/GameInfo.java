package com.programmers.ui_elements;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.programmers.game.Player;
import com.programmers.game.hotseat.HotseatGameController;
import com.programmers.game_objects.Car;
import com.programmers.interfaces.NetworkManager;
import com.programmers.screens.GameScreen;
import com.programmers.screens.ScreenLoader;

public abstract class GameInfo extends Table {

    GameScreen gameScreen;
    Dialog dialog;

    GameInfo() {
        setFillParent(true);
        top().left();
        addListener(new MyButton.Listener() {
            @Override
            public void call() {
                dialog.show(gameScreen);
            }
        });
    }

    public abstract void updateData();

    final class CarImage extends Image {

        Car.Color color;

        CarImage(Car.Color color) {
            super((Texture) gameScreen.getAssetManager().get("Sprites/Cars/" + color + "Off.png"));
            this.color = color;
        }

        void setOn(boolean on) {
            TextureRegionDrawable textureRegionDrawable;
            if (on) {
                textureRegionDrawable = new TextureRegionDrawable(
                        (Texture) gameScreen.getAssetManager().get("Sprites/Cars/" + color + "On.png")
                );
            } else {
                textureRegionDrawable = new TextureRegionDrawable(
                        (Texture) gameScreen.getAssetManager().get("Sprites/Cars/" + color + "Off.png")
                );
            }
            setDrawable(textureRegionDrawable);
        }
    }

    final class Dialog extends OKDialog {
        private Label[] livesUnknownCount;
        private Label[] livesYellowCount;
        private Label[] livesPurpleCount;
        private Label[] livesGreenCount;
        private Label[] livesBlueCount;

        private Dialog() {
            super("Player's score", ScreenLoader.getGameSkin());
            padLeft(30).padRight(15);
            getTitleLabel().setAlignment(Align.center);
            getContentTable().add();
        }

        Dialog(final HotseatGameController hotseatGameController) {
            this();
            final int n = hotseatGameController.getPlayers().length;
            livesUnknownCount = new Label[n];
            livesYellowCount = new Label[n];
            livesPurpleCount = new Label[n];
            livesGreenCount = new Label[n];
            livesBlueCount = new Label[n];
            //
            for (Player player : hotseatGameController.getPlayers()) {
                Car.Color color = player.getCar().getBase().getBaseColor();
                getContentTable().add(new Image((Texture) gameScreen.getAssetManager()
                        .get("Sprites/Cars/" + color + "On.png")));
            }
            getContentTable().row();
            //
            getContentTable().add(new Image((Texture) gameScreen.getAssetManager()
                    .get("Sprites/Lives/LifeObjectUnknown.png")));
            for (int i = 0; i < n; i++) {
                livesUnknownCount[i] = new Label("", ScreenLoader.getGameSkin());
                livesUnknownCount[i].setFontScale(1.5f);
                getContentTable().add(livesUnknownCount[i]);
            }
            getContentTable().row();
            getContentTable().add(new Image((Texture) gameScreen.getAssetManager()
                    .get("Sprites/Lives/LifeObjectYellow.png")));
            for (int i = 0; i < n; i++) {
                livesYellowCount[i] = new Label("", ScreenLoader.getGameSkin());
                livesYellowCount[i].setFontScale(1.5f);
                getContentTable().add(livesYellowCount[i]);
            }
            getContentTable().row();
            getContentTable().add(new Image((Texture) gameScreen.getAssetManager()
                    .get("Sprites/Lives/LifeObjectPurple.png")));
            for (int i = 0; i < n; i++) {
                livesPurpleCount[i] = new Label("", ScreenLoader.getGameSkin());
                livesPurpleCount[i].setFontScale(1.5f);
                getContentTable().add(livesPurpleCount[i]);
            }
            getContentTable().row();
            getContentTable().add(new Image((Texture) gameScreen.getAssetManager()
                    .get("Sprites/Lives/LifeObjectGreen.png")));
            for (int i = 0; i < n; i++) {
                livesGreenCount[i] = new Label("", ScreenLoader.getGameSkin());
                livesGreenCount[i].setFontScale(1.5f);
                getContentTable().add(livesGreenCount[i]);
            }
            getContentTable().row();
            getContentTable().add(new Image((Texture) gameScreen.getAssetManager()
                    .get("Sprites/Lives/LifeObjectBlue.png")));
            for (int i = 0; i < n; i++) {
                livesBlueCount[i] = new Label("", ScreenLoader.getGameSkin());
                livesBlueCount[i].setFontScale(1.5f);
                getContentTable().add(livesBlueCount[i]);
            }
        }

        Dialog(final NetworkManager.GameData gameData) {
            this();
            final int n = gameData.getPlayersData().getPlayers().size();
            livesUnknownCount = new Label[n];
            livesYellowCount = new Label[n];
            livesPurpleCount = new Label[n];
            livesGreenCount = new Label[n];
            livesBlueCount = new Label[n];
            //
            for (NetworkManager.GameData.Player player : gameData.getPlayersData().getPlayers()) {
                Car.Color color = player.getCar().getBase().getBaseColor();
                getContentTable().add(new Image((Texture) gameScreen.getAssetManager()
                        .get("Sprites/Cars/" + color + "On.png")));
            }
            getContentTable().row();
            //
            getContentTable().add(new Image((Texture) gameScreen.getAssetManager()
                    .get("Sprites/Lives/LifeObjectUnknown.png")));
            for (int i = 0; i < n; i++) {
                livesUnknownCount[i] = new Label("", ScreenLoader.getGameSkin());
                livesUnknownCount[i].setFontScale(1.5f);
                getContentTable().add(livesUnknownCount[i]);
            }
            getContentTable().row();
            getContentTable().add(new Image((Texture) gameScreen.getAssetManager()
                    .get("Sprites/Lives/LifeObjectYellow.png")));
            for (int i = 0; i < n; i++) {
                livesYellowCount[i] = new Label("", ScreenLoader.getGameSkin());
                livesYellowCount[i].setFontScale(1.5f);
                getContentTable().add(livesYellowCount[i]);
            }
            getContentTable().row();
            getContentTable().add(new Image((Texture) gameScreen.getAssetManager()
                    .get("Sprites/Lives/LifeObjectPurple.png")));
            for (int i = 0; i < n; i++) {
                livesPurpleCount[i] = new Label("", ScreenLoader.getGameSkin());
                livesPurpleCount[i].setFontScale(1.5f);
                getContentTable().add(livesPurpleCount[i]);
            }
            getContentTable().row();
            getContentTable().add(new Image((Texture) gameScreen.getAssetManager()
                    .get("Sprites/Lives/LifeObjectGreen.png")));
            for (int i = 0; i < n; i++) {
                livesGreenCount[i] = new Label("", ScreenLoader.getGameSkin());
                livesGreenCount[i].setFontScale(1.5f);
                getContentTable().add(livesGreenCount[i]);
            }
            getContentTable().row();
            getContentTable().add(new Image((Texture) gameScreen.getAssetManager()
                    .get("Sprites/Lives/LifeObjectBlue.png")));
            for (int i = 0; i < n; i++) {
                livesBlueCount[i] = new Label("", ScreenLoader.getGameSkin());
                livesBlueCount[i].setFontScale(1.5f);
                getContentTable().add(livesBlueCount[i]);
            }
        }

        @Override
        protected void result(Object object) {
            gameScreen.getGameInputProcessor().unlockCamera();
            super.result(object);
        }

        @Override
        public com.badlogic.gdx.scenes.scene2d.ui.Dialog show(Stage stage) {
            gameScreen.getGameInputProcessor().lockCamera();
            return super.show(stage);
        }

        Label[] getLivesBlueCount() {
            return livesBlueCount;
        }

        Label[] getLivesGreenCount() {
            return livesGreenCount;
        }

        Label[] getLivesPurpleCount() {
            return livesPurpleCount;
        }

        Label[] getLivesUnknownCount() {
            return livesUnknownCount;
        }

        Label[] getLivesYellowCount() {
            return livesYellowCount;
        }
    }
}
