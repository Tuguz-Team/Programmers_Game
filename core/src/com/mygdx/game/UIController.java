package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Disposable;

class UIController implements Disposable {

    private Stage stage;
    private Skin skin;
    private SpriteBatch spriteBatch;

    UIController(Skin skin) {
        this.skin = skin;
        stage = new Stage();
        spriteBatch = new SpriteBatch();
    }

    void draw() {
        spriteBatch.begin();
        // draw something
        spriteBatch.end();
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
        spriteBatch.dispose();
    }
}
