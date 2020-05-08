package com.programmers.ui_elements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;

public class AlgorithmCardWindow extends Table {

    private final CardContainer cyclesCardContainer;
    private final CardContainer actionsCardContainer;

    public AlgorithmCardWindow(final String name,
                               final Array<com.programmers.game.Card> cards) {
        setFillParent(true);
        setDebug(true);
        add(new Label(name, new Skin(Gdx.files.internal("uiskin.json")))).bottom();
        Table table = new Table();
        add(table);
        // Добавляем кнопку: table.add(...);
        cyclesCardContainer = new CardContainer(cards, CardContainer.Content.Cycles, false);
        table.add(cyclesCardContainer).spaceRight(10).bottom();
        actionsCardContainer = new CardContainer(cards, CardContainer.Content.Actions, false);
        table.add(actionsCardContainer).bottom();
        right().bottom();
    }
}
