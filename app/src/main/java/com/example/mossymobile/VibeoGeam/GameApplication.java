package com.example.mossymobile.VibeoGeam;

import com.example.mossymobile.MossFramework.Application;
import com.example.mossymobile.MossFramework.Systems.Scenes.SceneManager;

public class GameApplication extends Application {
    @Override
    protected boolean OnStart(){
        SceneManager.AddToSceneList("GameScene", new GameScene());
        return true;
    }

    @Override
    protected void OnRun(){
        super.OnRun();
    }

    @Override
    protected void OnExit(){
        super.OnExit();
    }
}
