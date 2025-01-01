package com.example.mossymobile.MossFramework.Systems.Inspector;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mossymobile.MossFramework.Components.Camera;
import com.example.mossymobile.MossFramework.DesignPatterns.Singleton;
import com.example.mossymobile.MossFramework.GameObject;
import com.example.mossymobile.MossFramework.GameView;
import com.example.mossymobile.MossFramework.MonoBehaviour;
import com.example.mossymobile.MossFramework.Scene;
import com.example.mossymobile.MossFramework.Systems.Debugging.Debug;
import com.example.mossymobile.MossFramework.Systems.Scenes.SceneManager;
import com.example.mossymobile.R;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class InspectorGUI extends Singleton<InspectorGUI> {
    public static InspectorGUI GetInstance()
    {
        return Singleton.GetInstance(InspectorGUI.class);
    }

    private final HashMap<String, View> layouts = new HashMap<>();

    private GameObject selectedGameObject = null;
    private Scene currScene = null;
    private int CurrGOCount = 0;

    public void AddLayoutComponent(String name, View layout)
    {
        layouts.put(name, layout);
    }

    public View GetLayoutComponent(String name)
    {
        if (layouts.containsKey(name))
        {
            return layouts.get(name);
        }

        return null;
    }

    public void UpdateHierarchyGUI(String name, int GODataLayoutID)
    {
        LinearLayout layout = (LinearLayout) GetLayoutComponent(name);
        if (layout == null || (currScene == SceneManager.GetCurrScene() && CurrGOCount == currScene.GetGameObjects().size())) { return; }

        Objects.requireNonNull(GameView.GetInstance()).GetActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                layout.removeAllViews();
                currScene = SceneManager.GetCurrScene();

                for (GameObject gameObject : currScene.GetGameObjects())
                {
                    View logComponent = Objects.requireNonNull(GameView.GetInstance()).GetActivity().getLayoutInflater().inflate(R.layout.gameobjectdata, layout, false);
                    Button gameObjectBtn = logComponent.findViewById(R.id.gameObjectName);

                    gameObjectBtn.setText(gameObject.GetName());
                    gameObjectBtn.setOnClickListener(v -> {
                        if (gameObject != selectedGameObject) {
                            selectedGameObject = gameObject;
                            UpdateComponentGUI("Components", R.layout.component, R.layout.componentdata);
                        }

                    });

                    layout.addView(logComponent);
                }

                CurrGOCount = currScene.GetGameObjects().size();

            }
        });

    }

    public void UpdateComponentGUI(String name, int ComponentBaseLayoutID, int ComponentDataLayoutID)
    {
        LinearLayout layout = (LinearLayout) GetLayoutComponent(name);
        layout.removeAllViews();

        for (MonoBehaviour Component : selectedGameObject.GetComponents())
        {
            View componentBase = Objects.requireNonNull(GameView.GetInstance()).GetActivity().getLayoutInflater().inflate(ComponentBaseLayoutID, layout, false);
            TextView componentName = componentBase.findViewById(R.id.componentName);
            String title = Component.getClass().getSimpleName();
            if (!Objects.equals(Component.name, "New Component"))
            {
                title += " (" + Component.name + ")";
            }

            LinearLayout dataContainer = componentBase.findViewById(R.id.componentBase);

            for (Map.Entry<String, InspectorData> set : Component.GetInspectorData().entrySet())
            {

                String ID = set.getKey();
                InspectorData data = set.getValue();

                View componentData = Objects.requireNonNull(GameView.GetInstance()).GetActivity().getLayoutInflater().inflate(ComponentDataLayoutID, dataContainer, false);
                LinearLayout dataList = componentData.findViewById(R.id.componentValues);
                TextView valueName = componentData.findViewById(R.id.dataName);

                valueName.setText(ID);

                if (data.obj instanceof ICustomInspectorGUI)
                {
                    ICustomInspectorGUI guiData = (ICustomInspectorGUI) data.obj;
                    guiData.SetGUIData(dataList);
                }

                dataContainer.addView(componentData);
            }

            componentName.setText(title);
            //for (Object obj)

            layout.addView(componentBase);
        }
    }

}
