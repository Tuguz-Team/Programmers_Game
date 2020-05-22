package com.programmers.ui_elements;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.programmers.game.GameCard;
import com.programmers.game.GameInputProcessor;

import static com.programmers.screens.ScreenLoader.getGameSkin;

public final class Card extends Image implements Comparable<Card> {

    private final GameCard gameCard;
    private CardContainer prevParent;
    private GameInputProcessor gameInputProcessor = null;
    private AssetManager assetManager;

    private Cell<Card> cell;
    private boolean cellEnabled = true,
            enabled = true, replaced = false;
    private int indexForCycles;

    public Card(final String name, AssetManager assetManager) {
        super((Texture) assetManager.get(name));
        this.assetManager = assetManager;
        this.gameCard = null;
        setDebug(true);
    }

    public Card(final GameCard gameCard, GameInputProcessor gameInputProcessor, AssetManager assetManager) {
        super((Texture) assetManager.get("Sprites/EnabledCards/" + gameCard.getCardType().toString() + ".png"));
        this.gameCard = gameCard;
        this.gameInputProcessor = gameInputProcessor;
        this.assetManager = assetManager;
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
                        if (container.getChildren().size < 5 && !thisCard.isReplaced()) {
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

                for (int i = 0; i < cardContainer.getChildren().size; i++) {
                    final Card card = (Card) cardContainer.getChild(i);
                    stagePos = card.localToStageCoordinates(new Vector2());
                    if (x >= stagePos.x && x < stagePos.x + card.getWidth() && y >= stagePos.y
                            && y < stagePos.y + card.getHeight() && !card.isEnabled() && !Card.this.isReplaced()
                            && card.getParent() == Card.this.getPrevParent()) {
                        YesNoDialog dialog = new YesNoDialog("   Do you want to replace card from " +
                                "previous move with your's new card? This change is irreversible.   ", getGameSkin()) {
                            @Override
                            public void call() {
                                Cell cell = getCell(card);
                                card.remove();
                                ((CardContainer) Card.this.getParent()).getGameController().getAlgorithmCardWindow().
                                        getActionsCardContainer().getCells().removeValue(cell, true);
                                invalidate();

                                Card.this.setReplaced(true);
                                ((CardContainer) Card.this.getParent()).getGameController().getDiscardPile().add(card.getGameCard());
                            }
                        };
                        dialog.show(card.getParent().getStage());
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
        return this.getGameCard().getCardType().compareTo(other.getGameCard().getCardType());
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

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isReplaced() {
        return replaced;
    }

    public void setReplaced(boolean replaced) {
        this.replaced = replaced;
    }

    public void setActionToPrevious(final CardContainer container) {
        if (this.getGameCard() != null) {
            this.setEnabled(false);
            this.setDrawable(new TextureRegionDrawable((Texture) assetManager.get("Sprites/DisabledCards/"
                    + this.getGameCard().getCardType().toString() + ".png")));
            this.removeListener(this.getListeners().get(0));
            this.addListener(new InputListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    CardContainer prevParent = (CardContainer) Card.this.getParent();
                    if (prevParent instanceof CycleCardContainer) {
                        ((CycleCardContainer) prevParent).restoreSpace(Card.this);
                    }
                    Group group = prevParent.getParent();
                    while (group != null) {
                        group.addActor(Card.this);
                        group = group.getParent();
                    }
                    if (prevParent instanceof CycleCardContainer) {
                        ((CycleCardContainer) prevParent).restoreSpace(Card.this);
                    }
                    Card.this.setZIndex(Card.this.getParent().getChildren().size + 1);
                    touchDragged(event, x, y, pointer);
                    return true;
                }

                @Override
                public void touchDragged(InputEvent event, float x, float y, int pointer) {
                    Card.this.setPosition(event.getStageX(), event.getStageY(), Align.center);
                }

                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    x = event.getStageX();
                    y = event.getStageY();

                    container.addCard(Card.this, x, y);
                }
            });
        }
    }

    public void setCycleToPrevious(final CycleCardContainer container) {
        if (this.getGameCard() != null) {
            this.setDrawable(new TextureRegionDrawable((Texture) assetManager.get("Sprites/DisabledCards/"
                    + this.getGameCard().getCardType().toString() + ".png")));
            this.removeListener(this.getListeners().get(0));
            this.addListener(new InputListener() {
                Card thisCard = Card.this;
                @Override
                public boolean touchDown(final InputEvent event, float x, float y, int pointer, int button) {
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

                    final Dialog dialog = new Dialog("   Do you really want to delete " +
                            "this cycle card? This change is irreversible.   ", getGameSkin()) {
                        @Override
                        protected void result(Object object) {
                            if (object.equals(true)) {
                                thisCard.remove();
                                container.getGameController().getDiscardPile().add(Card.this.getGameCard());
                                gameInputProcessor.unlockCamera();
                            } else {
                                thisCard.setVisible(true);
                                container.addCard(Card.this, event.getStageX(), event.getStageY());
                                gameInputProcessor.unlockCamera();
                            }
                        }
                    };

                    dialog.button("   YES   ", true).button("   NO   ", false);

                    dialog.getButtonTable().getCells().get(0).spaceRight(50);
                    ((TextButton) dialog.getButtonTable().getCells().get(0).getActor()).getLabel().setAlignment(2);
                    ((TextButton) dialog.getButtonTable().getCells().get(1).getActor()).getLabel().setAlignment(2);

                    dialog.setMovable(false);

                    thisCard.setVisible(false);
                    dialog.show(Card.this.getParent().getStage());
                    gameInputProcessor.lockCamera();

                    return true;
                }
            });
        }
    }
}