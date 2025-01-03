package com.example.mossymobile.MossFramework.Components;

import com.example.mossymobile.MossFramework.DesignPatterns.MutableWrapper;
import com.example.mossymobile.MossFramework.Math.Vector2;
import com.example.mossymobile.MossFramework.MonoBehaviour;
import com.example.mossymobile.MossFramework.Systems.Debugging.Debug;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Camera extends MonoBehaviour {
    public MutableWrapper<Float> a = new MutableWrapper<>(5.0f);
    public MutableWrapper<Boolean> b = new MutableWrapper<>(true);
    List<Vector2> ints = new ArrayList<>();
    public HashMap<MonoBehaviour, String> map = new HashMap<MonoBehaviour, String>();

    @Override
    protected void InitializeInspectorData() {
        EditInInspector("a", a);
        EditInInspector("b", b);
        EditInInspector("List", ints);
        EditInInspector("Map", map);
    }

    @Override
    public void Start() {
        ints.add(new Vector2());
        ints.add(new Vector2());
        ints.add(new Vector2());
        ints.add(new Vector2());
    }

    @Override
    public void Update() {
        a.value -= 0.1f;

        if (b.value == false) {
            Debug.Log("False", "This is false");
        }

    }
}
