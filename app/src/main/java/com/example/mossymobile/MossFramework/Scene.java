package com.example.mossymobile.MossFramework;

import com.example.mossymobile.MossFramework.Components.Renderer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class Scene implements Serializable {
    protected List<GameObject> gameObjects = new ArrayList<>();

    protected List<GameObject> gameObjectsToAdd = new ArrayList<>();
    protected List<GameObject> gameObjectsToRemove = new ArrayList<>();
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
        gameObjectsToAdd.clear();
        gameObjectsToAdd = null;
    }

    public final void Start()
    {
        Init();
    }

    public final void Run()
    {
        if (!gameObjectsToAdd.isEmpty())
        {
            gameObjects.addAll(gameObjectsToAdd);
            gameObjectsToAdd.clear();
        }

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

        if (!objectsToRender.isEmpty()) {

            for (GameObject gameObject : objectsToRender) {
                Renderer renderer = gameObject.GetComponent(Renderer.class, true);
                if (renderer == null) {
                    continue;
                }
                renderer.Render(Objects.requireNonNull(GameView.GetInstance()).canvas);
            }
        }

        if (!gameObjectsToRemove.isEmpty())
        {
            for (GameObject go : gameObjectsToRemove)
            {
                gameObjects.remove(go);
            }
        }
    }

    public int GetScenePriority()
    {
        return ScenePriority;
    }

    public void AddGOToScene(GameObject gameObject)
    {
        this.gameObjectsToAdd.add(gameObject);
    }

    public List<GameObject> GetGameObjects()
    {
        return this.gameObjects;
    }
}
