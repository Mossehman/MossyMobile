package com.example.mossymobile.MossFramework.Components;

import android.graphics.Color;

import com.example.mossymobile.MossFramework.Math.Vector2;
import com.example.mossymobile.MossFramework.MonoBehaviour;
import com.example.mossymobile.MossFramework.Systems.Audio.AudioPlayer;
import com.example.mossymobile.MossFramework.Systems.Debugging.Debug;
import com.example.mossymobile.MossFramework.Systems.Debugging.Gizmos;

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
        return;
    }

    @Override
    public void OnDrawGizmos() {
        Gizmos.DrawBox(new Vector2(400, 400), new Vector2(200, 200), Color.RED);
    }
}
