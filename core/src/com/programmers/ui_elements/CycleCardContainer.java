package com.programmers.ui_elements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.programmers.enums.CardType;
import com.programmers.game.GameController;

public final class CycleCardContainer extends CardContainer {

    private final Card[] cycleCards = new Card[9];
    private final GameController gameController;
    private static final int PAD = 29, WIDTH = 90;

    CycleCardContainer(final GameController gameController) {
        super(gameController.getAlgorithmCards(), gameController.getDifficulty(), null, gameController);
        this.gameController = gameController;
        pad(PAD, 0, PAD, 0);
        for (int i = 0; i < cycleCards.length; i++) {
            cycleCards[i] = new Card(
                    "Sprites/EnabledCards/CyclePointOn.png",
                    gameController.getGameScreen().getAssetManager()
            );
            Card card = cycleCards[i];
            add(card).row();
        }
    }

    @Override
    protected void setTouchable() { }

    @Override
    protected void childrenChanged() {
        super.childrenChanged();
        if (gameController != null && gameController.getAlgorithmCardWindow() != null) {
            if (gameController.getAlgorithmCardWindow().areContainersEmpty())
                gameController.getPlayerCardWindow().enableButton();
            else
                gameController.getPlayerCardWindow().disableButton();

            int size = actionSizeToUse();
            boolean secondCard, firstCard = getCells().get(size + 1).getActor() != null
                    && ((Card)getCells().get(size + 1).getActor()).getGameCard() != null;
            if (size == cycleCards.length - 2)
                secondCard = false;
            else secondCard = getCells().get(size + 2).getActor() != null
                    && ((Card) getCells().get(size + 2).getActor()).getGameCard() != null;

            if (firstCard || secondCard) gameController.getAlgorithmCardWindow().
                    getActionsCardContainer().setTouchable(Touchable.disabled);
            else gameController.getAlgorithmCardWindow().
                    getActionsCardContainer().setTouchable(Touchable.enabled);
        }
    }

    @Override
    public void addCard(Card card, float globalX, float globalY) {
        if (card.getGameCard().getCardType() == CardType.Cycle2
                || card.getGameCard().getCardType() == CardType.Cycle3) {
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
            if (cycleCard != null && getCell(cycleCard) != null
                    && getCell(cycleCard).getActor() != null
                    && getCell(cycleCard).getActor().isCellEnabled()) {
                Cell<Card> cell = getCell(cycleCard).setActor(card);
                card.setIndexForCycles(i);
                card.setCell(cell);
                removeSpace(card, getCells());
            } else if (card.getCell() == null) {
                if (card.getPrevParent() != null) {
                    card.getPrevParent().add(card).row();
                    removeSpace(card, getCells());
                } else {
                    Gdx.app.error("CCC", "ERROR");
                }
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
            settingCellEnabled(card.getCell().getActor(), false);
            int i = card.getIndexForCycles();
            card.getCell().spaceBottom(0);

            if (i > 0) {
                Cell prevCell = cells.get(i - 1);
                prevCell.spaceBottom(0);
                if (prevCell.getActor() != null && ((Card) prevCell.getActor()).getGameCard() == null) {
                    settingCellEnabled((Card) prevCell.getActor(), false);
                    prevCell.setActor(null);
                }
            } else {
                padTop(0);
                if (cells.get(i + 2).getActor() != null && ((Card) cells.get(i + 2).getActor()).getGameCard() == null)
                    cells.get(i + 1).spaceBottom(PAD);
                else if (cells.get(i + 2).getActor() == null)
                    cells.get(i + 1).spaceBottom(PAD * 2);
                else
                    cells.get(i + 1).spaceBottom(0);
            }

            if (i < cells.size - 1) {
                Cell nextCell = cells.get(i + 1);
                if (nextCell.getActor() != null && ((Card) nextCell.getActor()).getGameCard() == null) {
                    settingCellEnabled((Card) nextCell.getActor(), false);
                    nextCell.setActor(null);
                }
            } else {
                padBottom(0);
                if (cells.get(i - 2).getActor() != null && ((Card) cells.get(i - 2).getActor()).getGameCard() == null)
                    cells.get(i - 2).spaceBottom(PAD);
                else if (cells.get(i - 2).getActor() == null)
                    cells.get(i - 2).spaceBottom(PAD * 2);
                else
                    cells.get(i - 2).spaceBottom(0);
            }

            if (i == 1) {
                padTop(58);
                cells.get(i + 1).spaceBottom(PAD);

                if (cells.get(i + 1).getActor() == null)
                    cells.get(i + 1).spaceBottom(PAD * 2);

                if (cells.get(i + 2).getActor() != null)
                    settingCellEnabled((Card) cells.get(i + 2).getActor(),false);

                if (cells.get(i + 3).getActor() != null && ((Card) cells.get(i + 3).getActor()).getGameCard() == null)
                    cells.get(i + 1).spaceBottom(PAD);
            } else if (i == cells.size - 2) {
                padBottom(58);
                cells.get(i - 2).spaceBottom(PAD);

                if (cells.get(i - 2).getActor() == null)
                    cells.get(i - 2).spaceBottom(PAD * 2);
                else
                    settingCellEnabled((Card) cells.get(i - 2).getActor(),false);
            } else if (i > 1 && i < cells.size - 2) {
                if (i % 2 != 0) {
                    if (cells.get(i - 2).getActor() != null)
                        settingCellEnabled((Card) cells.get(i - 2).getActor(), false);
                    if (cells.get(i + 2).getActor() != null)
                        settingCellEnabled((Card) cells.get(i + 2).getActor(), false);

                    if (cells.get(i + 1).getActor() == null)
                        cells.get(i + 1).spaceBottom(PAD * 2);

                    if (cells.get(i + 3).getActor() != null && ((Card) cells.get(i + 3).getActor()).getGameCard() == null)
                        cells.get(i + 1).spaceBottom(PAD);

                    if (cells.get(i - 3).getActor() != null && ((Card) cells.get(i - 3).getActor()).getGameCard() != null)
                        cells.get(i - 2).spaceBottom(PAD * 2);
                    else
                        cells.get(i - 2).spaceBottom(PAD);
                } else {
                    if (cells.get(i - 2).getActor() != null && ((Card) cells.get(i - 2).getActor()).getGameCard() == null)
                        cells.get(i - 2).spaceBottom(PAD);
                    else if (cells.get(i - 2).getActor() == null)
                        cells.get(i - 2).spaceBottom(PAD * 2);
                    else
                        cells.get(i - 2).spaceBottom(0);

                    if (cells.get(i + 2).getActor() != null && ((Card) cells.get(i + 2).getActor()).getGameCard() == null)
                        cells.get(i + 1).spaceBottom(PAD);
                    else if (cells.get(i + 2).getActor() == null)
                        cells.get(i + 1).spaceBottom(PAD * 2);
                    else
                        cells.get(i + 1).spaceBottom(0);
                }
            }

            if (i % 2 != 0 && i < 4 && cells.get(i + 4).getActor() != null
                    && ((Card) cells.get(i + 4).getActor()).getGameCard() != null)
                cells.get(i + 1).spaceBottom(PAD);

            if (i > 2 && cells.get(i - 3).getActor() != null
                    && ((Card) cells.get(i - 3).getActor()).getGameCard() != null)
                cells.get(i - 3).spaceBottom(0);

            updatePoints(cells ,false);
            if (i == actionSizeToUse() + 1)
                padTop(0);
            else if (i == actionSizeToUse() + 2)
                padTop(PAD * 2);
        }
    }

    void restoreSpace(Card card) {
        card.getCell().setActor(
                ((CycleCardContainer) card.getPrevParent()).getCycleCards()[card.getIndexForCycles()]
        );
        Array<Cell> cells = card.getPrevParent().getCells();
        restoringCycle(cells, card);

        int i = card.getIndexForCycles();
        updatePoints(cells, false);
        if (i == 0) {
            card.getPrevParent().padTop(PAD);
            card.getCell().spaceBottom(0);
            if (cells.get(i + 3).getActor() != null && ((Card) cells.get(i + 3).getActor()).getGameCard() != null)
                cells.get(i + 1).spaceBottom(PAD);
            else
                cells.get(i + 1).spaceBottom(0);
        } else if (i == cells.size - 1) {
            card.getPrevParent().padBottom(PAD);
            cells.get(i - 1).spaceBottom(0);
            if (cells.get(i - 3).getActor() != null && ((Card) cells.get(i - 3).getActor()).getGameCard() != null)
                cells.get(i - 2).spaceBottom(PAD);
            else
                cells.get(i - 2).spaceBottom(0);
        } else if (i == 1) {
            card.getPrevParent().padTop(PAD);
            card.getCell().spaceBottom(0);
            cells.get(i - 1).spaceBottom(0);
            if (cells.get(i + 3).getActor() != null && ((Card) cells.get(i + 3).getActor()).getGameCard() != null)
                cells.get(i + 1).spaceBottom(PAD);
            else
                cells.get(i + 1).spaceBottom(0);
        } else if (i == cells.size - 2) {
            card.getPrevParent().padBottom(PAD);
            card.getCell().spaceBottom(0);
            cells.get(i - 1).spaceBottom(0);
            if (i > 2 && cells.get(i - 3).getActor() != null && ((Card) cells.get(i - 3).getActor()).getGameCard() != null)
                cells.get(i - 2).spaceBottom(PAD);
            else
                cells.get(i - 2).spaceBottom(0);
        } else {
            card.getCell().spaceBottom(0);
            if (i <= cells.size - 4
                    && cells.get(i + 3).getActor() != null && ((Card) cells.get(i + 3).getActor()).getGameCard() != null)
                cells.get(i + 1).spaceBottom(PAD);
            else
                cells.get(i + 1).spaceBottom(0);

            cells.get(i - 1).spaceBottom(0);
            if (i > 2 && cells.get(i - 3).getActor() != null && ((Card) cells.get(i - 3).getActor()).getGameCard() != null)
                cells.get(i - 2).spaceBottom(PAD);
            else
                cells.get(i - 2).spaceBottom(0);
        }

        restoreCheck(cells, i);
    }

    public void restoringCycle (Array<Cell> cells, Card card) {
        for (int i = 0; i < cells.size; i++) {
            if (cells.get(i).getActor() == null) {
                if ((i > 0 && cells.get(i - 1).getActor() != null && ((Card) cells.get(i - 1).getActor()).getGameCard() != null)
                        || (i < cells.size - 1 && cells.get(i + 1).getActor() != null
                        && ((Card) cells.get(i + 1).getActor()).getGameCard() != null)) {
                    cells.get(i).setActor(null);
                } else if (card != null)
                    cells.get(i).setActor(((CycleCardContainer) card.getPrevParent()).getCycleCards()[i]);
            } else if (((Card) cells.get(i).getActor()).getGameCard() == null) {
                if (i % 2 != 0 && i > 1 && cells.get(i - 2).getActor() != null
                        && ((Card) cells.get(i - 2).getActor()).getGameCard() != null)
                    settingCellEnabled((Card) cells.get(i).getActor(), false);
                else
                    settingCellEnabled((Card) cells.get(i).getActor(), true);
            }
            else if (i % 2 != 0) {
                if (i > 1 && cells.get(i - 2).getActor() != null)
                    settingCellEnabled((Card) cells.get(i - 2).getActor(), false);
                if (i < cells.size - 2 && cells.get(i + 2).getActor() != null)
                    settingCellEnabled((Card) cells.get(i + 2).getActor(), false);
            }
        }
    }

    public void restoreCheck(Array<Cell> cells, int i) {
        if (i % 2 == 0) {
            if (i > 0 && cells.get(i - 2).getActor() != null && ((Card) cells.get(i - 2).getActor()).getGameCard() != null)
                cells.get(i - 2).spaceBottom(PAD);
            if (i < cells.size - 1 && cells.get(i + 2).getActor() != null && ((Card) cells.get(i + 2).getActor()).getGameCard() != null)
                cells.get(i + 1).spaceBottom(0);
        }

        if (i < cells.size - 2 && cells.get(i + 2).getActor() != null && ((Card) cells.get(i + 2).getActor()).getGameCard() != null)
            cells.get(i).spaceBottom(PAD);

        if (i == actionSizeToUse() + 1 || i == actionSizeToUse() + 2)
            padTop(PAD);
    }

    public Card[] getCycleCards() {
        return cycleCards;
    }

    @Override
    public void clearChildren() {
        zeroingPoints();
    }

    public void settingCellEnabled (Card card, boolean cellEnabled) {
        if (card.getGameCard() == null) {
            if (cellEnabled && !card.isCellEnabled())
                card.setDrawable(new TextureRegionDrawable(
                        (Texture) gameController.getGameScreen().getAssetManager()
                                .get("Sprites/EnabledCards/CyclePointOn.png")));
            else if (!cellEnabled && card.isCellEnabled())
                card.setDrawable(new TextureRegionDrawable(
                        (Texture) gameController.getGameScreen().getAssetManager()
                                .get("Sprites/DisabledCards/CyclePointOff.png")));
        }
        card.setCellEnabled(cellEnabled);
    }

    public void updatePoints(Array<Cell> cells, boolean actionChanging) {
        for (int i = 0; i < cycleCards.length; i++) {
            if (i < actionSizeToUse() + 1) {
                cells.get(i).setActor(null);
                cells.get(i).spaceBottom(0);
            } else if (actionChanging && cells.get(i).getActor() != null
                    && ((Card)cells.get(i).getActor()).getGameCard() != null) {
                if (i == actionSizeToUse() + 3 && i > 1 && cells.get(i - 2).getActor() != null
                        && ((Card)cells.get(i - 2).getActor()).getGameCard() == null) {
                    cells.get(i - 1).setActor(null);
                    cells.get(i - 2).spaceBottom(PAD);
                    padTop(PAD);
                } else if (i == actionSizeToUse() + 4 && i > 2 && cells.get(i - 3).getActor() != null
                        && ((Card)cells.get(i - 3).getActor()).getGameCard() == null) {
                    cells.get(i - 2).spaceBottom(PAD);
                    padTop(PAD);
                } else if (i == actionSizeToUse() + 2)
                    padTop(PAD * 2);
                else if (i == actionSizeToUse() + 1)
                    padTop(0);
            }
        }
    }

    public void zeroingPoints() {
        padLeft(WIDTH);
        for (int i = 0; i < cycleCards.length; i++)
            getCells().get(i).setActor(null);
    }

    public void drawLast() {
        padLeft(0);
        if (getPadTop() != PAD)
            padTop(PAD);
        if (getPadBottom() != PAD)
            padBottom(PAD);
        int i = cycleCards.length - 1;

        cycleCards[i] = new Card(
                "Sprites/EnabledCards/CyclePointOn.png",
                gameController.getGameScreen().getAssetManager()
        );
        getCells().get(i).setActor(cycleCards[i]);
    }

    public void drawPoints(int prevSize, int currSize) {
        int i, n;
        Array<Cell> cells = getCells();

        if (prevSize < currSize && prevSize != 0) {
            i = cycleCards.length - currSize * 2 + 1;
            n = cycleCards.length - prevSize * 2 + 1;

            for (; i < n; i++) {
                cycleCards[i] = new Card(
                        "Sprites/EnabledCards/CyclePointOn.png",
                        gameController.getGameScreen().getAssetManager()
                );
                cells.get(i).setActor(cycleCards[i]);
            }
        } else if (currSize < prevSize) {
            i = cycleCards.length - prevSize * 2 + 1;
            n = cycleCards.length - currSize * 2 + 1;

            for (; i < n; i++)
                cells.get(i).setActor(null);
        } else if (prevSize == 0) {
            i = 0;
            n = cycleCards.length - currSize * 2 + 1;

            for (; i < n; i++)
                cells.get(i).setActor(null);

            //if (n == 8 && getCells().get(i).getActor() == null)
            //drawLast();
        }

        restoringCycle(cells, null);
        updatePoints(cells, true);
    }

    public int actionSizeToUse() {
        return cycleCards.length - 2 * gameController.getAlgorithmCardWindow().
                getActionsCardContainer().getChildren().size;
    }

    public void setCycleToPrevious() {
        for (int i = 0; i < cycleCards.length; i++) {
            Actor card = getCells().get(i).getActor();
            if (card != null)
                ((Card)card).setCycleToPrevious(this);
        }
    }

    /*public void restorePointsFromZero(int size) {
        if (size == 1 && getCells().get(8).getActor() == null) {
            //
        } else {
            size = cycleCards.length - (size * 2) + 1;
            for (int i = size; i < cycleCards.length; i++) {
                Card card = new Card(
                        "Sprites/EnabledCards/CyclePointOn.png",
                        gameController.getGameScreen().getAssetManager());
                card.setCellEnabled(true);
                getCells().get(i).setActor(card);
            }
            padLeft(0);
            if (getPadTop() != PAD)
                padTop(PAD);
            if (getPadBottom() != PAD)
                padBottom(PAD);
        }
    }*/
}
