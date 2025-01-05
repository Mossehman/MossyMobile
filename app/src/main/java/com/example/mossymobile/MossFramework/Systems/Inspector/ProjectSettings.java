package com.example.mossymobile.MossFramework.Systems.Inspector;

import android.widget.LinearLayout;

import com.example.mossymobile.MossFramework.DesignPatterns.Singleton;

import java.util.ArrayList;
import java.util.List;

public final class ProjectSettings extends Singleton<ProjectSettings> {

    public static ProjectSettings GetInstance()
    {
        return Singleton.GetInstance(ProjectSettings.class);
    }

    private List<String> CollisionLayers = new ArrayList<>();

    public void DisplayCollisionMatrix(LinearLayout verticalLayout)
    {

    }
}
