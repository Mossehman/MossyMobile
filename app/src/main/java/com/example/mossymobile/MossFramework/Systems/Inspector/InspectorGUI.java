package com.example.mossymobile.MossFramework.Systems.Inspector;

import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mossymobile.MossFramework.DesignPatterns.MutableWrapper;
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

    public long InspectorDelayMS = 10;
    private final HashMap<String, View> layouts = new HashMap<>();

    private GameObject selectedGameObject = null;
    private Scene currScene = null;
    private int CurrGOCount = 0;

    private boolean SwitchingGameObject = false;


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
        TextView GOName = (TextView) GetLayoutComponent("GOName");
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
                            GOName.setText(selectedGameObject.GetName());

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

                if (data.obj instanceof ICustomInspectorGUI && !data.IsReadOnly)
                {
                    ICustomInspectorGUI guiData = (ICustomInspectorGUI) data.obj;
                    guiData.SetGUIData(dataList, this.InspectorDelayMS);
                }
                else if (data.obj instanceof MutableWrapper<?> && !data.IsReadOnly)
                {
                    MutableWrapper<?> wrapper = (MutableWrapper<?>) data.obj;
                    if (wrapper.value instanceof Integer)
                    {
                        final int[] val = {(Integer) wrapper.value};

                        EditText intComponent = new EditText(Objects.requireNonNull(GameView.GetInstance()).GetContext());
                        intComponent.setInputType(InputType.TYPE_NUMBER_VARIATION_NORMAL);
                        intComponent.setText(String.valueOf(val[0]));

                        intComponent.addTextChangedListener(new TextWatcher() {
                            private String previousText = "";

                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                previousText = s.toString();
                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                String newText = s.toString();

                                if (!newText.equals(previousText)) {
                                    try {
                                        ((MutableWrapper<Integer>) wrapper).SetValue(val[0]);
                                    } catch (NumberFormatException e) {
                                        Debug.LogError("Vector2::SetGUIData()", "Invalid data set via Inspector, only input float values!");
                                    }
                                }
                            }

                            @Override
                            public void afterTextChanged(Editable s) {}
                        });

                        Handler handler = new Handler();
                        Runnable runnable = new Runnable() {
                            @Override
                            public void run() {
                                if (!intComponent.getText().toString().equals(String.valueOf(wrapper.value))) {
                                    intComponent.setText(String.valueOf(wrapper.value));
                                }


                                handler.postDelayed(this, InspectorDelayMS);
                            }
                        };
                        handler.post(runnable);

                        dataList.addView(intComponent);
                    }
                    else if (wrapper.value instanceof Float)
                    {
                        final float[] val = {(float) wrapper.value};

                        EditText floatComponent = new EditText(Objects.requireNonNull(GameView.GetInstance()).GetContext());
                        floatComponent.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
                        floatComponent.setText(String.valueOf(val[0]));

                        floatComponent.addTextChangedListener(new TextWatcher() {
                            private String previousText = "";

                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                previousText = s.toString();
                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                String newText = s.toString();

                                if (!newText.equals(previousText)) {
                                    try {
                                        val[0] = Float.parseFloat(newText);
                                        ((MutableWrapper<Float>) wrapper).SetValue(val[0]);
                                    } catch (NumberFormatException e) {
                                        Debug.LogError("Vector2::SetGUIData()", "Invalid data set via Inspector, only input float values!");
                                    }
                                }
                            }

                            @Override
                            public void afterTextChanged(Editable s) {}
                        });

                        Handler handler = new Handler();
                        Runnable runnable = new Runnable() {
                            @Override
                            public void run() {
                                if (!floatComponent.getText().toString().equals(String.valueOf(wrapper.value))) {
                                    floatComponent.setText(String.valueOf(wrapper.value));
                                }


                                handler.postDelayed(this, InspectorDelayMS);
                            }
                        };
                        handler.post(runnable);

                        dataList.addView(floatComponent);
                    }
                    else if (wrapper.value instanceof String)
                    {
                        final String[] val = {(String) wrapper.value};

                        EditText strComponent = new EditText(Objects.requireNonNull(GameView.GetInstance()).GetContext());
                        strComponent.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
                        strComponent.setText(val[0]);

                        strComponent.addTextChangedListener(new TextWatcher() {
                            private String previousText = "";

                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                previousText = s.toString();
                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                String newText = s.toString();

                                if (!newText.equals(previousText)) {
                                    try {
                                        val[0] = newText;
                                        ((MutableWrapper<String>) wrapper).SetValue(newText);

                                    } catch (NumberFormatException e) {
                                        Debug.LogError("Vector2::SetGUIData()", "Invalid data set via Inspector, only input float values!");
                                    }
                                }
                            }

                            @Override
                            public void afterTextChanged(Editable s) {}
                        });

                        Handler handler = new Handler();
                        Runnable runnable = new Runnable() {
                            @Override
                            public void run() {
                                if (!strComponent.getText().toString().equals(wrapper.value)) {
                                    strComponent.setText((String) wrapper.value);
                                }


                                handler.postDelayed(this, InspectorDelayMS);
                            }
                        };
                        handler.post(runnable);
                        dataList.addView(strComponent);

                    }
                    else if (wrapper.value instanceof Boolean)
                    {
                        final boolean[] val = {(Boolean) wrapper.value};


                        CheckBox boolComponent = new CheckBox(Objects.requireNonNull(GameView.GetInstance()).GetContext());
                        boolComponent.setChecked(val[0]);

                        // Update wrapper value when CheckBox is toggled
                        boolComponent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                val[0] = isChecked;
                                ((MutableWrapper<Boolean>) wrapper).SetValue(isChecked);
                            }
                        });

                        Handler handler = new Handler();
                        Runnable runnable = new Runnable() {
                            @Override
                            public void run() {
                                if (boolComponent.isChecked() != (Boolean) wrapper.value)
                                {
                                    boolComponent.setOnCheckedChangeListener(null);
                                    boolComponent.setChecked((Boolean) wrapper.value);
//
                                    boolComponent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                        @Override
                                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                            val[0] = isChecked;
                                            ((MutableWrapper<Boolean>) wrapper).SetValue(isChecked);
                                        }
                                    });
//
                                }
//
                                handler.postDelayed(this, InspectorDelayMS * 10);
                            }
                        };
                        //handler.post(runnable);
                        dataList.addView(boolComponent);
                    }
                }
                else {


                    TextView readOnlyComponent = new TextView(Objects.requireNonNull(GameView.GetInstance()).GetContext());

                    Handler handler = new Handler();
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            String val = "";
                            if (data.obj != null)
                            {
                                val = data.obj.toString();
                            }
                            else
                            {
                                val = "null";
                            }

                            if (!readOnlyComponent.getText().toString().equals(val)) {
                                readOnlyComponent.setText(val);
                            }

                            handler.postDelayed(this, InspectorDelayMS);
                        }
                    };
                    handler.post(runnable);

                    dataList.addView(readOnlyComponent);
                }


                dataContainer.addView(componentData);
            }

            componentName.setText(title);
            //for (Object obj)

            layout.addView(componentBase);
        }
    }

}
