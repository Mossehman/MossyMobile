package com.example.mossymobile.VibeoGeam;

import android.widget.ProgressBar;

import com.example.mossymobile.MossFramework.DesignPatterns.MutableWrapper;
import com.example.mossymobile.MossFramework.GameView;
import com.example.mossymobile.MossFramework.Math.Vector2;
import com.example.mossymobile.MossFramework.MonoBehaviour;
import com.example.mossymobile.MossFramework.Systems.UserInput.UI;
import com.example.mossymobile.R;
import com.example.mossymobile.VibeoGeam.Tank.UpgradesManager;

public class ImprovedProgressBar extends MonoBehaviour {
    ProgressBar bar;
    public MutableWrapper<Float> valueRef;
    public float maximumValue;
    public float progressValue;
    public boolean lerp = true;

    public ImprovedProgressBar SetRef(MutableWrapper<Float> valueRef){this.valueRef = valueRef; return this;}
    public ImprovedProgressBar SetMax(float max){maximumValue = max; return this;}
    public ImprovedProgressBar SetProgressBar(ProgressBar bar) {this.bar = bar; return this;}
    @Override
    public void Start() {

    }

    @Override
    public void Update() {
        if (lerp){
            progressValue = Lerp(progressValue,valueRef.value, 0.1f);
        }
        else {
            progressValue = valueRef.value;
        }
        GameView.GetInstance().GetActivity().runOnUiThread(() -> {
            ProgressBar view = UI.GetInstance().GetUIContainer().findViewById(R.id.circularProgressBar);
            view.setProgress((int)(progressValue * 100));
            view.setMax((int)(maximumValue * 100));
        });
    }

    private float Lerp(float from, float to, float t) {
        return from + (to - from) * t;
    }
}
