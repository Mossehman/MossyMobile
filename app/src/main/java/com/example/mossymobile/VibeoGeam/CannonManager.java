package com.example.mossymobile.VibeoGeam;

import com.example.mossymobile.MossFramework.DesignPatterns.Singleton;
import com.example.mossymobile.MossFramework.Systems.Inspector.InspectorGUI;
import com.example.mossymobile.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CannonManager extends Singleton<CannonManager> {
    public static CannonManager GetInstance()
    {
        return Singleton.GetInstance(CannonManager.class);
    }

    public List<CannonInfo> cannonData = new ArrayList<>(){};

    public void PopulateData()
    {
        // looks stupid i know
        cannonData.add(new CannonInfo(10f, 600f, 0, 1f, 0, 0.10f, 20.0f, 4.0f, 0.70f,R.drawable.cannon));
        cannonData.add(new CannonInfo( 8f, 600f, 0, 2f, 2, 0.30f, 02.0f, 3.0f, 0.35f, R.drawable.cannon1xx));
        cannonData.add(new CannonInfo( 4f, 750f, 1, 3f, 2, 0.10f, 01.0f, 3.0f, 0.10f, R.drawable.cannon2xx));
        cannonData.add(new CannonInfo( 3f, 900f, 2, 5f, 2, 0.04f, 00.5f, 1.6f, 0.05f, R.drawable.cannon3xx));
    }

    public CannonInfo FetchCannon(int id)
    {
        return cannonData.get(id);
    }
}
