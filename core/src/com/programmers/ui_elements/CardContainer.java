package com.programmers.ui_elements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Sort;

import java.util.Comparator;

public class CardContainer extends VerticalGroup {

    private final Array<Card> cards;
    private final Sort sort = new Sort();

    public CardContainer(final Array<com.programmers.game.Card> cards) {
        this.cards = new Array<>(cards.size);
        for (int i = 0; i < cards.size; i++) {
            this.cards.add(new Card(cards.get(i)));
            addActor(this.cards.get(i));
        }
        setDebug(true);
    }

    @Override
    protected void setParent(Group parent) {
        super.setParent(parent);
        for (Card card : cards) {
            this.addActor(card);
        }
    }

    @Override
    protected void childrenChanged() {
        super.childrenChanged();
        setSize(getPrefWidth(), getPrefHeight());
        setPosition(0, Gdx.graphics.getHeight(), Align.topLeft);
        this.sort.sort(getChildren(), new Comparator<Actor>() {
            @Override
            public int compare(Actor lhs, Actor rhs) {
                return ((Card) lhs).compareTo((Card) rhs);
            }
        });
    }
}
