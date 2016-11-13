package com.bromi.audio;

import android.content.Context;
import android.media.MediaPlayer;

import com.bromi.R;

import java.util.Collection;
import java.util.HashMap;

public class BackgroundMusic {

    public static HashMap<Integer, MediaPlayer> musicPlayers = new HashMap<>();

    public static void start(Context mContext, int music, boolean launch) {
        if (!launch) {
            musicPlayers.get(music).start();
            return;
        }

        MediaPlayer player = MediaPlayer.create(mContext, music);
        musicPlayers.put(music, player);
        player.setVolume(100,100);
        player.setLooping(true);
        player.start();
    }

    public static void pause() {
        Collection<MediaPlayer> mps = musicPlayers.values();
        for (MediaPlayer p : mps) {
            if (p.isPlaying()) {
                p.pause();
            }
        }
    }

    public static void release() {
        Collection<MediaPlayer> mps = musicPlayers.values();
        for (MediaPlayer p : mps) {
            if (p != null) {
                if (p.isPlaying()) {
                    p.stop();
                }
                p.release();
            }
        }
    }
}
