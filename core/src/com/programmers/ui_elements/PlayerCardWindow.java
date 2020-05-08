package com.programmers.ui_elements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class PlayerCardWindow extends Table {

    public PlayerCardWindow(final String name, final CardContainer cardContainer) {
        setFillParent(true);
        setDebug(true);
        add(cardContainer);
        add(new Label(name, new Skin(Gdx.files.internal("uiskin.json")))).bottom();
        left().bottom();
    }
}
