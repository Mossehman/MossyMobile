package com.example.mossymobile.MossFramework.Testing;

import com.example.mossymobile.MossFramework.Components.Colliders.BoxCollider;
import com.example.mossymobile.MossFramework.Components.RigidBody;
import com.example.mossymobile.MossFramework.GameObject;
import com.example.mossymobile.MossFramework.Math.MossMath;
import com.example.mossymobile.MossFramework.Math.Vector2;
import com.example.mossymobile.MossFramework.Scene;

public class TestScene extends Scene {
    @Override
    protected void Init() {
        GameObject go = new GameObject();
        BoxCollider col = go.AddComponent(BoxCollider.class);
        //col.Radius = 10;
        RigidBody rb = go.AddComponent(RigidBody.class);
        //go.AddComponent(MovingScript.class);
        rb.SetMass(5);
        go.GetTransform().SetPosition(new Vector2(1200, 800));

        col.hitboxDimensions.x = 400;
        col.hitboxDimensions.y = 20;
        rb.SetGravityEnabled(false);
        rb.SetKinematic(true);


        for (int i = 0; i < 2; i++)
        {
            GameObject otherGO = new GameObject();
            BoxCollider otherCol = otherGO.AddComponent(BoxCollider.class);
            RigidBody otherRb = otherGO.AddComponent(RigidBody.class);
            otherRb.SetMass(10);

            otherGO.GetTransform().SetPosition(new Vector2(1200, 300 - i * 300));
            otherCol.hitboxDimensions.x = 20;
            otherCol.hitboxDimensions.y = 20;
        }
    }
}
