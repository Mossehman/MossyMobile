package com.example.mossymobile.VibeoGeam;

import com.example.mossymobile.MossFramework.GameObject;
import com.example.mossymobile.MossFramework.Math.Vector2;
import com.example.mossymobile.MossFramework.MonoBehaviour;

public class EnemySpawner extends MonoBehaviour {
    public GameObject player;
    GameObject YellowCube;

    @Override
    public void Start() {
        YellowCube = new GameObject();
        EnemyYellowCube Cube = YellowCube.AddComponent(EnemyYellowCube.class);
        Cube.player = player;
        Instantiate(YellowCube).GetTransform().SetPosition(new Vector2(500, 500));

    }

    @Override
    public void Update() {

    }
}
