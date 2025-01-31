package com.example.mossymobile.VibeoGeam.Scenes;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mossymobile.MossFramework.Scene;
import com.example.mossymobile.MossFramework.Systems.Scenes.SceneManager;
import com.example.mossymobile.MossFramework.Systems.ScriptableObjects.ScriptableObject;
import com.example.mossymobile.MossFramework.Systems.UserInput.UI;
import com.example.mossymobile.R;
import com.example.mossymobile.VibeoGeam.Leaderboard;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MenuScene extends Scene {
    @Override
    protected void Init() {
        UI.GetInstance().RemoveAllUI();
        View ui = UI.GetInstance().AddLayoutToUI(R.layout.mainmenu);
        Button btn = ui.findViewById(R.id.play_btn);
        btn.setOnClickListener(l -> {
            UI.GetInstance().RemoveViewsFromLayout((LinearLayout) ui);
            SceneManager.LoadScene("GameScene");
        });
        Leaderboard lb = ScriptableObject.Create("highscores", Leaderboard.class, true);
        LinearLayout lbLayout = ui.findViewById(R.id.leaderboard_list);
        UI.GetInstance().RemoveViewsFromLayout(lbLayout);

        List<Map.Entry<String, Float>> entryList = new ArrayList<>(lb.Leaderboard.entrySet());

        // Sort the list by the values (Float)
        entryList.sort(Map.Entry.comparingByValue());

        for (Map.Entry<String, Float> entry : entryList) {
            //System.out.println(entry.getKey() + " = " + entry.getValue());
            TextView ent = new TextView(UI.GetInstance().GetUIContainer().getContext());
            ent.setText(entry.getKey() + " | " + entry.getValue());

            UI.GetInstance().AddViewToContainer(ent, lbLayout);
        }
    }
}
