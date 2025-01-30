package com.example.mossymobile.VibeoGeam;

import android.content.Context;
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
        //Application.pause = true;

        //View viewUI = UI.GetInstance().AddLayoutToUI(R.layout.ui_upgradeshop);
        LinearLayout uidocker = UI.GetInstance().GetUIContainer().findViewById(R.id.upgrades_ui);
        UI.GetInstance().AddLayoutToContainer(R.layout.ui_upgradeshop, uidocker);
        uidocker.removeAllViews();
        GridLayout upgradesGrid = uidocker.findViewById(R.id.upgrade_grid);

        Context context = uidocker.getContext();
        // Create a new ImageButton
        ImageButton twinBarrelsButton = new ImageButton(context);

        // Set layout parameters (width and height in dp)
        int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, context.getResources().getDisplayMetrics());
        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, context.getResources().getDisplayMetrics());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, height);

        // Set properties of the ImageButton
        twinBarrelsButton.setId(View.generateViewId());
        twinBarrelsButton.setLayoutParams(layoutParams);
        twinBarrelsButton.setBackgroundColor(ContextCompat.getColor(context, R.color.teal_200));
        twinBarrelsButton.setContentDescription("Twin Barrels");
        twinBarrelsButton.setScaleType(ImageView.ScaleType.CENTER_CROP);
        twinBarrelsButton.setImageResource(R.drawable.cannon1xx);

        //upgradesGrid.addView(twinBarrelsButton);
        //upgradesGrid.AddViewToContainer(twinBarrelsButton);
        UI.GetInstance().AddViewToContainer(twinBarrelsButton, upgradesGrid);
    }
}