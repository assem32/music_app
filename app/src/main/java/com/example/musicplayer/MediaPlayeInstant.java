package com.example.musicplayer;

import android.media.MediaPlayer;

public class MediaPlayeInstant {

    static MediaPlayer instance;

    public static MediaPlayer getInstance() {
        if (instance == null) {
            instance = new MediaPlayer();
        }
        return instance;


    }
}
