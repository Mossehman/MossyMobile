package com.example.mossymobile.MossFramework.Systems.Physics;

import com.example.mossymobile.MossFramework.Application;
import com.example.mossymobile.MossFramework.Components.Colliders.Collider;
import com.example.mossymobile.MossFramework.Components.RigidBody;
import com.example.mossymobile.MossFramework.GameObject;
import com.example.mossymobile.MossFramework.Math.Vector2;
import com.example.mossymobile.MossFramework.Systems.Debugging.BuildConfig;
import com.example.mossymobile.MossFramework.Systems.Debugging.Debug;
import com.example.mossymobile.MossFramework.Systems.Inspector.InspectorGUI;
import com.example.mossymobile.MossFramework.Systems.ScriptableObjects.ScriptableObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class Collision {
    final static String CollisionMatrixFilepath = "CollisionMatrix";
    private static CollisionMatrix mtx = null;
    public static LinkedHashMap<String, List<String>> CollisionLayers = new LinkedHashMap<>();
    public static void InitialiseCollisionMatrix()
    {
        mtx = ScriptableObject.Create(CollisionMatrixFilepath, "res", CollisionMatrix.class, true);
        if (mtx == null) { return; }


        for (Map.Entry<String, List<String>> set : CollisionLayers.entrySet())
        {
            String collisionLayer = set.getKey();
            List<String> layersToAvoid = set.getValue();

            if (!mtx.CollisionsLayers.containsKey(collisionLayer)) { continue; }
            if (Objects.requireNonNull(mtx.CollisionsLayers.get(collisionLayer)).isEmpty()) { continue; }

            layersToAvoid.addAll(mtx.CollisionsLayers.get(collisionLayer));
        }

        if (Debug.GetConfig() != BuildConfig.PRODUCTION)
        {
            InspectorGUI.GetInstance().CreateCollisionGUI("CollisionsTitlebar", "Collisions", CollisionLayers);
        }
    }

    public static void ConfigureCollisionLayer(String layer, String otherLayer, boolean canCollide)
    {
        if (!CollisionLayers.containsKey(layer) || !CollisionLayers.containsKey(otherLayer)) { return; }
        if (canCollide)
        {
            Objects.requireNonNull(CollisionLayers.get(layer)).add(otherLayer);
            Objects.requireNonNull(CollisionLayers.get(otherLayer)).add(layer);
        }
        else
        {
            Objects.requireNonNull(CollisionLayers.get(layer)).remove(otherLayer);
            Objects.requireNonNull(CollisionLayers.get(otherLayer)).remove(layer);
        }
    }


    public Collision(GameObject GO, Vector2 collisionNormal, Collider collider)
    {
        this.gameObject = GO;
        this.collisionNormal = collisionNormal;
        this.collider = collider;
    }

    public static void SaveCollisionMatrix()
    {
        mtx.CollisionsLayers = new HashMap<>(CollisionLayers);
    }

    public static void CreateCollisionLayer(String layer)
    {
        if (CollisionLayers.containsKey(layer) || Application.IsRunning()) { return; }
        CollisionLayers.put(layer, new ArrayList<>());
    }

    public static boolean CheckLayerExists(String layer)
    {
        return CollisionLayers.containsKey(layer);
    }


    private GameObject gameObject;
    private RigidBody rigidbody;
    private Vector2 collisionNormal;
    private Collider collider;

    public GameObject GetGameObject() {
        return this.gameObject;
    }

    public Vector2 GetCollisionNormal() {
        return this.collisionNormal;
    }

    public RigidBody GetRigidbody() {
        return this.rigidbody;
    }

    public Collider GetCollider() { return this.collider; }
}
