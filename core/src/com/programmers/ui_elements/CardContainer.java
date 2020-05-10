package com.programmers.ui_elements;

import com.badlogic.gdx.math.Vector2;
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

    private final Array<Card> cycleCards;

    public CardContainer(final Array<GameCard> gameCards,
                         final Content content, final boolean sorting) {
        this.sorting = sorting;
        this.content = content;
        emptyCard = new Card("Sprites/Cards/empty.png");
        addEmpty();
        if (gameCards != null) {
            for (GameCard gameCard : gameCards) {
                Card card = new Card(gameCard);
                addCard(card, 0, 0);
            }
        }
        //
        if (content == Content.Cycles) {
            cycleCards = new Array<>(9);
            pad(47, 0, 47, 0);
            for (int i = 0; i < 9; i++) {
                cycleCards.add(new Card("Sprites/Cards/CyclePoint.png"));
                Card card = cycleCards.get(i);
                add(card).spaceBottom(37).row();
            }
        } else {
            cycleCards = null;
        }
        //
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

    public void addCard(final Card card, final float globalX, final float globalY) {
        switch (content) {
            case Cycles:
                if (card.getGameCard().getType() == CardType.Cycle2
                        || card.getGameCard().getType() == CardType.Cycle3) {
                    // логика для сложного уровня
                    Card cycleCard = null;
                    Vector2 stagePos;
                    int i = 0;
                    for (; i < cycleCards.size; i++) {
                        Card _card = cycleCards.get(i);
                        stagePos = _card.localToStageCoordinates(new Vector2());
                        if (globalX >= stagePos.x && globalX < stagePos.x + _card.getWidth()
                                && globalY >= stagePos.y && globalY < stagePos.y + _card.getHeight()) {
                            cycleCard = _card;
                            break;
                        }
                    }
                    if (cycleCard != null) {
                        Cell<Card> cell = getCell(cycleCard).setActor(card);
                        card.setIndexForCycles(i);
                        card.setCell(cell);
                    } else if (card.getCell() == null) {
                        card.getPrevParent().add(card).row();
                    } else {
                        card.getCell().setActor(card);
                    }
                } else {
                    card.getPrevParent().add(card).row();
                }
                break;
            case Actions:
                if (card.getGameCard().getType() != CardType.Cycle2
                        && card.getGameCard().getType() != CardType.Cycle3) {
                    if (card.getCell() == null)
                        add(card).row();
                    else
                        card.getCell().setActor(card);
                } else {
                    if (card.getCell() == null)
                        card.getPrevParent().add(card).row();
                    else
                        card.getCell().setActor(card);
                }
                break;
            case All:
                add(card).row();
                card.setCell(null);
        }
    }

    @Override
    public void clearChildren() {
        super.clearChildren();
        controlEmpty();
    }

    public Content getContent() {
        return content;
    }

    public Array<Card> getCycleCards() {
        return cycleCards;
    }

    public enum Content {
        Cycles,
        Actions,
        All
    }
}
