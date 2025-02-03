package com.example.mossymobile.VibeoGeam.Enemy;

import com.example.mossymobile.MossFramework.DesignPatterns.MutableWrapper;
import com.example.mossymobile.MossFramework.GameObject;
import com.example.mossymobile.MossFramework.GameView;
import com.example.mossymobile.MossFramework.Math.MossMath;
import com.example.mossymobile.MossFramework.Math.Vector2;
import com.example.mossymobile.MossFramework.MonoBehaviour;
import com.example.mossymobile.MossFramework.Systems.Time.Time;
import com.example.mossymobile.VibeoGeam.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class EnemySpawner extends MonoBehaviour {
    public Player player;
    List<GameObject> spawnedEnemies = new ArrayList<>();
    public MutableWrapper<Integer> numOfEnemies = new MutableWrapper<>(0);
    private float spawnInterval = 3f;
    private float spawnWaveTimer = 0f;
    private int spawnCountMax = 30;
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
                    if (MossMath.randIntMinMax(0,100) >= 10) {
                        spawnedEnemies.add(SpawnCube());
                    } else {
                        spawnedEnemies.add(SpawnTriangle());
                    }
                }
                spawnWaveTimer = spawnInterval;
            }
        }
    }

    private GameObject SpawnCube(){
        GameObject yellowCube = new GameObject();
        EnemyYellowCube Cube = yellowCube.AddComponent(EnemyYellowCube.class);
        Cube.player = player;
        Cube.numOfEnemies = numOfEnemies;
        while (true) {
            float x = MossMath.randFloatMinMax(0, screenWidth);
            float y = MossMath.randFloatMinMax(0, screenHeight);
            Vector2 spawnPos = new Vector2(x, y);
            if (player.GetTransform().GetPosition().DistanceSq(spawnPos) >= 400 * 400){
                yellowCube.GetTransform().SetPosition(spawnPos);
                break;
            }
        }
        numOfEnemies.value++;
        return yellowCube;
    }

    private GameObject SpawnTriangle(){
        GameObject purpleTriangle = new GameObject();
        EnemyPurpleTriangle Triangle = purpleTriangle.AddComponent(EnemyPurpleTriangle.class);
        Triangle.player = player;
        Triangle.numOfEnemies = numOfEnemies;
        while (true) {
            float x = MossMath.randFloatMinMax(0, screenWidth);
            float y = MossMath.randFloatMinMax(0, screenHeight);
            Vector2 spawnPos = new Vector2(x, y);
            if (player.GetTransform().GetPosition().DistanceSq(spawnPos) >= 400 * 400){
                purpleTriangle.GetTransform().SetPosition(spawnPos);
                break;
            }
        }
        numOfEnemies.value++;
        return purpleTriangle;
    }

    public List<GameObject> OverlapCircle(Vector2 position, float radius) {
        List<GameObject> ObjectsInRange = new ArrayList<>();
        for (GameObject enemy : spawnedEnemies) {
            if (enemy.GetTransform().GetPosition().DistanceSq(position) <= radius * radius)
                ObjectsInRange.add(enemy);
        }
        // sort list by closest to position
        ObjectsInRange.sort((a, b) -> {
            float distA = a.GetTransform().GetPosition().DistanceSq(position);
            float distB = b.GetTransform().GetPosition().DistanceSq(position);
            return Float.compare(distA, distB);
        });
        return ObjectsInRange;
    }
}