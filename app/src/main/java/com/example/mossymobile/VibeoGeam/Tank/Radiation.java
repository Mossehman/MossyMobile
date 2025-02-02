package com.example.mossymobile.VibeoGeam.Tank;

import com.example.mossymobile.MossFramework.Components.Renderers.Renderer;
import com.example.mossymobile.MossFramework.GameObject;
import com.example.mossymobile.MossFramework.Math.Vector2;
import com.example.mossymobile.MossFramework.MonoBehaviour;
import com.example.mossymobile.MossFramework.Systems.Time.Time;
import com.example.mossymobile.R;
import com.example.mossymobile.VibeoGeam.Enemy.Enemy;
import com.example.mossymobile.VibeoGeam.Enemy.EnemySpawner;
import com.example.mossymobile.VibeoGeam.Player;
import com.example.mossymobile.VibeoGeam.Scenes.GameScene;

import java.util.List;

public class Radiation extends MonoBehaviour {
    float radius = 300f;
    float damageinterval = 0.5f;
    EnemySpawner enemySpawner;
    public Player player;

    @Override
    public void Start() {
        Renderer renderer = gameObject.AddComponent(Renderer.class);
        renderer.SetZLayer(-1);
        renderer.SetSprite(R.drawable.aura);
        Pulse pulse = new GameObject().AddComponent(Pulse.class);
        pulse.positionref = gameObject;
        pulse.pulseinterval = damageinterval;
        enemySpawner = ((GameScene)gameObject.GetScene()).enemySpawner;
        GetTransform().SetScale(new Vector2(radius * 2.25f,radius * 2.25f));
    }

    @Override
    public void Update() {
        if (damageinterval > 0f)
            damageinterval -= Time.GetDeltaTime();
        else{
            damageinterval = 0.6f;
            List<GameObject> enemiesInRange = enemySpawner.OverlapCircle(GetTransform().GetPosition(), radius);
            for (var enemy : enemiesInRange) enemy.GetComponent(Enemy.class).ModifyHealth(3f);
        }

        GetTransform().SetPosition(player.GetTransform().GetPosition());
    }
}
