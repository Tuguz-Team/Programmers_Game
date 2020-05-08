package com.programmers.ui_elements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;

public class Card extends Image implements Comparable<Card> {

    private final com.programmers.game.Card card;

    public Card(final com.programmers.game.Card card) {
        super(new Texture("Sprites/Cards/".concat(card.getType().toString()).concat(".png")));
        this.card = card;
        setDebug(true);
        addListener(new InputListener() {
            CardContainer prevParent;
            final Vector2 prevPosition = new Vector2();
            final Card thisCard = Card.this;

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                prevParent = (CardContainer) thisCard.getParent();
                prevPosition.set(thisCard.getX(), thisCard.getY());
                Group group = prevParent.getParent();
                while (group != null) {
                    group.addActor(thisCard);
                    group = group.getParent();
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
                x = event.getStageX();
                y = event.getStageY();
                CardContainer cardContainer = null;
                for (CardContainer container : CardContainer.cardContainers) {
                    if (x >= container.getX() && x < container.getRight()
                            && y >= container.getY() && y < container.getTop()) {
                        cardContainer = container;
                        break;
                    }
                }
                if (cardContainer == null) {
                    cardContainer = prevParent;
                }
                cardContainer.addCard(thisCard);
                thisCard.setPosition(prevPosition.x, prevPosition.y);
                Gdx.app.log("Card", thisCard.getParent().toString()
                        + " " + thisCard.getParent().hashCode());
            }
        });
    }

    @Override
    public int compareTo(Card other) {
        return getCard().getType().compareTo(other.getCard().getType());
    }

    public com.programmers.game.Card getCard() {
        return card;
    }
}
