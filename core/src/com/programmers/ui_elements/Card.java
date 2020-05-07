package com.programmers.ui_elements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;

public class Card extends Image {

    public Card(final com.programmers.game.Card card) {
        super(new Texture("Sprites/Cards/".concat(card.getType().toString()).concat(".png")));
        addListener(new InputListener() {
            Group prevParent;
            final Vector2 prevPosition = new Vector2();
            final Card thisCard = Card.this;

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                thisCard.setZIndex(thisCard.getParent().getChildren().size + 1);
                prevParent = thisCard.getParent();
                prevPosition.set(thisCard.getX(), thisCard.getY());
                Group group = prevParent.getParent();
                while (group != null) {
                    thisCard.setParent(group);
                    group = group.getParent();
                }
                return true;
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                thisCard.setPosition(event.getStageX() - prevParent.getX(),
                        event.getStageY() - prevParent.getY(), Align.center);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (thisCard.getParent().getParent() == null) {
                    thisCard.setParent(prevParent);
                    thisCard.setPosition(prevPosition.x, prevPosition.y);
                }
            }

            /* НЕ РАБОТАЕТ!
            private Actor getByPosition(final Actor rootActor, final float x, final float y) {
                if (rootActor instanceof Group) {
                    for (Actor actor : ((Group)rootActor).getChildren()) {
                        Actor temp = getByPosition(actor, x, y);
                        if (temp != null && temp != thisCard) {
                            return temp;
                        }
                    }
                }
                return rootActor.hit(x, y, true);
            }
            */
        });
    }

    @Override
    public void setParent(Group parent) {
        super.setParent(parent);
    }
}
