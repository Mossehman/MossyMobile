package com.example.mossymobile.MossFramework.Systems.Audio;

import com.example.mossymobile.MossFramework.Math.Vector2;

public class AudioClip2D extends AudioClip {
    public Vector2 AudioPosition = new Vector2();
    public float MaxAudioRange = 300.0f;

    public AudioClip2D(int musicFile) {
        super(musicFile);
    }

    @Override
    public void Update(Vector2 listenerPos) {
        if (!this.IsPlaying()) { return; }
        super.Update(listenerPos);

        float audioMultiplier = 1.0f;
        float audioDistance = AudioPosition.FastDistance(listenerPos);

        if (audioDistance > MaxAudioRange) {
            audioMultiplier = 0.0f;
        } else {
            audioMultiplier = 1.0f - (audioDistance / MaxAudioRange);
        }

        float xDiff = AudioPosition.x - listenerPos.x;
        float Pan = Math.max(-1.0f, Math.min(1.0f, xDiff / MaxAudioRange));
        soundClip.setVolume(MaxVolume * audioMultiplier * (1.0f - Pan) * 0.25f, MaxVolume * audioMultiplier * (1.0f + Pan) * 0.25f);
    }
}
