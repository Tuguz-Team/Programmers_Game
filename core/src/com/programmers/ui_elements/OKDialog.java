package com.programmers.ui_elements;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class OKDialog extends Dialog {

    public OKDialog(String title, Skin skin) {
        super(title, skin);
        button("   OK   ");
        setMovable(false);
    }
}
