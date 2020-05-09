package com.programmers.ui_elements;

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

    private int prevChildrenCount;

    public CardContainer(final Array<com.programmers.game.Card> cards,
                         final Content content, final boolean sorting) {
        this.sorting = sorting;
        this.content = content;
        addActor(new Card());
        setSize(100, 100);
        setPosition(0, 0, Align.bottomLeft);
        if (cards != null) {
            for (com.programmers.game.Card card : cards) addCard(new Card(card));
        }
        prevChildrenCount = getChildren().size;
        cardContainers.add(this);
        setDebug(true);
    }

    @Override
    protected void childrenChanged() {
        super.childrenChanged();
        setSize(getPrefWidth(), getPrefHeight());
        // if we add some stuff, but don't remove
        if (prevChildrenCount < getChildren().size) {
            if (sorting) {
                sort.sort(getChildren(), new Comparator<Actor>() {
                    @Override
                    public int compare(Actor lhs, Actor rhs) {
                        return ((Card) lhs).compareTo((Card) rhs);
                    }
                });
            } else {
                // move added item to the top
                for (int i = prevChildrenCount; i > 1; i--) {
                    getChildren().swap(i, i - 1);
                }
            }
        }
        prevChildrenCount = getChildren().size;
    }

    public void addCard(final Card card) {
        switch (content) {
            case Cycles:
                if (card.getCard().getType() == CardType.Cycle2
                        || card.getCard().getType() == CardType.Cycle3) {
                    addActor(card);
                } else {
                    card.getPrevParent().addActor(card);
                }
                break;
            case Actions:
                if (card.getCard().getType() != CardType.Cycle2
                        && card.getCard().getType() != CardType.Cycle3) {
                    addActor(card);
                } else {
                    card.getPrevParent().addActor(card);
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
