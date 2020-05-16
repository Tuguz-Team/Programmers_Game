package com.programmers.ui_elements;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.programmers.enums.Difficulty;
import com.programmers.game.GameCard;
import com.programmers.game.GameController;
import com.programmers.screens.ScreenLoader;

public final class AlgorithmCardWindow extends Table {

    private final GameController gameController;

    private final Button button;
    private CardContainer actionsCardContainer;
    private CardContainer cyclesCardContainer;

    public AlgorithmCardWindow(final String name, final GameController gameController) {
        this.gameController = gameController;
        setFillParent(true);
        setDebug(true);
        final Table table = new Table();
        button = new Button(
                new TextureRegionDrawable(new Texture("Sprites/AlgorithmButton/StartButtonOn.png")),
                new TextureRegionDrawable(new Texture("Sprites/AlgorithmButton/StartButtonOff.png"))
        );
        add(new Label(name, ScreenLoader.getDefaultGdxSkin())).bottom();
        add(table).right().bottom();
        table.setDebug(true);
        // if difficulty is hard
        if (gameController.getDifficulty() == Difficulty.Hard) {
            table.add(button).colspan(2).top().row();
            cyclesCardContainer = new CycleCardContainer(gameController);
            table.add(cyclesCardContainer).spaceRight(10).bottom();
        } else {
            table.add(button).top().row();
        }
        actionsCardContainer = new CardContainer(
                gameController.getAlgorithmCards(), gameController.getDifficulty(),
                CardContainer.Content.Actions, gameController) {
            @Override
            protected void setTouchable() { }

            @Override
            protected void childrenChanged() {
                super.childrenChanged();
                if (areContainersEmpty()) {
                    gameController.getPlayerCardWindow().enableButton();
                    if (cyclesCardContainer != null)
                        ((CycleCardContainer)cyclesCardContainer).zeroingPoints();
                } else {
                    gameController.getPlayerCardWindow().disableButton();
                    if (cyclesCardContainer != null && actionsCardContainer.getChildren().size == 1)
                        ((CycleCardContainer) cyclesCardContainer).drawLast();
                }
            }
        };
        table.add(actionsCardContainer).bottom();
        right().bottom();
        //
        button.addListener(new MyButton.Listener() {
            @Override
            public void call() {
                if (gameController.getDifficulty() == Difficulty.Easy) {
                    Array<Actor> array = actionsCardContainer.getChildren();
                    for (int i = 0; i < array.size + 10; i++) {
                        Card card = (Card) array.get(0);
                        GameCard gameCard = card.getGameCard();
                        if (gameCard == null)
                            continue;
                        gameCard.apply();
                        gameController.getDiscardPile().add(gameCard);

                        Cell cell = getCell(array.get(0));
                        array.get(0).remove();
                        getCells().removeValue(cell, true);
                        invalidate();

                        actionsCardContainer.childrenChanged();
                    }
                    actionsCardContainer.controlEmpty();
                }
                gameController.toNextPlayer();
            }
        });
    }

    public void enable() {
        button.setTouchable(Touchable.enabled);
        actionsCardContainer.setTouchable(Touchable.enabled);
        CardContainer.cardContainers.add(actionsCardContainer);
        if (gameController.getDifficulty() == Difficulty.Hard) {
            cyclesCardContainer.setTouchable(Touchable.enabled);
            CardContainer.cardContainers.add(cyclesCardContainer);
        }
    }

    public void disable() {
        button.setTouchable(Touchable.disabled);
        actionsCardContainer.setTouchable(Touchable.disabled);
        CardContainer.cardContainers.removeValue(actionsCardContainer, false);
        if (gameController.getDifficulty() == Difficulty.Hard) {
            cyclesCardContainer.setTouchable(Touchable.disabled);
            CardContainer.cardContainers.removeValue(cyclesCardContainer, false);
        }
    }

    public boolean areContainersEmpty() {
        if (actionsCardContainer != null) {
            boolean flag = ((Card)actionsCardContainer.getChild(0)).getGameCard() == null;
            if (gameController.getDifficulty() == Difficulty.Hard && cyclesCardContainer.getChildren().size != 0) {
                return flag && (((Card)cyclesCardContainer.getChild(0)).getGameCard() == null);
            }
            return flag;
        }
        return true;
    }

    public CardContainer getCyclesCardContainer () {
        return cyclesCardContainer;
    }

    public CardContainer getActionsCardContainer () {
        return actionsCardContainer;
    }
}
