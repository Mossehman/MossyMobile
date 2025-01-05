package com.example.mossymobile.MossFramework.Components;

import com.example.mossymobile.MossFramework.Math.Vector2;

public class CircleCollider extends Collider {
    public float Radius = 1.0f;


    @Override
    public void Awake() {
        collisionType = Collider.COLLISION_TYPE.NUM_TYPES;
    }

    @Override
    public Vector2 GetBounds() {
        return new Vector2(Radius, Radius);
    }

    @Override
    public void ResolveCollision() {
    }
}
