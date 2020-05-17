package com.programmers.ui_elements;

import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.programmers.enums.CardType;
import com.programmers.enums.Difficulty;
import com.programmers.game.GameCard;
import com.programmers.game.GameController;

public class CardContainer extends Table {

    public static final Array<CardContainer> cardContainers = new Array<>(3);
    private final Content content;
    private final Card emptyCard;
    private final Difficulty difficulty;
    private final GameController gameController;

    private int prevChildrenCount;
    public boolean discardMode = false;

    public CardContainer(final Array<GameCard> gameCards, final Difficulty difficulty,
                         final Content content, GameController gameController) {
        this.content = content;
        this.difficulty = difficulty;
        this.gameController = gameController;
        emptyCard = new Card("Sprites/Cards/empty.png");
        if (content != null)
            addEmpty();
        if (gameCards != null) {
            for (GameCard gameCard : gameCards) {
                Card card = new Card(gameCard);
                addCard(card, 0, 0);
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
        /*if (prevChildrenCount < getChildren().size && sorting) {
            sort.sort(getChildren(), new Comparator<Actor>() {
                @Override
                public int compare(Actor lhs, Actor rhs) {
                    return ((Card) lhs).compareTo((Card) rhs);
                }
            });
        }*/
        AlgorithmCardWindow window = gameController.getAlgorithmCardWindow();
        if (window != null) {
            CycleCardContainer cycleCardContainer = (CycleCardContainer) window.getCyclesCardContainer();
            if (difficulty == Difficulty.Hard && content == Content.Actions)
                cycleCardContainer.drawPoints(prevChildrenCount, getChildren().size);
        }

        prevChildrenCount = getChildren().size;
        setTouchable();
    }

    public void controlEmpty() {
        if (content != null) {
            if (getChildren().size > 1)
                removeEmpty();
            else if (getChildren().isEmpty())
                addEmpty();
        }
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
                    else {
                        card.getCell().setActor(card);
                        ((CycleCardContainer) card.getPrevParent()).removeSpace(
                                card, card.getPrevParent().getCells()
                        );
                    }
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

    public enum Content {
        Actions,
        All
    }
}
