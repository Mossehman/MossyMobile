package com.example.mossymobile.MossFramework.Testing;

import com.example.mossymobile.MossFramework.Components.Colliders.BoxCollider;
import com.example.mossymobile.MossFramework.GameObject;
import com.example.mossymobile.MossFramework.Math.MossMath;
import com.example.mossymobile.MossFramework.Math.Vector2;
import com.example.mossymobile.MossFramework.Scene;

public class TestScene extends Scene {
    @Override
    protected void Init() {


        for (int i = 0; i < 200; i++)
        {
            GameObject go = new GameObject();
            BoxCollider col = go.AddComponent(BoxCollider.class);
            go.AddComponent(MovingScript.class);
            go.GetTransform().SetPosition(new Vector2(MossMath.randFloatMinMax(400, 2000), MossMath.randFloatMinMax(100, 900)));
            col.hitboxDimensions.x = 20;
            col.hitboxDimensions.y = 20;
        }
    }
}
