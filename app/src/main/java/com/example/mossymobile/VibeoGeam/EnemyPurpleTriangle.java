package com.example.mossymobile.VibeoGeam;

import com.example.mossymobile.MossFramework.Components.Colliders.BoxCollider;
import com.example.mossymobile.MossFramework.Components.Renderers.Renderer;
import com.example.mossymobile.MossFramework.Components.RigidBody;
import com.example.mossymobile.MossFramework.DesignPatterns.MutableWrapper;
import com.example.mossymobile.MossFramework.GameObject;
import com.example.mossymobile.MossFramework.Math.Vector2;
import com.example.mossymobile.MossFramework.Systems.Physics.Collision;
import com.example.mossymobile.R;

import java.util.Objects;

public class EnemyPurpleTriangle extends Enemy {
    private Renderer renderer;
    private BoxCollider hitbox;
    private RigidBody rb;
    private GameObject bar;
    private float originalHp;
    private boolean isDying = false; // Prevent multiple destroy calls

    EnemyPurpleTriangle() {
        super(5, 20, 40, 15, R.drawable.redcircle);
    }

    @Override
    public void Start() {
        GetTransform().SetScale(new Vector2(30f, 30f));
        hitbox = gameObject.AddComponent(BoxCollider.class);
        rb = gameObject.AddComponent(RigidBody.class);
        renderer = gameObject.AddComponent(Renderer.class);
        hitbox.hitboxDimensions = new Vector2(10f, 10f);
        hitbox.SetCollisionLayer("Enemy");
        rb.SetRoughness(10f);
        renderer.ResourceID = super.resourceID;
        originalHp = super.health.value;
    }

    @Override
    public void Update() {
        if (!isDying) {
            if (super.health.value <= 0) {
                isDying = true; // Prevent multiple destroy calls
                if (hpBar != null) {
                    Destroy(bar);
                    hpBar = null;
                }
                numOfEnemies.value--;
                player.earnExp(super.expGain);
                Destroy(this.gameObject);
                return; // Stop further execution
            } else if (hpBar != null) {
                hpBar.position.value = Vector2.Add(GetTransform().GetPosition(), new Vector2(0, -40));
            }

            Vector2 moveDirection = Vector2.Sub(player.GetTransform().GetPosition(), GetTransform().GetPosition()).Normalized();
            rb.AddVelocity(moveDirection);

            if (health.value < originalHp && hpBar == null && health.value > 0) {
                bar = new GameObject();
                hpBar = bar.AddComponent(BarMeter.class);
                hpBar.resID = R.drawable.redsquare;
                hpBar.valueRef = super.health;
                hpBar.GetTransform().SetScale(new Vector2(200, 10));
                hpBar.position = new MutableWrapper<>(GetTransform().GetPosition());
                hpBar.barLength = 100;
            }

            if (GetTransform().GetPosition().MagnitudeSq() >= 2000 * 2000) {
                Destroy(gameObject);
            }
        }
    }

    @Override
    public void ModifyHealth(float amt) {
        if (!isDying) {
            super.health.value -= amt;
        }
    }

    @Override
    public void OnCollisionEnter(Collision collision) {
        super.OnCollisionEnter(collision);
        if (Objects.equals(collision.GetCollider().GetCollisionLayer(), "Player")) {
            if (hpBar != null) {
                Destroy(bar);
                hpBar = null;
            }
        }
    }

    @Override
    public void OnDestroy() {
        super.OnDestroy();
        if (hpBar != null) {
            Destroy(bar);
            hpBar = null;
        }
    }
}