package com.example.mossymobile.MossFramework.Components;

import com.example.mossymobile.MossFramework.Math.Vector2;
import com.example.mossymobile.MossFramework.MonoBehaviour;

public class Transform extends MonoBehaviour {

    private Vector2 position = new Vector2();
    private Vector2 scale = new Vector2();
    private float rotation = 0.0f;

    @Override
    public void Start() {
        return;
    }

    @Override
    public void Update() {
        return;
    }

    public void SetPosition(Vector2 position)
    {
        this.position = position;
    }

    public Vector2 GetPosition() { return this.position; }


    public void SetScale(Vector2 scale)
    {
        this.scale = scale;
    }

    public Vector2 GetScale() { return this.scale; }


    public void SetRotation(float angle)
    {
        this.rotation = angle;
    }

    public float GetRotation() { return this.rotation; }
}
