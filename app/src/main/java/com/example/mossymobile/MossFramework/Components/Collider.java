package com.example.mossymobile.MossFramework.Components;

import com.example.mossymobile.MossFramework.MonoBehaviour;

public abstract class Collider extends MonoBehaviour {

    public enum COLLISION_TYPE {
        CIRCLE,
        BOX,
        CAPSULE,
        NUM_TYPES
    }

    private COLLISION_TYPE collisionType = COLLISION_TYPE.NUM_TYPES;
    public boolean IsTrigger = false;

    public Collider() {
        super();
        collisionType = COLLISION_TYPE.NUM_TYPES;
    }

    public Collider(String name) {
        super(name);
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

    public final void OnCollisionDetected()
    {
        if (IsTrigger)
        {
            gameObject.OnTriggerEnter();
            return;
        }
        gameObject.OnCollisionEnter();
    }

    public abstract void ResolveCollision();

    public COLLISION_TYPE GetCollisionType() {
        return this.collisionType;
    }
}
