package com.example.mossymobile.MossFramework;

import java.util.ArrayList;
import java.util.List;

public class Scene {

    protected List<GameObject> gameObjects = new ArrayList<>();

    protected int ScenePriority = 0;

    protected void Init() { }
    protected void Update() {
        for (GameObject gameObject : gameObjects)
        {
            gameObject.Update();
        }
    }

    protected void LateUpdate() {
        for (GameObject gameObject : gameObjects)
        {
            gameObject.LateUpdate();
        }
    }

    public void Render() {}
    public final void Exit() {
        for (GameObject gameObject : gameObjects)
        {
            gameObject.OnDestroy();
        }

        gameObjects.clear();
        gameObjects = null;
    }

    public final void Start()
    {
        Init();
    }

    public final void Run()
    {

    }

    public int GetScenePriority()
    {
        return ScenePriority;
    }
}
