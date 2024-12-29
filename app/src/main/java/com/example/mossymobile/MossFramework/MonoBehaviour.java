package com.example.mossymobile.MossFramework;

import com.example.mossymobile.MossFramework.Systems.Debugging.Debug;
import com.example.mossymobile.MossFramework.Math.Vector2;

public abstract class MonoBehaviour {
    protected GameObject gameObject = null;
    public boolean IsEnabled = true;
    protected boolean HasRunStart = false;


    ///Awake is called when the Component is added to the GameObject.
    protected void Awake() {}

    ///Start is called once at the start of the frame when the scene is first run (this will not run if the component is inactive at the start, use {@code OnFirstEnabled()}).
    public void Start() {}

    ///OnEnabled is called everytime the component is activated.
    protected void OnEnabled() {}

    ///OnFirstEnabled is called the first time the component is activated.
    protected void OnFirstEnabled() {}

    public final void Enabled() {
        if (!IsEnabled) { return; }
        if (!HasRunStart)
        {
            OnFirstEnabled();
            HasRunStart = true;
        }
        OnEnabled();
    }

    public void Update() {}

    public void OnDestroy() {}

    public GameObject Instantiate(GameObject gameObject, Vector2 position, float rotationAngle)
    {
        ///TODO: Implement Cloneable interface for MonoBehaviour, find a way to clone the scripts and return a new gameObject + copy the existing components over
        return null;
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

    public GameObject GetGameObject()
    {
        return this.gameObject;
    }


}
