package com.example.mossymobile.MossFramework.Testing;

import com.example.mossymobile.MossFramework.Components.Transform;
import com.example.mossymobile.MossFramework.Math.MossMath;
import com.example.mossymobile.MossFramework.Math.Vector2;
import com.example.mossymobile.MossFramework.MonoBehaviour;
import com.example.mossymobile.MossFramework.Systems.Time.Time;

public class MovingScript extends MonoBehaviour {
    public Vector2 startPos = new Vector2();
    Vector2 targetPos = new Vector2();
    public float radius = 40.0f;
    boolean IsMoving = false;
    public float speed = 100.0f;

    @Override
    public void Start() {
        this.startPos.x = gameObject.GetTransform().GetPosition().x;
        this.startPos.y = gameObject.GetTransform().GetPosition().y;
    }

    @Override
    public void Update() {
        Transform t = this.gameObject.GetTransform();
        if (t == null) { return; }

        if (IsMoving)
        {
            if (t.GetPosition().DistanceSq(targetPos) <= speed * Time.GetDeltaTime())
            {
                IsMoving = false;
                return;
            }

            Vector2 moveVector = Vector2.Sub(targetPos, t.GetPosition()).Normalized();
            moveVector.Mul(new Vector2(speed * Time.GetDeltaTime(), speed * Time.GetDeltaTime()));
            t.SetPosition(Vector2.Add(t.GetPosition(), moveVector));
        }
        else
        {
            targetPos.x = MossMath.randFloatMinMax(startPos.x - radius, startPos.x + radius);
            targetPos.y = MossMath.randFloatMinMax(startPos.y - radius, startPos.y + radius);
            IsMoving = true;
        }
    }
}
