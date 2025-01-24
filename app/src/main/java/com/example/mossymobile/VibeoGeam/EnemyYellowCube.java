package com.example.mossymobile.VibeoGeam;

import com.example.mossymobile.MossFramework.Components.Colliders.BoxCollider;
import com.example.mossymobile.MossFramework.Components.Renderers.Renderer;
import com.example.mossymobile.MossFramework.Components.RigidBody;
import com.example.mossymobile.MossFramework.Math.Vector2;
import com.example.mossymobile.MossFramework.Systems.Physics.Collision;
import com.example.mossymobile.R;

import java.util.Objects;

public class EnemyYellowCube extends Enemy {
    private Renderer renderer;
    private BoxCollider hitbox;
    private RigidBody rb;
    EnemyYellowCube() {
        super(10, 5, 20, 5, R.drawable.redcircle);
    }

    @Override
    public void Start() {
        GetTransform().SetScale(new Vector2(100f, 100f));
        hitbox = gameObject.AddComponent(BoxCollider.class);
        rb = gameObject.AddComponent(RigidBody.class);
        renderer = gameObject.AddComponent(Renderer.class);
        hitbox.hitboxDimensions = new Vector2(20f, 20f);
        hitbox.SetCollisionLayer("Enemy");
        rb.SetRoughness(10f);
        renderer.ResourceID = super.resourceID;
    }

    @Override
    public void Update() {
        if (super.health <= 0) Destroy(this.gameObject);

        Vector2 moveDirection = Vector2.Sub(player.GetTransform().GetPosition(), GetTransform().GetPosition()).Normalized();
        //rb.AddForce(Vector2.Mul(moveDirection, 5.0f));
        rb.AddVelocity(moveDirection);
        //GetTransform().GetPosition().Add(Vector2.Mul(moveDirection, 0.1f));
    }

    @Override
    public void ModifyHealth(float amt) {
        super.health -= amt;
    }

    @Override
    public void OnCollisionEnter(Collision collision) {
        if (Objects.equals(collision.GetCollider().GetCollisionLayer(), "Player")){
            collision.GetGameObject().GetComponent(Player.class).ModifyHealth(super.damage);
            Destroy(this.gameObject);
        }
    }
}