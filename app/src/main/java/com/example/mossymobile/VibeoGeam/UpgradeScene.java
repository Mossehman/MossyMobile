package com.example.mossymobile.VibeoGeam;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.core.content.ContextCompat;

import com.example.mossymobile.MossFramework.Scene;
import com.example.mossymobile.MossFramework.Systems.UserInput.UI;
import com.example.mossymobile.R;

public class UpgradeScene extends Scene {
    @Override
    protected void Init() {
        LinearLayout uidocker = UI.GetInstance().GetUIContainer().findViewById(R.id.upgrades_ui);
        Context context = uidocker.getContext();
        //if (uidocker != null) Log.d("UpgradeScene", "UI Docker found");
        View ui = UI.GetInstance().AddLayoutToContainer(R.layout.ui_upgradeshop, uidocker);
        GridLayout upgradesGrid = ui.findViewById(R.id.upgrade_grid);
        //if (upgradesGrid != null) Log.d("UpgradeScene", "Upgrade Grid found");
        UI.GetInstance().RemoveViewsFromLayout(upgradesGrid);

        CannonManager.GetInstance().cannonData.size();
        for (int i = 0; i < 15; i++) {
            // Create a new ImageButton
            ImageButton twinBarrelsButton = new ImageButton(context);

            // Set layout parameters (width and height in dp)
            int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, context.getResources().getDisplayMetrics());
            int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, context.getResources().getDisplayMetrics());
            GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();
            layoutParams.width = width;
            layoutParams.height = height;

            // Set properties of the ImageButton
            twinBarrelsButton.setLayoutParams(layoutParams);
            twinBarrelsButton.setBackgroundColor(ContextCompat.getColor(context, R.color.teal_200));
            twinBarrelsButton.setScaleType(ImageView.ScaleType.CENTER_CROP);
            twinBarrelsButton.setImageResource(R.drawable.cannon1xx);
            UI.GetInstance().AddViewToContainer(twinBarrelsButton, upgradesGrid);
        }
    }
}