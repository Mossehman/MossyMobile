package com.example.mossymobile.MossFramework.Systems.Inspector;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
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
                TextView valueName = componentData.findViewById(R.id.dataName);

                valueName.setText(ID);
                AddComponentData(dataContainer, componentData, data.obj, data.IsReadOnly);

            }

            componentName.setText(title);
            //for (Object obj)

            layout.addView(componentBase);
        }
    }


    ///Note: do not look into this, it's a mess, but it works, had to do this because of fucking pass by value for primitives in java
    private void AddComponentData(LinearLayout dataContainer, View componentData, Object data, boolean IsReadOnly)
    {
        LinearLayout dataList = componentData.findViewById(R.id.componentValues);
        if (data instanceof ICustomInspectorGUI && !IsReadOnly)
        {
            ICustomInspectorGUI guiData = (ICustomInspectorGUI) data;
            guiData.SetGUIData(dataList, this.InspectorDelayMS);
        }
        else if (data instanceof MutableWrapper<?> && !IsReadOnly)
        {
            MutableWrapper<?> wrapper = (MutableWrapper<?>) data;
            HandlePrimitive(dataList, wrapper);
        }
        else if (data instanceof List<?>)
        {
            List<?> val = (List<?>) data;
            LinearLayout dataListVertical = componentData.findViewById(R.id.componentValuesVertical);

            Button expandList = new Button(GameView.GetInstance().GetContext());
            expandList.setText("Expand List");
            final boolean[] IsExpanded = {false};

            expandList.setOnClickListener(v -> {
                IsExpanded[0] = !IsExpanded[0];

                if (!IsExpanded[0]) {
                    dataListVertical.removeAllViews();
                }
            });

            dataList.addView(expandList);

            Handler handler = new Handler();
            Runnable updateListData = new Runnable() {
                @Override
                public void run() {
                    dataListVertical.removeAllViews();

                    if (!IsExpanded[0])
                    {
                        expandList.setText("Expand List" + " (" + val.size() + ")");
                    }
                    else
                    {
                        expandList.setText("Collapse List" + " (" + val.size() + ")");
                    }

                    if (IsExpanded[0]) {
                        if (val.isEmpty())
                        {
                            TextView emptyText = new TextView(GameView.GetInstance().GetContext());
                            emptyText.setText("List is empty");
                            emptyText.setTypeface(emptyText.getTypeface(), Typeface.ITALIC);

                            dataListVertical.addView(emptyText);
                        }

                        int i = 0;

                        List<?> valCached = new ArrayList<>(val);

                        for (Object obj : valCached) {
                            LinearLayout listElement = new LinearLayout(GameView.GetInstance().GetContext());
                            listElement.setOrientation(LinearLayout.HORIZONTAL);

                            LinearLayout.LayoutParams listElementParams = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                            );

                            TextView elementNumber = new TextView(GameView.GetInstance().GetContext());
                            elementNumber.setText("" + i);
                            i++;

                            TextView readOnlyComponent = new TextView(Objects.requireNonNull(GameView.GetInstance()).GetContext());
                            readOnlyComponent.setGravity(Gravity.CENTER);
                            String str = "";
                            if (obj != null) {
                                str = obj.toString();
                            } else {
                                str = "null";
                            }

                            if (!readOnlyComponent.getText().toString().equals(str)) {
                                readOnlyComponent.setText(str);
                            }

                            LinearLayout.LayoutParams readOnlyParams = new LinearLayout.LayoutParams(
                                    0,
                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                    1
                            );
                            readOnlyComponent.setLayoutParams(readOnlyParams);

                            listElement.addView(elementNumber);
                            listElement.addView(readOnlyComponent);

                            dataListVertical.addView(listElement);
                        }
                    }
                    handler.postDelayed(this, InspectorDelayMS * 5);
                }
            };
            handler.post(updateListData);

        }
        else if (data instanceof Map<?, ?>)
        {
            Map<?, ?> val = (Map<?, ?>) data;
            LinearLayout dataListVertical = componentData.findViewById(R.id.componentValuesVertical);

            Button expandList = new Button(GameView.GetInstance().GetContext());
            expandList.setText("Expand Map");
            final boolean[] IsExpanded = {false};

            expandList.setOnClickListener(v -> {
                IsExpanded[0] = !IsExpanded[0];
                //Debug.Log("Boolean", IsExpanded[0]);

                if (!IsExpanded[0]) {
                    dataListVertical.removeAllViews();
                }
            });

            dataList.addView(expandList);

            Handler handler = new Handler();
            Runnable updateListData = new Runnable() {
                @Override
                public void run() {
                    dataListVertical.removeAllViews();

                    if (!IsExpanded[0])
                    {
                        expandList.setText("Expand Map" + " (" + val.size() + ")");
                    }
                    else
                    {
                        expandList.setText("Collapse Map" + " (" + val.size() + ")");
                    }

                    if (IsExpanded[0]) {
                        if (val.isEmpty())
                        {
                            TextView emptyText = new TextView(GameView.GetInstance().GetContext());
                            emptyText.setText("Map is empty");
                            emptyText.setTypeface(emptyText.getTypeface(), Typeface.ITALIC);

                            dataListVertical.addView(emptyText);
                        }

                        Map<Object, Object> valCached = new HashMap<>();
                        valCached.putAll(val);

                        for (Map.Entry<Object, Object> set : valCached.entrySet()) {

                            Object key = set.getKey();
                            Object obj = set.getValue();

                            LinearLayout mapElement = new LinearLayout(GameView.GetInstance().GetContext());
                            mapElement.setOrientation(LinearLayout.HORIZONTAL);

                            TextView mapKeyComponent = new TextView(Objects.requireNonNull(GameView.GetInstance()).GetContext());
                            mapKeyComponent.setGravity(Gravity.CENTER);
                            String keyStr = "";
                            if (key != null) {
                                keyStr = key.toString();
                            } else {
                                keyStr = "null";
                            }

                            if (!mapKeyComponent.getText().toString().equals(keyStr)) {
                                mapKeyComponent.setText(keyStr);
                            }

                            LinearLayout.LayoutParams keyParams = new LinearLayout.LayoutParams(
                                    0,
                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                    1
                            );
                            mapKeyComponent.setLayoutParams(keyParams);


                            TextView readOnlyComponent = new TextView(Objects.requireNonNull(GameView.GetInstance()).GetContext());
                            readOnlyComponent.setGravity(Gravity.CENTER);
                            String str = "";
                            if (obj != null) {
                                str = obj.toString();
                            } else {
                                str = "null";
                            }

                            if (!readOnlyComponent.getText().toString().equals(str)) {
                                readOnlyComponent.setText(str);
                            }

                            LinearLayout.LayoutParams readOnlyParams = new LinearLayout.LayoutParams(
                                    0,
                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                    1
                            );
                            readOnlyComponent.setLayoutParams(readOnlyParams);

                            mapElement.addView(mapKeyComponent);
                            mapElement.addView(readOnlyComponent);


                            dataListVertical.addView(mapElement);
                        }
                    }
                    handler.postDelayed(this, InspectorDelayMS * 5);
                }
            };
            handler.post(updateListData);

        }

        else {
            TextView readOnlyComponent = new TextView(Objects.requireNonNull(GameView.GetInstance()).GetContext());

            Handler handler = new Handler();
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    String val = "";
                    if (data != null)
                    {
                        val = data.toString();
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

    private void HandlePrimitive(LinearLayout dataList, MutableWrapper<?> wrapper)
    {
        if (wrapper.value instanceof Integer)
        {
            final int[] val = {(Integer) wrapper.value};
            final boolean[] isEditing = {false};

            EditText intComponent = new EditText(Objects.requireNonNull(GameView.GetInstance()).GetContext());

            intComponent.setMaxLines(1); // Restrict to a single line
            intComponent.setSingleLine(true); // Ensure single-line behavior

            intComponent.setInputType(InputType.TYPE_NUMBER_VARIATION_NORMAL);
            intComponent.setText(String.valueOf(val[0]));

            intComponent.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        isEditing[0] = true;
                    }
                    else {
                        isEditing[0] = false;
                    }
                }
            });

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
                            val[0] = Integer.parseInt(newText);
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
                    if (!intComponent.getText().toString().equals(String.valueOf(wrapper.value)) && !isEditing[0]) {
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
            final boolean[] isEditing = {false};

            EditText floatComponent = new EditText(Objects.requireNonNull(GameView.GetInstance()).GetContext());

            floatComponent.setMaxLines(1);


            floatComponent.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
            floatComponent.setText(String.valueOf(val[0]));

            floatComponent.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        isEditing[0] = true;
                    }
                    else {
                        isEditing[0] = false;
                    }
                }
            });


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
                    if (!floatComponent.getText().toString().equals(String.valueOf(wrapper.value)) && !isEditing[0]) {
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
            final boolean[] isEditing = {false};

            EditText strComponent = new EditText(Objects.requireNonNull(GameView.GetInstance()).GetContext());
            strComponent.setMaxLines(1);
            strComponent.setSingleLine(true);

            strComponent.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
            strComponent.setText(val[0]);

            strComponent.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        isEditing[0] = true;
                    }
                    else {
                        isEditing[0] = false;
                    }
                }
            });

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
                    if (!strComponent.getText().toString().equals(wrapper.value) && !isEditing[0]) {
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
                    handler.postDelayed(this, InspectorDelayMS);
                }
            };
            //handler.post(runnable);
            dataList.addView(boolComponent);
        }
    }


}
