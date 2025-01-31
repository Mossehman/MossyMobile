package com.example.mossymobile.VibeoGeam;

import com.example.mossymobile.MossFramework.Application;
import com.example.mossymobile.MossFramework.Systems.Physics.Collision;
import com.example.mossymobile.MossFramework.Systems.Scenes.SceneManager;
import com.example.mossymobile.MossFramework.Testing.TestScene;

public class GameApplication extends Application {
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

        Collision.ConfigureCollisionLayer("PlayerBullet", "EnemyBullet", false);
        Collision.ConfigureCollisionLayer("Enemy", "EnemyBullet", false);
        Collision.ConfigureCollisionLayer("Player", "PlayerBullet", false);
        Collision.ConfigureCollisionLayer("NoWallCollision", "Wall", false);

        CannonManager.GetInstance().PopulateData();

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
