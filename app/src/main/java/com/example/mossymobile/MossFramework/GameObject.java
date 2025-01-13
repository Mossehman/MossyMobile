package com.example.mossymobile.MossFramework;

import android.os.Handler;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.mossymobile.MossFramework.Components.Transform;
import com.example.mossymobile.MossFramework.DesignPatterns.Factory;
import com.example.mossymobile.MossFramework.Systems.Debugging.BuildConfig;
import com.example.mossymobile.MossFramework.Systems.Debugging.Debug;
import com.example.mossymobile.MossFramework.Systems.Inspector.ICustomInspectorGUI;
import com.example.mossymobile.MossFramework.Systems.Scenes.SceneManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class GameObject implements Serializable, ICustomInspectorGUI {
    public String name = "GameObject";

    List<String> tags = new ArrayList<>();

    private LinkedHashMap<Class<?>, MonoBehaviour> Components = new LinkedHashMap<>();
    private List<MonoBehaviour> ComponentsToRemove = new ArrayList<>();
    private Transform transform = null;
    public boolean ToRender = false;

    private transient Scene CurrentScene = null;

    public GameObject(String name)
    {
        Transform goTransform = new Transform();
        transform = goTransform;
        AddComponent(goTransform);
        this.name = name;

        this.CurrentScene = SceneManager.GetCurrScene();
        CurrentScene.AddGOToScene(this);

    }

    public GameObject()
    {
        Transform goTransform = new Transform();
        transform = goTransform;
        AddComponent(goTransform);

        this.CurrentScene = SceneManager.GetCurrScene();
        CurrentScene.AddGOToScene(this);

    }

    public void AddComponent(MonoBehaviour component)
    {
        component.AddToList();
        Class<?> componentType = component.getClass();
        if (Components.containsKey(componentType))
        {
            Debug.LogError(name + "::AddComponent()", "Error when adding Component to GameObject, duplicate component types detected!");
            return;
        }
        else if (!Application.CheckAllowDuplicates(componentType)) // component does not allow for multiple derived types to be added onto the same gameObject
        {
            //Ensures we do not add multiple derived types
            for (Class<?> type : Components.keySet())
            {
                if (type.isAssignableFrom(componentType) || componentType.isAssignableFrom(type))
                {
                    Debug.LogError(name + "::AddComponent()", "Error when adding Component to GameObject, multiple derived types detected!");
                    return;
                }
            }
        }
        if (Debug.GetConfig() != BuildConfig.PRODUCTION) {
            component.InspectorGUI();
        }

        if (component instanceof Transform) {
            transform = (Transform) component;
        }

        component.SetGameObject(this);
        component.Awake();

        Components.put(componentType, component);
    }

    public <T extends MonoBehaviour> T AddComponent(Class<T> type)
    {
        MonoBehaviour component = Factory.CreateObject(type);
        if (component != null) {
            AddComponent(component);
        }

        return type.cast(component);
    }

    public <T extends MonoBehaviour> void RemoveComponent(Class<T> type, boolean GetInherited)
    {
        for (Map.Entry<Class<?>, MonoBehaviour> set : Components.entrySet())
        {
            MonoBehaviour component = set.getValue();

            if (type.isInstance(component) || (GetInherited && ( type.isAssignableFrom(component.getClass()) || component.getClass().isAssignableFrom(type) )))
            {
                ComponentsToRemove.add(component);
            }
        }
    }

    public <T extends MonoBehaviour> void RemoveComponent(Class<T> type)
    {
        RemoveComponent(type, false);
    }

    public <T extends MonoBehaviour> T GetComponentFast(Class<T> type)
    {
        return type.cast(Components.get(type));
    }

    public <T extends MonoBehaviour> T GetComponent(Class<T> type)
    {
        return GetComponent(type, true);
    }

    public <T extends MonoBehaviour> T GetComponent(Class<T> type, boolean GetInherited)
    {
        for (MonoBehaviour component : Components.values())
        {
            if (type.isInstance(component) || (GetInherited && component.getClass().isAssignableFrom(type) ))
            {
                return type.cast(Components.get(component.getClass()));
            }
        }

        return null;
    }


    ///Init is called once per frame to run the Start functions of all components that have not run their start function yet. If run, they will be skipped.
    public void Init()
    {
        if (Components.isEmpty()) { return; }

        for (MonoBehaviour component : Components.values())
        {
            if (!component.IsEnabled || component.RunStartFunction()) { continue; }
            component.Init();
        }
    }

    ///Update is called once per frame
    public void Update() {
        for (MonoBehaviour component : Components.values())
        {
            if (!component.IsEnabled) { continue; }
            component.Update();
        }
    }

    ///LateUpdate is called after all GameObjects have run their Update loop
    public void LateUpdate() {
        for (MonoBehaviour component : Components.values())
        {
            if (!component.IsEnabled) { continue; }
            component.LateUpdate();
        }

        for (int i = ComponentsToRemove.size() - 1; i >= 0; i--)
        {
            Components.remove(ComponentsToRemove.get(i).getClass());
            ComponentsToRemove.set(i, null);
        }

        ComponentsToRemove.clear();
    }

    public void Gizmos() {
        for (MonoBehaviour component : Components.values())
        {
            if (!component.IsEnabled) { continue; }
            component.OnDrawGizmos();
        }
    }


    public void OnDestroy()
    {
        for (MonoBehaviour component : Components.values()) {
            component = null;
        }

        Components.clear();
        Components = null;
        ComponentsToRemove = null;
    }

    public Transform GetTransform()
    {
        return transform;
    }

    public Scene GetScene()
    {
        return this.CurrentScene;
    }

    public String GetName()
    {
        return this.name;
    }

    public List<MonoBehaviour> GetComponents()
    {
        return new ArrayList<>(Components.values());
    }

    @NonNull
    @Override
    public String toString() {
        return name;
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
                    dataComponent.setText(toString());
                }

                handler.postDelayed(this, updateDelay);
            }
        };

        handler.post(updateTextData);

        componentList.addView(dataComponent);
    }

    public void AddTag(String tag) {
        this.tags.add(tag);
    }

    public boolean HasTag(String tag) {
        return tags.contains(tag);
    }

    public void RemoveTag(String tag) {
        this.tags.remove(tag);
    }

}
