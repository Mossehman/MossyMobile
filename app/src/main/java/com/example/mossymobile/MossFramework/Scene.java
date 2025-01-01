package com.example.mossymobile.MossFramework;

import com.example.mossymobile.MossFramework.Components.Renderer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class Scene {
    protected List<GameObject> gameObjects = new ArrayList<>();
    protected List<GameObject> objectsToRender = new ArrayList<>();

    protected int ScenePriority = 0;

    protected abstract void Init();
    protected void Update() {}

    protected void LateUpdate() {}

    protected void Render() {}
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
        for (GameObject gameObject : gameObjects)
        {
            gameObject.Init();
        }

        Update();

        for (GameObject gameObject : gameObjects)
        {
            gameObject.Update();

            if (gameObject.ToRender) {
                objectsToRender.add(gameObject);
            }
        }

        for (GameObject gameObject : gameObjects)
        {
            gameObject.LateUpdate();
        }

        LateUpdate();

        if (objectsToRender.isEmpty() || GameView.GetInstance() == null || GameView.GetInstance().canvas == null) { return; }

        for (GameObject gameObject : objectsToRender)
        {
            Renderer renderer = gameObject.GetComponent(Renderer.class, true);
            if (renderer == null) { continue; }
            renderer.Render(Objects.requireNonNull(GameView.GetInstance()).canvas);
        }
    }

    public int GetScenePriority()
    {
        return ScenePriority;
    }

    public void AddGOToScene(GameObject gameObject)
    {
        this.gameObjects.add(gameObject);
    }

    public List<GameObject> GetGameObjects()
    {
        return this.gameObjects;
    }
}
