package com.example.mossymobile.MossFramework.Systems.UserInput;

import android.view.View;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.LinearLayout;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.mossymobile.MossFramework.Application;
import com.example.mossymobile.MossFramework.DesignPatterns.Singleton;
import com.example.mossymobile.MossFramework.GameView;
import com.example.mossymobile.MossFramework.Systems.Debugging.Debug;

import java.util.Objects;

public class UI extends Singleton<UI> {

    public static UI GetInstance() {
        return Singleton.GetInstance(UI.class);
    }

    private ConstraintLayout UIContainer = null;

    /**
     * Sets the default container for our UI Components to be inflated into, function should only be called once in {@code Application::OnStart()}
     *
     * @param uiContainer the ID for our UI container
     *
     * @return the success of setting the default view container
     */
    public boolean SetUIContainer(int uiContainer) {
        if (UIContainer != null || Application.IsRunning()) { return false; }
        UIContainer = Objects.requireNonNull(GameView.GetInstance()).GetActivity().findViewById(uiContainer);
        return UIContainer != null;
    }

    public View GetUIContainer() { return UIContainer; }

    /**
     * Adds a layout component to the UI container, will not add if layout is null
     *
     * @param layoutID the ID for the UI to be added to the UI container
     *
     * @return the added UI Layout
     */
    public View AddLayoutToUI(int layoutID) {

        if (UIContainer == null) { Debug.Log("UI", "UI was null!"); return null; }
        final View[] inflatedLayout = { Objects.requireNonNull(GameView.GetInstance()).GetActivity().getLayoutInflater().inflate(layoutID, UIContainer, false) };
        Objects.requireNonNull(GameView.GetInstance()).GetActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (inflatedLayout[0] != null) {
                    UIContainer.addView(inflatedLayout[0]);
                }
            }
        });

        return inflatedLayout[0];
    }

    /**
     * Adds a layout component to the UI container, will not add if layout is null
     *
     * @param layoutID the ID for the UI to be added to the UI container
     * @param params additional linear layout parameters for the added UI
     *
     * @return the added UI Layout
     */
    public View AddViewLayoutToUI(int layoutID, LinearLayout.LayoutParams params) {
        if (UIContainer == null) { return null; }
        final View[] inflatedLayout = {Objects.requireNonNull(GameView.GetInstance()).GetActivity().getLayoutInflater().inflate(layoutID, UIContainer, false)};
        Objects.requireNonNull(GameView.GetInstance()).GetActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (inflatedLayout[0] != null) {
                    inflatedLayout[0].setLayoutParams(params);
                    UIContainer.addView(inflatedLayout[0]);
                }
            }
        });
        return inflatedLayout[0];
    }

    ///Removes all current UI elements
    public void RemoveAllUI() {
        if (UIContainer == null) { return; }
        Objects.requireNonNull(GameView.GetInstance()).GetActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                UIContainer.removeAllViews();
            }
        });
    }

    public View AddLayoutToContainer(int layoutID, LinearLayout layout, LinearLayout.LayoutParams params)
    {
        if (layout == null) { return null; }
        final View[] inflatedLayout = {Objects.requireNonNull(GameView.GetInstance()).GetActivity().getLayoutInflater().inflate(layoutID, layout, false)};
        Objects.requireNonNull(GameView.GetInstance()).GetActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (inflatedLayout[0] != null) {
                    inflatedLayout[0].setLayoutParams(params);
                    layout.addView(inflatedLayout[0]);
                }
            }
        });
        return inflatedLayout[0];
    }

    public View AddLayoutToContainer(int layoutID, LinearLayout layout)
    {
        if (layout == null) { return null; }
        final View[] inflatedLayout = {Objects.requireNonNull(GameView.GetInstance()).GetActivity().getLayoutInflater().inflate(layoutID, layout, false)};
        Objects.requireNonNull(GameView.GetInstance()).GetActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (inflatedLayout[0] != null) {
                    layout.addView(inflatedLayout[0]);
                }
            }
        });
        return inflatedLayout[0];
    }

    public void AddViewToUI(View v, LinearLayout.LayoutParams params)
    {
        if (UIContainer == null) { return; }
        Objects.requireNonNull(GameView.GetInstance()).GetActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (v != null) {
                    v.setLayoutParams(params);
                    UIContainer.addView(v);
                }
            }
        });
    }

    public void AddViewToUI(View v)
    {
        if (UIContainer == null) { return; }
        Objects.requireNonNull(GameView.GetInstance()).GetActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (v != null) {
                    UIContainer.addView(v);
                }
            }
        });
    }

    public void AddViewToContainer(View v, LinearLayout layout, LinearLayout.LayoutParams params)
    {
        if (layout == null) { return; }
        Objects.requireNonNull(GameView.GetInstance()).GetActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (v != null) {
                    v.setLayoutParams(params);
                    layout.addView(v);
                }
            }
        });
    }

    public void AddViewToContainer(View v, LinearLayout layout)
    {
        if (layout == null) { return; }
        Objects.requireNonNull(GameView.GetInstance()).GetActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (v != null) {
                    layout.addView(v);
                }
            }
        });
    }

    public void AddViewToContainer(View v, GridLayout layout, GridLayout.LayoutParams params)
    {
        if (layout == null) { return; }
        Objects.requireNonNull(GameView.GetInstance()).GetActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (v != null) {
                    v.setLayoutParams(params);
                    layout.addView(v);
                }
            }
        });
    }

    public void AddViewToContainer(View v, GridLayout layout)
    {
        if (layout == null) { return; }
        Objects.requireNonNull(GameView.GetInstance()).GetActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (v != null) {
                    layout.addView(v);
                }
            }
        });
    }

    public void RemoveViewsFromLayout(LinearLayout layout)
    {
        if (layout == null) { return; }
        Objects.requireNonNull(GameView.GetInstance()).GetActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                layout.removeAllViews();
            }
        });
    }

    public void RemoveViewsFromLayout(GridLayout layout)
    {
        if (layout == null) { return; }
        Objects.requireNonNull(GameView.GetInstance()).GetActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                layout.removeAllViews();
            }
        });
    }

}
