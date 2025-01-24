package com.example.mossymobile.VibeoGeam;

import com.example.mossymobile.MossFramework.Components.Renderers.Renderer;
import com.example.mossymobile.MossFramework.Math.Vector2;
import com.example.mossymobile.MossFramework.MonoBehaviour;

public class BarMeter extends MonoBehaviour {
    private Renderer renderer;
    public int resID;


    @Override
    public void Start() {
        renderer = gameObject.AddComponent(Renderer.class);
        renderer.RenderOffset = new Vector2(0,0);
        renderer.ResourceID = resID;

    }

    @Override
    public void Update() {

    }
}
