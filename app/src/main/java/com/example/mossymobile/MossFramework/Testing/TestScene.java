package com.example.mossymobile.MossFramework.Testing;

import com.example.mossymobile.MossFramework.Components.Camera;
import com.example.mossymobile.MossFramework.GameObject;
import com.example.mossymobile.MossFramework.Math.Vector2;
import com.example.mossymobile.MossFramework.Scene;

public class TestScene extends Scene {
    @Override
    protected void Init() {
        GameObject newGO = new GameObject();
        Camera camera = newGO.AddComponent(Camera.class);
        newGO.AddComponent(Camera.class);
        camera.a = 10.0f;

        newGO.GetTransform().SetPosition(new Vector2(1, 0));

        GameObject anotherGO = new GameObject("Other GameObject");
        GameObject GO2 = new GameObject("Other GameObject 1");
        GameObject GO3 = new GameObject("Other GameObject 2");
        GameObject GO4 = new GameObject("Other GameObject 3");
    }
}
