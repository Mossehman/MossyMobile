package com.example.mossymobile.MossFramework.Systems.Audio;

import android.media.MediaPlayer;

import com.example.mossymobile.MossFramework.GameView;
import com.example.mossymobile.MossFramework.Math.Vector2;
import com.example.mossymobile.MossFramework.Systems.Debugging.Debug;
import com.example.mossymobile.MossFramework.Systems.Time.Time;

import java.util.Objects;

public class AudioClip {
    protected MediaPlayer soundClip = null;
    protected boolean toStop = false;
    protected float stopTime = 1.0f;

    public float MaxVolume = 1.0f;

    public AudioClip(int musicFile)
    {
        soundClip = MediaPlayer.create(Objects.requireNonNull(GameView.GetInstance()).GetContext(), musicFile);
        if (soundClip == null)
        {
            Debug.LogError("AudioClip()", "Error when creating audio file! Music file not found!");
        }
    }

    public boolean IsPlaying()
    {
        if (soundClip == null) { return false; }
        return soundClip.isPlaying();
    }

    public void SetVolume(float volume)
    {
        if (soundClip == null) { return; }
        soundClip.setVolume(volume, volume);
        MaxVolume = volume;
    }

    public void Update(Vector2 listenerPos) {
        if (!this.IsPlaying()) { return; }

        if (listenerPos == null || stopTime <= 0.0f) {
            soundClip.stop();
            return;
        }

        if (toStop)
        {
            stopTime -= Time.GetDeltaTime();
            float volumeDecrease = MaxVolume * (Time.GetDeltaTime() / (stopTime + Time.GetDeltaTime()));

            SetVolume(Math.max(0.0f, MaxVolume - volumeDecrease));

            if (stopTime <= 0.0f) {
                soundClip.stop();
            }
        }
    }

    public void StopAudio(float time)
    {
        if (toStop) { return; }

        this.toStop = true;
        this.stopTime = time;
    }




}
