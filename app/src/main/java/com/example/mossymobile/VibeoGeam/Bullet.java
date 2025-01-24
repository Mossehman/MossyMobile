package com.example.mossymobile.VibeoGeam;

import com.example.mossymobile.MossFramework.Components.Colliders.CircleCollider;
import com.example.mossymobile.MossFramework.Math.Vector2;
import com.example.mossymobile.MossFramework.MonoBehaviour;
import com.example.mossymobile.MossFramework.Systems.Debugging.Debug;
import com.example.mossymobile.MossFramework.Systems.Physics.Collision;
import com.example.mossymobile.MossFramework.Systems.Time.Time;

import java.util.Objects;

public class Bullet extends MonoBehaviour {
    float timer = 0f;
    public CannonInfo cannonInfo;
    public Vector2 direction;
    private CircleCollider circleCollider;
    @Override
    public void Start() {
        timer = cannonInfo.lifetime;
        circleCollider = gameObject.AddComponent(CircleCollider.class);
        circleCollider.Radius = 10;
        circleCollider.SetCollisionLayer("");
    }

    @Override
    public void Update() {
        GetTransform().GetPosition().Add(Vector2.Mul(direction, cannonInfo.speed));
        Debug.Log("Bullet", direction);
        if (timer >= 0)
        {
            timer -= Time.GetDeltaTime();
        }
        else {
            Destroy(this.gameObject);
        }
    }

    @Override
    public void OnCollisionEnter(Collision collision) {
        Debug.Log("Bullet", "Enter");
        if (Objects.equals(collision.GetCollider().GetCollisionLayer(), "Player") ||
            Objects.equals(collision.GetCollider().GetCollisionLayer(), "PlayerBullet") ||
            Objects.equals(collision.GetCollider().GetCollisionLayer(), "EnemyBullet"))
            return;

        if (Objects.equals(collision.GetCollider().GetCollisionLayer(), "Enemy")){
            collision.GetGameObject().GetComponent(Enemy.class).ModifyHealth(cannonInfo.damage);

        }
        Destroy(this.gameObject);
    }
}