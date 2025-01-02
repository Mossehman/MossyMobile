package com.example.mossymobile.MossFramework.Components;

import com.example.mossymobile.MossFramework.DesignPatterns.MutableWrapper;
import com.example.mossymobile.MossFramework.MonoBehaviour;
import com.example.mossymobile.MossFramework.Systems.Debugging.Debug;

public class Camera extends MonoBehaviour {
    public MutableWrapper<Float> a = new MutableWrapper<>(5.0f);
    public MutableWrapper<Boolean> b = new MutableWrapper<>(true);

    @Override
    protected void InitializeInspectorData() {
        EditInInspector("a", a);
        EditInInspector("b", b);
    }

    @Override
    public void Start() {

    }

    @Override
    public void Update() {
        a.value -= 0.1f;

        if (b.value == false) {
            Debug.Log("False", "This is false");
        }
    }
}
