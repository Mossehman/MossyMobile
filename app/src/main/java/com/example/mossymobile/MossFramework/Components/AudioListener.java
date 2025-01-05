package com.example.mossymobile.MossFramework.Components;

import com.example.mossymobile.MossFramework.Math.Vector2;
import com.example.mossymobile.MossFramework.MonoBehaviour;
import com.example.mossymobile.MossFramework.Systems.Audio.AudioPlayer;
import com.example.mossymobile.MossFramework.Systems.Debugging.Debug;

public class AudioListener extends MonoBehaviour {

    @Override
    public void Start() {
        Transform t = gameObject.GetTransform();
        if (t == null)
        {
            Debug.LogError("AudioListener()", "Audio Listener requires gameObject to have a Transform component!");
            return;
        }

        AudioPlayer.ListenerPosition = t.GetPosition();
    }

    @Override
    public void Update() {
    }
}
