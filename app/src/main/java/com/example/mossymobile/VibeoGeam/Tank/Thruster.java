package com.example.mossymobile.VibeoGeam.Tank;

import com.example.mossymobile.MossFramework.Components.Renderers.Renderer;
import com.example.mossymobile.MossFramework.MonoBehaviour;
import com.example.mossymobile.VibeoGeam.Player;

public class Thruster extends MonoBehaviour {
    public Player player;
    private Renderer renderer;

    @Override
    public void Start() {
        renderer = gameObject.AddComponent(Renderer.class);
        //renderer.SetSprite();
    }

    @Override
    public void Update() {

    }
}
