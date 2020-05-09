package com.programmers.ui_elements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.programmers.game.GameCard;
import com.programmers.game.GameController;

public final class PlayerCardWindow extends Table {

    private boolean discarding = false;
    private CardContainer cardContainer;

    public PlayerCardWindow(final String name, final CardContainer cardContainer,
                            final GameController gameController) {
        setFillParent(true);
        setDebug(true);
        this.cardContainer = cardContainer;
        final Table tableTemp = new Table();
        final Table table = new Table();
        final Button button = new Button(
                new TextureRegionDrawable(new Texture("Sprites/AlgorithmButton/StartButtonOn.png")),
                new TextureRegionDrawable(new Texture("Sprites/AlgorithmButton/StartButtonOff.png"))
        );
        final CardContainer discardContainer = new CardContainer(null, CardContainer.Content.All, false) {
            @Override
            protected void childrenChanged() {
                super.childrenChanged();
                Card card = (Card)getChild(0);
                if (card.getGameCard() != null) {
                    GameCard gameCard = card.getGameCard();
                    gameCard.setPlayer(null);
                    gameController.getDiscardPile().add(gameCard);
                    clearChildren();
                    addEmpty();
                }
            }
        };
        add(cardContainer).bottom();
        add(tableTemp).bottom();
        tableTemp.add(table);
        table.setFillParent(true);
        CardContainer.cardContainers.removeValue(discardContainer, false);
        discardContainer.setVisible(false);
        table.add(button).left();
        table.add(discardContainer).left().row();
        button.addListener(new MyButton.Listener() {
            @Override
            public void call() {
                discarding = !discarding;
                discardContainer.setVisible(discarding);
                if (discarding) {
                    gameController.getAlgorithmCardWindow().disable();
                    cardContainer.setTouchable(Touchable.enabled);
                    cardContainer.discardMode = true;
                    CardContainer.cardContainers.add(discardContainer);
                } else {
                    gameController.getAlgorithmCardWindow().enable();
                    cardContainer.discardMode = false;
                    CardContainer.cardContainers.removeValue(discardContainer, false);
                    gameController.toNextPlayer();
                }
                cardContainer.setTouchable();
            }
        });
        table.add(new Label(name, new Skin(Gdx.files.internal("uiskin.json")))).bottom();
        left().bottom();
    }

    public CardContainer getCardContainer() {
        return cardContainer;
    }
}
