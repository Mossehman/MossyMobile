package com.example.mossymobile.MossFramework.Components;

import com.example.mossymobile.MossFramework.Components.Colliders.Collider;
import com.example.mossymobile.MossFramework.Math.Vector2;
import com.example.mossymobile.MossFramework.MonoBehaviour;
import com.example.mossymobile.MossFramework.Systems.Debugging.Debug;

public final class RigidBody extends MonoBehaviour {
    public float mass = 1.0f;
    Vector2 velocity = new Vector2();
    Collider collider = null;

    Vector2 force = new Vector2();

    @Override
    public void Start() {

    }

    @Override
    public void Update() {
        Debug.Log("AAA", "AAAA");
    }

    public void AddVelocity(Vector2 vel) {
        this.velocity.Add(vel);
    }

    public Vector2 GetVelocity() {
        return this.velocity;
    }

    public Vector2 GetForce() {
        return this.force;
    }

    public void AddForce(Vector2 force)
    {
        this.force.Add(force);
    }
}
