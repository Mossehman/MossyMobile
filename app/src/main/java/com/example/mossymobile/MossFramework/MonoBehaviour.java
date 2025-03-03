package com.example.mossymobile.MossFramework;

import android.os.Handler;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.mossymobile.MossFramework.Components.Colliders.Collider;
import com.example.mossymobile.MossFramework.Components.Transform;
import com.example.mossymobile.MossFramework.DesignPatterns.Factory;
import com.example.mossymobile.MossFramework.Systems.Debugging.Debug;
import com.example.mossymobile.MossFramework.Systems.Inspector.ICustomInspectorGUI;
import com.example.mossymobile.MossFramework.Systems.Inspector.InspectorData;
import com.example.mossymobile.MossFramework.Systems.Physics.Collision;
import com.example.mossymobile.MossFramework.Systems.Scenes.SceneManager;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Objects;

public abstract class MonoBehaviour implements Serializable, ICustomInspectorGUI {
    protected GameObject gameObject = null;
    public boolean IsEnabled = true;
    private boolean HasRunStart = false;

    ///Determines if the component allows derived types to be added onto the same GameObject if one is already present (eg: sprite renderer and animated sprite renderer), defined in the constructor
    protected boolean AllowDerivedTypes = false;
    private boolean InitInspector = false;

    private HashMap<String, InspectorData> GUIData = new HashMap<>();

    public String name;

    public MonoBehaviour(String name)
    {
        this.name = name;
    }

    public MonoBehaviour()
    {
        this.name = "New Component";
    }

    public final void AddToList()
    {
        if (Application.AllowClassDuplicates.containsKey(this.getClass())) { return; }
        Application.AllowClassDuplicates.put(this.getClass(), AllowDerivedTypes);
    }


    ///Awake is called when the Component is added to the GameObject.
    public void Awake() {}

    ///Start is called once at the start of the frame which the component is enabled (typically when the component gets added)
    public abstract void Start();

    public final void InspectorGUI() {
        InitInspector = true;
        InitializeInspectorData();
        InitInspector = false;
    }
    protected void InitializeInspectorData() {}

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

    public GameObject Instantiate(GameObject gameObject)
    {
        if (gameObject == null) { return null; }

        GameObject go;
        if (SceneManager.GetCurrScene().GetGameObjects().contains(gameObject))
        {
            go = Factory.CopyObject(gameObject);
            if (go != null) {
                SceneManager.GetCurrScene().AddGOToScene(go);
            }
        }
        else {
            go = gameObject;
        }

        return go;
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

    public void ShowInInspector(String data, Object obj)
    {
        if (!InitInspector) {
            Debug.LogError("MonoBehaviour::ShowInInspector()", "You can only call this function in { InitialiseInspectorData() }!");
        }
        GUIData.put(data, new InspectorData(obj, true));
    }

    public void EditInInspector(String data, Object obj)
    {
        if (!InitInspector) {
            Debug.LogError("MonoBehaviour::ShowInInspector()", "You can only call this function in { InitialiseInspectorData() }!");
        }
        GUIData.put(data, new InspectorData(obj, false));
    }

    public HashMap<String, InspectorData> GetInspectorData()
    {
        return this.GUIData;
    }


    @NonNull
    @Override
    public String toString() {
        return getClass().getSimpleName() + " (" + gameObject.name + ")";
    }

    @Override
    public void SetGUIData(LinearLayout componentList, long updateDelay) {
        TextView dataComponent = new TextView(Objects.requireNonNull(GameView.GetInstance()).GetContext());
        dataComponent.setText(toString());

        Handler handler = new Handler();
        Runnable updateTextData = new Runnable() {
            @Override
            public void run() {
                if (!dataComponent.getText().toString().equals(toString())) {
                    dataComponent.setText(toString()); // Update X text
                }

                handler.postDelayed(this, updateDelay); // Repeat every 100ms
            }
        };

        handler.post(updateTextData);

        componentList.addView(dataComponent);
    }

    public void OnDrawGizmos() { return; }

    public void OnCollisionEnter(Collision collision) {}
    public void OnCollisionStay(Collision collision) {}
    public void OnCollisionExit(Collider collider) {}

    public void OnTriggerEnter(Collider collider) {}
    public void OnTriggerStay(Collider collider) {}
    public void OnTriggerExit(Collider collider) {}

    public void Destroy(GameObject go) {
        if (go != null) {
            go.GetScene().gameObjectsToRemove.add(go);
        }
    }


    public Transform GetTransform() {
        return gameObject.GetTransform();
    }




}
