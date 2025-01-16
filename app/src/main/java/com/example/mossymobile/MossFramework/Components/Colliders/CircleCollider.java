package com.example.mossymobile.MossFramework.Components.Colliders;

import android.graphics.Color;

import com.example.mossymobile.MossFramework.Math.Vector2;
import com.example.mossymobile.MossFramework.Systems.Debugging.Gizmos;
import com.example.mossymobile.MossFramework.Systems.Physics.Collision;

public class CircleCollider extends Collider {
    public float Radius = 1.0f;
    protected Vector2 pos = new Vector2();
    protected Vector2 bounds = new Vector2();

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
    public boolean ResolveHalfCollision(Collider other, Vector2 direction) {
        return false;
    }

    @Override
    public void OnDrawGizmos() {
        if (!DrawGizmos.value) { return; }
        //Gizmos.DrawCircle(Vector2.Add(gameObject.GetTransform().GetPosition(), Offset), Radius, gizmoColor, 24);

        Gizmos.DrawBox(Vector2.Add(gameObject.GetTransform().GetPosition(), Offset), bounds, gizmoColor);
    }

    @Override
    public void Start() {
        super.Start();
        bounds.x = Radius;
        bounds.y = Radius;
    }

    @Override
    public void LateUpdate() {
        pos.x = gameObject.GetTransform().GetPosition().x;
        pos.y = gameObject.GetTransform().GetPosition().y;

        gizmoColor = Color.GREEN;
        collidersToCheck = gameObject.GetScene().GetTree().Query(this);
        boolean collided = false;

        ///TODO: Store a reference to the last collider we collided with, then call the collision enter functions when that collider changes

        Vector2 thisPosition = Vector2.Add(gameObject.GetTransform().GetPosition(), Offset);

        for (Collider col : collidersToCheck) {
            if (col.HasCheckedCollision(this) || this.HasCheckedCollision(col)) {
                continue;
            }
            Vector2 otherPosition = Vector2.Add(col.GetGameObject().GetTransform().GetPosition(), col.Offset);

            if (col.collisionType == COLLISION_TYPE.CIRCLE) {
                CircleCollider circleCollider = (CircleCollider) col;

                float otherRadiusSum = circleCollider.Radius + Radius;
                pos.Sub(otherPosition);
                float distSq = pos.MagnitudeSq();

                if (distSq <= otherRadiusSum * otherRadiusSum) {
                    collidedWith.add(col);
                    gizmoColor = Color.RED;

                    if (!collided) { collided = true; }

                    //if we js started colliding
                    if (!IsColliding)
                    {
                        IsColliding = true;
                        if (IsTrigger || col.IsTrigger)
                        {
                            gameObject.OnTriggerEnter(col);
                            col.GetGameObject().OnTriggerEnter(this);
                        }
                        else
                        {
                            Vector2 collisionNormal = Vector2.Sub(otherPosition, thisPosition).Normalized();
                            Collision collision1 = new Collision(col.GetGameObject(), collisionNormal, col);
                            Collision collision2 = new Collision(gameObject, Vector2.Mul(collisionNormal, new Vector2(-1, -1)), this);

                            gameObject.OnCollisionEnter(collision1);
                            col.GetGameObject().OnCollisionEnter(collision2);
                        }

                    }
                    //if we are still colliding
                    else
                    {
                        if (IsTrigger || col.IsTrigger)
                        {
                            gameObject.OnTriggerStay(col);
                            col.GetGameObject().OnTriggerStay(this);
                        }
                        else
                        {
                            Vector2 collisionNormal = Vector2.Sub(otherPosition, thisPosition).Normalized();
                            Collision collision1 = new Collision(col.GetGameObject(), collisionNormal, col);
                            Collision collision2 = new Collision(gameObject, Vector2.Mul(collisionNormal, new Vector2(-1, -1)), this);

                            gameObject.OnCollisionStay(collision1);
                            col.GetGameObject().OnCollisionStay(collision2);
                        }
                    }
                }
            }
        }


        if (!collided)
        {
            IsColliding = false;
        }
    }
}
