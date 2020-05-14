package com.programmers.ui_elements;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.programmers.game.GameCard;
import com.programmers.game.hotseat.HotseatGameController;
import com.programmers.screens.ScreenLoader;

public final class PlayerCardWindow extends Table {

    private boolean discarding = false;
    private CardContainer cardContainer;

    private final Button discardButton;

    public PlayerCardWindow(final String name, final CardContainer cardContainer,
                            final HotseatGameController hotseatGameController) {
        setFillParent(true);
        setDebug(true);
        this.cardContainer = cardContainer;
        final Table tableTemp = new Table();
        final Table table = new Table();
        discardButton = new Button(
                new TextureRegionDrawable(new Texture("Sprites/AlgorithmButton/StartButtonOn.png")),
                new TextureRegionDrawable(new Texture("Sprites/AlgorithmButton/StartButtonOff.png"))
        );
        final CardContainer discardContainer = new CardContainer(null, CardContainer.Content.All, false) {
            @Override
            protected void childrenChanged() {
                super.childrenChanged();
                Card card = (Card)getChild(0);
                if (card.getGameCard() != null) {
                    removeEmpty();
                    GameCard gameCard = card.getGameCard();
                    gameCard.setHotseatPlayer(null);
                    hotseatGameController.getDiscardPile().add(gameCard);
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
        table.add(discardButton).left();
        table.add(discardContainer).left().row();
        discardButton.addListener(new MyButton.Listener() {
            @Override
            public void call() {
                discarding = !discarding;
                discardContainer.setVisible(discarding);
                if (discarding) {
                    hotseatGameController.getAlgorithmCardWindow().disable();
                    cardContainer.setTouchable(Touchable.enabled);
                    cardContainer.discardMode = true;
                    CardContainer.cardContainers.add(discardContainer);
                } else {
                    hotseatGameController.getAlgorithmCardWindow().enable();
                    cardContainer.discardMode = false;
                    CardContainer.cardContainers.removeValue(discardContainer, false);
                    hotseatGameController.toNextPlayer();
                }
                cardContainer.setTouchable();
            }
        });
        table.add(new Label(name, ScreenLoader.getDefaultGdxSkin())).bottom();
        left().bottom();
    }

    public CardContainer getCardContainer() {
        return cardContainer;
    }

    public void enableButton() {
        discardButton.setTouchable(Touchable.enabled);
    }

    public void disableButton() {
        discardButton.setTouchable(Touchable.disabled);
    }
}
