package com.example.mossymobile.MossFramework.Components.Colliders;

import com.example.mossymobile.MossFramework.Math.Vector2;

public class BoxCollider extends Collider {

    public Vector2 hitboxDimensions = new Vector2();

    @Override
    public void Awake() {
        collisionType = COLLISION_TYPE.BOX;
    }

    @Override
    public Vector2 GetBounds() {
        return hitboxDimensions;
    }
}
