package com.programmers.ui_elements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
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

public class AlgorithmCardWindow extends Table {

    public AlgorithmCardWindow(final String name, final GameController gameController) {
        setFillParent(true);
        setDebug(true);
        final CardContainer actionsCardContainer;
        final CardContainer cyclesCardContainer;
        final Table table = new Table();
        final Button button = new Button(
                new TextureRegionDrawable(new Texture("Sprites/AlgorithmButton/StartButtonOn.png")),
                new TextureRegionDrawable(new Texture("Sprites/AlgorithmButton/StartButtonOff.png"))
        );
        table.add(button).row();
        add(new Label(name, new Skin(Gdx.files.internal("uiskin.json")))).bottom();
        add(table);
        if (gameController.getDifficulty() == Difficulty.Hard) {
            cyclesCardContainer = new CardContainer(
                    gameController.getAlgorithm(),
                    CardContainer.Content.Cycles,
                    false
            );
            table.add(cyclesCardContainer).spaceRight(10).bottom();
            actionsCardContainer = new CardContainer(
                    gameController.getAlgorithm(),
                    CardContainer.Content.Actions, false);
        } else {
            actionsCardContainer = new CardContainer(
                    gameController.getAlgorithm(),
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
                    iterator.next();
                    while (iterator.hasNext()) {
                        Card card = (Card) iterator.next();
                        GameCard gameCard = card.getGameCard();
                        cards.add(gameCard);
                        gameCard.apply();
                        iterator.remove();
                        actionsCardContainer.childrenChanged();
                    }
                }
            }
        });
    }
}
