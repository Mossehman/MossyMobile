package com.example.mossymobile.MossFramework.Components.Colliders;

import com.example.mossymobile.MossFramework.Math.Vector2;
import com.example.mossymobile.MossFramework.MonoBehaviour;
import com.example.mossymobile.MossFramework.Systems.Physics.Collision;

public abstract class Collider extends MonoBehaviour {

    public enum COLLISION_TYPE {
        CIRCLE,
        BOX,
        CAPSULE,
        NUM_TYPES
    }

    protected COLLISION_TYPE collisionType = COLLISION_TYPE.NUM_TYPES;
    public boolean IsTrigger = false;

    ///Adds to the position when calculating the hitbox center
    public Vector2 Offset = new Vector2();

    private String CollisionLayer = "Default";

    @Override
    public void Awake() {
        collisionType = COLLISION_TYPE.NUM_TYPES;
    }

    @Override
    public void Start() {
        return;
    }

    @Override
    public void Update() {
        return;
    }

    public abstract Vector2 GetBounds();

    public COLLISION_TYPE GetCollisionType() {
        return this.collisionType;
    }

    public void SetCollisionLayer(String layer)
    {
        if (Collision.CheckLayerExists(layer))
        {
            this.CollisionLayer = layer;
        }
    }

    public String GetCollisionLayer()
    {
        return this.CollisionLayer;
    }
}
