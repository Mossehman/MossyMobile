package com.example.mossymobile.VibeoGeam.Tank;

import com.example.mossymobile.MossFramework.Components.Colliders.BoxCollider;
import com.example.mossymobile.MossFramework.Components.Renderers.Renderer;
import com.example.mossymobile.MossFramework.Components.RigidBody;
import com.example.mossymobile.MossFramework.GameObject;
import com.example.mossymobile.MossFramework.Math.MossMath;
import com.example.mossymobile.MossFramework.Math.Vector2;
import com.example.mossymobile.MossFramework.MonoBehaviour;
import com.example.mossymobile.MossFramework.Systems.Audio.AudioClip;
import com.example.mossymobile.MossFramework.Systems.Audio.AudioPlayer;
import com.example.mossymobile.MossFramework.Systems.Time.Time;
import com.example.mossymobile.R;
import com.example.mossymobile.VibeoGeam.Enemy.Enemy;
import com.example.mossymobile.VibeoGeam.Enemy.EnemySpawner;
import com.example.mossymobile.VibeoGeam.Scenes.GameScene;

import java.util.List;

public class MiniTank extends MonoBehaviour {
    private RigidBody rb;
    float lifetime = 10f;
    float fireTimer = 0f;
    float retargetTimer = 0f;
    public CannonInfo cannonInfo;

    Vector2 currentFireDirection = new Vector2(0,1);
    Vector2 targetFireDirection;
    EnemySpawner enemySpawner;
    GameObject targetEnemy;
    @Override
    public void Start() {
        float size = 50f;
        GetTransform().SetScale(new Vector2(50f*5, 50f*5));
        rb = gameObject.AddComponent(RigidBody.class);
        rb.SetRoughness(20f);
        BoxCollider col = gameObject.AddComponent(BoxCollider.class);
        col.hitboxDimensions = new Vector2(15f, 15f);
        col.SetCollisionLayer("PlayerNPC");
        gameObject.AddComponent(Renderer.class).SetSprite(R.drawable.minitank);
        enemySpawner = ((GameScene)gameObject.GetScene()).enemySpawner;
        retargetTimer = 0.7f;
        TargetEnemy();
    }

    @Override
    public void Update() {
        if (lifetime > 0f){
            lifetime -= Time.GetDeltaTime();
        } else{
            Destroy(gameObject);
        }
        if (retargetTimer > 0f){
            retargetTimer -= Time.GetDeltaTime();
        } else {
            TargetEnemy();
            retargetTimer = 0.7f;
        }

        if (targetFireDirection != null && targetEnemy != null) {
            currentFireDirection = Slerp(currentFireDirection, targetFireDirection, cannonInfo.aimspeed);
            GetTransform().SetRotation(Vector2.DirectionToAngle(currentFireDirection));
            if (FireCooldown()) {
                FireBullet(currentFireDirection);
            }
            if (targetEnemy.GetTransform().GetPosition().DistanceSq(GetTransform().GetPosition()) >= 150f * 150f)
                rb.AddVelocity(Vector2.Mul(targetFireDirection, 10f));
            else {
                Vector2 reverse = Vector2.Mul(targetFireDirection, -1);
                rb.AddVelocity(Vector2.Mul(reverse, 10f));
            }
        }
    }

    private void FireBullet(Vector2 targetDirection)
    {

        fireTimer = cannonInfo.fireinterval;
        AudioPlayer.PlayAudio(new AudioClip(R.raw.lmg_fire), false);
        Vector2 fireDirection = targetDirection;
        int n = 1;
        if (cannonInfo.firetype == 1) n = cannonInfo.numofpellets;
        for (int i = 0; i < n; i++) {
            GameObject instBullet = Instantiate(new GameObject());
            float spreadAngle = cannonInfo.spread;
            if (spreadAngle > 0) {
                float randomOffset = MossMath.randFloatMinMax(-spreadAngle * 0.5f, spreadAngle * 0.5f);
                fireDirection = Vector2.RotateVector(fireDirection, randomOffset);
            }
            Bullet bulletfunc = instBullet.AddComponent(Bullet.class);
            bulletfunc.cannonInfo = cannonInfo;
            bulletfunc.direction = fireDirection;
            instBullet.AddComponent(Renderer.class).ResourceID = R.drawable.bluecircle;
            instBullet.GetTransform().SetPosition(Vector2.Add(GetTransform().GetPosition(), Vector2.Mul(fireDirection, 80f)));
            instBullet.GetTransform().SetScale(new Vector2(cannonInfo.bulletsize * 5f, cannonInfo.bulletsize * 5f));
            bulletfunc.speed = cannonInfo.speed * MossMath.randFloatMinMax(1,1+cannonInfo.randspeed);
        }
    }

    private boolean FireCooldown(){
        if (fireTimer > 0){
            fireTimer -= Time.GetDeltaTime();
        }
        else {
            fireTimer = 0;
            return true;
        }
        return false;
    }

    private void TargetEnemy(){
        List<GameObject> targets = enemySpawner.OverlapCircle(GetTransform().GetPosition(), 900f);
        for ( var enemy : targets) { targetEnemy = enemy; targetFireDirection =
                Vector2.Sub(
                        Vector2.Add(enemy.GetTransform().GetPosition(),
                                Vector2.Mul(enemy.GetComponent(Enemy.class).moveDirection,
                                        GetTransform().GetPosition().FastDistance(enemy.GetTransform().GetPosition()) * 0.5f)),
                        GetTransform().GetPosition()).FastNormalize(); break; }
    }

    private Vector2 Slerp(Vector2 from, Vector2 to, float t) {
        // Ensure both vectors are normalized
        from = from.FastNormalize();
        to = to.FastNormalize();

        // Compute the dot product (cosine of the angle)
        float dot = from.Dot(to);
        // Clamp dot product to avoid errors in acos
        dot = Math.max(-1f, Math.min(1f, dot));

        // Compute the angle between the vectors
        float theta = (float) Math.acos(dot);

        // If the angle is small, linear interpolation is sufficient
        if (theta < 1e-5) {
            return Vector2.Lerp(from, to, t).FastNormalize();
        }

        // Perform spherical linear interpolation
        float sinTheta = (float) Math.sin(theta);
        float scaleFrom = (float) Math.sin((1 - t) * theta) / sinTheta;
        float scaleTo = (float) Math.sin(t * theta) / sinTheta;

        return Vector2.Add(Vector2.Mul(from, scaleFrom), Vector2.Mul(to, scaleTo));
    }
}
