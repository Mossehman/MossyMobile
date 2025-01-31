package com.example.mossymobile.VibeoGeam;

import android.util.Log;

import com.example.mossymobile.MossFramework.DesignPatterns.MutableWrapper;
import com.example.mossymobile.MossFramework.GameObject;
import com.example.mossymobile.MossFramework.GameView;
import com.example.mossymobile.MossFramework.Math.MossMath;
import com.example.mossymobile.MossFramework.Math.Vector2;
import com.example.mossymobile.MossFramework.MonoBehaviour;
import com.example.mossymobile.MossFramework.Systems.Time.Time;

import java.util.Objects;

public class EnemySpawner extends MonoBehaviour {
    public Player player;
    GameObject YellowCube;
    private MutableWrapper<Integer> numOfEnemies = new MutableWrapper<>(0);
    private float spawnInterval = 3f;
    private float spawnWaveTimer = 0f;
    private int spawnCountMax = 50;
    private int spawnWaveAmt = 5;

    float screenWidth = Objects.requireNonNull(GameView.GetInstance()).getWidth();
    float screenHeight = Objects.requireNonNull(GameView.GetInstance()).getHeight();

    @Override
    public void Start() {
        spawnWaveTimer = spawnInterval;
    }

    @Override
    public void Update() {
        if (numOfEnemies.value <= spawnCountMax) {
            if (spawnWaveTimer >= 0f){
                spawnWaveTimer -= Time.GetDeltaTime();
            }
            else{
                int amtToSpawn = MossMath.clamp(numOfEnemies.value + spawnWaveAmt, 1, spawnCountMax) - numOfEnemies.value;
                for (int i = 0; i < amtToSpawn; i++) {
                    SpawnCube();
                }
                spawnWaveTimer = spawnInterval;
            }
        }
    }

    private void SpawnCube(){
        YellowCube = new GameObject();
        EnemyYellowCube Cube = YellowCube.AddComponent(EnemyYellowCube.class);
        Cube.player = player;
        Cube.numOfEnemies = numOfEnemies;
        while (true) {
            float x = MossMath.randFloatMinMax(0, screenWidth);
            float y = MossMath.randFloatMinMax(0, screenHeight);
            Vector2 spawnPos = new Vector2(x, y);
            if (player.GetTransform().GetPosition().Distance(spawnPos) >= 50){
                Instantiate(YellowCube).GetTransform().SetPosition(spawnPos);
                break;
            }
        }
        numOfEnemies.value++;
    }
}
