package com.example.mossymobile.MossFramework.Components.Colliders;

import android.graphics.Color;

import com.example.mossymobile.MossFramework.DesignPatterns.MutableWrapper;
import com.example.mossymobile.MossFramework.Math.Vector2;
import com.example.mossymobile.MossFramework.MonoBehaviour;
import com.example.mossymobile.MossFramework.Systems.Physics.Collision;

import java.util.ArrayList;
import java.util.List;

public abstract class Collider extends MonoBehaviour {

    public enum COLLISION_TYPE {
        CIRCLE,
        BOX,
        NUM_TYPES
    }

    protected List<Collider> collidedWith = new ArrayList<>();
    protected Collider otherCollider = null;

    protected boolean IsColliding = false;

    public int gizmoColor = Color.GREEN;

    protected COLLISION_TYPE collisionType = COLLISION_TYPE.NUM_TYPES;
    protected List<Collider> collidersToCheck = new ArrayList<>();
    public boolean IsTrigger = false;

    public MutableWrapper<Boolean> DrawGizmos = new MutableWrapper<>(true);

    @Override
    protected void InitializeInspectorData() {
        ShowInInspector("Offset", Offset);
        EditInInspector("Wireframe", DrawGizmos);
    }

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
        collidedWith.clear();
        gameObject.GetScene().GetTree().AddCollider(this);
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

    public boolean CheckCollision()
    {
        return this.IsColliding;
    }

    public boolean HasCheckedCollision(Collider col)
    {
        return collidedWith.contains(col);
    }

    public abstract boolean ResolveHalfCollision(Collider other, Vector2 direction);
}
