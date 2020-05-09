package com.programmers.ui_elements;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.programmers.interfaces.Procedure;

public abstract class MyButton extends ImageTextButton implements Procedure {

    public MyButton(String text, Skin skin) {
        super(text, skin);
        addMyListener();
    }

    public MyButton(String text, Skin skin, String styleName) {
        super(text, skin, styleName);
        addMyListener();
    }

    public MyButton(String text, ImageTextButtonStyle style) {
        super(text, style);
        addMyListener();
    }

    private void addMyListener() {
        addListener(new Listener() {
            @Override
            public void call() {
                MyButton.this.call();
            }
        });
    }

    public static abstract class Listener extends InputListener implements Procedure {
        boolean wasPressed;

        @Override
        public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
            return wasPressed = true;
        }

        @Override
        public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
            if (wasPressed)
                call();
            wasPressed = false;
        }

        @Override
        public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
            wasPressed = true;
        }

        @Override
        public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
            wasPressed = false;
        }
    }
}
