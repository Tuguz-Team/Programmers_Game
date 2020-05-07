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
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Card.this.setZIndex(getParent().getChildren().size + 1);
                return true;
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                Vector2 position = new Vector2(
                        event.getStageX() - getParent().getX(),
                        event.getStageY() - getParent().getY()
                );
                Card.this.setPosition(position.x, position.y, Align.center);
                if (position.x >= 0 && position.x < getParent().getWidth()
                        && position.y >= 0 && position.y < getParent().getHeight()) {
                    Gdx.app.log("Card", "Into the parent");
                } else {
                    Gdx.app.log("Card", "Out of the parent");
                }
            }
        });
    }

    @Override
    public void setParent(Group parent) {
        super.setParent(parent);
    }
}
