package com.example.mossymobile.VibeoGeam.Tank;

import com.example.mossymobile.MossFramework.Components.Renderers.Renderer;
import com.example.mossymobile.MossFramework.GameObject;
import com.example.mossymobile.MossFramework.Math.MossMath;
import com.example.mossymobile.MossFramework.Math.Vector2;
import com.example.mossymobile.MossFramework.MonoBehaviour;
import com.example.mossymobile.MossFramework.Systems.Audio.AudioClip;
import com.example.mossymobile.MossFramework.Systems.Audio.AudioManager;
import com.example.mossymobile.MossFramework.Systems.Audio.AudioPlayer;
import com.example.mossymobile.MossFramework.Systems.Time.Time;
import com.example.mossymobile.R;
import com.example.mossymobile.VibeoGeam.Player;

public class Trapper extends MonoBehaviour {
    CannonInfo cannonInfo = new CannonInfo(5f, 15f,3,
            0, 2, 1.f,
            0f, 10f, 1f, -1, 0, "uxx2").SetBulletSize(25);
    public Player player;
    float fireTimer = 0f;
    Vector2 currentFireDirection = new Vector2(0,1);
    @Override
    public void Start() {

    }

    @Override
    public void Update() {
        GetTransform().SetPosition(player.GetTransform().GetPosition());

        if (player.movement.direction.MagnitudeSq() > 0) currentFireDirection = player.movement.direction.FastNormalize();
        else currentFireDirection = Vector2.GetVectorFromAngle(player.GetTransform().GetRotation()-90);
        currentFireDirection.RotateVector(90);

        if (FireCooldown()) {
            FireBullet(currentFireDirection);
            FireBullet(Vector2.Mul(currentFireDirection, -1));
        }

    }

    private void FireBullet(Vector2 targetDirection) {
        fireTimer = cannonInfo.fireinterval;
        AudioManager.playSound(cannonInfo.soundName, MossMath.randFloatMinMax(0.75f,1.25f));
        Vector2 fireDirection = targetDirection;
        int n = 1;
        if (cannonInfo.firetype == 1) n = cannonInfo.numofpellets;

        GameObject instBullet = Instantiate(new GameObject());
        float spreadAngle = cannonInfo.spread;
        if (spreadAngle > 0) {
            float randomOffset = MossMath.randFloatMinMax(-spreadAngle * 0.5f, spreadAngle * 0.5f);
            fireDirection = Vector2.RotateVector(fireDirection, randomOffset);
        }
        Bullet bulletfunc = instBullet.AddComponent(Bullet.class);
        bulletfunc.cannonInfo = cannonInfo;
        bulletfunc.direction = fireDirection;
        instBullet.AddComponent(Renderer.class).ResourceID = R.drawable.caltrop;
        instBullet.GetTransform().SetPosition(Vector2.Add(GetTransform().GetPosition(), Vector2.Mul(fireDirection, 80f)));
        instBullet.GetTransform().SetScale(new Vector2(cannonInfo.bulletsize * 3.5f, cannonInfo.bulletsize * 3.5f));
        instBullet.GetTransform().SetRotation(player.GetTransform().GetRotation());
        bulletfunc.speed = cannonInfo.speed * MossMath.randFloatMinMax(1, 1 + cannonInfo.randspeed);
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
}
