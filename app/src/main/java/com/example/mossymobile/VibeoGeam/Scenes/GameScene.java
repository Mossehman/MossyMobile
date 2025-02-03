package com.example.mossymobile.VibeoGeam.Scenes;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.mossymobile.MossFramework.Components.Colliders.BoxCollider;
import com.example.mossymobile.MossFramework.Components.Renderers.Renderer;
import com.example.mossymobile.MossFramework.Components.RigidBody;
import com.example.mossymobile.MossFramework.GameObject;
import com.example.mossymobile.MossFramework.GameView;
import com.example.mossymobile.MossFramework.Math.Vector2;
import com.example.mossymobile.MossFramework.Scene;
import com.example.mossymobile.MossFramework.Systems.Audio.AudioManager;
import com.example.mossymobile.MossFramework.Systems.Scenes.SceneLoadMode;
import com.example.mossymobile.MossFramework.Systems.Scenes.SceneManager;
import com.example.mossymobile.MossFramework.Systems.ScriptableObjects.ScriptableObject;
import com.example.mossymobile.MossFramework.Systems.Time.Time;
import com.example.mossymobile.MossFramework.Systems.UserInput.UI;
import com.example.mossymobile.R;
import com.example.mossymobile.VibeoGeam.Enemy.EnemySpawner;
import com.example.mossymobile.VibeoGeam.GameApplication;
import com.example.mossymobile.VibeoGeam.ImprovedProgressBar;
import com.example.mossymobile.VibeoGeam.JoystickKnob;
import com.example.mossymobile.VibeoGeam.Leaderboard;
import com.example.mossymobile.VibeoGeam.Player;
import com.example.mossymobile.VibeoGeam.Tank.ActiveUpgrade;
import com.example.mossymobile.VibeoGeam.Tank.TankCosmetic;
import com.example.mossymobile.VibeoGeam.Tank.UpgradesManager;
import com.example.mossymobile.VibeoGeam.WallSpawner;

import java.util.Objects;

public class GameScene extends Scene {
    Player playerScript;
    boolean hasLost = false;
    View viewUI;
    GameObject abilityProgressbar;
    public EnemySpawner enemySpawner;
    public boolean hasActive = false;
    public boolean hasPassive = false;
    @Override
    protected void Init() {
        AudioManager.playMusic(GameView.GetInstance().GetContext(), R.raw.main1, true);

        float screenWidth = Objects.requireNonNull(GameView.GetInstance()).getWidth();
        float screenHeight = Objects.requireNonNull(GameView.GetInstance()).getHeight();

        hasActive = false;
        hasPassive = false;

        GameObject player = new GameObject("Player");
        player.AddComponent(Renderer.class).ResourceID = R.drawable.cannon;
        playerScript = player.AddComponent(Player.class);
        player.AddComponent(RigidBody.class).SetGravityEnabled(false);
        BoxCollider playerHitbox = player.AddComponent(BoxCollider.class);
        playerHitbox.hitboxDimensions = new Vector2(20f, 20f);
        playerHitbox.SetCollisionLayer("Player");

        viewUI = UI.GetInstance().AddLayoutToUI(R.layout.ui_game);
        InitalizeUI();
        {
            Button upgradesBtn = viewUI.findViewById(R.id.upgrades_enter_button);
            upgradesBtn.setOnClickListener(v -> {
                UI.GetInstance().GetUIContainer().setVisibility(View.GONE);
                SceneManager.LoadScene("UpgradeScene", SceneLoadMode.ADDITIVE);
            });
        }

        UpgradesManager.GetInstance().ScheduledCannonSwitch = true;
        UpgradesManager.GetInstance().Init(playerScript);

        GameObject wallSpawner = new GameObject("WallSpawner");
        wallSpawner.AddComponent(WallSpawner.class);

        GameObject waveSpawner = new GameObject("WaveSpawner");
        enemySpawner = waveSpawner.AddComponent(EnemySpawner.class);
        enemySpawner.player = playerScript;
    }

    @Override
    protected void Update() {
        if (UpgradesManager.GetInstance().ScheduledCannonSwitch) {
            playerScript.cannonInfo = UpgradesManager.GetInstance().FetchCannon(UpgradesManager.GetInstance().PlayerCannonLevel);
            UpgradesManager.GetInstance().ScheduledCannonSwitch = false;
        }
        if (playerScript.Health.value <= 0 && !hasLost) {
            DisplayScoreSubmission();
            GameApplication.isResetting = true;
            hasLost = true;
            AudioManager.playSound("playerdeath");
            AudioManager.playMusic(GameView.GetInstance().GetContext(), R.raw.lose, false);
        }

        if (UpgradesManager.GetInstance().PlayerActiveAbility >= 0) {
            ActiveUpgrade active = UpgradesManager.GetInstance().FetchActiveUpgrade(UpgradesManager.GetInstance().PlayerActiveAbility);
            if (active.cooldown.value <= active.maxcooldown)
                active.cooldown.value += Time.GetDeltaTime();
            else
                active.cooldown.value = active.maxcooldown;


            abilityProgressbar.GetComponent(ImprovedProgressBar.class)
                    .SetRef(active.cooldown)
                    .SetMax(active.maxcooldown);
        }

        GameView.GetInstance().GetActivity().runOnUiThread(() -> {
            View ensnared = UI.GetInstance().GetUIContainer().findViewById(R.id.ensnaredlayout);

            TextView debug = UI.GetInstance().GetUIContainer().findViewById(R.id.debug);
            debug.setText(enemySpawner.numOfEnemies.value.toString());
            if (playerScript.isEnsnared) ensnared.setVisibility(View.VISIBLE);
            else ensnared.setVisibility(View.INVISIBLE);

            if (UpgradesManager.GetInstance().PlayerActiveAbility >= 0) viewUI.findViewById(R.id.joystick_region3).setVisibility(View.VISIBLE);
        });

        if (UpgradesManager.GetInstance().PlayerActiveAbility >= 0 && !hasActive) {
            GameObject cosmetic = new GameObject();
            TankCosmetic cos = cosmetic.AddComponent(TankCosmetic.class);
            cos.attachedObject = playerScript.GetGameObject();
            switch (UpgradesManager.GetInstance().PlayerActiveAbility){
                case 0: cos.drawable = R.drawable.thruster; break;
                case 1: cos.drawable = R.drawable.backpack; break;
                case 2: cos.drawable = R.drawable.factory; cos.ZLayer = 6; break;
            }
            hasActive = true;
        }
        if (UpgradesManager.GetInstance().PlayerPassiveAbility >= 0 && !hasPassive) {
            GameObject cosmetic = new GameObject();
            TankCosmetic cos = cosmetic.AddComponent(TankCosmetic.class);
            cos.attachedObject = playerScript.GetGameObject();
            GameObject passive = UpgradesManager.GetInstance().FetchPassiveUpgrade(UpgradesManager.GetInstance().PlayerPassiveAbility).Initalize();
            switch (UpgradesManager.GetInstance().PlayerPassiveAbility){
                case 0: cos.drawable = R.drawable.reactor; break;
                case 1: cos.drawable = R.drawable.deployer; break;
                case 2: cos.drawable = R.drawable.turret; cos.ZLayer = 6; cos.attachedObject = passive; break;
            }
            hasPassive = true;
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
            if (userInput.isEmpty() || userInput.isEmpty()) return;
            Leaderboard lb = ScriptableObject.Create("highscores", Leaderboard.class, true);
            lb.Leaderboard.put(userInput, finalScore);
            lb.SaveToExternalStorage();
            hasLost = false;
            SceneManager.LoadScene("MenuScene");
        });
    }

    private void InitalizeUI()
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
            FrameLayout region = viewUI.findViewById(R.id.joystick_region3);
            ProgressBar view = viewUI.findViewById(R.id.circularProgressBar);
            GameView.GetInstance().GetActivity().runOnUiThread(() -> {
                        region.setVisibility(View.GONE);
                    });
            abilityProgressbar = new GameObject("AbilityProgress");
            abilityProgressbar.AddComponent(ImprovedProgressBar.class).SetProgressBar(view);
                    //.SetRef(UpgradesManager.GetInstance().FetchActiveUpgrade(UpgradesManager.GetInstance().PlayerActiveAbility).cooldown)
                    //.SetMax(UpgradesManager.GetInstance().FetchActiveUpgrade(UpgradesManager.GetInstance().PlayerActiveAbility).maxcooldown);

            ImageView abilityBtn = viewUI.findViewById(R.id.ability_btn);
            abilityBtn.setImageResource(R.drawable.purplecircle);
            abilityBtn.setOnClickListener(l -> {
                if (UpgradesManager.GetInstance().PlayerActiveAbility < 0) return;
                ActiveUpgrade active = UpgradesManager.GetInstance().FetchActiveUpgrade(UpgradesManager.GetInstance().PlayerActiveAbility);
                if (active.cooldown.value != active.maxcooldown) return;
                active.TriggerActive();
                active.cooldown.value = 0f;
            });
        }
        {
            View statbars = viewUI.findViewById(R.id.stat_bars);
            ProgressBar hp = viewUI.findViewById(R.id.healthbar);
            ProgressBar ammo = viewUI.findViewById(R.id.ammobar);
            ProgressBar exp = viewUI.findViewById(R.id.expbar);

            GameObject bar1 = new GameObject("Health");
            GameObject bar2 = new GameObject("Ammo");
            GameObject bar3 = new GameObject("Exp");

            bar1.AddComponent(ImprovedProgressBar.class).SetProgressBar(hp).SetRef(playerScript.Health).SetMax(UpgradesManager.GetInstance().FetchBaseUpgrade(2).GetCurrentMod());
            bar2.AddComponent(ImprovedProgressBar.class).SetProgressBar(ammo).SetRef(playerScript.Ammo).SetMax(100);
            bar3.AddComponent(ImprovedProgressBar.class).SetProgressBar(exp).SetRef(playerScript.Exp).SetMax(100);
        }
        GameView.GetInstance().GetActivity().runOnUiThread(() -> {
            ImageView effect = (UI.GetInstance().GetUIContainer().findViewById(R.id.ensnaredlayout)).findViewById(R.id.ensnaredeffect);
            effect.setImageResource(R.drawable.ensnared);
        });
    }
}