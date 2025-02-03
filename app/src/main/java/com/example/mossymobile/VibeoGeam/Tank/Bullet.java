package com.example.mossymobile.VibeoGeam.Tank;

import com.example.mossymobile.MossFramework.Components.Colliders.BoxCollider;
import com.example.mossymobile.MossFramework.Components.Colliders.Collider;
import com.example.mossymobile.MossFramework.Components.RigidBody;
import com.example.mossymobile.MossFramework.Math.MossMath;
import com.example.mossymobile.MossFramework.Math.Vector2;
import com.example.mossymobile.MossFramework.MonoBehaviour;
import com.example.mossymobile.MossFramework.Systems.Audio.AudioManager;
import com.example.mossymobile.MossFramework.Systems.Debugging.Debug;
import com.example.mossymobile.MossFramework.Systems.Time.Time;
import com.example.mossymobile.VibeoGeam.Enemy.Enemy;

import java.util.Objects;

public class Bullet extends MonoBehaviour {
    float timer = 0f;
    public CannonInfo cannonInfo;
    private int pierce;
    public Vector2 direction;
    public float speed;

    @Override
    public void Start() {
        timer = cannonInfo.lifetime;
        pierce = cannonInfo.pierce;
        BoxCollider boxCollider = gameObject.AddComponent(BoxCollider.class);
        boxCollider.hitboxDimensions = new Vector2(cannonInfo.bulletsize, cannonInfo.bulletsize);
        boxCollider.SetCollisionLayer("PlayerBullet");
        boxCollider.IsTrigger = true;
        RigidBody rb = gameObject.AddComponent(RigidBody.class);
        rb.SetGravityEnabled(false);
        rb.AddVelocity(Vector2.Mul(direction, speed));
    }

    @Override
    public void Update() {
        //GetTransform().GetPosition().Add(Vector2.Mul(direction, cannonInfo.speed));
        if (timer >= 0) {
            timer -= Time.GetDeltaTime();
        } else {
            Destroy(this.gameObject);
        }
    }
    /*
    @Override
    public void OnCollisionEnter(Collision collision) {
        if (Objects.equals(collision.GetCollider().GetCollisionLayer(), "Enemy")) {
            Debug.Log("Bullet", "EnemyEnter");
            collision.GetGameObject().GetComponent(Enemy.class).ModifyHealth(cannonInfo.damage);
            if (pierce > 0) {
                pierce--;
            } else {
                Destroy(this.gameObject);
            }
            return;
        }

        if (Objects.equals(collision.GetCollider().GetCollisionLayer(), "Player") ||
                Objects.equals(collision.GetCollider().GetCollisionLayer(), "Bullet") ||
                Objects.equals(collision.GetCollider().GetCollisionLayer(), "EnemyBullet")) {
            return;
        } else {
            Debug.Log("Bullet", "MiscEnter");
            Destroy(this.gameObject);
            return;
        }
    }*/
    @Override
    public void OnTriggerEnter(Collider collider) {
        if (Objects.equals(collider.GetCollisionLayer(), "Enemy")) {
            Debug.Log("Bullet", "EnemyEnter");
            collider.GetGameObject().GetComponent(Enemy.class).ModifyHealth(cannonInfo.damage);
            AudioManager.playSound("enemyhit", MossMath.randFloatMinMax(1.75f,2.25f), 0.4f);
            if (pierce > 0) {
                pierce--;
            } else {
                Destroy(this.gameObject);
            }
            return;
        }

        if (Objects.equals(collider.GetCollisionLayer(), "Player") ||
                Objects.equals(collider.GetCollisionLayer(), "Bullet") ||
                Objects.equals(collider.GetCollisionLayer(), "EnemyBullet")) {
            return;
        }
        if (Objects.equals(collider.GetCollisionLayer(), "Wall")) {
            Destroy(this.gameObject);
        }
    }
}