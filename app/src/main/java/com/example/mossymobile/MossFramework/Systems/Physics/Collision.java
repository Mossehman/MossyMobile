package com.example.mossymobile.MossFramework.Systems.Physics;

import com.example.mossymobile.MossFramework.Components.Rigidbody;
import com.example.mossymobile.MossFramework.GameObject;
import com.example.mossymobile.MossFramework.Math.Vector2;

public class Collision {
    private GameObject gameObject;
    private Rigidbody rigidbody;
    private Vector2 collisionNormal;

    public GameObject GetGameObject() {
        return this.gameObject;
    }

    public Vector2 GetCollisionNormal() {
        return this.collisionNormal;
    }

    public Rigidbody GetRigidbody() {
        return this.rigidbody;
    }
}
