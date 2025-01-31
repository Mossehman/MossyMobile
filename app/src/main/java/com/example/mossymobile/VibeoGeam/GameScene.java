package com.example.mossymobile.VibeoGeam;

import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mossymobile.MossFramework.Application;
import com.example.mossymobile.MossFramework.Components.Colliders.BoxCollider;
import com.example.mossymobile.MossFramework.Components.Colliders.CircleCollider;
import com.example.mossymobile.MossFramework.Components.Renderers.Renderer;
import com.example.mossymobile.MossFramework.Components.RigidBody;
import com.example.mossymobile.MossFramework.DesignPatterns.MutableWrapper;
import com.example.mossymobile.MossFramework.GameObject;
import com.example.mossymobile.MossFramework.GameView;
import com.example.mossymobile.MossFramework.Math.Vector2;
import com.example.mossymobile.MossFramework.MonoBehaviour;
import com.example.mossymobile.MossFramework.Scene;
import com.example.mossymobile.MossFramework.Systems.Scenes.SceneLoadMode;
import com.example.mossymobile.MossFramework.Systems.Scenes.SceneManager;
import com.example.mossymobile.MossFramework.Systems.UserInput.UI;
import com.example.mossymobile.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GameScene extends Scene {
    Player playerScript;
    @Override
    protected void Init() {
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

        for (int i = 0; i < 0; i++) {
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

        GameObject player = new GameObject("Player");
        player.AddComponent(Renderer.class).ResourceID = R.drawable.cannon;
        playerScript = player.AddComponent(Player.class);
        player.AddComponent(RigidBody.class).SetGravityEnabled(false);
        BoxCollider playerHitbox = player.AddComponent(BoxCollider.class);
        playerHitbox.hitboxDimensions = new Vector2(20f, 20f);
        playerHitbox.SetCollisionLayer("Player");
        View viewUI = UI.GetInstance().AddLayoutToUI(R.layout.ui_game);
        {
            FrameLayout knob = viewUI.findViewById(R.id.joystick_region);

            GameObject joystick = new GameObject("MovementJoystick");
            JoystickKnob knobfunction = joystick.AddComponent(JoystickKnob.class);

            Renderer r = joystick.AddComponent(Renderer.class);
            r.ResourceID = R.drawable.bluecircle;
            r.SetZLayer(10);
            knobfunction.knob = viewUI.findViewById(R.id.joystick_knob);
            knobfunction.minDistance = 5f;
            playerScript.movement = knobfunction;
        }
        {
            FrameLayout knob = viewUI.findViewById(R.id.joystick_region2);

            GameObject joystick = new GameObject("LookJoystick");
            JoystickKnob knobfunction = joystick.AddComponent(JoystickKnob.class);
            knobfunction.offset = new Vector2(
                    screenWidth - 170f,
                    screenHeight - 300f
            );

            Renderer r = joystick.AddComponent(Renderer.class);
            r.ResourceID = R.drawable.redcircle;
            r.SetZLayer(10);
            knobfunction.knob = viewUI.findViewById(R.id.joystick_knob2);
            knobfunction.resetDirection = false;
            knobfunction.minDistance = 50f;
            playerScript.look = knobfunction;
        }
        {
            Button upgradesBtn = viewUI.findViewById(R.id.upgrades_btn);
            upgradesBtn.setOnClickListener(v -> {
                SceneManager.LoadScene("UpgradeScene", SceneLoadMode.ADDITIVE);
            });
        }
        CannonManager.GetInstance().ScheduledCannonSwitch = true;
        //CannonManager.GetInstance().PlayerCannonLevel = 3;
        //playerScript.cannonInfo = CannonManager.GetInstance().FetchCannon(8);

        GameObject healthBar = new GameObject("HealthBar");
        BarMeter hpfunc = healthBar.AddComponent(BarMeter.class);
        hpfunc.resID = R.drawable.redsquare;
        hpfunc.valueRef = playerScript.Health;
        hpfunc.barLength = 1000;
        healthBar.GetTransform().SetPosition(new Vector2(screenWidth * 0.5f, 100));
        healthBar.GetTransform().SetScale(new Vector2(200, 50));

        GameObject ammoBar = new GameObject("AmmoBar");
        BarMeter ammofunc = ammoBar.AddComponent(BarMeter.class);
        ammofunc.resID = R.drawable.yellowsquare;
        ammofunc.valueRef = playerScript.Ammo;
        ammofunc.barLength = 1000;
        ammoBar.GetTransform().SetPosition(new Vector2(screenWidth * 0.5f, 150));
        ammoBar.GetTransform().SetScale(new Vector2(200, 50));

        GameObject expBar = new GameObject("ExpBar");
        BarMeter expfunc = expBar.AddComponent(BarMeter.class);
        expfunc.resID = R.drawable.bluesquare;
        expfunc.valueRef = playerScript.Exp;
        expfunc.barLength = 1000;
        expfunc.startsAtZero = true;
        expfunc.maximumValue = 100f;
        expBar.GetTransform().SetPosition(new Vector2(screenWidth * 0.5f, 200));
        expBar.GetTransform().SetScale(new Vector2(200, 50));

        GameObject wallSpawner = new GameObject("WallSpawner");
        wallSpawner.AddComponent(WallSpawner.class);

        GameObject waveSpawner = new GameObject("WaveSpawner");
        waveSpawner.AddComponent(EnemySpawner.class).player = playerScript;
    }

    @Override
    protected void Update() {
        if (CannonManager.GetInstance().ScheduledCannonSwitch)
        {
            playerScript.cannonInfo = CannonManager.GetInstance().FetchCannon(CannonManager.GetInstance().PlayerCannonLevel);
            CannonManager.GetInstance().ScheduledCannonSwitch = false;
        }
        if (playerScript.Health.value <= 0)
        {
            SceneManager.LoadScene("MenuScene");
        }
    }

}