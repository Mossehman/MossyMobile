package com.example.mossymobile.VibeoGeam.Tank;

import com.example.mossymobile.MossFramework.Components.Renderers.Renderer;
import com.example.mossymobile.MossFramework.GameObject;
import com.example.mossymobile.MossFramework.MonoBehaviour;
import com.example.mossymobile.R;
import com.example.mossymobile.VibeoGeam.Player;

public class TankCosmetic extends MonoBehaviour {
    public GameObject attachedObject;
    public int ZLayer = 3;
    public int drawable = -1;
    @Override
    public void Start() {
        Renderer renderer = gameObject.AddComponent(Renderer.class);
        renderer.SetSprite(drawable);
        renderer.SetZLayer(ZLayer);
    }

    @Override
    public void Update() {
        GetTransform().SetPosition(attachedObject.GetTransform().GetPosition());
        GetTransform().SetRotation(attachedObject.GetTransform().GetRotation());
        GetTransform().SetScale(attachedObject.GetTransform().GetScale());
    }
}
