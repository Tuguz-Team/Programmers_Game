package com.programmers.ui_elements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.utils.Array;
import com.programmers.enums.CardType;
import com.programmers.game.hotseat.HotseatGameController;

public final class CycleCardContainer extends CardContainer {

    private final Card[] cycleCards = new Card[9];
    private final HotseatGameController hotseatGameController;

    public CycleCardContainer(final HotseatGameController hotseatGameController) {
        super(hotseatGameController.getAlgorithmCards(), null, false);
        this.hotseatGameController = hotseatGameController;
        pad(29, 0, 29, 0);
        for (int i = 0; i < cycleCards.length; i++) {
            cycleCards[i] = new Card("Sprites/Cards/NewCyclePoint.png");
            Card card = cycleCards[i];
            add(card).row();
        }
    }

    @Override
    protected void setTouchable() { }

    @Override
    protected void childrenChanged() {
        super.childrenChanged();
        if (hotseatGameController != null && hotseatGameController.getAlgorithmCardWindow() != null) {
            if (hotseatGameController.getAlgorithmCardWindow().areContainersEmpty()) {
                hotseatGameController.getPlayerCardWindow().enableButton();
            } else {
                hotseatGameController.getPlayerCardWindow().disableButton();
            }
        }
    }

    @Override
    public void addCard(Card card, float globalX, float globalY) {
        if (card.getGameCard().getType() == CardType.Cycle2
                || card.getGameCard().getType() == CardType.Cycle3) {
            Card cycleCard = null;
            Vector2 stagePos;
            int i = 0;
            for (; i < cycleCards.length; i++) {
                Card tempCard = cycleCards[i];
                stagePos = tempCard.localToStageCoordinates(new Vector2());
                if (globalX >= stagePos.x && globalX < stagePos.x + tempCard.getWidth()
                        && globalY >= stagePos.y && globalY < stagePos.y + tempCard.getHeight()) {
                    cycleCard = tempCard;
                    break;
                }
            }
            if (cycleCard != null && getCell(cycleCard).getActor().isCellEnabled()) {
                Cell<Card> cell = getCell(cycleCard).setActor(card);
                card.setIndexForCycles(i);
                card.setCell(cell);
                removeSpace(card, getCells());
            } else if (card.getCell() == null) {
                card.getPrevParent().add(card).row();
                removeSpace(card, getCells());
            } else {
                card.getCell().setActor(card);
                removeSpace(card, getCells());
            }
        } else {
            card.getPrevParent().add(card).row();
            removeSpace(card, getCells());
        }
    }

    public void removeSpace(final Card card, final Array<Cell> cells) {
        if (card.getCell() != null && card.getCell().getActor() != null) {
            card.getCell().getActor().setCellEnabled(false);
            int i = card.getIndexForCycles();
            card.getCell().spaceBottom(0);

            if (i > 0) {
                Cell prevCell = cells.get(i - 1);
                prevCell.spaceBottom(0);
                if (prevCell.getActor() != null && ((Card) prevCell.getActor()).getGameCard() == null) {
                    ((Card) prevCell.getActor()).setCellEnabled(false);
                    prevCell.setActor(null);
                }
            } else {
                padTop(0);
                if (cells.get(i + 2).getActor() != null && ((Card) cells.get(i + 2).getActor()).getGameCard() == null)
                    cells.get(i + 1).spaceBottom(29);
                else if (cells.get(i + 2).getActor() == null)
                    cells.get(i + 1).spaceBottom(58);
                else
                    cells.get(i + 1).spaceBottom(0);
            }

            if (i < cells.size - 1) {
                Cell nextCell = cells.get(i + 1);
                if (nextCell.getActor() != null && ((Card) nextCell.getActor()).getGameCard() == null) {
                    ((Card) nextCell.getActor()).setCellEnabled(false);
                    nextCell.setActor(null);
                }
            } else {
                padBottom(0);
                if (cells.get(i - 2).getActor() != null && ((Card) cells.get(i - 2).getActor()).getGameCard() == null)
                    cells.get(i - 2).spaceBottom(29);
                else if (cells.get(i - 2).getActor() == null)
                    cells.get(i - 2).spaceBottom(58);
                else
                    cells.get(i - 2).spaceBottom(0);
            }

            if (i == 1) {
                padTop(58);
                cells.get(i + 1).spaceBottom(29);
                if (cells.get(i + 1).getActor() == null)
                    cells.get(i + 1).spaceBottom(58);
                if (cells.get(i + 2).getActor() != null)
                    ((Card) cells.get(i + 2).getActor()).setCellEnabled(false);
                if (cells.get(i + 3).getActor() != null && ((Card) cells.get(i + 3).getActor()).getGameCard() == null)
                    cells.get(i + 1).spaceBottom(29);
            } else if (i == cells.size - 2) {
                padBottom(58);
                cells.get(i - 2).spaceBottom(29);
                if (cells.get(i - 2).getActor() == null)
                    cells.get(i - 2).spaceBottom(58);
                if (cells.get(i - 3).getActor() != null)
                    ((Card) cells.get(i - 3).getActor()).setCellEnabled(false);
                if (cells.get(i - 2).getActor() != null && ((Card) cells.get(i - 2).getActor()).getGameCard() == null)
                    cells.get(i - 2).spaceBottom(29);
            } else if (i > 1 && i < cells.size - 2) {
                /** !!! */
                if (i % 2 != 0) {
                    if (cells.get(i - 2).getActor() != null)
                        ((Card) cells.get(i - 2).getActor()).setCellEnabled(false);
                    if (cells.get(i + 2).getActor() != null)
                        ((Card) cells.get(i + 2).getActor()).setCellEnabled(false);

                    if (cells.get(i + 1).getActor() == null)
                        cells.get(i + 1).spaceBottom(58);
                    if (cells.get(i + 3).getActor() != null && ((Card) cells.get(i + 3).getActor()).getGameCard() == null)
                        cells.get(i + 1).spaceBottom(29);
                    if (cells.get(i - 3).getActor() != null && ((Card) cells.get(i - 3).getActor()).getGameCard() != null)
                        cells.get(i - 2).spaceBottom(58);
                    else
                        cells.get(i - 2).spaceBottom(29);
                } else {
                    if (cells.get(i - 2).getActor() != null && ((Card) cells.get(i - 2).getActor()).getGameCard() == null)
                        cells.get(i - 2).spaceBottom(29);
                    else if (cells.get(i - 2).getActor() == null)
                        cells.get(i - 2).spaceBottom(58);
                    else
                        cells.get(i - 2).spaceBottom(0);

                    if (cells.get(i + 2).getActor() != null && ((Card) cells.get(i + 2).getActor()).getGameCard() == null)
                        cells.get(i + 1).spaceBottom(29);
                    else if (cells.get(i + 2).getActor() == null)
                        cells.get(i + 1).spaceBottom(58);
                    else
                        cells.get(i + 1).spaceBottom(0);
                }
            }

            if (i > 2) {
                if (cells.get(i - 3).getActor() != null && ((Card) cells.get(i - 3).getActor()).getGameCard() != null) {
                    cells.get(i - 3).spaceBottom(0);
                }
            }

            /*StringBuilder stringBuilder = new StringBuilder();
            for (Cell cell : getCells()) {
                if (cell.getActor() == null)
                    stringBuilder.append("null ");
                else
                    stringBuilder.append(((Card) cell.getActor()).isCellEnabled()).append(' ');
            }
            Gdx.app.log("removeSpace  ", stringBuilder.toString());*/
        }
    }

    public void restoreSpace(Card card) {
        card.getCell().setActor(
                ((CycleCardContainer) card.getPrevParent()).getCycleCards()[card.getIndexForCycles()]
        );
        Array<Cell> cells = card.getPrevParent().getCells();

        int i = 0, n = cells.size;
        for (; i < n; i++) {
            if (cells.get(i).getActor() == null) {
                if ((i > 0 && cells.get(i - 1).getActor() != null && ((Card) cells.get(i - 1).getActor()).getGameCard() != null)
                        || (i < cells.size - 1 && cells.get(i + 1).getActor() != null
                        && ((Card) cells.get(i + 1).getActor()).getGameCard() != null)) {
                    cells.get(i).setActor(null);
                } else {
                    cells.get(i).setActor(((CycleCardContainer) card.getPrevParent()).getCycleCards()[i]);
                }
            } else if (((Card) cells.get(i).getActor()).getGameCard() == null) {
                if (i % 2 != 0 && i > 1 && cells.get(i - 2).getActor() != null
                        && ((Card) cells.get(i - 2).getActor()).getGameCard() != null)
                    ((Card) cells.get(i).getActor()).setCellEnabled(false);
                else
                    ((Card) cells.get(i).getActor()).setCellEnabled(true);
            }
            else if (i % 2 != 0) {
                if (i > 1 && cells.get(i - 2).getActor() != null) {
                    ((Card) cells.get(i - 2).getActor()).setCellEnabled(false);
                }
                if (i < cells.size - 2 && cells.get(i + 2).getActor() != null) {
                    ((Card) cells.get(i + 2).getActor()).setCellEnabled(false);
                }
            }
        }

        i = card.getIndexForCycles();
        if (i == 0) {
            card.getPrevParent().padTop(29);
            card.getCell().spaceBottom(0);
            if (cells.get(i + 3).getActor() != null && ((Card) cells.get(i + 3).getActor()).getGameCard() != null)
                cells.get(i + 1).spaceBottom(29);
            else
                cells.get(i + 1).spaceBottom(0);
        } else if (i == cells.size - 1) {
            card.getPrevParent().padBottom(29);
            cells.get(i - 1).spaceBottom(0);
            cells.get(i - 2).spaceBottom(0);
            if (cells.get(i - 3).getActor() != null && ((Card) cells.get(i - 3).getActor()).getGameCard() != null)
                cells.get(i - 2).spaceBottom(29);
        } else if (i == 1) {
            card.getPrevParent().padTop(29);
            card.getCell().spaceBottom(0);
            cells.get(i - 1).spaceBottom(0);
            if (cells.get(i + 3).getActor() != null && ((Card) cells.get(i + 3).getActor()).getGameCard() != null)
                cells.get(i + 1).spaceBottom(29);
            else
                cells.get(i + 1).spaceBottom(0);
        } else if (i == cells.size - 2) {
            card.getPrevParent().padBottom(29);
            card.getCell().spaceBottom(0);
            cells.get(i - 1).spaceBottom(0);
            cells.get(i - 2).spaceBottom(0);
            if (i > 2 && cells.get(i - 3).getActor() != null && ((Card) cells.get(i - 3).getActor()).getGameCard() != null)
                cells.get(i - 2).spaceBottom(29);
        } else {
            /** !!! */
            card.getCell().spaceBottom(0);
            if (i <= cells.size - 4
                    && cells.get(i + 3).getActor() != null && ((Card) cells.get(i + 3).getActor()).getGameCard() != null)
                cells.get(i + 1).spaceBottom(29);
            else
                cells.get(i + 1).spaceBottom(0);
            cells.get(i - 1).spaceBottom(0);
            cells.get(i - 2).spaceBottom(0);
            if (i > 2 && cells.get(i - 3).getActor() != null && ((Card) cells.get(i - 3).getActor()).getGameCard() != null)
                cells.get(i - 2).spaceBottom(29);
        }

        if (i % 2 == 0) {
            if (i > 0 && cells.get(i - 2).getActor() != null && ((Card) cells.get(i - 2).getActor()).getGameCard() != null) {
                cells.get(i - 2).spaceBottom(29);
            }
            if (i < cells.size - 1 && cells.get(i + 2).getActor() != null && ((Card) cells.get(i + 2).getActor()).getGameCard() != null) {
                cells.get(i + 1).spaceBottom(0);
            }
        }

        if (i < cells.size - 2) {
            if (cells.get(i + 2).getActor() != null && ((Card) cells.get(i + 2).getActor()).getGameCard() != null)
                cells.get(i).spaceBottom(29);
        }
    }

    public Card[] getCycleCards() {
        return cycleCards;
    }
}
