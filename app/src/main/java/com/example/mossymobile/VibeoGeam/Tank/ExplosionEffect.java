package com.example.mossymobile.VibeoGeam.Tank;

import com.example.mossymobile.MossFramework.Components.Renderers.Renderer;
import com.example.mossymobile.MossFramework.Math.Vector2;
import com.example.mossymobile.MossFramework.MonoBehaviour;
import com.example.mossymobile.MossFramework.Systems.Time.Time;
import com.example.mossymobile.R;

public class ExplosionEffect extends MonoBehaviour {
    private Vector2 finalScale = new Vector2(500f,500f);
    private float lifetime = 0.4f;
    @Override
    public void Start() {
        Renderer renderer = gameObject.AddComponent(Renderer.class);
        renderer.SetSprite(R.drawable.explosion);
        renderer.SetZLayer(5);
        GetTransform().SetScale(new Vector2(50,50));
    }

    @Override
    public void Update() {
        if (lifetime > 0f){
            lifetime -= Time.GetDeltaTime();
        }
        else{
            Destroy(gameObject);
        }
        GetTransform().SetScale(Vector2.Lerp(GetTransform().GetScale(), finalScale, 0.4f));
        GetTransform().SetRotation(GetTransform().GetRotation() + Time.GetDeltaTime() * 500f);
    }
}
