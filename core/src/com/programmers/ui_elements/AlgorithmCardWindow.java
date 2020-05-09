package com.programmers.ui_elements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.programmers.enums.Difficulty;
import com.programmers.game.GameCard;
import com.programmers.game.GameController;

import java.util.Iterator;

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
        table.add(button).row();
        add(new Label(name, new Skin(Gdx.files.internal("uiskin.json")))).bottom();
        add(table);
        if (gameController.getDifficulty() == Difficulty.Hard) {
            cyclesCardContainer = new CardContainer(
                    gameController.getAlgorithmCards(),
                    CardContainer.Content.Cycles,
                    false
            );
            table.add(cyclesCardContainer).spaceRight(10).bottom();
            actionsCardContainer = new CardContainer(
                    gameController.getAlgorithmCards(),
                    CardContainer.Content.Actions, false);
        } else {
            actionsCardContainer = new CardContainer(
                    gameController.getAlgorithmCards(),
                    CardContainer.Content.Actions, false) {
                @Override
                protected void setTouchable() { }
            };
        }
        table.add(actionsCardContainer).bottom();
        right().bottom();
        button.addListener(new MyButton.Listener() {
            @Override
            public void call() {
                if (gameController.getDifficulty() == Difficulty.Easy) {
                    Array<GameCard> cards = new Array<>();
                    Iterator<Actor> iterator = actionsCardContainer.getChildren().iterator();
                    while (iterator.hasNext()) {
                        Card card = (Card) iterator.next();
                        GameCard gameCard = card.getGameCard();
                        if (gameCard == null)
                            continue;
                        cards.add(gameCard);
                        gameCard.apply();
                        gameController.getDiscardPile().add(gameCard);
                        iterator.remove();
                        actionsCardContainer.childrenChanged();
                    }
                    actionsCardContainer.addEmpty();
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

    public Button getButton() {
        return button;
    }
}
