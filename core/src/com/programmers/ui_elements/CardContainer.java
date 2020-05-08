package com.programmers.ui_elements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Sort;
import com.programmers.enums.CardType;

import java.util.Comparator;

public class CardContainer extends VerticalGroup {

    public static final Array<CardContainer> cardContainers = new Array<>(3);
    private static final Sort sort = new Sort();
    private final boolean sorting;
    private final Content content;

    public CardContainer(final Array<com.programmers.game.Card> cards,
                         final Content content, boolean sorting) {
        this.sorting = sorting;
        this.content = content;
        setSize(100, 100);
        setPosition(0, 0, Align.bottomLeft);
        if (cards != null)
            for (com.programmers.game.Card card : cards) addCard(new Card(card));
        cardContainers.add(this);
        setDebug(true);
    }

    @Override
    protected void childrenChanged() {
        super.childrenChanged();
        setSize(getPrefWidth(), getPrefHeight());
        setPosition(0, Gdx.graphics.getHeight(), Align.topLeft);
        if (sorting)
            sort.sort(getChildren(), new Comparator<Actor>() {
                @Override
                public int compare(Actor lhs, Actor rhs) {
                    return ((Card) lhs).compareTo((Card) rhs);
                }
            });
    }

    public void addCard(final Card card) {
        switch (content) {
            case Cycles:
                if (card.getCard().getType() == CardType.Cycle2
                        || card.getCard().getType() == CardType.Cycle3) {
                    addActor(card);
                }
                break;
            case Actions:
                if (card.getCard().getType() != CardType.Cycle2
                        && card.getCard().getType() != CardType.Cycle3) {
                    addActor(card);
                }
                break;
            case All:
                addActor(card);
        }
    }

    public enum Content {
        Cycles,
        Actions,
        All
    }
}
