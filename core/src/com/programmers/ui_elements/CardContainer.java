package com.programmers.ui_elements;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Sort;
import com.programmers.enums.CardType;
import com.programmers.game.GameCard;

import java.util.Comparator;

public class CardContainer extends VerticalGroup {

    public static final Array<CardContainer> cardContainers = new Array<>(3);
    private static final Sort sort = new Sort();
    private final boolean sorting;
    private final Content content;
    private final Card emptyCard;

    private int prevChildrenCount;

    public CardContainer(final Array<GameCard> cards,
                         final Content content, final boolean sorting) {
        this.sorting = sorting;
        this.content = content;
        emptyCard = new Card();
        addActor(emptyCard);
        setSize(100, 100);
        setPosition(0, 0, Align.bottomLeft);
        if (cards != null) {
            for (GameCard gameCard : cards) addCard(new Card(gameCard));
        }
        prevChildrenCount = getChildren().size;
        cardContainers.add(this);
        setDebug(true);
    }

    @Override
    protected void childrenChanged() {
        super.childrenChanged();
        if (getChildren().size > 1) {
            removeActor(emptyCard);
        } else if (getChildren().isEmpty()) {
            addActor(emptyCard);
        }
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
                for (int i = prevChildrenCount; i > 0; i--) {
                    getChildren().swap(i, i - 1);
                }
            }
        }
        prevChildrenCount = getChildren().size;
        setTouchable();
    }

    protected void setTouchable() {
        if (getChildren().size < 3)
            setTouchable(Touchable.disabled);
        else
            setTouchable(Touchable.enabled);
    }

    public void addEmpty() {
        addActor(emptyCard);
    }

    public void addCard(final Card card) {
        switch (content) {
            case Cycles:
                if (card.getGameCard().getType() == CardType.Cycle2
                        || card.getGameCard().getType() == CardType.Cycle3) {
                    addActor(card);
                } else {
                    card.getPrevParent().addActor(card);
                }
                break;
            case Actions:
                if (card.getGameCard().getType() != CardType.Cycle2
                        && card.getGameCard().getType() != CardType.Cycle3) {
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
