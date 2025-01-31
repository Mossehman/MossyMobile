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

    public void Init(){
        PlayerCannonLevel = 0;
        PlayerLevelPoints = 0;
    }
    public void PopulateData()
    {
        cannonData.add(new CannonInfo(10f, 600f, 0,1.0f, 0, 0.10f, 12.5f, 4.0f, 0.70f,R.drawable.cannon, 0).SetCannonName("Cannon"));

        cannonData.add(new CannonInfo( 8f, 600f, 0,2.0f, 2, 0.30f, 02.0f, 3.0f, 0.35f, R.drawable.cannon1xx, 2).SetCannonName("Twin Barrels").SetBulletSize(8.0f));
        cannonData.add(new CannonInfo( 4f, 750f, 1,3.0f, 2, 0.10f, 01.0f, 1.8f, 0.10f, R.drawable.cannon2xx, 2).SetCannonName("Machine Gun").SetBulletSize(6.5f));
        cannonData.add(new CannonInfo( 3f, 900f, 2,5.0f, 2, 0.07f, 00.5f, 0.9f, 0.05f, R.drawable.cannon3xx, 3).SetCannonName("Minigun").SetBulletSize(5.0f));

        cannonData.add(new CannonInfo(15f, 700f, 1,0.7f, 0, 0.10f, 24.0f, 4.0f, 0.23f, R.drawable.cannonx1x, 3).SetCannonName("Longer Barrel"));
        cannonData.add(new CannonInfo(30f, 800f, 3,0.4f, 0, 0.15f, 35.0f, 3.0f, 0.16f, R.drawable.cannonx2x, 3).SetCannonName("Higher Calibre").SetBulletSize(12.5f));
        cannonData.add(new CannonInfo(120f,1100f,15,0.0f, 0, 0.30f, 50.0f, 1.6f, 0.09f, R.drawable.cannonx3x, 4).SetCannonName("Anti-Material").SetBulletSize(15.0f));

        cannonData.add(new CannonInfo(12f, 600f, 2,10.f, 1, 0.10f, 18.0f, 4.0f, 0.85f, R.drawable.cannonxx1, 1).SetCannonName("Buckshot").SetBurstFireInfo( 8, 0.2f).SetBulletSize(7.0f));
        cannonData.add(new CannonInfo( 8f, 650f, 1,16.f, 1, 0.15f, 08.0f, 1.5f, 0.90f, R.drawable.cannonxx2, 2).SetCannonName("Birdshot").SetBurstFireInfo(28, 0.4f).SetBulletSize(4.0f));
        cannonData.add(new CannonInfo(24f, 700f, 5,5.0f, 1, 0.10f, 20.0f, 2.0f, 0.87f, R.drawable.cannonxx3, 3).SetCannonName("Flechette").SetBurstFireInfo(20, 0.3f).SetBulletSize(9.5f));

    }

    public CannonInfo FetchCannon(int id)
    {
        return cannonData.get(id);
    }
}
