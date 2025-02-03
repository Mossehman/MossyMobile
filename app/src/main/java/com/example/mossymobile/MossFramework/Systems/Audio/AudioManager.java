package com.example.mossymobile.MossFramework.Systems.Audio;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;

import com.example.mossymobile.MossFramework.Math.MossMath;
import com.example.mossymobile.MossFramework.Math.Vector2;
import com.example.mossymobile.R;

import java.util.HashMap;

public class AudioManager {
    private static SoundPool soundPool;
    private static HashMap<String, Integer> soundMap = new HashMap<>();
    private static MediaPlayer musicPlayer = null;

    // Initialize SoundPool
    public static void initialize(Context context) {
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        soundPool = new SoundPool.Builder()
                .setMaxStreams(10)
                .setAudioAttributes(audioAttributes)
                .build();

        // Load sound effects into soundMap
        soundMap.put("cxxx", soundPool.load(context, R.raw.cxxx, 1));
        soundMap.put("c2xx", soundPool.load(context, R.raw.c2xx, 1));
        soundMap.put("c3xx", soundPool.load(context, R.raw.c3xx, 1));
        soundMap.put("cx2x", soundPool.load(context, R.raw.cx2x, 1));
        soundMap.put("cx3x", soundPool.load(context, R.raw.cx3x, 1));
        soundMap.put("cxx2", soundPool.load(context, R.raw.cxx2, 1));
        soundMap.put("cxx3", soundPool.load(context, R.raw.cxx3, 1));

        soundMap.put("ux1x", soundPool.load(context, R.raw.ux1x, 1));
        soundMap.put("ux2x", soundPool.load(context, R.raw.ux2x, 1));
        soundMap.put("ux3x", soundPool.load(context, R.raw.ux3x, 1));

        soundMap.put("uxx1", soundPool.load(context, R.raw.uxx1, 1));
        soundMap.put("uxx2", soundPool.load(context, R.raw.uxx2, 1));


        soundMap.put("ooa", soundPool.load(context, R.raw.outofammo, 1));
        soundMap.put("explosion", soundPool.load(context, R.raw.explosion, 1));
        soundMap.put("upgrade", soundPool.load(context, R.raw.upgrade, 1));

        soundMap.put("enemyhit", soundPool.load(context, R.raw.hurt, 1));
        soundMap.put("playerhit", soundPool.load(context, R.raw.metal_interaction1, 1));
        soundMap.put("playerdeath", soundPool.load(context, R.raw.tankexplosion, 1));
        soundMap.put("playerensnare", soundPool.load(context, R.raw.electricity, 1));
        soundMap.put("playerunsnare", soundPool.load(context, R.raw.engine_rev, 1));
    }

    public static void playSound(String soundKey) {
        Integer soundId = soundMap.get(soundKey);
        if (soundId != null) {
            soundPool.play(soundId, 1, 1, 1, 0, 1f);
        }
    }

    public static void playSound(String soundKey, float rate) {
        Integer soundId = soundMap.get(soundKey);
        if (soundId != null) {
            soundPool.play(soundId, 1, 1, 1, 0, rate);
        }
    }

    public static void playSound(String soundKey, float rate, float volume) {
        Integer soundId = soundMap.get(soundKey);
        if (soundId != null) {
            soundPool.play(soundId, volume, volume, 1, 0, rate);
        }
    }

    public static void playSound(String soundKey, Vector2 listenerPos, Vector2 soundPos, float rate) {
        Integer soundId = soundMap.get(soundKey);
        if (soundId != null) {
            float[] volumes = calculateStereoVolume(listenerPos, soundPos);
            soundPool.play(soundId, volumes[0], volumes[1], 1, 0, rate);  // (soundId, leftVolume, rightVolume, priority, loop, rate)
        }
    }

    public static void playMusic(Context context, int resId, boolean loop) {
        stopMusic();
        musicPlayer = MediaPlayer.create(context, resId);
        if (musicPlayer != null) {
            musicPlayer.setLooping(loop);
            musicPlayer.setVolume(1.0f, 1.0f);
            musicPlayer.start();
        }
    }

    public static void stopMusic() {
        if (musicPlayer != null) {
            if (musicPlayer.isPlaying()) {
                musicPlayer.stop();
            }
            musicPlayer.release();
            musicPlayer = null;
        }
    }

    // Release all resources
    public static void release() {
        if (soundPool != null) {
            soundPool.release();
            soundPool = null;
            soundMap.clear();
        }
        stopMusic();
    }

    private static float[] calculateStereoVolume(Vector2 playerPos, Vector2 soundPos) {
        float distance = playerPos.Distance(soundPos);
        float maxDistance = 3000f;

        float distanceFactor = Math.min(distance / maxDistance, 1.0f);

        float overallVolume = 1.0f - distanceFactor;

        float pan = (soundPos.x - playerPos.x) / maxDistance;
        pan = MossMath.clamp(pan, -1.0f, 1.0f);

        float leftVolume = overallVolume * (pan <= 0 ? 1.0f : 1.0f - pan);
        float rightVolume = overallVolume * (pan >= 0 ? 1.0f : 1.0f + pan);

        return new float[]{leftVolume, rightVolume};
    }

}
