package com.example.mossymobile.MossFramework.Testing;

import com.example.mossymobile.MossFramework.Components.Camera;
import com.example.mossymobile.MossFramework.Components.CircleCollider;
import com.example.mossymobile.MossFramework.Components.Collider;
import com.example.mossymobile.MossFramework.GameObject;
import com.example.mossymobile.MossFramework.Math.Vector2;
import com.example.mossymobile.MossFramework.Scene;

public class TestScene extends Scene {
    @Override
    protected void Init() {
        GameObject newGO = new GameObject();
        Camera camera = newGO.AddComponent(Camera.class);
        newGO.GetTransform().SetPosition(new Vector2(1, 0));

        GameObject anotherGO = new GameObject("Other GameObject");
        anotherGO.AddComponent(CircleCollider.class);
        anotherGO.GetTransform().SetPosition(new Vector2(-4, 0));

        camera.map.put(newGO.GetComponent(Collider.class, true), "aaa");
    }
}
