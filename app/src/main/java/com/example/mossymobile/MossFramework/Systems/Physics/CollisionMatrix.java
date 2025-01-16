package com.example.mossymobile.MossFramework.Systems.Physics;

import com.example.mossymobile.MossFramework.Systems.ScriptableObjects.ScriptableObject;

import java.util.HashMap;
import java.util.List;

///This class is for saving any collision matrix data the user inputs in during runtime
public final class CollisionMatrix extends ScriptableObject {
    public HashMap<String, List<String>> CollisionsLayers = new HashMap<>();

    @Override
    protected void OnLoad() {
        return;
    }
}
