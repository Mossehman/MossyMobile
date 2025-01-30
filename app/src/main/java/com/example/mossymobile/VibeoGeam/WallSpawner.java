package com.example.mossymobile.VibeoGeam;

import com.example.mossymobile.MossFramework.Components.Colliders.BoxCollider;
import com.example.mossymobile.MossFramework.Components.Renderers.Renderer;
import com.example.mossymobile.MossFramework.Components.RigidBody;
import com.example.mossymobile.MossFramework.GameObject;
import com.example.mossymobile.MossFramework.GameView;
import com.example.mossymobile.MossFramework.Math.MossMath;
import com.example.mossymobile.MossFramework.Math.Vector2;
import com.example.mossymobile.MossFramework.MonoBehaviour;
import com.example.mossymobile.MossFramework.Systems.Time.Time;
import com.example.mossymobile.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WallSpawner extends MonoBehaviour {
    private float NewWallsTimer;
    private float NewWallsInterval = 30f;
    private List<GameObject> Walls = new ArrayList<>();

    float screenWidth = Objects.requireNonNull(GameView.GetInstance()).getWidth();
    float screenHeight = Objects.requireNonNull(GameView.GetInstance()).getHeight();
    @Override
    public void Start() {
        NewWallsTimer = NewWallsInterval;
        GenerateWalls();
    }

    @Override
    public void Update() {
        if (NewWallsTimer > 0){
            NewWallsTimer -= Time.GetDeltaTime();
        }
        else {
            NewWallsTimer = NewWallsInterval;
            DestroyWalls();
            GenerateWalls();
        }
    }

    private void GenerateWalls(){
        for (int i = 0; i < 5; i++){
            float x = MossMath.randFloatMinMax(0f, screenWidth);
            float y = MossMath.randFloatMinMax(0f, screenHeight);
            float w = MossMath.randFloatMinMax(20f, 500f);
            float h = MossMath.randFloatMinMax(20f, 500f);
            GameObject wall = new GameObject();
            wall.GetTransform().SetPosition(new Vector2(x, y));
            wall.GetTransform().SetScale(Vector2.Mul(new Vector2(w, h), 1.5f));
            BoxCollider col = wall.AddComponent(BoxCollider.class);
            col.hitboxDimensions = new Vector2(w * 0.5f,h* 0.5f);
            col.SetCollisionLayer("Wall");
            RigidBody rb = wall.AddComponent(RigidBody.class);
            rb.SetGravityEnabled(false);
            rb.SetKinematic(true);
            wall.AddComponent(Renderer.class).ResourceID = R.drawable.lightgreysquare;

            Walls.add(wall);
        }
    }

    private void DestroyWalls(){
        for (var w : Walls) {
            Destroy(w);
        }
        Walls.clear();
    }
}
