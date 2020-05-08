package com.programmers.ui_elements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class CardWindow extends Table {

    public CardWindow(final String name, final CardContainer cardContainer) {
        setFillParent(true);
        setDebug(true);
        add(new Label(name, new Skin(Gdx.files.internal("uiskin.json"))));
        row();
        add(cardContainer);
    }
}
