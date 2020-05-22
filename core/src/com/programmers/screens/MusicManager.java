package com.programmers.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

public class MusicManager {
    private final Music mainTheme, gameTheme, menuTheme;

    public MusicManager() {
        mainTheme = Gdx.audio.newMusic(Gdx.files.internal("Music/Space_Ambient.mp3"));
        mainTheme.setVolume(0.4f);
        mainTheme.setLooping(true);

        gameTheme = Gdx.audio.newMusic(Gdx.files.internal("Music/GameTheme.mp3"));
        gameTheme.setLooping(true);

        menuTheme = Gdx.audio.newMusic(Gdx.files.internal("Music/MenuTheme.mp3"));
        menuTheme.setVolume(0.2f);
        menuTheme.setLooping(true);
    }

    public Music getMainTheme() {
        return mainTheme;
    }

    public Music getGameTheme() {
        return gameTheme;
    }

    public Music getMenuTheme() {
        return menuTheme;
    }
}
