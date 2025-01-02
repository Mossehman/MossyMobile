package com.example.mossymobile.MossFramework.Components;

import com.example.mossymobile.MossFramework.DesignPatterns.MutableWrapper;
import com.example.mossymobile.MossFramework.Math.Vector2;
import com.example.mossymobile.MossFramework.MonoBehaviour;

public final class Transform extends MonoBehaviour {

    private Vector2 position = new Vector2();
    private Vector2 scale = new Vector2();
    private MutableWrapper<Float> rotation = new MutableWrapper<>(0.0f);

    @Override
    protected void InitializeInspectorData() {
        super.InitializeInspectorData();
        EditInInspector("Position", position);
        EditInInspector("Scale", scale);
        EditInInspector("Rotation", rotation);
    }

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
        this.position.x = position.x;
        this.position.y = position.y;
    }

    public Vector2 GetPosition() { return this.position; }


    public void SetScale(Vector2 scale)
    {
        this.scale.x = scale.x;
        this.scale.y = scale.y;
    }

    public Vector2 GetScale() { return this.scale; }


    public void SetRotation(float angle)
    {
        this.rotation.value = angle;
    }

    public float GetRotation() { return this.rotation.value; }
}
