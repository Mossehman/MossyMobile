package com.example.mossymobile.VibeoGeam;

import com.example.mossymobile.MossFramework.Application;
import com.example.mossymobile.MossFramework.GameView;
import com.example.mossymobile.MossFramework.Systems.Audio.AudioManager;
import com.example.mossymobile.MossFramework.Systems.Physics.Collision;
import com.example.mossymobile.MossFramework.Systems.Scenes.SceneManager;
import com.example.mossymobile.MossFramework.Systems.ScriptableObjects.ScriptableObject;
import com.example.mossymobile.VibeoGeam.Scenes.GameScene;
import com.example.mossymobile.VibeoGeam.Scenes.MenuScene;
import com.example.mossymobile.VibeoGeam.Scenes.UpgradeScene;
import com.example.mossymobile.VibeoGeam.Tank.UpgradesManager;

public class GameApplication extends Application {

    public static boolean isResetting = false;
    @Override
    protected boolean OnStart(){
        SceneManager.AddToSceneList("MenuScene", new MenuScene());
        SceneManager.AddToSceneList("GameScene", new GameScene());
        SceneManager.AddToSceneList("UpgradeScene", new UpgradeScene());

        Collision.CreateCollisionLayer("Player");
        Collision.CreateCollisionLayer("PlayerBullet");
        Collision.CreateCollisionLayer("Enemy");
        Collision.CreateCollisionLayer("EnemyBullet");
        Collision.CreateCollisionLayer("Wall");
        Collision.CreateCollisionLayer("NoWallCollision");
        Collision.CreateCollisionLayer("PlayerNPC");

        Collision.ConfigureCollisionLayer("PlayerBullet", "EnemyBullet", false);
        Collision.ConfigureCollisionLayer("Enemy", "EnemyBullet", false);
        Collision.ConfigureCollisionLayer("Player", "PlayerBullet", false);
        Collision.ConfigureCollisionLayer("Player", "PlayerNPC", false);
        Collision.ConfigureCollisionLayer("NoWallCollision", "Wall", false);

        UpgradesManager.GetInstance().PopulateData();
        Leaderboard lb = ScriptableObject.Create("highscores", Leaderboard.class, true);
        AudioManager.initialize(GameView.GetInstance().GetContext());
        AudioManager.playMusic("main_menu", -1);
        SceneManager.LoadScene("MenuScene");
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
