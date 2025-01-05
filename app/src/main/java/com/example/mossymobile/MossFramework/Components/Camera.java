package com.example.mossymobile.MossFramework.Components;

import com.example.mossymobile.MossFramework.DesignPatterns.MutableWrapper;
import com.example.mossymobile.MossFramework.GameObject;
import com.example.mossymobile.MossFramework.MonoBehaviour;
import com.example.mossymobile.MossFramework.Systems.Debugging.Debug;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Camera extends MonoBehaviour {
    public MutableWrapper<Float> a = new MutableWrapper<>(5.0f);
    public MutableWrapper<Boolean> b = new MutableWrapper<>(true);
    public List<Integer> test = new ArrayList<>();
    public HashMap<Integer, String> test2 = new HashMap<>();

    public GameObject go;



    @Override
    protected void InitializeInspectorData() {
        EditInInspector("a", a);
        EditInInspector("b", b);
        EditInInspector("List", test2);
    }

    @Override
    public void Start() {
        GameObject newGO = Instantiate(go);
        newGO.name = "testing";

        test2.put(10, "str1");
    }

    @Override
    public void Update() {
        a.value -= 0.1f;

        if (b.value == false) {
            Debug.Log("False", "This is false");
        }

    }
}
