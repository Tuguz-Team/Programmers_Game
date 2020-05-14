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
import com.programmers.game.hotseat.HotseatGameController;
import com.programmers.screens.ScreenLoader;

public final class AlgorithmCardWindow extends Table {

    private final HotseatGameController hotseatGameController;

    private final Button button;
    private CardContainer actionsCardContainer;
    private CardContainer cyclesCardContainer;

    public AlgorithmCardWindow(final String name, final HotseatGameController hotseatGameController) {
        this.hotseatGameController = hotseatGameController;
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
        if (hotseatGameController.getDifficulty() == Difficulty.Hard) {
            table.add(button).colspan(2).top().row();
            cyclesCardContainer = new CycleCardContainer(hotseatGameController);
            table.add(cyclesCardContainer).spaceRight(10).bottom();
        } else {
            table.add(button).top().row();
        }
        actionsCardContainer = new CardContainer(
                hotseatGameController.getAlgorithmCards(),
                CardContainer.Content.Actions, false) {
            @Override
            protected void setTouchable() { }

            @Override
            protected void childrenChanged() {
                super.childrenChanged();
                if (areContainersEmpty()) {
                    hotseatGameController.getPlayerCardWindow().enableButton();
                } else {
                    hotseatGameController.getPlayerCardWindow().disableButton();
                }
            }
        };
        table.add(actionsCardContainer).bottom();
        right().bottom();
        //
        button.addListener(new MyButton.Listener() {
            @Override
            public void call() {
                if (hotseatGameController.getDifficulty() == Difficulty.Easy) {
                    Array<Actor> array = actionsCardContainer.getChildren();
                    for (int i = 0; i < array.size + 10; i++) {
                        Card card = (Card) array.get(0);
                        GameCard gameCard = card.getGameCard();
                        if (gameCard == null)
                            continue;
                        gameCard.apply();
                        hotseatGameController.getDiscardPile().add(gameCard);

                        Cell cell = getCell(array.get(0));
                        array.get(0).remove();
                        getCells().removeValue(cell, true);
                        invalidate();

                        actionsCardContainer.childrenChanged();
                    }
                    actionsCardContainer.controlEmpty();
                }
                hotseatGameController.toNextPlayer();
            }
        });
    }

    public void enable() {
        button.setTouchable(Touchable.enabled);
        actionsCardContainer.setTouchable(Touchable.enabled);
        CardContainer.cardContainers.add(actionsCardContainer);
        if (hotseatGameController.getDifficulty() == Difficulty.Hard) {
            cyclesCardContainer.setTouchable(Touchable.enabled);
            CardContainer.cardContainers.add(cyclesCardContainer);
        }
    }

    public void disable() {
        button.setTouchable(Touchable.disabled);
        actionsCardContainer.setTouchable(Touchable.disabled);
        CardContainer.cardContainers.removeValue(actionsCardContainer, false);
        if (hotseatGameController.getDifficulty() == Difficulty.Hard) {
            cyclesCardContainer.setTouchable(Touchable.disabled);
            CardContainer.cardContainers.removeValue(cyclesCardContainer, false);
        }
    }

    public boolean areContainersEmpty() {
        if (actionsCardContainer != null) {
            boolean flag = ((Card)actionsCardContainer.getChild(0)).getGameCard() == null;
            if (hotseatGameController.getDifficulty() == Difficulty.Hard) {
                return flag && (((Card)cyclesCardContainer.getChild(0)).getGameCard() == null);
            }
            return flag;
        }
        return true;
    }
}
