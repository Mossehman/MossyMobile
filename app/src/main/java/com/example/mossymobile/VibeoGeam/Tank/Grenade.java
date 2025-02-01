package com.example.mossymobile.VibeoGeam.Tank;

import com.example.mossymobile.MossFramework.Components.Renderers.Renderer;
import com.example.mossymobile.MossFramework.GameObject;
import com.example.mossymobile.MossFramework.Math.Vector2;
import com.example.mossymobile.MossFramework.MonoBehaviour;
import com.example.mossymobile.MossFramework.Systems.Time.Time;
import com.example.mossymobile.R;
import com.example.mossymobile.VibeoGeam.Enemy.Enemy;
import com.example.mossymobile.VibeoGeam.Scenes.GameScene;

import java.util.List;

public class Grenade extends MonoBehaviour {
    private Renderer renderer;
    public Vector2 targetPosition;
    private Vector2 startPosition;
    private float initialDistance;
    private float lifetime = 2.f;
    @Override
    public void Start() {
        renderer = gameObject.AddComponent(Renderer.class);
        renderer.SetSprite(R.drawable.grenade);
        renderer.SetZLayer(5);
        GetTransform().SetScale(new Vector2(50,50));
        startPosition = GetTransform().GetPosition();
        initialDistance = startPosition.DistanceSq(targetPosition); // Store initial distance
    }

    @Override
    public void Update() {
        if (lifetime > 0f){
            lifetime -= Time.GetDeltaTime();
        }
        else{
            GameObject explosion = new GameObject();
            explosion.AddComponent(ExplosionEffect.class);
            explosion.GetTransform().SetPosition(GetTransform().GetPosition());

            List<GameObject> list = ((GameScene)gameObject.GetScene()).enemySpawner.OverlapCircle(GetTransform().GetPosition(), 200f);
            for ( var enemy : list) enemy.GetComponent(Enemy.class).ModifyHealth(50f);
            Destroy(gameObject);
        }
        GetTransform().SetPosition(Vector2.Lerp(GetTransform().GetPosition(), targetPosition, 0.05f));
        GetTransform().SetRotation(GetTransform().GetRotation() + Time.GetDeltaTime() * 50f);

        float currentDistance = GetTransform().GetPosition().DistanceSq(targetPosition);

        float progress = 1f - (currentDistance / initialDistance);
        progress = Math.max(0f, Math.min(1f, progress));

        float scaleFactor = (float) Math.sin(progress * Math.PI);

        float baseScale = 50f;
        float maxScale = 80f;

        float currentScale = baseScale + (maxScale - baseScale) * scaleFactor;
        GetTransform().SetScale(new Vector2(currentScale, currentScale));
    }
}

