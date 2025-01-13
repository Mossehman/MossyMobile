package com.example.mossymobile.MossFramework.Components;
import com.example.mossymobile.MossFramework.MonoBehaviour;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Camera extends MonoBehaviour {
    List<Integer> list = new ArrayList<>();
    HashMap<String, String> strLisr = new HashMap<>();
    @Override
    protected void InitializeInspectorData() {
        ShowInInspector("List", list);
        ShowInInspector("Hashmap", strLisr);
    }

    @Override
    public void Start() {
        list.add(10);
        list.add(12);

        strLisr.put("hi", "hashmap");
    }

    @Override
    public void Update() {

    }
}
