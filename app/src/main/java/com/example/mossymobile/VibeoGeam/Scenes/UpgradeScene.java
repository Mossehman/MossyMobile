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
import com.example.mossymobile.MossFramework.Scene;
import com.example.mossymobile.MossFramework.Systems.Scenes.SceneManager;
import com.example.mossymobile.MossFramework.Systems.UserInput.UI;
import com.example.mossymobile.R;
import com.example.mossymobile.VibeoGeam.Tank.BaseUpgrade;
import com.example.mossymobile.VibeoGeam.Tank.CannonInfo;
import com.example.mossymobile.VibeoGeam.GameApplication;
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
                }
                else {
                    int selectedUpgrade2 = selectedUpgrade - UpgradesManager.GetInstance().cannonData.size();
                    TankUpgrade upgrade = UpgradesManager.GetInstance().tankData.get(selectedUpgrade2);

                    if (upgrade instanceof BaseUpgrade)
                    {
                        BaseUpgrade baseUpgrade = (BaseUpgrade) upgrade;
                        TextView cannoncost = ui.findViewById(R.id.cannon_cost);
                        TextView upgradename = ui.findViewById(R.id.upgrade_name);
                        if (baseUpgrade.currentLvl < 5) {
                            int nextLevel = baseUpgrade.currentLvl + 1;

                            // Ensure nextLevel does not exceed the array bounds
                            if (nextLevel < baseUpgrade.lvlCosts.length) {
                                int cost = baseUpgrade.lvlCosts[nextLevel];

                                if (UpgradesManager.GetInstance().PlayerLevelPoints >= cost) {
                                    UpgradesManager.GetInstance().PlayerLevelPoints -= cost;
                                    baseUpgrade.currentLvl++;

                                    if (baseUpgrade.currentLvl < baseUpgrade.lvlCosts.length - 1) {
                                        cannoncost.setText("Costs " + baseUpgrade.lvlCosts[baseUpgrade.currentLvl + 1] + " Points");
                                    } else {
                                        cannoncost.setText("MAX Level");
                                        buyBtn.setText("Locked");
                                        buyBtn.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.grey));
                                    }

                                    upgradename.setText(baseUpgrade.upgradename + " Lvl " + baseUpgrade.currentLvl);

                                    // Refresh UI to reflect purchase
                                    InitializeUI(); RefreshBaseUpgradeStats();
                                }
                            }
                        }
                    }
                    else{
                        buyBtn.setText("Locked");
                        buyBtn.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.grey));
                    }
                }
            }
        });

        Button exitBtn = ui.findViewById(R.id.upgrade_exit_button);
        exitBtn.setOnClickListener(l ->{
            Application.pause = false;
            UI.GetInstance().GetUIContainer().setVisibility(View.VISIBLE);
            UI.GetInstance().RemoveViewsFromLayout(uidocker);
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


            BaseUpgrade baseUpgrade = null;
            int upgradePath = (i <= 2) ? 1 : (i <= 5) ? 2 : 3;
            int colorId;
            switch (upgradePath) {
                case 1: colorId = R.color.teal_200; baseUpgrade = (BaseUpgrade)tankUpgrade; break;
                case 2: colorId = R.color.purple_200; break;
                case 3: colorId = R.color.orange_200; break;
                default: colorId = R.color.black; break;
            }

            upgradeBtn.setLayoutParams(layoutParams);
            upgradeBtn.setBackgroundColor(ContextCompat.getColor(context, colorId));
            upgradeBtn.setScaleType(ImageView.ScaleType.CENTER_CROP);
            upgradeBtn.setImageResource(tankUpgrade.spriteResourceID);

            BaseUpgrade finalBaseUpgrade = baseUpgrade;
            int finalI = i;
            upgradeBtn.setOnClickListener(l -> {
                upgradeImagePreview.setImageResource(tankUpgrade.spriteResourceID);
                upgradeImagePreview.setColorFilter(0);
                if (finalBaseUpgrade != null) { // Check if it's a BaseUpgrade
                    if (finalBaseUpgrade.currentLvl < 4) {
                        int nextCost = finalBaseUpgrade.lvlCosts[finalBaseUpgrade.currentLvl + 1];
                        cannoncost.setText("Costs " + nextCost + " Points");
                        canPurchase = (UpgradesManager.GetInstance().PlayerLevelPoints >= nextCost);
                    } else {
                        cannoncost.setText("MAX Level");
                        canPurchase = false;
                    }
                    upgradename.setText(tankUpgrade.upgradename + " Lvl " + finalBaseUpgrade.currentLvl);
                    RefreshBaseUpgradeStats();
                } else { // Single purchase upgrade
                    cannoncost.setText("Costs " + 5 + " Points"); // Example cost for non-base upgrades
                    canPurchase = (UpgradesManager.GetInstance().PlayerLevelPoints >= 5);
                    upgradename.setText(tankUpgrade.upgradename);
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

        AddStatToContainer("Damage", cannonInfo.damage, 120);
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
        ;
        AddStatToContainer("Regen Rate", UpgradesManager.GetInstance().FetchBaseUpgrade(0).GetCurrentMod() * 10f + 20f, 44f);
        AddStatToContainer("Reload Rate", UpgradesManager.GetInstance().FetchBaseUpgrade(1).GetCurrentMod() * 10f + 40f, 66f);
        AddStatToContainer("Maximum HP", UpgradesManager.GetInstance().FetchBaseUpgrade(2).GetCurrentMod(), 250f);
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
        statText.setTextColor(R.color.black);
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