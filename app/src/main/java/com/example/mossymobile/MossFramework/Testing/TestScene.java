package com.example.mossymobile.MossFramework.Testing;

import com.example.mossymobile.MossFramework.Components.AudioListener;
import com.example.mossymobile.MossFramework.Components.Camera;
import com.example.mossymobile.MossFramework.Components.Colliders.CircleCollider;
import com.example.mossymobile.MossFramework.GameObject;
import com.example.mossymobile.MossFramework.Math.Vector2;
import com.example.mossymobile.MossFramework.Scene;

public class TestScene extends Scene {
    @Override
    protected void Init() {
        GameObject newGO = new GameObject();
        Camera camera = newGO.AddComponent(Camera.class);
        newGO.GetTransform().SetPosition(new Vector2(0, 0));
        newGO.AddComponent(AudioListener.class);

        GameObject anotherGO = new GameObject("Other GameObject");
        anotherGO.AddComponent(CircleCollider.class);
        anotherGO.GetTransform().SetPosition(new Vector2(-4, 0));

        camera.go = anotherGO;
    }
}
