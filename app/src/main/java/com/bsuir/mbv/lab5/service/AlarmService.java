package com.bsuir.mbv.lab5.service;


import android.media.MediaPlayer;
import android.net.Uri;

public class AlarmService {
    private int id;
    private MediaPlayer mediaPlayer;
    private Uri ringtone;
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

    public Uri getRingtone() {
        return ringtone;
    }

    public void setRingtone(Uri ringtone) {
        this.ringtone = ringtone;
    }
}
