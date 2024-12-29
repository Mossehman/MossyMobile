package com.example.mossymobile.MossFramework;

import java.util.ArrayList;
import java.util.List;

public class Scene {

    protected List<GameObject> gameObjects = new ArrayList<>();

    protected int ScenePriority = 0;

    public void Init() { }
    public void Update() { }

    public void LateUpdate() {}

    public void Render() {}
    public void Exit() {}

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
