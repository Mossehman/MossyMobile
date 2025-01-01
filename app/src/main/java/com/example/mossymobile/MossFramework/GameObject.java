package com.example.mossymobile.MossFramework;

import com.example.mossymobile.MossFramework.Components.Transform;
import com.example.mossymobile.MossFramework.DesignPatterns.Factory;
import com.example.mossymobile.MossFramework.Systems.Debugging.Debug;
import com.example.mossymobile.MossFramework.Systems.Scenes.SceneManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class GameObject implements Serializable {
    String name = "GameObject";
    private HashMap<Class<?>, MonoBehaviour> Components = new HashMap<>();
    private List<MonoBehaviour> ComponentsToRemove = new ArrayList<>();
    private Transform transform = null;
    public boolean ToRender = false;

    private Scene CurrentScene = null;

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

        component.InspectorGUI();

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
        AddComponent(component);

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

    public <T extends MonoBehaviour> T GetComponent(Class<T> type)
    {
        return type.cast(Components.get(type));
    }

    public <T extends MonoBehaviour> T GetComponent(Class<T> type, boolean GetInherited)
    {
        for (MonoBehaviour component : Components.values())
        {
            if (type.isInstance(component) || (GetInherited && ( type.isAssignableFrom(component.getClass()) || component.getClass().isAssignableFrom(type) )))
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
        }

        ComponentsToRemove.clear();
    }

    public void OnDestroy()
    {
        Components.clear();

        Components = null;
        ComponentsToRemove = null;
    }

    public Transform GetTransform()
    {
        return transform;
    }

    public void OnCollisionEnter() {
    }

    public void OnCollisionHold() {

    }

    public void OnCollisionExit() {

    }


    public void OnTriggerEnter() {

    }

    public void OnTriggerHold() {

    }

    public void OnTriggerExit() {

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

}
