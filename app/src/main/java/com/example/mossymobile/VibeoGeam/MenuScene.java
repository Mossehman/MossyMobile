package com.example.mossymobile.VibeoGeam;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.mossymobile.MossFramework.Scene;
import com.example.mossymobile.MossFramework.Systems.Scenes.SceneManager;
import com.example.mossymobile.MossFramework.Systems.UserInput.UI;
import com.example.mossymobile.R;

public class MenuScene extends Scene {
    @Override
    protected void Init() {
        View ui = UI.GetInstance().AddLayoutToUI(R.layout.mainmenu);
        Button btn = ui.findViewById(R.id.play_btn);
        btn.setOnClickListener(l -> {
            UI.GetInstance().RemoveViewsFromLayout((LinearLayout) ui);
            SceneManager.LoadScene("GameScene");
        });
    }
}
