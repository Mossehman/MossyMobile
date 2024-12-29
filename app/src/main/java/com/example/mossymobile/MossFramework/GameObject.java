package com.example.mossymobile.MossFramework;

import com.example.mossymobile.MossFramework.Systems.Debugging.Debug;

import java.util.ArrayList;
import java.util.List;

public final class GameObject {

    String name = "GameObject";
    private List<MonoBehaviour> components = new ArrayList<>();

    public void AddComponent(MonoBehaviour component)
    {
        if (component == null) {
            Debug.LogError(name + "::AddComponent()", "Error when adding Component to GameObject, component was null!");
            return;
        }


        component.Awake();

    }

    public void OnDestroy()
    {

    }
}
