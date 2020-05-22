package com.programmers.ui_elements;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.programmers.enums.Difficulty;
import com.programmers.game.GameCard;
import com.programmers.game.GameController;
import com.programmers.screens.ScreenLoader;

public final class PlayerCardWindow extends Table {

    private boolean discarding = false;
    private CardContainer cardContainer;
    private final GameController gameController;

    private final Button discardButton;

    public PlayerCardWindow(final String name, final CardContainer cardContainer,
                            final GameController gameController, final AssetManager assetManager) {
        setFillParent(true);
        this.gameController = gameController;
        this.cardContainer = cardContainer;
        final Table tableTemp = new Table();
        final Table table = new Table();
        discardButton = new Button(
                new TextureRegionDrawable((Texture) assetManager.get("Sprites/AlgorithmButton/ExchangeButtonOn.png")),
                new TextureRegionDrawable((Texture) assetManager.get("Sprites/AlgorithmButton/ExchangeButtonOff.png"))
        );
        final CardContainer discardContainer = new CardContainer(null,
                gameController.getDifficulty(), CardContainer.Content.All, gameController)
        {
            @Override
            protected void childrenChanged() {
                super.childrenChanged();
                Card card = (Card)getChild(0);
                if (card.getGameCard() != null) {
                    removeEmpty();
                    GameCard gameCard = card.getGameCard();
                    gameCard.setPlayer(null);
                    gameController.getDiscardPile().add(gameCard);
                    clearChildren();
                    addEmpty();
                }
            }
        };
        discardContainer.setBackground(new BackgroundColor("Sprites/Background/background-red.png"));

        add(cardContainer).bottom();
        add(tableTemp).bottom();
        tableTemp.add(table);
        table.setFillParent(true);
        CardContainer.cardContainers.removeValue(discardContainer, false);
        discardContainer.setVisible(false);

        table.add(discardButton).center();
        table.add(discardContainer).center().row();

        discardButton.addListener(new MyButton.Listener() {
            @Override
            public void call() {
                final YesNoDialog dialog = new YesNoDialog("   Do you want to exchange "
                        + "some of your cards for new? You won't be able to make your move.   ",
                        ScreenLoader.getGameSkin()) {
                    @Override
                    public void call() {
                        discarding(discardContainer);
                    }
                };
                if (!discarding) {
                    dialog.show(gameController.getGameScreen());
                } else
                    discarding(discardContainer);
            }
        });

        Label label = new Label(name, ScreenLoader.getGameSkin());
        label.setFontScale(2);

        table.add(label).colspan(2).bottom();
        left().bottom();
    }

    public void discarding(CardContainer discardContainer) {
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
            if (gameController.getDifficulty() == Difficulty.Easy) {
                gameController.getAlgorithmToDo().clear();
            }
            gameController.toNextPlayer();
        }
        cardContainer.setTouchable();
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
