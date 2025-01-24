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

public class EnemyYellowCube extends Enemy {
    private Renderer renderer;
    private BoxCollider hitbox;
    private RigidBody rb;
    private GameObject bar;
    EnemyYellowCube() {
        super(20, 5, 20, 5, R.drawable.redsquare);
    }

    @Override
    public void Start() {
        GetTransform().SetScale(new Vector2(60f, 60f));
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
        if (super.health.value <= 0) {
            numOfEnemies.value--;
            player.GetComponentFast(Player.class).Exp.value += super.expGain;
            Destroy(this.gameObject);
        }
        else if (hpBar != null){
            //Vector2 pos = new Vector2(GetTransform().GetPosition());
            //hpBar.GetTransform().SetPosition(Vector2.Add(pos, new Vector2(0, 0)));
            //hpBar.renderer.RenderOffset = new Vector2(0, 20);
            hpBar.position.value = Vector2.Add(GetTransform().GetPosition(), new Vector2(0, -40));
        }
        Vector2 moveDirection = Vector2.Sub(player.GetTransform().GetPosition(), GetTransform().GetPosition()).Normalized();
        //rb.AddForce(Vector2.Mul(moveDirection, 5.0f));
        rb.AddVelocity(moveDirection);
        //GetTransform().GetPosition().Add(Vector2.Mul(moveDirection, 0.1f));
    }

    @Override
    public void ModifyHealth(float amt) {
        super.health.value -= amt;
        if (hpBar == null) {
            bar = new GameObject();
            hpBar = bar.AddComponent(BarMeter.class);
            hpBar.resID = R.drawable.redsquare;
            hpBar.valueRef = super.health;
            hpBar.GetTransform().SetScale(new Vector2(200, 10));
            hpBar.position = new MutableWrapper<>(GetTransform().GetPosition());
            hpBar.barLength = 100;
        }
    }
    @Override
    public void OnCollisionEnter(Collision collision) {
        if (Objects.equals(collision.GetCollider().GetCollisionLayer(), "Player")){
            collision.GetGameObject().GetComponent(Player.class).ModifyHealth(super.damage);
            if (hpBar != null) Destroy(bar);
            Destroy(this.gameObject);
        }
    }

    @Override
    public void OnDestroy() {
        if (hpBar != null) Destroy(bar);
    }
}