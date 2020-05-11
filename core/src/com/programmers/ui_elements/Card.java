package com.programmers.ui_elements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
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
                Group group = prevParent.getParent();
                while (group != null) {
                    group.addActor(thisCard);
                    group = group.getParent();
                }
                if (thisCard.getCell() != null) {
                    thisCard.getCell().setActor(
                            prevParent.getCycleCards().get(thisCard.getIndexForCycles())
                    );
                    thisCard.getCell().getActor().setCellEnabled(true);
                    Array<Cell> cells = thisCard.getPrevParent().getCells();
                    int i = 0, n = cells.size;
                    for (; i < n; i++) {
                        if (cells.get(i).getActor() == null) {
                            cells.get(i).setActor(prevParent.getCycleCards().get(i));
                            ((Card) cells.get(i).getActor()).setCellEnabled(false);
                            if (((Card) cells.get(i).getActor()).getGameCard() != null) {
                                if (i > 1 && i < cells.size - 2) {
                                    if (cells.get(i - 1).getActor() != null)
                                        ((Card) cells.get(i - 1).getActor()).setCellEnabled(false);
                                    if (cells.get(i + 1).getActor() != null)
                                        ((Card) cells.get(i + 1).getActor()).setCellEnabled(false);
                                    if (i % 2 != 0) {
                                        if (cells.get(i - 2).getActor() != null)
                                            ((Card) cells.get(i - 2).getActor()).setCellEnabled(false);
                                        if (cells.get(i + 2).getActor() != null)
                                            ((Card) cells.get(i + 2).getActor()).setCellEnabled(false);
                                    }
                                } else if (i == 0) {
                                    if (cells.get(i + 1).getActor() != null)
                                        ((Card) cells.get(i + 1).getActor()).setCellEnabled(false);
                                } else if (i == 1) {
                                    if (cells.get(i + 1).getActor() != null)
                                        ((Card) cells.get(i + 1).getActor()).setCellEnabled(false);
                                    if (cells.get(i + 2).getActor() != null)
                                        ((Card) cells.get(i + 2).getActor()).setCellEnabled(false);
                                    if (cells.get(i - 1).getActor() != null)
                                        ((Card) cells.get(i - 1).getActor()).setCellEnabled(false);
                                } else if (i == cells.size - 1) {
                                    if (cells.get(i - 1).getActor() != null)
                                        ((Card) cells.get(i - 1).getActor()).setCellEnabled(false);
                                } else {
                                    if (cells.get(i + 1).getActor() != null)
                                        ((Card) cells.get(i + 1).getActor()).setCellEnabled(false);
                                    if (cells.get(i - 1).getActor() != null)
                                        ((Card) cells.get(i - 1).getActor()).setCellEnabled(false);
                                    if (cells.get(i - 2).getActor() != null)
                                        ((Card) cells.get(i - 2).getActor()).setCellEnabled(false);
                                }
                            }
                        }
                    }
                    i = thisCard.getIndexForCycles();
                    if (i == 0) {
                        prevParent.padTop(47);
                        thisCard.getCell().spaceBottom(37);
                        thisCard.getCell().getActor().setCellEnabled(true);
                        cells.get(i + 1).spaceBottom(37);
                        if (cells.get(i + 1).getActor() != null)
                            ((Card) cells.get(i + 1).getActor()).setCellEnabled(true);
                    } else if (i == cells.size - 1) {
                        prevParent.padBottom(47);
                        cells.get(i - 1).spaceBottom(37);
                        if (cells.get(i - 1).getActor() != null)
                            ((Card) cells.get(i - 1).getActor()).setCellEnabled(true);
                        cells.get(i - 2).spaceBottom(37);
                        if (cells.get(i - 2).getActor() != null)
                            ((Card) cells.get(i - 2).getActor()).setCellEnabled(true);
                    } else if (i == 1) {
                        thisCard.getPrevParent().padTop(47);
                        thisCard.getCell().spaceBottom(37);
                        thisCard.getCell().getActor().setCellEnabled(true);
                        cells.get(i - 1).spaceBottom(37);
                        if (cells.get(i - 1).getActor() != null)
                            ((Card) cells.get(i - 1).getActor()).setCellEnabled(true);
                        cells.get(i + 1).spaceBottom(37);
                        if (cells.get(i + 1).getActor() != null)
                            ((Card) cells.get(i + 1).getActor()).setCellEnabled(true);
                    } else if (i == cells.size - 2) {
                        thisCard.getPrevParent().padBottom(47);
                        thisCard.getCell().spaceBottom(37);
                        thisCard.getCell().getActor().setCellEnabled(true);
                        cells.get(i - 1).spaceBottom(37);
                        if (cells.get(i - 1).getActor() != null)
                            ((Card) cells.get(i - 1).getActor()).setCellEnabled(true);
                        cells.get(i - 2).spaceBottom(37);
                        if (cells.get(i - 2).getActor() != null)
                            ((Card) cells.get(i - 2).getActor()).setCellEnabled(true);
                        if (cells.get(i + 1).getActor() != null)
                            ((Card) cells.get(i + 1).getActor()).setCellEnabled(true);
                    } else {
                        thisCard.getCell().spaceBottom(37);
                        thisCard.getCell().getActor().setCellEnabled(true);
                        cells.get(i + 1).spaceBottom(37);
                        if (cells.get(i + 1).getActor() != null)
                            ((Card) cells.get(i + 1).getActor()).setCellEnabled(true);
                        cells.get(i - 1).spaceBottom(37);
                        if (cells.get(i - 1).getActor() != null)
                            ((Card) cells.get(i - 1).getActor()).setCellEnabled(true);
                        cells.get(i - 2).spaceBottom(37);
                        if (cells.get(i - 2).getActor() != null)
                            ((Card) cells.get(i - 2).getActor()).setCellEnabled(true);
                    }
                }
                // logging
                StringBuilder stringBuilder = new StringBuilder();
                for (Cell cell : thisCard.getPrevParent().getCells()) {
                    if (cell.getActor() != null)
                        stringBuilder.append(((Card) cell.getActor()).isCellEnabled()).append(' ');
                    else
                        stringBuilder.append("null ");
                }
                Gdx.app.log("Card touchDown", stringBuilder.toString());
                //
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
                        } else if (container.getContent() == CardContainer.Content.Cycles) {
                            cardContainer = container;
                            break;
                        }
                    }
                }
                if (cardContainer == null) {
                    if (thisCard.cell == null) {
                        cardContainer = prevParent;
                    } else {
                        thisCard.cell.setActor(thisCard);
                        int i = thisCard.getIndexForCycles();
                        Array<Cell> cells = thisCard.getPrevParent().getCells();
                        if (i > 0) {
                            Cell cardCell = cells.get(i - 1);
                            if (cardCell.getActor() != null && ((Card) cardCell.getActor()).getGameCard() == null) {
                                ((Card) cardCell.getActor()).setCellEnabled(false);
                                cardCell.setActor(null);
                            }
                        } else {
                            thisCard.getPrevParent().padTop(0);
                            thisCard.getCell().spaceBottom(0);
                            if (thisCard.getCell().getActor() != null)
                                thisCard.getCell().getActor().setCellEnabled(false);
                            cells.get(i + 1).spaceBottom(47);
                            if (cells.get(i + 1).getActor() != null)
                                ((Card) cells.get(i + 1).getActor()).setCellEnabled(false);
                        }
                        if (i < cells.size - 1) {
                            Cell cardCell = cells.get(i + 1);
                            if (cardCell.getActor() != null && ((Card) cardCell.getActor()).getGameCard() == null) {
                                ((Card) cardCell.getActor()).setCellEnabled(false);
                                cardCell.setActor(null);
                            }
                        } else {
                            thisCard.getPrevParent().padBottom(0);
                            if (thisCard.getCell().getActor() != null)
                                thisCard.getCell().getActor().setCellEnabled(false);
                            cells.get(i - 1).spaceBottom(0);
                            if (cells.get(i - 1).getActor() != null)
                                ((Card) cells.get(i - 1).getActor()).setCellEnabled(false);
                            cells.get(i - 2).spaceBottom(47);
                        }
                        if (i == 1) {
                            thisCard.getPrevParent().padTop(58);
                            thisCard.getCell().spaceBottom(0);
                            if (thisCard.getCell().getActor() != null)
                                thisCard.getCell().getActor().setCellEnabled(false);
                            cells.get(i - 1).spaceBottom(0);
                            if (cells.get(i - 1).getActor() != null)
                                ((Card) cells.get(i - 1).getActor()).setCellEnabled(false);
                            cells.get(i + 1).spaceBottom(47);
                            if (cells.get(i + 1).getActor() != null)
                                ((Card) cells.get(i + 1).getActor()).setCellEnabled(false);
                        } else if (i == cells.size - 2) {
                            thisCard.getPrevParent().padBottom(58);
                            thisCard.getCell().spaceBottom(0);
                            if (thisCard.getCell().getActor() != null)
                                thisCard.getCell().getActor().setCellEnabled(false);
                            cells.get(i - 1).spaceBottom(0);
                            if (cells.get(i - 1).getActor() != null)
                                ((Card) cells.get(i - 1).getActor()).setCellEnabled(false);
                            cells.get(i - 2).spaceBottom(47);
                            if (cells.get(i - 2).getActor() != null)
                                ((Card) cells.get(i - 2).getActor()).setCellEnabled(false);
                        } else if (i > 1 && i < cells.size - 2) {
                            thisCard.getCell().spaceBottom(0);
                            cells.get(i + 1).spaceBottom(47);
                            cells.get(i - 1).spaceBottom(0);
                            cells.get(i - 2).spaceBottom(47);
                            if (i % 2 != 0) {
                                if (cells.get(i - 2).getActor() != null)
                                    ((Card) cells.get(i - 2).getActor()).setCellEnabled(false);
                                if (cells.get(i + 2).getActor() != null)
                                    ((Card) cells.get(i + 2).getActor()).setCellEnabled(false);
                            }
                            if (cells.get(i - 1).getActor() != null)
                                ((Card) cells.get(i - 1).getActor()).setCellEnabled(false);
                            if (thisCard.getCell().getActor() != null)
                                thisCard.getCell().getActor().setCellEnabled(false);
                            if (cells.get(i + 1).getActor() != null)
                                ((Card) cells.get(i + 1).getActor()).setCellEnabled(false);
                        }
                        return;
                    }
                }
                cardContainer.addCard(thisCard, x, y);
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
