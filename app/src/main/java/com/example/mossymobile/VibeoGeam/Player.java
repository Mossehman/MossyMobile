package com.example.mossymobile.VibeoGeam;

import com.example.mossymobile.MossFramework.Components.Transform;
import com.example.mossymobile.MossFramework.Math.Vector2;
import com.example.mossymobile.MossFramework.MonoBehaviour;

public class Player extends MonoBehaviour {
    public JoystickKnob movement;

    @Override
    public void Start() {
        GetTransform().SetPosition(new Vector2(0f,0f));
        GetTransform().SetScale(new Vector2(200f, 200f));

    }

    @Override
    public void Update() {
        if (movement == null) return;
        Vector2 direction = movement.direction;
        GetTransform().GetPosition().Add(Vector2.Mul(direction, new Vector2(0.5f, 0.5f)));
    }
}
