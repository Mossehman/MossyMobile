package com.example.mossymobile.MossFramework.Systems.Audio;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.SoundPool;

import com.example.mossymobile.R;

import java.util.HashMap;

public class AudioManager {
    private static SoundPool soundPool;
    private static SoundPool musicPool;
    private static HashMap<String, Integer> soundMap = new HashMap<>();
    private static HashMap<String, Integer> musicMap = new HashMap<>();

    // Initialize SoundPool
    public static void initialize(Context context) {
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        AudioAttributes musicAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        soundPool = new SoundPool.Builder()
                .setMaxStreams(10)  // Maximum number of sounds that can play simultaneously
                .setAudioAttributes(audioAttributes)
                .build();

        //musicPool = new SoundPool.Builder()
        //        .setMaxStreams(2)
        //        .setAudioAttributes(musicAttributes)
        //        .build();

        // Load sounds into soundMap with unique keys
        soundMap.put("cannon_fire", soundPool.load(context, R.raw.cannon_fire, 1));
        soundMap.put("c1xx", soundPool.load(context, R.raw.lmg_fire, 1));
        soundMap.put("explosion", soundPool.load(context, R.raw.explosion, 1));
        //musicMap.put("main_menu", musicPool.load(context, R.raw.main1, 1));
    }

    // Play sound by key
    public static void playSound(String soundKey) {
        Integer soundId = soundMap.get(soundKey);
        if (soundId != null) {
            soundPool.play(soundId, 1, 1, 1, 0, 1f);  // (soundId, leftVolume, rightVolume, priority, loop, rate)
        }
    }

    public static void playSound(String soundKey, float rate) {
        Integer soundId = soundMap.get(soundKey);
        if (soundId != null) {
            soundPool.play(soundId, 1, 1, 1, 0, rate);  // (soundId, leftVolume, rightVolume, priority, loop, rate)
        }
    }

    public static void playMusic(String soundKey, int loop) {
        Integer soundId = soundMap.get(soundKey);
        if (soundId != null) {
            musicPool.play(soundId, 1, 1, 1, loop, 1f);
        }
    }

    // Release resources
    public static void release() {
        if (soundPool != null) {
            soundPool.release();
            soundPool = null;
            soundMap.clear();
        }
        if (musicPool != null) {
            musicPool.release();
            musicPool = null;
            musicMap.clear();
        }
    }
}
