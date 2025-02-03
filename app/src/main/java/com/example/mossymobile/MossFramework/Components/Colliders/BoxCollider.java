package com.example.mossymobile.MossFramework.Components.Colliders;

import android.graphics.Color;

import com.example.mossymobile.MossFramework.Math.MossMath;
import com.example.mossymobile.MossFramework.Math.Vector2;
import com.example.mossymobile.MossFramework.Systems.Debugging.Debug;
import com.example.mossymobile.MossFramework.Systems.Debugging.Gizmos;
import com.example.mossymobile.MossFramework.Systems.Physics.Collision;

import java.util.Objects;

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
    public boolean ResolveHalfCollision(Collider other, Vector2 direction) {
        Vector2 thisPosition = Vector2.Add(gameObject.GetTransform().GetPosition(), Offset);
        Vector2 otherPosition = Vector2.Add(other.GetGameObject().GetTransform().GetPosition(), other.Offset);

        if (other instanceof BoxCollider)
        {
            BoxCollider col = (BoxCollider) other;

            if (direction.x == -1) { // Moving left
                float thisBounds = thisPosition.x - this.hitboxDimensions.x; // Left edge of this
                float otherBounds = otherPosition.x + col.hitboxDimensions.x; // Right edge of other

                if (thisBounds >= otherBounds) { // No overlap
                    return false;
                }

                float penetration = (otherBounds - thisBounds) * 0.5f; // Overlap amount
                gameObject.GetTransform().SetPosition(new Vector2(thisPosition.x + penetration, thisPosition.y));
            }
            else if (direction.x == 1) { // Moving right
                float thisBounds = thisPosition.x + this.hitboxDimensions.x; // Right edge of this
                float otherBounds = otherPosition.x - col.hitboxDimensions.x; // Left edge of other

                if (thisBounds <= otherBounds) { // No overlap
                    return false;
                }

                float penetration = (thisBounds - otherBounds) * 0.5f; // Overlap amount
                gameObject.GetTransform().SetPosition(new Vector2(thisPosition.x - penetration, thisPosition.y));
            }
            if (direction.y == -1) {
                float thisBounds = thisPosition.y - this.hitboxDimensions.y; // Right edge of this
                float otherBounds = otherPosition.y + col.hitboxDimensions.y; // Left edge of other

                if (thisBounds >= otherBounds) { // No overlap
                    return false;
                }

                float penetration = (otherBounds - thisBounds) * 0.5f; // Overlap amount
                gameObject.GetTransform().SetPosition(new Vector2(thisPosition.x, thisPosition.y + penetration));
            }
            else if (direction.y == 1) {
                float thisBounds = thisPosition.y + this.hitboxDimensions.y; // Right edge of this
                float otherBounds = otherPosition.y - col.hitboxDimensions.y; // Left edge of other

                if (thisBounds <= otherBounds) { // No overlap
                    return false;
                }

                float penetration = (thisBounds - otherBounds) * 0.5f; // Overlap amount
                gameObject.GetTransform().SetPosition(new Vector2(thisPosition.x, thisPosition.y - penetration));
            }
        }

        return true;
    }

    @Override
    public void OnDrawGizmos() {
        if (!DrawGizmos.value) { return; }
        Gizmos.DrawBox(Vector2.Add(gameObject.GetTransform().GetPosition(), Offset), hitboxDimensions, gizmoColor);
    }

    @Override
    public void LateUpdate() {
        gizmoColor = Color.GREEN;
        collidersToCheck = gameObject.GetScene().GetTree().Query(this);
        boolean collided = false;

        Vector2 thisPosition = Vector2.Add(gameObject.GetTransform().GetPosition(), Offset);

        for (Collider col : collidersToCheck)
        {
            if (col.HasCheckedCollision(this) || this.HasCheckedCollision(col) || 
                    Objects.requireNonNull(Collision.CollisionLayers.get(this.CollisionLayer)).contains(col.GetCollisionLayer())) {
                continue;
            }
            

            Vector2 otherPosition = Vector2.Add(col.GetGameObject().GetTransform().GetPosition(), col.Offset);

            if (col.collisionType == COLLISION_TYPE.BOX)
            {
                BoxCollider boxCollider = (BoxCollider) col;
                if (CheckAABBCollision(this, boxCollider))
                {
                    if (!collided) { collided = true; }

                    collidedWith.add(col);
                    gizmoColor = Color.RED;

                    if (col != otherCollider) {
                        IsColliding = true;

                        if (IsTrigger || col.IsTrigger)
                        {
                            gameObject.OnTriggerEnter(col);
                            col.GetGameObject().OnTriggerEnter(this);

                            if (otherCollider != null) {
                                gameObject.OnTriggerExit(otherCollider);
                                otherCollider.GetGameObject().OnTriggerExit(this);
                            }
                        }
                        else
                        {
                            Vector2 collisionNormal = GetCollisionNormal(boxCollider, this);
                            Collision collision1 = new Collision(col.GetGameObject(), collisionNormal, col);
                            Collision collision2 = new Collision(gameObject, Vector2.Mul(collisionNormal, new Vector2(-1, -1)), this);

                            gameObject.OnCollisionEnter(collision1);
                            col.GetGameObject().OnCollisionEnter(collision2);

                            if (otherCollider != null) {
                                gameObject.OnCollisionExit(otherCollider);
                                otherCollider.GetGameObject().OnCollisionExit(this);
                            }
                        }

                        otherCollider = col;

                    }
                    else {
                        if (IsTrigger || col.IsTrigger)
                        {
                            gameObject.OnTriggerStay(col);
                            col.GetGameObject().OnTriggerStay(this);
                        }
                        else
                        {
                            Vector2 collisionNormal = GetCollisionNormal(boxCollider, this);
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
            if (otherCollider != null) {
                otherCollider.GetGameObject().OnCollisionExit(this);
                gameObject.OnCollisionExit(otherCollider);
                otherCollider = null;
            }
        }
    }

    private boolean CheckAABBCollision(BoxCollider a, BoxCollider b) {
        Vector2 aCenter = Vector2.Add(a.gameObject.GetTransform().GetPosition(), a.Offset);
        Vector2 bCenter = Vector2.Add(b.gameObject.GetTransform().GetPosition(), b.Offset);

        // Check for overlap on both axes
        boolean overlapX = Math.abs(aCenter.x - bCenter.x) <= (a.hitboxDimensions.x + b.hitboxDimensions.x);
        boolean overlapY = Math.abs(aCenter.y - bCenter.y) <= (a.hitboxDimensions.y + b.hitboxDimensions.y);

        return overlapX && overlapY;
    }

    private Vector2 GetCollisionNormal(BoxCollider a, BoxCollider b) {
        Vector2 aMinDimensions = Vector2.Mul(a.hitboxDimensions, new Vector2(-1, -1));
        Vector2 bMinDimensions = Vector2.Mul(b.hitboxDimensions, new Vector2(-1, -1));

        Vector2 aMaxDimensions = new Vector2(a.hitboxDimensions.x, a.hitboxDimensions.y);
        Vector2 bMaxDimensions = new Vector2(b.hitboxDimensions.x, b.hitboxDimensions.y);

        Vector2 minA = Vector2.Add(aMinDimensions, Vector2.Add(a.GetGameObject().GetTransform().GetPosition(), a.Offset));
        Vector2 maxA = Vector2.Add(aMaxDimensions, Vector2.Add(a.GetGameObject().GetTransform().GetPosition(), a.Offset));

        Vector2 minB = Vector2.Add(bMinDimensions, Vector2.Add(b.GetGameObject().GetTransform().GetPosition(), b.Offset));
        Vector2 maxB = Vector2.Add(bMaxDimensions, Vector2.Add(b.GetGameObject().GetTransform().GetPosition(), b.Offset));

        float overlapX = Math.min(maxA.x, maxB.x) - Math.max(minA.x, minB.x);
        float overlapY = Math.min(maxA.y, maxB.y) - Math.max(minA.y, minB.y);

        if (overlapX < overlapY) {
            // Horizontal collision
            if (minA.x < minB.x) {
                return new Vector2(-1, 0);
            } else {
                return new Vector2(1, 0);
            }
        } else {
            // Vertical collision
            if (minA.y < minB.y) {
                return new Vector2(0, -1);
            } else {
                return new Vector2(0, 1);
            }
        }
    }
}
