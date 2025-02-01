package com.example.mossymobile.VibeoGeam.Scenes;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.mossymobile.MossFramework.Components.Colliders.BoxCollider;
import com.example.mossymobile.MossFramework.Components.Renderers.Renderer;
import com.example.mossymobile.MossFramework.Components.RigidBody;
import com.example.mossymobile.MossFramework.GameObject;
import com.example.mossymobile.MossFramework.GameView;
import com.example.mossymobile.MossFramework.Math.Vector2;
import com.example.mossymobile.MossFramework.Scene;
import com.example.mossymobile.MossFramework.Systems.Scenes.SceneLoadMode;
import com.example.mossymobile.MossFramework.Systems.Scenes.SceneManager;
import com.example.mossymobile.MossFramework.Systems.ScriptableObjects.ScriptableObject;
import com.example.mossymobile.MossFramework.Systems.UserInput.UI;
import com.example.mossymobile.R;
import com.example.mossymobile.VibeoGeam.Enemy.EnemySpawner;
import com.example.mossymobile.VibeoGeam.GameApplication;
import com.example.mossymobile.VibeoGeam.ImprovedProgressBar;
import com.example.mossymobile.VibeoGeam.JoystickKnob;
import com.example.mossymobile.VibeoGeam.Leaderboard;
import com.example.mossymobile.VibeoGeam.Player;
import com.example.mossymobile.VibeoGeam.Tank.UpgradesManager;
import com.example.mossymobile.VibeoGeam.WallSpawner;

import java.util.Objects;

public class GameScene extends Scene {
    Player playerScript;
    boolean hasLost = false;
    @Override
    protected void Init() {
        float screenWidth = Objects.requireNonNull(GameView.GetInstance()).getWidth();
        float screenHeight = Objects.requireNonNull(GameView.GetInstance()).getHeight();

        GameObject player = new GameObject("Player");
        player.AddComponent(Renderer.class).ResourceID = R.drawable.cannon;
        playerScript = player.AddComponent(Player.class);
        player.AddComponent(RigidBody.class).SetGravityEnabled(false);
        BoxCollider playerHitbox = player.AddComponent(BoxCollider.class);
        playerHitbox.hitboxDimensions = new Vector2(20f, 20f);
        playerHitbox.SetCollisionLayer("Player");

        View viewUI = UI.GetInstance().AddLayoutToUI(R.layout.ui_game);
        InitalizeUI(viewUI);
        {
            Button upgradesBtn = viewUI.findViewById(R.id.upgrades_enter_button);
            upgradesBtn.setOnClickListener(v -> {
                UI.GetInstance().GetUIContainer().setVisibility(View.GONE);
                SceneManager.LoadScene("UpgradeScene", SceneLoadMode.ADDITIVE);
            });
        }

        UpgradesManager.GetInstance().ScheduledCannonSwitch = true;
        UpgradesManager.GetInstance().Init();

/*
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
*/
        GameObject wallSpawner = new GameObject("WallSpawner");
        wallSpawner.AddComponent(WallSpawner.class);

        GameObject waveSpawner = new GameObject("WaveSpawner");
        waveSpawner.AddComponent(EnemySpawner.class).player = playerScript;
    }

    @Override
    protected void Update() {
        if (UpgradesManager.GetInstance().ScheduledCannonSwitch)
        {
            playerScript.cannonInfo = UpgradesManager.GetInstance().FetchCannon(UpgradesManager.GetInstance().PlayerCannonLevel);
            UpgradesManager.GetInstance().ScheduledCannonSwitch = false;
        }
        if (playerScript.Health.value <= 0 && !hasLost)
        {
            //SceneManager.LoadScene("MenuScene");
            DisplayScoreSubmission();
            GameApplication.isResetting = true;
            hasLost = true;
        }

    }

    private void DisplayScoreSubmission(){
        View ui = UI.GetInstance().AddLayoutToUI(R.layout.gameover);
        TextView score = ui.findViewById(R.id.score_txt);
        float finalScore = playerScript.cumulativeExpEarned;
        playerScript.Destroy(playerScript.GetGameObject());
        score.setText("Final Score: " + finalScore);
        EditText usernameI = ui.findViewById(R.id.username_input);
        ui.findViewById(R.id.score_submit_btn).setOnClickListener(l -> {
            String userInput = usernameI.getText().toString();

            Leaderboard lb = ScriptableObject.Create("highscores", Leaderboard.class, true);
            lb.Leaderboard.put(userInput, finalScore);
            lb.SaveToExternalStorage();
            hasLost = false;
            SceneManager.LoadScene("MenuScene");
        });
    }

    private void InitalizeUI(View viewUI)
    {
        float screenWidth = Objects.requireNonNull(GameView.GetInstance()).getWidth();
        float screenHeight = Objects.requireNonNull(GameView.GetInstance()).getHeight();
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
            FrameLayout knob = viewUI.findViewById(R.id.joystick_region3);
            ProgressBar view = viewUI.findViewById(R.id.circularProgressBar);
            //GameObject abilityProgressbar = new GameObject("AbilityProgress");
            //abilityProgressbar.AddComponent(ImprovedProgressBar.class).SetProgressBar(view).SetRef(playerScript.Health).SetMax(UpgradesManager.GetInstance().FetchBaseUpgrade(2).GetCurrentMod());
        }
        {
            View statbars = viewUI.findViewById(R.id.stat_bars);
            ProgressBar hp = statbars.findViewById(R.id.healthbar);
            ProgressBar ammo = statbars.findViewById(R.id.ammobar);
            ProgressBar exp = statbars.findViewById(R.id.expbar);

            GameObject bar1 = new GameObject("Health");
            GameObject bar2 = new GameObject("Ammo");
            GameObject bar3 = new GameObject("Exp");

            bar1.AddComponent(ImprovedProgressBar.class).SetProgressBar(hp).SetRef(playerScript.Health).SetMax(UpgradesManager.GetInstance().FetchBaseUpgrade(2).GetCurrentMod());
            bar2.AddComponent(ImprovedProgressBar.class).SetProgressBar(ammo).SetRef(playerScript.Ammo).SetMax(100);
            bar3.AddComponent(ImprovedProgressBar.class).SetProgressBar(exp).SetRef(playerScript.Exp).SetMax(100);

        }
    }
}