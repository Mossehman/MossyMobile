package com.example.mossymobile.VibeoGeam;

import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

import com.example.mossymobile.MossFramework.Components.Colliders.CircleCollider;
import com.example.mossymobile.MossFramework.Components.Renderers.Renderer;
import com.example.mossymobile.MossFramework.Components.RigidBody;
import com.example.mossymobile.MossFramework.GameObject;
import com.example.mossymobile.MossFramework.GameView;
import com.example.mossymobile.MossFramework.Math.Vector2;
import com.example.mossymobile.MossFramework.Scene;
import com.example.mossymobile.MossFramework.Systems.UserInput.UI;
import com.example.mossymobile.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GameScene extends Scene {
    List<BulletInfo> bulletData = new ArrayList<>();
    @Override
    protected void Init() {
        //GameObject go = new GameObject();
        //BoxCollider col = go.AddComponent(BoxCollider.class);

        //RigidBody rb = go.AddComponent(RigidBody.class);
        //go.AddComponent(PlayerController.class);
        //rb.SetMass(5);
        //go.GetTransform().SetPosition(new Vector2(1200, 800));

        //col.hitboxDimensions.x = 400;
        //col.hitboxDimensions.y = 20;
        //rb.SetGravityEnabled(false);
        //rb.SetKinematic(true);

        GameObject player = new GameObject();
        player.AddComponent(Renderer.class).ResourceID = R.drawable.cannon;
        Player playerScript = player.AddComponent(Player.class);
        player.AddComponent(RigidBody.class).SetGravityEnabled(false);
        player.AddComponent(CircleCollider.class).Radius = 20;
        View joystickUI = UI.GetInstance().AddLayoutToUI(R.layout.ui_game);
        {
            FrameLayout knob = joystickUI.findViewById(R.id.joystick_region);

            GameObject joystick = new GameObject();
            JoystickKnob knobfunction = joystick.AddComponent(JoystickKnob.class);

            Renderer r = joystick.AddComponent(Renderer.class);
            r.ResourceID = R.drawable.bluecircle;

            knobfunction.knob = joystickUI.findViewById(R.id.joystick_knob);
            knobfunction.minDistance = 5f;
            playerScript.movement = knobfunction;
        }
        {
            FrameLayout knob = joystickUI.findViewById(R.id.joystick_region2);

            GameObject joystick = new GameObject();
            JoystickKnob knobfunction = joystick.AddComponent(JoystickKnob.class);
            knobfunction.offset = new Vector2(
                    Objects.requireNonNull(GameView.GetInstance()).getWidth() - 170f,
                    Objects.requireNonNull(GameView.GetInstance()).getHeight() - 300f
            );

            Renderer r = joystick.AddComponent(Renderer.class);
            r.ResourceID = R.drawable.redcircle;

            knobfunction.knob = joystickUI.findViewById(R.id.joystick_knob2);
            knobfunction.resetDirection = false;
            knobfunction.minDistance = 50f;
            playerScript.look = knobfunction;
        }

        bulletData.add(new BulletInfo(10f, 45f, 0,  1f, 0, 0.40f, 1.0f, 4.0f, R.drawable.cannon)); // Basic single shot
        bulletData.add(new BulletInfo( 6f, 50f, 0,  3f, 2, 0.10f, 1.0f, 3.0f, R.drawable.cannon2xx));
        bulletData.add(new BulletInfo( 4f, 55f, 0,  5f, 2, 0.04f, 1.0f, 1.6f, R.drawable.cannon3xx));

        playerScript.bulletInfo = bulletData.get(2);

    }

}