package com.programmers.screens;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public final class UIController {

    private Stage stage;
    private Skin skin;
    private SpriteBatch spriteBatch;

    public UIController(Skin skin) {
        this.skin = skin;
        stage = new Stage();
        spriteBatch = new SpriteBatch();
    }

    public void draw() {
        spriteBatch.begin();
        // draw something
        spriteBatch.end();
    }

    public void dispose() {
        stage.dispose();
        skin.dispose();
        spriteBatch.dispose();
    }
}
