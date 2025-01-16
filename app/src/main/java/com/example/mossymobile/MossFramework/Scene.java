package com.example.mossymobile.MossFramework;

import com.example.mossymobile.MossFramework.Components.Colliders.Collider;
import com.example.mossymobile.MossFramework.Components.Renderer;
import com.example.mossymobile.MossFramework.Components.RigidBody;
import com.example.mossymobile.MossFramework.Math.Vector2;
import com.example.mossymobile.MossFramework.Systems.Debugging.BuildConfig;
import com.example.mossymobile.MossFramework.Systems.Debugging.Debug;
import com.example.mossymobile.MossFramework.Systems.Physics.QuadTree;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class Scene implements Serializable {
    protected List<GameObject> gameObjects = new ArrayList<>();

    protected List<GameObject> gameObjectsToAdd = new ArrayList<>();
    protected List<GameObject> gameObjectsToRemove = new ArrayList<>();
    protected List<Renderer> objectsToRender = new ArrayList<>();
    protected List<Collider> collidersToCheck = new ArrayList<>();

    protected QuadTree tree;

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

        //construct the tree each frame
        tree = new QuadTree(2, new QuadTree.Rectangle(new Vector2(1200, 500), new Vector2(800, 400)));

        for (GameObject gameObject : gameObjects)
        {
            gameObject.Update();
        }

        for (GameObject gameObject : gameObjects)
        {
            gameObject.LateUpdate();
        }

        LateUpdate();

        if (!objectsToRender.isEmpty()) {

            for (Renderer renderer : objectsToRender) {
                if (renderer == null) {
                    continue;
                }
                renderer.Render(Objects.requireNonNull(GameView.GetInstance()).canvas);
            }

            objectsToRender.clear();
        }
        if (Debug.GetConfig() != BuildConfig.PRODUCTION) {

        }

        for (GameObject gameObject : gameObjects) {
            gameObject.Gizmos();
        }

        //tree.Render();

        if (!gameObjectsToRemove.isEmpty())
        {
            for (GameObject go : gameObjectsToRemove)
            {
                gameObjects.remove(go);
            }
        }

        tree = null;
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

    public void RenderGameObject(Renderer renderer)
    {
        objectsToRender.add(renderer);
    }

    public QuadTree GetTree()
    {
        return this.tree;
    }
}
