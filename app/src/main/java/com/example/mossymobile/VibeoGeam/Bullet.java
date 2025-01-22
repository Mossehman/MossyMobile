package com.example.mossymobile.VibeoGeam;

import com.example.mossymobile.MossFramework.Components.Colliders.CircleCollider;
import com.example.mossymobile.MossFramework.Components.Transform;
import com.example.mossymobile.MossFramework.Math.Vector2;
import com.example.mossymobile.MossFramework.MonoBehaviour;
import com.example.mossymobile.MossFramework.Systems.Debugging.Debug;
import com.example.mossymobile.MossFramework.Systems.Physics.Collision;
import com.example.mossymobile.MossFramework.Systems.Time.Time;

import java.util.Objects;

public class Bullet extends MonoBehaviour {
    float timer = 0f;
    public BulletInfo bulletInfo;
    public Vector2 direction;
    private CircleCollider circleCollider;
    @Override
    public void Start() {
        timer = bulletInfo.lifetime;
        circleCollider = gameObject.AddComponent(CircleCollider.class);
        circleCollider.Radius = 10;
        circleCollider.SetCollisionLayer("");
    }

    @Override
    public void Update() {
        GetTransform().GetPosition().Add(Vector2.Mul(direction, bulletInfo.speed));
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
        if (Objects.equals(collision.GetCollider().GetCollisionLayer(), "")){

        }
    }
}