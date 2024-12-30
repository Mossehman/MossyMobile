package com.example.mossymobile.MossFramework;

import com.example.mossymobile.MossFramework.DesignPatterns.Factory;
import com.example.mossymobile.MossFramework.Systems.Debugging.Debug;
import com.example.mossymobile.MossFramework.Math.Vector2;

import java.io.Serializable;

public abstract class MonoBehaviour implements Serializable {
    protected GameObject gameObject = null;
    public boolean IsEnabled = true;
    protected boolean HasRunStart = false;

    public String name;

    public MonoBehaviour(String name)
    {
        this.name = name;
    }

    public MonoBehaviour()
    {
        this.name = "New Component";
    }


    ///Awake is called when the Component is added to the GameObject.
    public void Awake() {}

    ///Start is called once at the start of the frame when the scene is first run (this will not run if the component is inactive at the start, use {@code OnFirstEnabled()}).
    public abstract void Start();

    ///OnEnabled is called everytime the component is activated.
    protected void OnEnabled() {}

    //public final void Enabled() {
    //    if (!IsEnabled) { return; }
    //    if (!HasRunStart)
    //    {
    //        OnFirstEnabled();
    //        HasRunStart = true;
    //    }
    //    OnEnabled();
    //}

    public final boolean Init()
    {
        if (HasRunStart) { return false; }

        Start();
        HasRunStart = true;
        return true;
    }

    public abstract void Update();
    public void LateUpdate() {}

    public void OnDestroy() {}

    public GameObject Instantiate(GameObject gameObject, Vector2 position, float rotationAngle)
    {
        return Factory.CopyObject(gameObject);
    }


    public void SetGameObject(GameObject go, boolean forceSet)
    {

        if (gameObject != null)
        {
            if (forceSet)
            {
                Debug.LogWarning("MonoBehaviour::SetGameObject()", "Dynamically changing the attached GameObject of a MonoBehaviour script at runtime! This is not advised! { " + gameObject.name + " -> " + go.name + " }", "Modify MonoBehaviour GameObject");
            }
            else
            {
                Debug.LogError("MonoBehaviour::SetGameObject()", "Attempting to dynamically change the attached GameObject of a MonoBehaviour script at runtime!");
                return;
            }
        }

        this.gameObject = go;
    }

    public void SetGameObject(GameObject go)
    {
        SetGameObject(go, false);
    }

    public GameObject GetGameObject()
    {
        return this.gameObject;
    }

    public boolean RunStartFunction() { return HasRunStart; }

}
