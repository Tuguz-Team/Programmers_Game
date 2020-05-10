package com.programmers.ui_elements;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.programmers.game.GameCard;

public class Card extends Image implements Comparable<Card> {

    private final GameCard gameCard;
    private CardContainer prevParent;

    public Card() {
        super(new Texture("Sprites/Cards/empty.png"));
        this.gameCard = null;
        setDebug(true);
    }

    public Card(final GameCard gameCard) {
        super(new Texture("Sprites/Cards/".concat(gameCard.getType().toString()).concat(".png")));
        this.gameCard = gameCard;
        setDebug(true);
        addListener(new InputListener() {
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
                    Vector2 tmp = container.localToStageCoordinates(new Vector2());
                    if (x >= tmp.x && x < tmp.x + container.getWidth()
                            && y >= tmp.y && y < tmp.y + container.getHeight()
                            && container.getChildren().size < 5) {
                        cardContainer = container;
                        break;
                    }
                }
                if (cardContainer == null) {
                    cardContainer = prevParent;
                }
                cardContainer.addCard(thisCard);
                thisCard.setPosition(prevPosition.x, prevPosition.y);
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
        return this.getGameCard().getType().compareTo(other.getGameCard().getType());
    }

    public GameCard getGameCard() {
        return gameCard;
    }

    public CardContainer getPrevParent() {
        return prevParent;
    }
}
