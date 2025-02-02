package com.example.mossymobile.VibeoGeam.Tank;

import com.example.mossymobile.MossFramework.Components.Renderers.Renderer;
import com.example.mossymobile.MossFramework.GameObject;
import com.example.mossymobile.MossFramework.Math.Vector2;
import com.example.mossymobile.MossFramework.MonoBehaviour;
import com.example.mossymobile.MossFramework.Systems.Time.Time;
import com.example.mossymobile.R;

public class Pulse extends MonoBehaviour {
    public GameObject positionref;
    public float pulseinterval;
    float maxinterval;
    @Override
    public void Start() {
        Renderer renderer = gameObject.AddComponent(Renderer.class);
        renderer.SetZLayer(3);
        renderer.SetSprite(R.drawable.aura_pulse);
        maxinterval = pulseinterval;
    }

    @Override
    public void Update() {
        if (pulseinterval > 0f) pulseinterval -= Time.GetDeltaTime();
        else {
            pulseinterval = maxinterval;
        }
        GetTransform().SetPosition(positionref.GetTransform().GetPosition());

        float progress = 1 - (pulseinterval / maxinterval);

        Vector2 targetScale = positionref.GetTransform().GetScale();
        Vector2 currentScale = Vector2.Lerp(new Vector2(0, 0), targetScale, progress);
        GetTransform().SetScale(currentScale);
    }
}
