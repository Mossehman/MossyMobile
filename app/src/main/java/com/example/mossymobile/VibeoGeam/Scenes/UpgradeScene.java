package com.example.mossymobile.VibeoGeam.Scenes;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.mossymobile.MossFramework.Application;
import com.example.mossymobile.MossFramework.GameView;
import com.example.mossymobile.MossFramework.Math.MossMath;
import com.example.mossymobile.MossFramework.Scene;
import com.example.mossymobile.MossFramework.Systems.Audio.AudioManager;
import com.example.mossymobile.MossFramework.Systems.Scenes.SceneManager;
import com.example.mossymobile.MossFramework.Systems.UserInput.UI;
import com.example.mossymobile.R;
import com.example.mossymobile.VibeoGeam.Tank.ActiveUpgrade;
import com.example.mossymobile.VibeoGeam.Tank.BasicUpgrade;
import com.example.mossymobile.VibeoGeam.Tank.CannonInfo;
import com.example.mossymobile.VibeoGeam.GameApplication;
import com.example.mossymobile.VibeoGeam.Tank.PassiveUpgrade;
import com.example.mossymobile.VibeoGeam.Tank.TankUpgrade;
import com.example.mossymobile.VibeoGeam.Tank.UpgradesManager;

public class UpgradeScene extends Scene {
    int selectedUpgrade = 0;
    int currentUpgradePath = 0;
    boolean canPurchase = false;
    LinearLayout uidocker;
    Context context;
    View ui;
    GridLayout upgradesGrid;
    LinearLayout upgradestatsdocker;
    void Reset(){
        selectedUpgrade = 0;
        currentUpgradePath = 0;
        canPurchase = false;
    }

    @Override
    protected void Init() {
        if (GameApplication.isResetting) Reset();
        AudioManager.playMusic(GameView.GetInstance().GetContext(), R.raw.shop, true);

        uidocker = UI.GetInstance().GetUIContainer().getRootView().findViewById(R.id.upgrades_ui);
        context = uidocker.getContext();
        ui = UI.GetInstance().AddLayoutToContainer(R.layout.ui_upgradeshop, uidocker);
        upgradesGrid = ui.findViewById(R.id.upgrade_grid);
        upgradestatsdocker = ui.findViewById(R.id.upgrade_stats);


        Button buyBtn = ui.findViewById(R.id.upgrade_button);

        selectedUpgrade = UpgradesManager.GetInstance().PlayerCannonLevel;
        if (selectedUpgrade > 0)
            currentUpgradePath = (selectedUpgrade <= 3) ? 1 : (selectedUpgrade <= 6) ? 2 : 3;

        buyBtn.setOnClickListener(l -> {
            if (selectedUpgrade != -1 && canPurchase) {
                if (selectedUpgrade < UpgradesManager.GetInstance().cannonData.size() &&
                        UpgradesManager.GetInstance().PlayerLevelPoints >= UpgradesManager.GetInstance().FetchCannon(selectedUpgrade).lvlcost)
                {
                    UpgradesManager.GetInstance().PlayerLevelPoints -= UpgradesManager.GetInstance().FetchCannon(selectedUpgrade).lvlcost;
                    UpgradesManager.GetInstance().PlayerCannonLevel = selectedUpgrade;
                    UpgradesManager.GetInstance().ScheduledCannonSwitch = true;

                    // Lock the upgrade path once a purchase is made
                    if (currentUpgradePath == 0) {
                        currentUpgradePath = (selectedUpgrade <= 3) ? 1 : (selectedUpgrade <= 6) ? 2 : 3;
                    }
                    canPurchase = false;
                    buyBtn.setText("Locked");
                    buyBtn.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.grey));
                    // Refresh UI to reflect locked paths and new upgrade state
                    InitializeUI();
                    AudioManager.playSound("upgrade",
                            ((selectedUpgrade <= 3) ? selectedUpgrade : (selectedUpgrade <= 6) ? selectedUpgrade - 3 : selectedUpgrade - 6) * 0.3f);
                }
                else {
                    int selectedUpgrade2 = selectedUpgrade - UpgradesManager.GetInstance().cannonData.size();
                    TankUpgrade upgrade = UpgradesManager.GetInstance().tankData.get(selectedUpgrade2);
                    TextView cannoncost = ui.findViewById(R.id.cannon_cost);
                    TextView upgradename = ui.findViewById(R.id.upgrade_name);
                    if (upgrade instanceof BasicUpgrade)
                    {
                        BasicUpgrade basicUpgrade = (BasicUpgrade) upgrade;

                        if (basicUpgrade.currentLvl < 5) {
                            int nextLevel = basicUpgrade.currentLvl + 1;

                            // Ensure nextLevel does not exceed the array bounds
                            if (nextLevel < basicUpgrade.lvlCosts.length) {
                                int cost = basicUpgrade.lvlCosts[nextLevel];

                                if (UpgradesManager.GetInstance().PlayerLevelPoints >= cost) {
                                    UpgradesManager.GetInstance().PlayerLevelPoints -= cost;
                                    basicUpgrade.currentLvl++;

                                    if (basicUpgrade.currentLvl < basicUpgrade.lvlCosts.length - 1) {
                                        cannoncost.setText("Costs " + basicUpgrade.lvlCosts[basicUpgrade.currentLvl + 1] + " Points");
                                    } else {
                                        cannoncost.setText("MAX Level");
                                        buyBtn.setText("Locked");
                                        buyBtn.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.grey));
                                    }

                                    upgradename.setText(basicUpgrade.upgradename + " Lvl " + basicUpgrade.currentLvl);

                                    // Refresh UI to reflect purchase
                                    InitializeUI(); RefreshBaseUpgradeStats();
                                    AudioManager.playSound("upgrade", basicUpgrade.currentLvl * 0.3f);
                                }
                            }
                        }
                    }
                    else if (upgrade instanceof ActiveUpgrade) {
                        if (UpgradesManager.GetInstance().PlayerActiveAbility >= 0) return;
                        ActiveUpgrade activeUpgrade = (ActiveUpgrade) upgrade;
                        int cost = activeUpgrade.cost;
                        if (UpgradesManager.GetInstance().PlayerLevelPoints >= cost) {
                            UpgradesManager.GetInstance().PlayerLevelPoints -= cost;
                            UpgradesManager.GetInstance().PlayerActiveAbility = selectedUpgrade2 - 3;

                            buyBtn.setText("Locked");
                            buyBtn.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.grey));
                            InitializeUI();
                            AudioManager.playSound("upgrade", 1.25f);
                        }
                    }
                    else if (upgrade instanceof PassiveUpgrade) {
                        if (UpgradesManager.GetInstance().PlayerPassiveAbility >= 0) return;
                        PassiveUpgrade passiveUpgrade = (PassiveUpgrade) upgrade;
                        int cost = passiveUpgrade.cost;
                        if (UpgradesManager.GetInstance().PlayerLevelPoints >= cost) {
                            UpgradesManager.GetInstance().PlayerLevelPoints -= cost;
                            UpgradesManager.GetInstance().PlayerPassiveAbility = selectedUpgrade2 - 6;

                            buyBtn.setText("Locked");
                            buyBtn.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.grey));
                            InitializeUI();
                            AudioManager.playSound("upgrade", 1.25f);
                        }
                    }
                }
            }
        });

        Button exitBtn = ui.findViewById(R.id.upgrade_exit_button);
        exitBtn.setOnClickListener(l ->{
            Application.pause = false;
            UI.GetInstance().GetUIContainer().setVisibility(View.VISIBLE);
            UI.GetInstance().RemoveViewsFromLayout(uidocker);
            AudioManager.playMusic(GameView.GetInstance().GetContext(), R.raw.main1, true);
            SceneManager.UnloadScene("UpgradeScene");
        });

        InitializeUI();
        Application.pause = true;
    }

    private void InitializeUI(){
        UI.GetInstance().RemoveViewsFromLayout(upgradesGrid);

        TextView points = ui.findViewById(R.id.player_points);
        points.setText(UpgradesManager.GetInstance().PlayerLevelPoints + " Available Points");
        TextView cannoncost = ui.findViewById(R.id.cannon_cost);
        TextView upgradename = ui.findViewById(R.id.upgrade_name);

        ImageView upgradeImagePreview = ui.findViewById(R.id.cannon);
        if (selectedUpgrade < UpgradesManager.GetInstance().cannonData.size()){
            CannonInfo cannonInfo = UpgradesManager.GetInstance().FetchCannon(UpgradesManager.GetInstance().PlayerCannonLevel);
            upgradeImagePreview.setImageResource(cannonInfo.spriteResourceID);
            upgradename.setText(cannonInfo.cannonname);
        }

        Button buyBtn = ui.findViewById(R.id.upgrade_button);

        for (int i = 1; i < UpgradesManager.GetInstance().cannonData.size(); i++) {
            CannonInfo cannonInfo = UpgradesManager.GetInstance().cannonData.get(i);
            // Create a new ImageButton
            ImageButton upgradeBtn = new ImageButton(context);

            // Set layout parameters (width and height in dp)
            int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, context.getResources().getDisplayMetrics());
            int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, context.getResources().getDisplayMetrics());
            GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();
            layoutParams.width = width;
            layoutParams.height = height;


            // Determine the upgrade path
            int upgradePath = (i <= 3) ? 1 : (i <= 6) ? 2 : 3;
            int prerequisiteUpgrade = (i % 3 == 1) ? 0 : i - 1;

            // Set background color based on path
            int colorId;
            switch (upgradePath) {
                case 1: colorId = R.color.teal_200; break;
                case 2: colorId = R.color.purple_200; break;
                case 3: colorId = R.color.orange_200; break;
                default: colorId = R.color.black; break;
            }

            upgradeBtn.setLayoutParams(layoutParams);
            upgradeBtn.setBackgroundColor(ContextCompat.getColor(context, colorId));
            upgradeBtn.setScaleType(ImageView.ScaleType.CENTER_CROP);
            upgradeBtn.setImageResource(cannonInfo.spriteResourceID);

            boolean isAvailable = (prerequisiteUpgrade == 0 || UpgradesManager.GetInstance().PlayerCannonLevel >= prerequisiteUpgrade) &&
                    (currentUpgradePath == 0 || currentUpgradePath == upgradePath);

            if (!isAvailable) upgradeBtn.setColorFilter(colorId, PorterDuff.Mode.MULTIPLY);
            int finalI = i;
            upgradeBtn.setOnClickListener(l -> {
                upgradeImagePreview.setImageResource(cannonInfo.spriteResourceID);
                if (isAvailable) {
                    selectedUpgrade = finalI;
                    canPurchase = finalI > UpgradesManager.GetInstance().PlayerCannonLevel;
                    upgradeImagePreview.setColorFilter(0);
                }
                else{
                    canPurchase = false;
                    upgradeImagePreview.setColorFilter(R.color.black, PorterDuff.Mode.MULTIPLY);
                }
                if (canPurchase) {
                    buyBtn.setText("Buy");
                    buyBtn.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.lime));
                } else {
                    buyBtn.setText("Locked");
                    buyBtn.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.grey));
                }
                cannoncost.setText("Costs " + UpgradesManager.GetInstance().FetchCannon(finalI).lvlcost + " Points");
                upgradename.setText(UpgradesManager.GetInstance().FetchCannon(finalI).cannonname);
                RefreshStats(finalI);
            });

            UI.GetInstance().AddViewToContainer(upgradeBtn, upgradesGrid);
        }

        for (int i = 0; i < UpgradesManager.GetInstance().tankData.size(); i++)
        {
            TankUpgrade tankUpgrade = UpgradesManager.GetInstance().tankData.get(i);

            ImageButton upgradeBtn = new ImageButton(context);
            // Set layout parameters (width and height in dp)
            int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, context.getResources().getDisplayMetrics());
            int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, context.getResources().getDisplayMetrics());
            GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();
            layoutParams.width = width;
            layoutParams.height = height;


            int upgradePath = (i <= 2) ? 1 : (i <= 5) ? 2 : 3;
            int colorId;
            switch (upgradePath) {
                case 1: colorId = R.color.teal_200; break;
                case 2: colorId = R.color.purple_200; break;
                case 3: colorId = R.color.orange_200; break;
                default: colorId = R.color.black; break;
            }

            upgradeBtn.setLayoutParams(layoutParams);
            upgradeBtn.setBackgroundColor(ContextCompat.getColor(context, colorId));
            upgradeBtn.setScaleType(ImageView.ScaleType.CENTER_CROP);
            upgradeBtn.setImageResource(tankUpgrade.spriteResourceID);

            TankUpgrade finalTankUpgrade = tankUpgrade;

            if (finalTankUpgrade instanceof ActiveUpgrade) {
                if (UpgradesManager.GetInstance().PlayerActiveAbility >= 0 && i != UpgradesManager.GetInstance().PlayerActiveAbility + 3)
                    upgradeBtn.setColorFilter(R.color.black, PorterDuff.Mode.MULTIPLY);
            }else if (finalTankUpgrade instanceof PassiveUpgrade){
                if (UpgradesManager.GetInstance().PlayerPassiveAbility >= 0 && i != UpgradesManager.GetInstance().PlayerPassiveAbility + 6)
                    upgradeBtn.setColorFilter(R.color.black, PorterDuff.Mode.MULTIPLY);
            }
            int finalI = i;
            upgradeBtn.setOnClickListener(l -> {
                upgradeImagePreview.setImageResource(tankUpgrade.spriteResourceID);
                upgradeImagePreview.setColorFilter(0);
                if (finalTankUpgrade instanceof BasicUpgrade) {
                    BasicUpgrade basicUpgrade = (BasicUpgrade)finalTankUpgrade;
                    if (basicUpgrade.currentLvl < 5) {
                        int nextCost = basicUpgrade.lvlCosts[basicUpgrade.currentLvl + 1];
                        cannoncost.setText("Costs " + nextCost + " Points");
                        canPurchase = (UpgradesManager.GetInstance().PlayerLevelPoints >= nextCost);
                    } else {
                        cannoncost.setText("MAX Level");
                        canPurchase = false;
                    }
                    upgradename.setText(tankUpgrade.upgradename + " Lvl " + basicUpgrade.currentLvl);
                    RefreshBaseUpgradeStats();
                } else if (finalTankUpgrade instanceof ActiveUpgrade) { // Single purchase upgrade
                    ActiveUpgrade activeUpgrade = (ActiveUpgrade)finalTankUpgrade;
                    cannoncost.setText("Costs " + activeUpgrade.cost + " Points"); // Example cost for non-base upgrades
                    canPurchase = UpgradesManager.GetInstance().PlayerActiveAbility < 0;
                    upgradename.setText(tankUpgrade.upgradename);
                    DisplayUpgradeDescription(tankUpgrade.upgradedescription);
                } else if (finalTankUpgrade instanceof PassiveUpgrade) { // Single purchase upgrade
                    PassiveUpgrade passiveUpgrade = (PassiveUpgrade)finalTankUpgrade;
                    cannoncost.setText("Costs " + passiveUpgrade.cost + " Points"); // Example cost for non-base upgrades
                    canPurchase = UpgradesManager.GetInstance().PlayerPassiveAbility < 0;
                    upgradename.setText(tankUpgrade.upgradename);
                    DisplayUpgradeDescription(tankUpgrade.upgradedescription);
                }
                if (canPurchase) {
                    buyBtn.setText("Buy");
                    buyBtn.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.lime));
                } else {
                    buyBtn.setText("Locked");
                    buyBtn.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.grey));
                }
                selectedUpgrade = finalI + UpgradesManager.GetInstance().cannonData.size();
            });

            UI.GetInstance().AddViewToContainer(upgradeBtn, upgradesGrid);
        }
    }

    private void RefreshStats(int i){
        UI.GetInstance().RemoveViewsFromLayout(upgradestatsdocker);
        // imagine feature creeping a shop ui
        CannonInfo cannonInfo = UpgradesManager.GetInstance().FetchCannon(i);

        AddStatToContainer("Damage", cannonInfo.damage * cannonInfo.numofpellets, 120);
        AddStatToContainer("Speed", cannonInfo.speed, 1200);
        AddStatToContainer("Pierce", cannonInfo.pierce, 15);
        AddStatToContainer("Spread", cannonInfo.spread * 10, 200);
        AddStatToContainer("Fire Rate", 1 / cannonInfo.fireinterval, 15);
        AddStatToContainer("Ammo Cost", cannonInfo.ammocost * 10, 500);
        AddStatToContainer("Aim Speed", cannonInfo.aimspeed * 100, 100);
    }

    private void RefreshBaseUpgradeStats()
    {
        UI.GetInstance().RemoveViewsFromLayout(upgradestatsdocker);
        AddStatToContainer("Regen Rate", UpgradesManager.GetInstance().FetchBaseUpgrade(0).GetCurrentMod() * 10f + 20f, 44f);
        AddStatToContainer("Reload Rate", UpgradesManager.GetInstance().FetchBaseUpgrade(1).GetCurrentMod() * 10f + 40f, 96f);
        AddStatToContainer("Maximum HP", UpgradesManager.GetInstance().FetchBaseUpgrade(2).GetCurrentMod(), 250f);
    }
    private void DisplayUpgradeDescription(String description)
    {
        UI.GetInstance().RemoveViewsFromLayout(upgradestatsdocker);
        LinearLayout statLayout = new LinearLayout(context);
        statLayout.setOrientation(LinearLayout.VERTICAL);
        statLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        TextView statText = new TextView(context);
        statText.setText(description);
        statText.setTextSize(18);
        statText.setTextColor(ContextCompat.getColor(context, R.color.black));
        statText.setTypeface(null, Typeface.BOLD);
        statText.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        statLayout.addView(statText);
        upgradestatsdocker.addView(statLayout);
    }

    private void AddStatToContainer(String statName, float value, float maxValue) {
        Context context = upgradestatsdocker.getContext();

        LinearLayout statLayout = new LinearLayout(context);
        statLayout.setOrientation(LinearLayout.VERTICAL);
        statLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        // Stat name and bar
        TextView statText = new TextView(context);
        statText.setText(statName);
        statText.setTextSize(18);
        statText.setTextColor(ContextCompat.getColor(context, R.color.black));
        statText.setTypeface(null, Typeface.BOLD);
        statText.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        ProgressBar statMeter = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
        statMeter.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        statMeter.setMax((int) maxValue);
        statMeter.setProgress((int) value);
        statMeter.setProgressDrawable(ContextCompat.getDrawable(context, R.drawable.custom_stat_bar));

        statLayout.addView(statText);
        statLayout.addView(statMeter);
        upgradestatsdocker.addView(statLayout);
    }
}