package com.example.mossymobile.MossFramework.Systems.Audio;

import com.example.mossymobile.MossFramework.Math.Vector2;
import com.example.mossymobile.MossFramework.Systems.Debugging.Debug;

import java.util.ArrayList;
import java.util.List;

public final class AudioPlayer {

    public static Vector2 ListenerPosition = null;
    private static List<AudioClip> CurrentAudio = new ArrayList<>();

    public static void PlayAudio(AudioClip audioClip, boolean toLoop)
    {
        if (audioClip.soundClip == null)
        {
            Debug.LogError("AudioClip()", "Error when playing audio, sound clip was null!");
            return;
        }

        CurrentAudio.add(audioClip);
        audioClip.soundClip.setLooping(toLoop);
        audioClip.soundClip.start();
    }

    public static void StopAudio(AudioClip audioClip)
    {
        if (CurrentAudio.contains(audioClip))
        {
            audioClip.soundClip.stop();
        }
    }

    public static void StopAudio(AudioClip audioClip, float time)
    {
        if (CurrentAudio.contains(audioClip))
        {
            audioClip.StopAudio(time);
        }
    }

    public static void Update()
    {
        if (CurrentAudio.isEmpty() || ListenerPosition == null) { return; } //no audio files to update or no audio listener
        for (int i = CurrentAudio.size() - 1; i >= 0; i--)
        {
            if (!CurrentAudio.get(i).soundClip.isPlaying())
            {
                CurrentAudio.remove(i);
                return;
            }
            CurrentAudio.get(i).Update(ListenerPosition);

        }
    }

}
