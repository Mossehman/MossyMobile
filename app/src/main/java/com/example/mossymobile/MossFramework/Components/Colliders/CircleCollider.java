package com.example.mossymobile.MossFramework.Components.Colliders;

import android.graphics.Color;

import com.example.mossymobile.MossFramework.Math.Vector2;
import com.example.mossymobile.MossFramework.Systems.Debugging.Gizmos;

public class CircleCollider extends Collider {
    public float Radius = 1.0f;

    @Override
    protected void InitializeInspectorData() {
        ShowInInspector("Radius", Radius);
        super.InitializeInspectorData();
    }

    @Override
    public void Awake() {
        collisionType = COLLISION_TYPE.CIRCLE;
    }

    @Override
    public Vector2 GetBounds() {
        return new Vector2(Radius, Radius);
    }

    @Override
    public void OnDrawGizmos() {
        if (!DrawGizmos.value) { return; }
        Gizmos.DrawCircle(Vector2.Add(gameObject.GetTransform().GetPosition(), Offset), Radius, Color.GREEN, 24);
    }
}
