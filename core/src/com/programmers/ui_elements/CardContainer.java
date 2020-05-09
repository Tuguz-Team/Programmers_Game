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
    private Array<GameCard> gameCards;

    public boolean discardMode = false;

    public CardContainer(final Array<GameCard> gameCards,
                         final Content content, final boolean sorting) {
        this.gameCards = gameCards;
        this.sorting = sorting;
        this.content = content;
        emptyCard = new Card();
        addActor(emptyCard);
        setSize(100, 100);
        setPosition(0, 0, Align.bottomLeft);
        if (gameCards != null) {
            for (GameCard gameCard : gameCards) {
                Card card = new Card(gameCard);
                addCard(card);
            }
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
        if (prevChildrenCount < getChildren().size && sorting) {
            sort.sort(getChildren(), new Comparator<Actor>() {
                @Override
                public int compare(Actor lhs, Actor rhs) {
                    return ((Card) lhs).compareTo((Card) rhs);
                }
            });
        }
        prevChildrenCount = getChildren().size;
        setTouchable();
    }

    protected void setTouchable() {
        if (!discardMode) {
            if (getChildren().size < 3)
                setTouchable(Touchable.disabled);
            else
                setTouchable(Touchable.enabled);
        }
    }

    public void addEmpty() {
        addActor(emptyCard);
    }

    public Array<GameCard> getGameCards() {
        return gameCards;
    }

    public void setGameCards(Array<GameCard> gameCards) {
        this.gameCards = gameCards;
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

    @Override
    public void clearChildren() {
        super.clearChildren();
        addEmpty();
    }

    public enum Content {
        Cycles,
        Actions,
        All
    }
}
