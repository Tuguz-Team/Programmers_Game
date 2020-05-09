package com.programmers.ui_elements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.programmers.game.GameController;

public class PlayerCardWindow extends Table {

    public PlayerCardWindow(final String name, final CardContainer cardContainer,
                            final GameController gameController) {
        setFillParent(true);
        setDebug(true);
        final Table table = new Table();
        add(cardContainer);
        add(table);
        final Button button = new Button(
                new TextureRegionDrawable(new Texture("Sprites/AlgorithmButton/StartButtonOn.png")),
                new TextureRegionDrawable(new Texture("Sprites/AlgorithmButton/StartButtonOff.png"))
        );
        table.add(button);
        button.addListener(new MyButton.Listener() {
            @Override
            public void call() {

            }
        });
        table.add(new Label(name, new Skin(Gdx.files.internal("uiskin.json")))).bottom();
        left().bottom();
    }
}
