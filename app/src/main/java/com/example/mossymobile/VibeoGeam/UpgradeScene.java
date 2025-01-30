package com.example.mossymobile.VibeoGeam;

import android.content.Context;
import android.graphics.PorterDuff;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.mossymobile.MossFramework.Scene;
import com.example.mossymobile.MossFramework.Systems.Scenes.SceneManager;
import com.example.mossymobile.MossFramework.Systems.UserInput.UI;
import com.example.mossymobile.R;

public class UpgradeScene extends Scene {
    int selectedUpgrade = 0;
    int currentUpgradePath = 0;
    boolean canPurchase = false;
    LinearLayout uidocker;
    Context context;
    View ui;
    GridLayout upgradesGrid;

    @Override
    protected void Init() {
        uidocker = UI.GetInstance().GetUIContainer().findViewById(R.id.upgrades_ui);
        context = uidocker.getContext();
        ui = UI.GetInstance().AddLayoutToContainer(R.layout.ui_upgradeshop, uidocker);
        upgradesGrid = ui.findViewById(R.id.upgrade_grid);

        Button buyBtn = ui.findViewById(R.id.upgrade_button);

        selectedUpgrade = CannonManager.GetInstance().PlayerCannonLevel;
        if (selectedUpgrade > 0)
            currentUpgradePath = (selectedUpgrade <= 3) ? 1 : (selectedUpgrade <= 6) ? 2 : 3;



        buyBtn.setOnClickListener(l -> {
            if (selectedUpgrade != -1 && canPurchase &&
                CannonManager.GetInstance().PlayerLevelPoints >= CannonManager.GetInstance().FetchCannon(selectedUpgrade).lvlcost) {
                CannonManager.GetInstance().PlayerLevelPoints -= CannonManager.GetInstance().FetchCannon(selectedUpgrade).lvlcost;
                CannonManager.GetInstance().PlayerCannonLevel = selectedUpgrade;
                CannonManager.GetInstance().ScheduledCannonSwitch = true;

                // Lock the upgrade path once a purchase is made
                if (currentUpgradePath == 0) {
                    currentUpgradePath = (selectedUpgrade <= 3) ? 1 : (selectedUpgrade <= 6) ? 2 : 3;
                }
                canPurchase = false;
                // Refresh UI to reflect locked paths and new upgrade state
                InitalizeUI();
            }
        });

        Button exitBtn = ui.findViewById(R.id.upgrade_exit_button);
        exitBtn.setOnClickListener(l ->{
            UI.GetInstance().RemoveViewsFromLayout(uidocker);
            SceneManager.UnloadScene("UpgradeScene");
        });

        InitalizeUI();
    }

    private void InitalizeUI(){
        UI.GetInstance().RemoveViewsFromLayout(upgradesGrid);

        TextView points = ui.findViewById(R.id.player_points);
        points.setText(CannonManager.GetInstance().PlayerLevelPoints + " Available Points");

        ImageView cannonImagePreview = ui.findViewById(R.id.cannon);
        cannonImagePreview.setImageResource(CannonManager.GetInstance().FetchCannon(CannonManager.GetInstance().PlayerCannonLevel).spriteResourceID);

        Button buyBtn = ui.findViewById(R.id.upgrade_button);

        for (int i = 1; i < CannonManager.GetInstance().cannonData.size(); i++) {
            CannonInfo cannonInfo = CannonManager.GetInstance().cannonData.get(i);
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

            // Set properties of the ImageButton
            upgradeBtn.setLayoutParams(layoutParams);
            upgradeBtn.setBackgroundColor(ContextCompat.getColor(context, colorId));
            upgradeBtn.setScaleType(ImageView.ScaleType.CENTER_CROP);
            upgradeBtn.setImageResource(cannonInfo.spriteResourceID);
            // Determine if upgrade is locked
            //boolean isUnlocked = (prerequisiteUpgrade == 0 || CannonManager.GetInstance().PlayerCannonLevel >= prerequisiteUpgrade);
            //boolean isCorrectPath = (currentUpgradePath == 0 || currentUpgradePath == upgradePath);
            boolean isAvailable = (prerequisiteUpgrade == 0 || CannonManager.GetInstance().PlayerCannonLevel >= prerequisiteUpgrade) &&
                    (currentUpgradePath == 0 || currentUpgradePath == upgradePath);
            //if (!isUnlocked || !isCorrectPath) {
            //    upgradeBtn.setColorFilter(colorId, PorterDuff.Mode.MULTIPLY); // Tint unavailable upgrades
            //upgradeBtn.setEnabled(false);
            //}

            if (!isAvailable) upgradeBtn.setColorFilter(colorId, PorterDuff.Mode.MULTIPLY);
            int finalI = i;
            upgradeBtn.setOnClickListener(l -> {
                cannonImagePreview.setImageResource(cannonInfo.spriteResourceID);
                if (isAvailable) {
                    selectedUpgrade = finalI;
                    canPurchase = finalI > CannonManager.GetInstance().PlayerCannonLevel;
                    cannonImagePreview.setColorFilter(0);
                }
                else{
                    canPurchase = false;
                    cannonImagePreview.setColorFilter(R.color.black, PorterDuff.Mode.MULTIPLY);
                }
                if (canPurchase) buyBtn.setText("Buy");
                else buyBtn.setText("Locked");
            });

            UI.GetInstance().AddViewToContainer(upgradeBtn, upgradesGrid);
        }
    }
}