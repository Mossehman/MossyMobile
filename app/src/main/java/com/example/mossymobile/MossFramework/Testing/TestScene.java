package com.example.mossymobile.MossFramework.Testing;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.mossymobile.MossFramework.Components.Camera;
import com.example.mossymobile.MossFramework.Components.Colliders.BoxCollider;
import com.example.mossymobile.MossFramework.Components.Renderers.AnimatedRenderer;
import com.example.mossymobile.MossFramework.Components.Renderers.Renderer;
import com.example.mossymobile.MossFramework.Components.RigidBody;
import com.example.mossymobile.MossFramework.GameObject;
import com.example.mossymobile.MossFramework.Math.MossMath;
import com.example.mossymobile.MossFramework.Math.Vector2;
import com.example.mossymobile.MossFramework.Scene;
import com.example.mossymobile.MossFramework.Systems.Debugging.Debug;
import com.example.mossymobile.MossFramework.Systems.UserInput.UI;
import com.example.mossymobile.R;

public class TestScene extends Scene {
    @Override
    protected void Init() {
        //GameObject go = new GameObject();
        //BoxCollider col = go.AddComponent(BoxCollider.class);
        ////col.Radius = 10;
        //RigidBody rb = go.AddComponent(RigidBody.class);
        ////go.AddComponent(MovingScript.class);
        //rb.SetMass(5);
        //go.GetTransform().SetPosition(new Vector2(1200, 800));
//
        //col.hitboxDimensions.x = 400;
        //col.hitboxDimensions.y = 20;
        //rb.SetGravityEnabled(false);
        //rb.SetKinematic(true);
//
        //go.AddComponent(Camera.class);


        //for (int i = 0; i < 300; i++)
        //{
        //    GameObject otherGO = new GameObject();
        //    BoxCollider otherCol = otherGO.AddComponent(BoxCollider.class);
        //    //RigidBody otherRb = otherGO.AddComponent(RigidBody.class);
        //    //otherRb.SetMass(10);
        //    otherGO.AddComponent(MovingScript.class);
//
//
        //    otherGO.GetTransform().SetPosition(new Vector2(MossMath.randFloatMinMax(600, 1400), MossMath.randFloatMinMax(200, 900)));
        //    otherCol.hitboxDimensions.x = 20;
        //    otherCol.hitboxDimensions.y = 20;
        //}

        GameObject go = new GameObject();
        go.GetTransform().SetScale(new Vector2(300, 300));
        AnimatedRenderer renderer = go.AddComponent(AnimatedRenderer.class);
        renderer.SetSprite(R.drawable.baubau);
        renderer.SplitSprite(1, 5);
        renderer.CreateAnimation("Test", new AnimatedRenderer.AnimationData());
        go.GetTransform().SetPosition(new Vector2(1200, 400));
    }
}
