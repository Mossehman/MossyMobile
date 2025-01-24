package com.example.mossymobile.VibeoGeam;

import android.view.View;
import android.widget.FrameLayout;

import com.example.mossymobile.MossFramework.Components.Colliders.BoxCollider;
import com.example.mossymobile.MossFramework.Components.Colliders.CircleCollider;
import com.example.mossymobile.MossFramework.Components.Renderers.Renderer;
import com.example.mossymobile.MossFramework.Components.RigidBody;
import com.example.mossymobile.MossFramework.GameObject;
import com.example.mossymobile.MossFramework.GameView;
import com.example.mossymobile.MossFramework.Math.Vector2;
import com.example.mossymobile.MossFramework.MonoBehaviour;
import com.example.mossymobile.MossFramework.Scene;
import com.example.mossymobile.MossFramework.Systems.UserInput.UI;
import com.example.mossymobile.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GameScene extends Scene {
    List<CannonInfo> bulletData = new ArrayList<>();
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



        float screenWidth = Objects.requireNonNull(GameView.GetInstance()).getWidth();
        float screenHeight = Objects.requireNonNull(GameView.GetInstance()).getHeight();

        Vector2[] wallcoords = {
                new Vector2(screenWidth * 0.5f, 0),
                new Vector2(screenWidth * 0.5f, screenHeight),
                new Vector2(0, screenHeight * 0.5f),
                new Vector2(screenWidth, screenHeight * 0.5f),
        };

        Vector2[] wallscales = {
                new Vector2(screenWidth, 100),
                new Vector2(screenWidth, 100),
                new Vector2(100, screenHeight),
                new Vector2(100, screenHeight),
        };

        for (int i = 0; i < 4; i++) {
            GameObject wall = new GameObject();
            BoxCollider col = wall.AddComponent(BoxCollider.class);
            RigidBody rb = wall.AddComponent(RigidBody.class);
            rb.SetGravityEnabled(false);
            rb.SetKinematic(true);
            //wall.AddComponent(Renderer.class).ResourceID = R.drawable.bluesquare;
            wall.GetTransform().SetPosition(wallcoords[i]);
            wall.GetTransform().SetScale(wallscales[i]);
            col.hitboxDimensions = wallscales[i];
        }

        GameObject player = new GameObject();
        player.AddComponent(Renderer.class).ResourceID = R.drawable.cannon;
        Player playerScript = player.AddComponent(Player.class);
        player.AddComponent(RigidBody.class).SetGravityEnabled(false);
        BoxCollider playerHitbox = player.AddComponent(BoxCollider.class);
        playerHitbox.hitboxDimensions = new Vector2(20f, 20f);
        playerHitbox.SetCollisionLayer("Player");
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
                    screenWidth - 170f,
                    screenHeight - 300f
            );

            Renderer r = joystick.AddComponent(Renderer.class);
            r.ResourceID = R.drawable.redcircle;

            knobfunction.knob = joystickUI.findViewById(R.id.joystick_knob2);
            knobfunction.resetDirection = false;
            knobfunction.minDistance = 50f;
            playerScript.look = knobfunction;
        }

        bulletData.add(new CannonInfo(10f, 45f, 0, 1f, 0, 0.10f, 1.0f, 4.0f, 0.70f, R.drawable.cannon)); // Basic single shot
        bulletData.add(new CannonInfo(6f, 50f, 0, 3f, 2, 0.10f, 1.0f, 3.0f, 0.10f, R.drawable.cannon2xx));
        bulletData.add(new CannonInfo(4f, 55f, 0, 5f, 2, 0.04f, 1.0f, 1.6f, 0.05f, R.drawable.cannon3xx));

        playerScript.cannonInfo = bulletData.get(0);
        GameObject waveSpawner = new GameObject();
        waveSpawner.AddComponent(EnemySpawner.class).player = player;

        GameObject healthBar = new GameObject();
        BarMeter hpfunc = healthBar.AddComponent(BarMeter.class);
        hpfunc.resID = R.drawable.redsquare;
        healthBar.GetTransform().SetPosition(new Vector2(100, 100));
        healthBar.GetTransform().SetScale(new Vector2(200, 200));


    }
}