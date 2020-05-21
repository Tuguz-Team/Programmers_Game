package com.programmers.ui_elements;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.programmers.interfaces.Procedure;

public abstract class YesNoDialog extends Dialog implements Procedure {

    public YesNoDialog(String title, Skin skin) {
        super(title, skin);
        button("   YES   ", true).button("   NO   ", false);

        getButtonTable().getCells().get(0).spaceRight(50);
        ((TextButton) getButtonTable().getCells().get(0).getActor()).getLabel().setAlignment(2);
        ((TextButton) getButtonTable().getCells().get(1).getActor()).getLabel().setAlignment(2);
        setMovable(false);
    }

    @Override
    protected void result(Object object) {
        if (object.equals(true)) {
            call();
        }
    }
}
