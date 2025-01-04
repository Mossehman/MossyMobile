package com.example.mossymobile.MossFramework.Testing;

import com.example.mossymobile.MossFramework.Systems.Debugging.Debug;
import com.example.mossymobile.MossFramework.Systems.ScriptableObjects.ScriptableObject;

public class TestSO extends ScriptableObject {

    float foo = 1.0f;
    @Override
    protected void OnLoad() {
        Debug.Log("TestSO", "I have loaded!");
    }
}
