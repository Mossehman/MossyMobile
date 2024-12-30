package com.example.mossymobile.MossFramework;

import com.example.mossymobile.MossFramework.Components.Collider;
import com.example.mossymobile.MossFramework.Components.Renderer;
import com.example.mossymobile.MossFramework.Components.Transform;
import com.example.mossymobile.MossFramework.DesignPatterns.Factory;
import com.example.mossymobile.MossFramework.Systems.Debugging.Debug;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public final class GameObject implements Serializable {

    String name = "GameObject";

    //private List<MonoBehaviour> Components = new ArrayList<>();
    private HashMap<Class<?>, MonoBehaviour> Components = new HashMap<>();


    ///Pre-define certain variables for optimising function calls later on (we won't have to do GetComponent) every time we need these specific components
    private Transform transform = null;
    private Renderer renderer = null;
    private Collider collider = null;

    public GameObject(String name)
    {
        transform = new Transform();
        this.name = name;
    }

    public GameObject()
    {
        transform = new Transform();
    }

    public void AddComponent(MonoBehaviour component)
    {
        Class<?> componentType = component.getClass();
        if (Components.containsKey(componentType))
        {
            Debug.LogError(name + "::AddComponent()", "Error when adding Component to GameObject, duplicate component types detected!");
            return;
        }

        component.SetGameObject(this);
        component.Awake();

        Components.put(componentType, component);
    }

    public <T extends MonoBehaviour> void AddComponent(Class<T> type)
    {
        MonoBehaviour component = Factory.CreateObject(type);
        component.SetGameObject(this);
        component.Awake();

        Components.put(type, component);
    }

    public <T extends MonoBehaviour> void RemoveComponent(Class<T> type)
    {
        for (Map.Entry<Class<?>, MonoBehaviour> set : Components.entrySet())
        {
            MonoBehaviour component = set.getValue();

            if (type.isInstance(component))
            {
                Components.remove(component.getClass());
            }
        }
    }

    public <T extends MonoBehaviour> T GetComponent(Class<T> type)
    {
        return type.cast(Components.get(type));
    }


    ///Init is called once per frame to run the Start functions of all inactive components
    public void Init()
    {
        if (Components.isEmpty()) { return; } ///prevents us from doing an additional loop over all components if nothing new has been added


        for (Map.Entry<Class<?>, MonoBehaviour> set : Components.entrySet())
        {
            MonoBehaviour component = set.getValue();
            if (!component.IsEnabled || component.RunStartFunction()) { continue; }
            component.Init();
        }

    }

    ///Update is called once per frame
    public void Update() {
        for (Map.Entry<Class<?>, MonoBehaviour> set : Components.entrySet())
        {
            MonoBehaviour component = set.getValue();
            if (!component.IsEnabled) { continue; }
            component.Update();
        }
    }

    ///LateUpdate is called after all GameObjects have run their Update loop
    public void LateUpdate() {
        for (Map.Entry<Class<?>, MonoBehaviour> set : Components.entrySet())
        {
            MonoBehaviour component = set.getValue();
            component.LateUpdate();
        }
    }

    public void OnDestroy()
    {
        Components.clear();
        Components = null;
    }

    public Transform GetTransform()
    {
        return transform;
    }

}
