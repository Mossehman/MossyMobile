package com.example.mossymobile.VibeoGeam;

import com.example.mossymobile.MossFramework.Components.Renderers.Renderer;
import com.example.mossymobile.MossFramework.DesignPatterns.MutableWrapper;
import com.example.mossymobile.MossFramework.Math.Vector2;
import com.example.mossymobile.MossFramework.MonoBehaviour;

public class BarMeter extends MonoBehaviour {
    public Renderer renderer;
    public int resID;
    public MutableWrapper<Float> valueRef;
    public float maximumValue;
    public float barLength = 2000;
    public boolean startsAtZero = false;
    public boolean lerp = true;
    float fill = 0;
    public MutableWrapper<Vector2> position;
    @Override
    public void Start() {
        renderer = gameObject.AddComponent(Renderer.class);
        renderer.RenderOffset = new Vector2(0,0);
        renderer.ResourceID = resID;

        if (!startsAtZero) maximumValue = valueRef.value;
        fill = barLength;
    }

    @Override
    public void Update() {
        if (valueRef == null || valueRef.value == null) Destroy(gameObject);

        if (lerp){
            fill = Lerp(fill,barLength * (valueRef.value / maximumValue), 0.1f);
            GetTransform().SetScale(new Vector2(fill, GetTransform().GetScale().y));
        }
        else {
            fill = barLength * (valueRef.value / maximumValue);
            GetTransform().SetScale(new Vector2(fill, GetTransform().GetScale().y));
        }
        if (position != null) GetTransform().SetPosition(position.value);
    }

    private float Lerp(float from, float to, float t) {
        return from + (to - from) * t;
    }
}
