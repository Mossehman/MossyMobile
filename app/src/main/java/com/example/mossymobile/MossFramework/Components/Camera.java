package com.example.mossymobile.MossFramework.Components;
import com.example.mossymobile.MossFramework.Math.Vector2;
import com.example.mossymobile.MossFramework.MonoBehaviour;
import com.example.mossymobile.MossFramework.Systems.Debugging.Debug;
import com.example.mossymobile.MossFramework.Systems.Physics.Collision;
import com.example.mossymobile.MossFramework.Systems.Physics.Physics;
import com.example.mossymobile.MossFramework.Systems.UserInput.Input;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Camera extends MonoBehaviour {
    List<Integer> list = new ArrayList<>();
    HashMap<String, String> strLisr = new HashMap<>();

    int pointerID = -1;
    Vector2 touchPos = new Vector2();
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
        GetTransform().SetRotation(GetTransform().GetRotation() + 0.5f);

    }

    @Override
    public void OnCollisionEnter(Collision collision) {
        Debug.Log("Collision!", "Collision!");
    }
}
