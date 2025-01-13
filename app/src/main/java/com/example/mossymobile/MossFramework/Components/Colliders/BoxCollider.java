package com.example.mossymobile.MossFramework.Components.Colliders;

import android.graphics.Color;

import com.example.mossymobile.MossFramework.Math.Vector2;
import com.example.mossymobile.MossFramework.Systems.Debugging.Gizmos;

public class BoxCollider extends Collider {

    public Vector2 hitboxDimensions = new Vector2();

    @Override
    protected void InitializeInspectorData() {
        ShowInInspector("Bounds", hitboxDimensions);
        super.InitializeInspectorData();
    }

    @Override
    public void Awake() {
        collisionType = COLLISION_TYPE.BOX;
    }

    @Override
    public Vector2 GetBounds() {
        return hitboxDimensions;
    }

    @Override
    public void OnDrawGizmos() {
        if (!DrawGizmos.value) { return; }
        Gizmos.DrawBox(Vector2.Add(gameObject.GetTransform().GetPosition(), Offset), hitboxDimensions, Color.GREEN);
    }
}
