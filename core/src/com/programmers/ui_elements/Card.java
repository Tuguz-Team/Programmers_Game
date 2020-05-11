package com.programmers.ui_elements;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.programmers.game.GameCard;

public class Card extends Image implements Comparable<Card> {

    private final GameCard gameCard;
    private CardContainer prevParent;

    private Cell<Card> cell;
    private boolean cellEnabled = true;
    private int indexForCycles;

    public Card(final String name) {
        super(new Texture(name));
        this.gameCard = null;
        setDebug(true);
    }

    public Card(final GameCard gameCard) {
        super(new Texture("Sprites/Cards/".concat(gameCard.getType().toString()).concat(".png")));
        this.gameCard = gameCard;
        setDebug(true);
        addListener(new InputListener() {
            final Card thisCard = Card.this;
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                prevParent = (CardContainer) thisCard.getParent();
                if (prevParent instanceof CycleCardContainer) {
                    ((CycleCardContainer) prevParent).restoreSpace(thisCard);
                }
                Group group = prevParent.getParent();
                while (group != null) {
                    group.addActor(thisCard);
                    group = group.getParent();
                }
                if (prevParent instanceof CycleCardContainer) {
                    ((CycleCardContainer) prevParent).restoreSpace(thisCard);
                }
                thisCard.setZIndex(thisCard.getParent().getChildren().size + 1);
                touchDragged(event, x, y, pointer);
                return true;
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                thisCard.setPosition(event.getStageX(), event.getStageY(), Align.center);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Vector2 stagePos;
                x = event.getStageX();
                y = event.getStageY();
                CardContainer cardContainer = null;
                for (CardContainer container : CardContainer.cardContainers) {
                    stagePos = container.localToStageCoordinates(new Vector2());
                    if (x >= stagePos.x && x < stagePos.x + container.getWidth()
                            && y >= stagePos.y && y < stagePos.y + container.getHeight())
                    {
                        if (container.getChildren().size < 5) {
                            cardContainer = container;
                            break;
                        } else if (container instanceof CycleCardContainer) {
                            cardContainer = container;
                            break;
                        }
                    }
                }
                if (cardContainer == null) {
                    if (thisCard.cell == null) {
                        cardContainer = prevParent;
                    } else if (thisCard.getPrevParent() instanceof CycleCardContainer) {
                        thisCard.cell.setActor(thisCard);
                        Array<Cell> cells = thisCard.getPrevParent().getCells();
                        ((CycleCardContainer) thisCard.getPrevParent()).removeSpace(thisCard, cells);
                        return;
                    }
                }
                cardContainer.addCard(thisCard, x, y);
                if (thisCard.getPrevParent() instanceof CycleCardContainer)
                    ((CycleCardContainer) thisCard.getPrevParent()).removeSpace(thisCard, thisCard.getPrevParent().getCells());
            }
        });
    }

    @Override
    public int compareTo(Card other) {
        if (this.getGameCard() == null) {
            return -1;
        } else if (other.getGameCard() == null) {
            return 1;
        }
        return this.getGameCard().getType().compareTo(other.getGameCard().getType());
    }

    public GameCard getGameCard() {
        return gameCard;
    }

    public CardContainer getPrevParent() {
        return prevParent;
    }

    public Cell<Card> getCell() {
        return cell;
    }

    public void setCell(Cell<Card> cell) {
        this.cell = cell;
    }

    public void setIndexForCycles(int indexForCycles) {
        this.indexForCycles = indexForCycles;
    }

    public int getIndexForCycles() {
        return indexForCycles;
    }

    public boolean isCellEnabled() {
        return cellEnabled;
    }

    public void setCellEnabled(boolean cellEnabled) {
        this.cellEnabled = cellEnabled;
    }
}
