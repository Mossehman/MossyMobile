package com.example.mossymobile.VibeoGeam;

import android.widget.Button;

import com.example.mossymobile.MossFramework.Components.Colliders.BoxCollider;
import com.example.mossymobile.MossFramework.Components.RigidBody;
import com.example.mossymobile.MossFramework.GameObject;
import com.example.mossymobile.MossFramework.GameView;
import com.example.mossymobile.MossFramework.Math.MossMath;
import com.example.mossymobile.MossFramework.Math.Vector2;
import com.example.mossymobile.MossFramework.Scene;
import com.example.mossymobile.R;

public class GameScene extends Scene {
    @Override
    protected void Init() {
        //GameObject go = new GameObject();
        //BoxCollider col = go.AddComponent(BoxCollider.class);

        //RigidBody rb = go.AddComponent(RigidBody.class);
        //go.AddComponent(PlayerController.class);
        //rb.SetMass(5);
        //go.GetTransform().SetPosition(new Vector2(1200, 800));

        //col.hitboxDimensions.x = 400;
        //col.hitboxDimensions.y = 20;
        //rb.SetGravityEnabled(false);
        //rb.SetKinematic(true);

        Button joystickKnob = GameView.GetInstance().GetActivity().findViewById(R.id.joystick_knob);
    }
}