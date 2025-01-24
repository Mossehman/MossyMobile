package com.example.mossymobile.VibeoGeam;

import com.example.mossymobile.MossFramework.Components.Renderers.Renderer;
import com.example.mossymobile.MossFramework.Components.RigidBody;
import com.example.mossymobile.MossFramework.GameObject;
import com.example.mossymobile.MossFramework.GameView;
import com.example.mossymobile.MossFramework.Math.Vector2;
import com.example.mossymobile.MossFramework.MonoBehaviour;
import com.example.mossymobile.MossFramework.Systems.Debugging.Debug;
import com.example.mossymobile.MossFramework.Systems.Time.Time;
import com.example.mossymobile.R;

import java.util.Objects;

public class Player extends MonoBehaviour implements IDamageable {
    public JoystickKnob movement;
    public JoystickKnob look;

    private RigidBody rb;

    public float Health = 100f;
    public CannonInfo cannonInfo;
    private float fireTimer = 0f;

    private Vector2 moveDirection = new Vector2();
    private Vector2 lookDirection = new Vector2();
    private Vector2 currentFireDirection = new Vector2();

    private Renderer sprite;

    @Override
    public void Start() {
        GetTransform().SetPosition(
                new Vector2(Objects.requireNonNull(GameView.GetInstance()).getWidth() * 0.5f,
                        Objects.requireNonNull(GameView.GetInstance()).getHeight() * 0.5f));
        GetTransform().SetScale(new Vector2(200f, 200f));
        rb = gameObject.GetComponent(RigidBody.class);
        rb.SetRoughness(10f);
        sprite = gameObject.GetComponent(Renderer.class);
    }

    @Override
    public void Update() {
        gameObject.GetScene().quadtreePos = GetTransform().GetPosition();
        if (sprite.ResourceID != cannonInfo.spriteResourceID){
            sprite.ResourceID = cannonInfo.spriteResourceID;
        }
        if (look != null && movement != null) {
            if (moveDirection.MagnitudeSq() > 0)
                rb.AddVelocity(Vector2.Mul(moveDirection, 0.1f));
                //GetTransform().GetPosition().Add(Vector2.Mul(moveDirection, 0.1f));

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
        fireTimer = cannonInfo.fireinterval;

        Vector2 fireDirection = targetDirection;
        GameObject instBullet = Instantiate(new GameObject());
        float spreadAngle = cannonInfo.spread; // Spread in degrees
        if (spreadAngle > 0) {
            float randomOffset = (float) (Math.random() * spreadAngle - (spreadAngle / 2.0)); // Random angle within spread
            fireDirection = Vector2.RotateVector(fireDirection, randomOffset);
        }
        Bullet bulletfunc = instBullet.AddComponent(Bullet.class);
        bulletfunc.cannonInfo = cannonInfo;
        bulletfunc.direction = fireDirection;
        instBullet.AddComponent(Renderer.class).ResourceID = R.drawable.bluecircle;
        instBullet.GetTransform().SetPosition(GetTransform().GetPosition());
        instBullet.GetTransform().SetScale(new Vector2(50,50));
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
        Health -= amt;
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
            return Lerp(from, to, t).Normalized();
        }

        // Perform spherical linear interpolation
        float sinTheta = (float) Math.sin(theta);
        float scaleFrom = (float) Math.sin((1 - t) * theta) / sinTheta;
        float scaleTo = (float) Math.sin(t * theta) / sinTheta;

        return Vector2.Add(Vector2.Mul(from, scaleFrom), Vector2.Mul(to, scaleTo));
    }
    private Vector2 Lerp(Vector2 from, Vector2 to, float t) {
        // Clamp t between 0 and 1
        t = Math.max(0f, Math.min(1f, t));

        // Linearly interpolate each component
        float x = from.x + (to.x - from.x) * t;
        float y = from.y + (to.y - from.y) * t;

        return new Vector2(x, y);
    }

    private float SlerpFloat(float from, float to, float t) {
        // Wrap angles to [-180, 180]
        float delta = (to - from + 180f) % 360f - 180f;

        // Interpolate using the delta
        float result = from + delta * t;

        // Wrap the result to [0, 360) for consistency
        return (result + 360f) % 360f;
    }


}
