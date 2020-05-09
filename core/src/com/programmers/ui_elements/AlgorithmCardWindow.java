package com.programmers.ui_elements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.programmers.enums.Difficulty;

public class AlgorithmCardWindow extends Table {

    public AlgorithmCardWindow(final String name,
                               final Array<com.programmers.game.Card> cards,
                               final Difficulty difficulty) {
        setFillParent(true);
        setDebug(true);
        add(new Label(name, new Skin(Gdx.files.internal("uiskin.json")))).bottom();
        Table table = new Table();
        add(table);
        // Добавляем кнопку: table.add(...);
        if (difficulty == Difficulty.Hard) {
            CardContainer cyclesCardContainer = new CardContainer(cards, CardContainer.Content.Cycles, false);
            table.add(cyclesCardContainer).spaceRight(10).bottom();
        }
        CardContainer actionsCardContainer = new CardContainer(cards, CardContainer.Content.Actions, false);
        table.add(actionsCardContainer).bottom();
        right().bottom();
    }
}
