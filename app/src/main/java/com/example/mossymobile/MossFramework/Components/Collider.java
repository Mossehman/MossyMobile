package com.example.mossymobile.MossFramework.Components;

import com.example.mossymobile.MossFramework.MonoBehaviour;

public abstract class Collider extends MonoBehaviour {

    public boolean IsTrigger = false;

    @Override
    public void Start() {
        return;
    }

    @Override
    public void Update() {
        return;
    }

    public final void OnCollisionDetected()
    {
        if (IsTrigger)
        {
            gameObject.OnTriggerEnter();
            return;
        }
        gameObject.OnCollisionEnter();
    }

    public abstract void ResolveCollision();
}
