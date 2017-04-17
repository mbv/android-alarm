package com.bsuir.mbv.lab5;


import android.media.MediaPlayer;

public class AlarmService {
    private int id;
    private MediaPlayer mediaPlayer;
    private boolean isPlaying;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public void setMediaPlayer(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }
}
