package com.programmers.ui_elements;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.utils.Array;

public class CardContainer extends VerticalGroup {

    private final Array<Card> cards;

    public CardContainer(final Array<com.programmers.game.Card> cards) {
        this.cards = new Array<>(cards.size);
        for (int i = 0; i < cards.size; i++) {
            this.cards.add(new Card(cards.get(i)));
            addActor(this.cards.get(i));
        }
        space(10);
        setSize(getPrefWidth(), getPrefHeight());
        setDebug(true);
    }

    @Override
    protected void setParent(Group parent) {
        super.setParent(parent);
        for (Card card : cards) {
            card.setParent(this);
        }
    }

    @Override
    protected void childrenChanged() {
        super.childrenChanged();
        setSize(getPrefWidth(), getPrefHeight());
    }
}
