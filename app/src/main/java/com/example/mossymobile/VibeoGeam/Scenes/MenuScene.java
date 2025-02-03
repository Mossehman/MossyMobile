package com.example.mossymobile.VibeoGeam.Scenes;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.mossymobile.MossFramework.Application;
import com.example.mossymobile.MossFramework.GameView;
import com.example.mossymobile.MossFramework.Scene;
import com.example.mossymobile.MossFramework.Systems.Audio.AudioManager;
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
        AudioManager.playMusic(GameView.GetInstance().GetContext(), R.raw.title, true);

        UI.GetInstance().RemoveAllUI();
        View ui = UI.GetInstance().AddLayoutToUI(R.layout.mainmenu);
        Context context = ui.getContext();
        Button play = ui.findViewById(R.id.play_btn);
        play.setOnClickListener(l -> {
            UI.GetInstance().RemoveViewsFromLayout((LinearLayout) ui);
            SceneManager.LoadScene("GameScene");
        });
        Button exit = ui.findViewById(R.id.appexit_btn);
        exit.setOnClickListener(l -> {
            UI.GetInstance().RemoveViewsFromLayout((LinearLayout) ui);
            Application.closeApplication = true;
        });
        Leaderboard lb = ScriptableObject.Create("highscores", Leaderboard.class, true);
        LinearLayout lbLayout = ui.findViewById(R.id.leaderboard_list);
        UI.GetInstance().RemoveViewsFromLayout(lbLayout);

        List<Map.Entry<String, Float>> entryList = new ArrayList<>(lb.Leaderboard.entrySet());

        // Sort the list by the values (Float)
        entryList.sort((a, b) -> {
            float scoreA = a.getValue();
            float scoreB = b.getValue();
            return Float.compare(scoreB, scoreA);
        });
        int i = 1;
        for (Map.Entry<String, Float> entry : entryList) {
            LinearLayout container = new LinearLayout(ui.getContext());
            container.setOrientation(LinearLayout.HORIZONTAL);
            container.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            container.setBackgroundColor(ContextCompat.getColor(context, R.color.purple_200));
            LinearLayout.LayoutParams textLayoutParams = new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    0.3f
            );
            TextView place = new TextView(UI.GetInstance().GetUIContainer().getContext());
            TextView name = new TextView(UI.GetInstance().GetUIContainer().getContext());
            TextView score = new TextView(UI.GetInstance().GetUIContainer().getContext());
            place.setLayoutParams(textLayoutParams);
            name.setLayoutParams(textLayoutParams);
            score.setLayoutParams(textLayoutParams);
            place.setText(String.valueOf(i)); i++;
            name.setText(entry.getKey());
            score.setText(entry.getValue().toString());
            place.setTextColor(ContextCompat.getColor(context, R.color.white));
            name.setTextColor(ContextCompat.getColor(context, R.color.white));
            score.setTextColor(ContextCompat.getColor(context, R.color.white));
            container.addView(place);
            container.addView(name);
            container.addView(score);
            UI.GetInstance().AddViewToContainer(container, lbLayout);
        }
    }
}
