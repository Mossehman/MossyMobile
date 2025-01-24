package com.example.mossymobile.MossFramework.Components;

import com.example.mossymobile.MossFramework.Components.Colliders.Collider;
import com.example.mossymobile.MossFramework.DesignPatterns.MutableWrapper;
import com.example.mossymobile.MossFramework.Math.MossMath;
import com.example.mossymobile.MossFramework.Math.Vector2;
import com.example.mossymobile.MossFramework.MonoBehaviour;
import com.example.mossymobile.MossFramework.Systems.Debugging.Debug;
import com.example.mossymobile.MossFramework.Systems.Physics.Collision;
import com.example.mossymobile.MossFramework.Systems.Physics.Physics;
import com.example.mossymobile.MossFramework.Systems.Time.Time;

public final class RigidBody extends MonoBehaviour {
    private MutableWrapper<Float> mass = new MutableWrapper<>(1.0f);
    private MutableWrapper<Boolean> isKinematic = new MutableWrapper<>(false);
    private MutableWrapper<Float> elasticity = new MutableWrapper<>(0.2f);
    private MutableWrapper<Boolean> gravityEnabled = new MutableWrapper<>(true);

    private MutableWrapper<Float> roughness = new MutableWrapper<>(1.0f);

    Vector2 gravity = new Vector2(0, -9.81f);
    Vector2 velocity = new Vector2();
    Vector2 acceleration = new Vector2();
    Vector2 initialVelocity = new Vector2();
    Collider collider = null;
    Vector2 force = new Vector2();

    float resetVelocityDelay = 0.0f;

    @Override
    protected void InitializeInspectorData() {
        EditInInspector("Mass", mass);
        EditInInspector("Elasticity", elasticity);
        EditInInspector("Kinematic", isKinematic);
        EditInInspector("Gravity", gravityEnabled);
        ShowInInspector("Velocity", velocity);
    }

    @Override
    public void Start() {
        collider = this.gameObject.GetComponent(Collider.class, true);
    }

    @Override
    public void Update() {
        if (collider == null) {
            this.gameObject.RemoveComponent(RigidBody.class);
            return;
        }

        elasticity.value = MossMath.clamp(elasticity.value, 0.0f, 1.0f);
        if (this.gravityEnabled.value && !this.isKinematic.value) {
            Vector2 gravityInfluence = Vector2.Mul(gravity, new Vector2(-mass.value, -mass.value));
            force.Add(gravityInfluence);
        }
    }

    private void Physics() {

        if (this.isKinematic.value) { return; }

        Vector2 friction = Vector2.Mul(velocity, new Vector2(-Physics.AirResistance * roughness.value, -Physics.AirResistance * roughness.value));
        friction.y = 0;

        acceleration.y = force.y;
        acceleration.x = force.x / mass.value;


        initialVelocity.x = velocity.x;
        initialVelocity.y = velocity.y;

        velocity.Add(Vector2.Mul(friction, new Vector2(Time.GetDeltaTime(), Time.GetDeltaTime())));
        velocity.Add(new Vector2(acceleration.x * Time.GetDeltaTime(), acceleration.y * Time.GetDeltaTime()));

        Vector2 positionChange = Vector2.Mul(new Vector2(Time.GetDeltaTime(), Time.GetDeltaTime()), velocity);
        //positionChange.Sub(Vector2.Mul(Vector2.Mul(acceleration, new Vector2(Time.GetDeltaTime(), Time.GetDeltaTime())), new Vector2(0.5f, 0.5f)));
        gameObject.GetTransform().SetPosition(Vector2.Add(gameObject.GetTransform().GetPosition(), positionChange));


        force.x = 0;
        force.y = 0;

    }

    @Override
    public void LateUpdate() {
        Physics();
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
        AddForce(force, Physics.ForceMode2D.DEFAULT);
    }

    public void AddForce(Vector2 force, Physics.ForceMode2D forceMode) {
        if (forceMode == Physics.ForceMode2D.DEFAULT) {
            this.force.Add(force);
            return;
        }
        velocity.Add(Vector2.Div(force, new Vector2(mass.value, mass.value)));
    }

    @Override
    public void OnCollisionEnter(Collision collision) {
        if (this.isKinematic.value) {
            collision.GetCollider().ResolveHalfCollision(collider, Vector2.Mul(collision.GetCollisionNormal(), new Vector2(-1, -1)));
            return;
        }

        if (collider.ResolveHalfCollision(collision.GetCollider(), collision.GetCollisionNormal()))
        {
            velocity.x *= elasticity.value;
            velocity.y *= elasticity.value;

            if (collision.GetCollisionNormal().x != 0) {
                velocity.x *= -1;
            }
            if (collision.GetCollisionNormal().y != 0) {
                velocity.y *= -1;
            }

            resetVelocityDelay = 0.0f;
        }
    }

    @Override
    public void OnCollisionStay(Collision collision) {
        if (this.isKinematic.value) {
            if (!collision.GetGameObject().GetComponent(RigidBody.class).GetKinematic()) {
                collision.GetCollider().ResolveHalfCollision(collider, Vector2.Mul(collision.GetCollisionNormal(), new Vector2(-1, -1)));
            }
            return;
        }

        resetVelocityDelay += Time.GetDeltaTime();
        if (resetVelocityDelay > 0.05f) {
            //if (collision.GetCollisionNormal().x != 0) {
            //    velocity.x = 0;
            //}
            if (collision.GetCollisionNormal().y == 1) {
                velocity.y = 0;
            }
        }

        collider.ResolveHalfCollision(collision.GetCollider(), collision.GetCollisionNormal());
    }

    public void SetMass(float mass)
    {
        this.mass.value = mass;
    }

    public float GetMass() {
        return this.mass.value;
    }

    public void SetKinematic(boolean IsKinematic) {
        this.isKinematic.value = IsKinematic;
    }

    public boolean GetKinematic() {
        return this.isKinematic.value;
    }

    public void SetElasticity(float elasticity) {
        this.elasticity.value = elasticity;
    }

    public float GetElasticity() {
        return this.elasticity.value;
    }

    public void SetGravityEnabled(boolean GravityEnabled) {
        this.gravityEnabled.value = GravityEnabled;
    }

    public boolean GetGravityEnabled() {
        return this.gravityEnabled.value;
    }

    public void SetRoughness(float roughness) {
        this.roughness.value = roughness;
    }

    public float GetRoughness() {
        return this.roughness.value;
    }



}
