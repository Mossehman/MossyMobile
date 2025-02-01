package com.example.mossymobile.VibeoGeam;

import android.graphics.Color;

import com.example.mossymobile.MossFramework.Components.Renderers.Renderer;
import com.example.mossymobile.MossFramework.Components.RigidBody;
import com.example.mossymobile.MossFramework.DesignPatterns.MutableWrapper;
import com.example.mossymobile.MossFramework.GameObject;
import com.example.mossymobile.MossFramework.GameView;
import com.example.mossymobile.MossFramework.Math.MossMath;
import com.example.mossymobile.MossFramework.Math.Vector2;
import com.example.mossymobile.MossFramework.Math.Vector2Int;
import com.example.mossymobile.MossFramework.MonoBehaviour;
import com.example.mossymobile.MossFramework.Systems.Audio.AudioClip;
import com.example.mossymobile.MossFramework.Systems.Audio.AudioPlayer;
import com.example.mossymobile.MossFramework.Systems.Debugging.Gizmos;
import com.example.mossymobile.MossFramework.Systems.Time.Time;
import com.example.mossymobile.MossFramework.Systems.UserInput.Vibration;
import com.example.mossymobile.R;
import com.example.mossymobile.VibeoGeam.Tank.Bullet;
import com.example.mossymobile.VibeoGeam.Tank.CannonInfo;
import com.example.mossymobile.VibeoGeam.Tank.UpgradesManager;

import java.util.Objects;

public class Player extends MonoBehaviour implements IDamageable {
    public JoystickKnob movement;
    public JoystickKnob look;

    private RigidBody rb;

    public MutableWrapper<Float> Health = new MutableWrapper<>(100f);
    public MutableWrapper<Float> Ammo = new MutableWrapper<>(100f);
    public MutableWrapper<Float> Exp = new MutableWrapper<>(0f);
    public float cumulativeExpEarned = 0f;
    public CannonInfo cannonInfo;
    private float fireTimer = 0f;
    private Vector2 moveDirection = new Vector2();
    private Vector2 lookDirection = new Vector2();
    private Vector2 currentFireDirection = new Vector2();
    private Renderer sprite;
    public float Regen = 2f;
    public float Reload = 4f;

    private UpgradesManager upgrades = UpgradesManager.GetInstance();

    @Override
    public void Start() {
        GetTransform().SetPosition(
                new Vector2(Objects.requireNonNull(GameView.GetInstance()).getWidth() * 0.5f,
                        Objects.requireNonNull(GameView.GetInstance()).getHeight() * 0.5f));
        GetTransform().SetScale(new Vector2(200f, 200f));
        rb = gameObject.GetComponent(RigidBody.class);
        rb.SetRoughness(20f);
        sprite = gameObject.GetComponent(Renderer.class);

        gameObject.GetScene().quadtreeScale = new Vector2(Objects.requireNonNull(GameView.GetInstance()).getWidth(),
                Objects.requireNonNull(GameView.GetInstance()).getHeight());
    }

    @Override
    public void Update() {
        // holy yandere dev if statements
        gameObject.GetScene().quadtreePos = GetTransform().GetPosition();
        if (sprite.ResourceID != cannonInfo.spriteResourceID){
            sprite.ResourceID = cannonInfo.spriteResourceID;
        }
        float x = GetTransform().GetPosition().x;
        float y = GetTransform().GetPosition().y;

        x = MossMath.clamp(x, 0f, (float)Objects.requireNonNull(GameView.GetInstance()).getWidth());
        y = MossMath.clamp(y, 0f, (float)Objects.requireNonNull(GameView.GetInstance()).getHeight());
        GetTransform().SetPosition(new Vector2(x,y));

        if (look != null && movement != null) {
            if (moveDirection.MagnitudeSq() > 0)
                rb.AddVelocity(Vector2.Mul(moveDirection, 0.1f));

            boolean fireCondition = false;
            switch (cannonInfo.firetype){
                case 0:
                case 1: fireCondition = look.isJoystickUp; break;
                case 2: fireCondition = look.isJoystickHeld; break;
                default: break;
            }
            if (fireCondition && FireCooldown() && look.direction.MagnitudeSq() > 0) { // Shooting
                FireBullet(currentFireDirection);
                look.isJoystickUp = false;
            }
            if (!look.isJoystickHeld){
                if (Ammo.value < 100f) Ammo.value += Time.GetDeltaTime() * (Reload + upgrades.FetchBaseUpgrade(1).GetCurrentMod());
                Ammo.value = MossMath.clamp(Ammo.value, 0f, 100f);
                if (Health.value < upgrades.FetchBaseUpgrade(2).GetCurrentMod()) Health.value += Time.GetDeltaTime() * (Regen + upgrades.FetchBaseUpgrade(0).GetCurrentMod());
                Health.value = MossMath.clamp(Health.value, 0f, upgrades.FetchBaseUpgrade(2).GetCurrentMod());
            }
            if (Exp.value >= 100f) {
                Exp.value -= 100f;
                UpgradesManager.GetInstance().PlayerLevelPoints++;
            }
            if (look.isJoystickHeld) { // Aiming
                Vector2 targetDirection = look.isJoystickDead && moveDirection.MagnitudeSq() > 0 ?
                        moveDirection.Normalized() : lookDirection.Normalized();
                currentFireDirection = Slerp(currentFireDirection, targetDirection, cannonInfo.aimspeed);
                GetTransform().SetRotation(Vector2.DirectionToAngle(currentFireDirection));
            }
            else if (moveDirection.MagnitudeSq() > 0) { // Moving Look
                currentFireDirection = Slerp(currentFireDirection, moveDirection, 0.1f);
                GetTransform().SetRotation(Vector2.DirectionToAngle(currentFireDirection));
            }
            moveDirection = movement.direction;
            lookDirection = look.direction;
        }
    }

    private void FireBullet(Vector2 targetDirection)
    {
        if (Ammo.value < cannonInfo.ammocost){
            return;
        }
        Ammo.value -= cannonInfo.ammocost;
        fireTimer = cannonInfo.fireinterval;
        AudioPlayer.PlayAudio(new AudioClip(R.raw.lmg_fire), false);
        Vector2 fireDirection = targetDirection;
        int n = 1;
        if (cannonInfo.firetype == 1) n = cannonInfo.numofpellets;
        for (int i = 0; i < n; i++) {
            GameObject instBullet = Instantiate(new GameObject());
            float spreadAngle = cannonInfo.spread; // Spread in degrees
            if (spreadAngle > 0) {
                float randomOffset =MossMath.randFloatMinMax(-spreadAngle * 0.5f, spreadAngle * 0.5f);
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
        else{
            fireTimer = 0;
            return true;
        }
        return false;
    }

    @Override
    public void ModifyHealth(float amt) {
        Health.value -= amt;
        Vibration.Vibrate(new Vector2Int(500, 10));
    }

    @Override
    public void OnDrawGizmos() {
        Vector2 direction = new Vector2(0,0);
        if (look.isJoystickHeld) { // Aiming
            direction = lookDirection;
        }
        else if (moveDirection.MagnitudeSq() > 0) { // Moving Look
            direction = moveDirection;
        }
        Gizmos.DrawLine(GetTransform().GetPosition(), Vector2.Add(GetTransform().GetPosition(), Vector2.Mul(direction, 400f)), Color.WHITE);
    }

    private Vector2 Slerp(Vector2 from, Vector2 to, float t) {
        // Ensure both vectors are normalized
        from = from.Normalized();
        to = to.Normalized();

        // Compute the dot product (cosine of the angle)
        float dot = from.Dot(to);
        // Clamp dot product to avoid errors in acos
        dot = Math.max(-1f, Math.min(1f, dot));

        // Compute the angle between the vectors
        float theta = (float) Math.acos(dot);

        // If the angle is small, linear interpolation is sufficient
        if (theta < 1e-5) {
            return Vector2.Lerp(from, to, t).Normalized();
        }

        // Perform spherical linear interpolation
        float sinTheta = (float) Math.sin(theta);
        float scaleFrom = (float) Math.sin((1 - t) * theta) / sinTheta;
        float scaleTo = (float) Math.sin(t * theta) / sinTheta;

        return Vector2.Add(Vector2.Mul(from, scaleFrom), Vector2.Mul(to, scaleTo));
    }

    public Player earnExp(float exp){
        Exp.value += exp;
        cumulativeExpEarned += exp;
        return this;
    }

    public RigidBody getRb() { return rb; }
}
