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
    public int PlayerCannonLevel = 0;
    public int PlayerLevelPoints = 0;
    public boolean ScheduledCannonSwitch = false;
    public void PopulateData()
    {
        cannonData.add(new CannonInfo(10f, 600f, 0,1.0f, 0, 0.10f, 20.0f, 4.0f, 0.70f,R.drawable.cannon, 0));

        cannonData.add(new CannonInfo( 8f, 600f, 0,2.0f, 2, 0.30f, 02.0f, 3.0f, 0.35f, R.drawable.cannon1xx, 2));
        cannonData.add(new CannonInfo( 4f, 750f, 1,3.0f, 2, 0.10f, 01.0f, 3.0f, 0.10f, R.drawable.cannon2xx, 2));
        cannonData.add(new CannonInfo( 3f, 900f, 2,5.0f, 2, 0.04f, 00.5f, 1.6f, 0.05f, R.drawable.cannon3xx, 3));

        cannonData.add(new CannonInfo(15f, 700f, 1,0.7f, 0, 0.10f, 24.0f, 4.0f, 0.23f, R.drawable.cannonx1x, 3));
        cannonData.add(new CannonInfo(30f, 800f, 3,0.4f, 0, 0.15f, 35.0f, 3.0f, 0.10f, R.drawable.cannonx2x, 3));
        cannonData.add(new CannonInfo(90f,1100f,10,0.0f, 0, 0.30f, 70.0f, 1.6f, 0.03f, R.drawable.cannonx3x, 4));

        cannonData.add(new CannonInfo( 7f, 600f, 2,7.0f, 1, 0.10f, 18.0f, 4.0f, 0.85f, R.drawable.cannonxx1, 1));
        cannonData.add(new CannonInfo( 3f, 650f, 0,10.f, 1, 0.15f, 08.0f, 1.5f, 0.90f, R.drawable.cannonxx2, 2));
        cannonData.add(new CannonInfo(14f, 700f, 5,5.0f, 1, 0.10f, 20.0f, 2.0f, 0.87f, R.drawable.cannonxx3, 3));

    }

    public CannonInfo FetchCannon(int id)
    {
        return cannonData.get(id);
    }
}
