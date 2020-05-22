package com.programmers.ui_elements;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.programmers.screens.ScreenLoader;

public class LabelDisappear extends Label {

    private long animationStart = System.currentTimeMillis();
    private final long animationDuration;

    public LabelDisappear(String text, long animationDuration) {
        super(text, ScreenLoader.getGameSkin());
        setFontScale(2);
        this.animationDuration = animationDuration;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (animationStart + animationDuration < System.currentTimeMillis()) {
            remove();
            return;
        }
        float elapsed = System.currentTimeMillis() - animationStart;
        setColor(getColor().r, getColor().g, getColor().b, parentAlpha * (1 - elapsed / animationDuration));
    }
}
