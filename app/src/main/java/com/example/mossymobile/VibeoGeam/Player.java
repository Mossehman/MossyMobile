package com.example.mossymobile.VibeoGeam;

import android.util.Log;

import com.example.mossymobile.MossFramework.Components.Renderers.Renderer;
import com.example.mossymobile.MossFramework.Components.RigidBody;
import com.example.mossymobile.MossFramework.Components.Transform;
import com.example.mossymobile.MossFramework.GameObject;
import com.example.mossymobile.MossFramework.Math.Vector2;
import com.example.mossymobile.MossFramework.MonoBehaviour;
import com.example.mossymobile.MossFramework.Scene;
import com.example.mossymobile.MossFramework.Systems.Debugging.Debug;
import com.example.mossymobile.MossFramework.Systems.Physics.Physics;
import com.example.mossymobile.R;

public class Player extends MonoBehaviour implements IDamageable {
    public JoystickKnob movement;
    public JoystickKnob look;

    private RigidBody rb;

    public float Health = 100f;
    public BulletInfo bulletInfo;

    @Override
    public void Start() {
        GetTransform().SetPosition(new Vector2(0f,0f));
        GetTransform().SetScale(new Vector2(200f, 200f));
        rb = gameObject.GetComponent(RigidBody.class);


    }

    @Override
    public void Update() {
        if (look != null && movement != null) {

            Vector2 moveDirection = movement.direction;
            //rb.AddForce(moveDirection, Physics.ForceMode2D.IMPULSE);
            //rb.AddVelocity(moveDirection);
            GetTransform().GetPosition().Add(Vector2.Mul(moveDirection, 0.1f));
            Vector2 lookDirection = look.direction;
            if (look.isJoystickUp) {
                //Debug.Log("Player", "Fire!");
                GameObject instBullet = Instantiate(new GameObject());
                
                float spreadAngle = bulletInfo.spread; // Spread in degrees
                if (spreadAngle > 0) {
                    float randomOffset = (float) (Math.random() * spreadAngle - (spreadAngle / 2.0)); // Random angle within spread
                    lookDirection = RotateVector(lookDirection, randomOffset);
                }
                instBullet.AddComponent(Bullet.class).bulletInfo = bulletInfo;
                instBullet.AddComponent(RigidBody.class).AddVelocity(lookDirection);
                instBullet.AddComponent(Renderer.class).ResourceID = R.drawable.bluecircle;
                instBullet.GetTransform().SetScale(new Vector2(20,20));

                look.isJoystickUp = false;
            }
            if (lookDirection.MagnitudeSq() > 0)
                GetTransform().SetRotation(DirectionToAngle(lookDirection));
            else if (moveDirection.MagnitudeSq() > 0)
                GetTransform().SetRotation(DirectionToAngle(moveDirection));
        }
    }

    private float DirectionToAngle(Vector2 dir){
        float dx = dir.x;
        float dy = dir.y;

        // Calculate angle in radians and convert to degrees
        float angleRadians = (float) Math.atan2(dx, -dy); // Negate dx for upward 0 degrees
        float angleDegrees = (float) Math.toDegrees(angleRadians);

        // Normalize to [0, 360)
        float rotation = (angleDegrees + 360) % 360;
        return rotation;
    }

    private Vector2 RotateVector(Vector2 vector, float angleDegrees) {
        double angleRadians = Math.toRadians(angleDegrees);
        float cos = (float) Math.cos(angleRadians);
        float sin = (float) Math.sin(angleRadians);

        float x = vector.x * cos - vector.y * sin;
        float y = vector.x * sin + vector.y * cos;

        return new Vector2(x, y);
    }


    @Override
    public void ModifyHealth(float amt) {
        Health -= amt;
    }
}
