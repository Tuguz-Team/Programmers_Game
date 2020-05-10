package com.programmers.ui_elements;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Sort;
import com.programmers.enums.CardType;
import com.programmers.game.GameCard;

import java.util.Comparator;

public class CardContainer extends Table {

    public static final Array<CardContainer> cardContainers = new Array<>(3);
    private static final Sort sort = new Sort();
    private final boolean sorting;
    private final Content content;
    private final Card emptyCard;

    private int prevChildrenCount;

    public boolean discardMode = false;

    public CardContainer(final Array<GameCard> gameCards,
                         final Content content, final boolean sorting) {
        this.sorting = sorting;
        this.content = content;
        emptyCard = new Card();
        addEmpty();
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
        controlEmpty();

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

    public void controlEmpty() {
        if (getChildren().size > 1)
            removeEmpty();
        else if (getChildren().isEmpty())
            addEmpty();
    }

    public void removeEmpty() {
        Cell cell = getCell(emptyCard);
        emptyCard.remove();
        getCells().removeValue(cell, true);
        invalidate();
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
        add(emptyCard).row();
    }

    public void addCard(final Card card) {
        switch (content) {
            case Cycles:
                if (card.getGameCard().getType() == CardType.Cycle2
                        || card.getGameCard().getType() == CardType.Cycle3) {
                    add(card).row();
                } else {
                    card.getPrevParent().add(card).row();
                }
                break;
            case Actions:
                if (card.getGameCard().getType() != CardType.Cycle2
                        && card.getGameCard().getType() != CardType.Cycle3) {
                    add(card).row();
                } else {
                    card.getPrevParent().add(card).row();
                }
                break;
            case All:
                add(card).row();
        }
    }

    @Override
    public void clearChildren() {
        super.clearChildren();
        controlEmpty();
    }

    public enum Content {
        Cycles,
        Actions,
        All
    }
}
