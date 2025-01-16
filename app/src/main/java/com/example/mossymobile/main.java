package com.example.mossymobile;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;


import com.example.mossymobile.MossFramework.Application;
import com.example.mossymobile.MossFramework.GameView;
import com.example.mossymobile.MossFramework.Systems.Debugging.BuildConfig;
import com.example.mossymobile.MossFramework.Systems.Debugging.Debug;
import com.example.mossymobile.MossFramework.Systems.Inspector.InspectorGUI;

public class main extends Activity {
    private final Application app = new Application();
    GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Debug.SetConfig(BuildConfig.DEBUG);
        this.setContentView(R.layout.mainscreen);

        gameView = findViewById(R.id.gameView);
        if (Debug.GetConfig() == BuildConfig.PRODUCTION)
        {
            View gameScreen = findViewById(R.id.game);
            LinearLayout.LayoutParams paramsGame = (LinearLayout.LayoutParams) gameScreen.getLayoutParams();

            paramsGame.weight = 0;
            gameScreen.setLayoutParams(paramsGame);

            View mainScreen = findViewById(R.id.gameScreen);
            LinearLayout.LayoutParams paramsMain = (LinearLayout.LayoutParams) mainScreen.getLayoutParams();

            paramsMain.weight = 0;
            mainScreen.setLayoutParams(paramsMain);

        }

        gameView.Init(this);

        InspectorGUI.GetInstance().AddLayoutComponent("LogPanel", findViewById(R.id.logDataPanel));

        app.Start();

        this.findViewById(R.id.closeBtn).setOnClickListener(v -> {
            Application.closeApplication = true;
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                app.Run();
                app.Exit();

                finish();
                System.exit(0);
            }
        }).start();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        app.Exit();
    }

    @Override
    protected void onStop() {
        super.onStop();
        app.Exit();
    }
}
