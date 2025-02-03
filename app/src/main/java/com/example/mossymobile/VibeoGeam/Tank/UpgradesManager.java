package com.example.mossymobile.VibeoGeam.Tank;

import com.example.mossymobile.MossFramework.DesignPatterns.MutableWrapper;
import com.example.mossymobile.MossFramework.DesignPatterns.Singleton;
import com.example.mossymobile.MossFramework.GameObject;
import com.example.mossymobile.MossFramework.Math.Vector2;
import com.example.mossymobile.MossFramework.Systems.Physics.Physics;
import com.example.mossymobile.R;
import com.example.mossymobile.VibeoGeam.Player;
import com.example.mossymobile.VibeoGeam.Scenes.GameScene;

import java.util.ArrayList;
import java.util.List;

public class UpgradesManager extends Singleton<UpgradesManager> {
    public static UpgradesManager GetInstance() {return Singleton.GetInstance(UpgradesManager.class);}
    public List<CannonInfo> cannonData = new ArrayList<>(){};
    public List<TankUpgrade> tankData = new ArrayList<>(){};

    public int PlayerCannonLevel = 0;
    public int PlayerActiveAbility = -1;
    public int PlayerPassiveAbility = -1;
    public int PlayerLevelPoints = 0;
    public boolean ScheduledCannonSwitch = false;
    public Player player;

    public void Init(Player player){
        PlayerCannonLevel = 0;
        PlayerLevelPoints = 90;
        PlayerActiveAbility = -1;
        PlayerPassiveAbility = -1;
        ScheduledCannonSwitch = true;
        this.player = player;
    }
    public void PopulateData()
    {
        cannonData.add(new CannonInfo(10f, 600f, 0,1.0f, 0, 0.10f, 12.5f, 4.0f, 0.70f,R.drawable.cannon, 0, "cxxx").SetCannonName("Cannon"));

        cannonData.add(new CannonInfo( 8f, 600f, 0,2.0f, 2, 0.30f, 02.0f, 3.0f, 0.35f, R.drawable.cannon1xx, 2, "cxxx").SetCannonName("Twin Barrels").SetBulletSize(8.0f));
        cannonData.add(new CannonInfo( 4f, 750f, 1,3.0f, 2, 0.10f, 01.0f, 1.8f, 0.10f, R.drawable.cannon2xx, 2, "c2xx").SetCannonName("Machine Gun").SetBulletSize(6.5f));
        cannonData.add(new CannonInfo( 3f, 900f, 2,5.0f, 2, 0.07f, 00.5f, 0.9f, 0.05f, R.drawable.cannon3xx, 3, "c3xx").SetCannonName("Minigun").SetBulletSize(5.0f));

        cannonData.add(new CannonInfo(15f, 700f, 1,0.7f, 0, 0.10f, 24.0f, 4.0f, 0.23f, R.drawable.cannonx1x, 3, "cxxx").SetCannonName("Longer Barrel"));
        cannonData.add(new CannonInfo(30f, 800f, 3,0.4f, 0, 0.15f, 35.0f, 3.0f, 0.16f, R.drawable.cannonx2x, 3, "cx2x").SetCannonName("Higher Calibre").SetBulletSize(12.5f));
        cannonData.add(new CannonInfo(120f,1100f,15,0.0f, 0, 0.30f, 50.0f, 1.6f, 0.09f, R.drawable.cannonx3x, 4,"cx3x").SetCannonName("Anti-Material").SetBulletSize(15.0f));

        cannonData.add(new CannonInfo(12f, 600f, 2,10.f, 1, 0.10f, 18.0f, 4.0f, 0.85f, R.drawable.cannonxx1, 1, "cxxx").SetCannonName("Buckshot").SetBurstFireInfo( 8, 0.2f).SetBulletSize(7.0f));
        cannonData.add(new CannonInfo( 8f, 650f, 1,16.f, 1, 0.15f, 08.0f, 1.5f, 0.90f, R.drawable.cannonxx2, 2, "cxx2").SetCannonName("Birdshot").SetBurstFireInfo(28, 0.4f).SetBulletSize(4.0f));
        cannonData.add(new CannonInfo(24f, 700f, 5,5.0f, 1, 0.10f, 20.0f, 2.0f, 0.87f, R.drawable.cannonxx3, 3, "cxx3").SetCannonName("Flechette").SetBurstFireInfo(20, 0.3f).SetBulletSize(9.5f));

        tankData.add(new BasicUpgrade("Repair", R.drawable.upgrade1xx,"", new int[]{0,1,1,2,3,4}, new float[]{0,0.5f,0.8f,1.3f,1.8f,2.4f}));
        tankData.add(new BasicUpgrade("Reload", R.drawable.upgrade2xx,"", new int[]{0,1,1,3,3,4}, new float[]{0,0.6f,1.2f,2.0f,3.4f,5.6f}));
        tankData.add(new BasicUpgrade("Reinforcement", R.drawable.upgrade3xx,"", new int[]{0,1,2,2,3,4}, new float[]{100f,120f,140f,160f,200f,250f}));

        tankData.add(new ActiveUpgrade("Thruster", R.drawable.upgradex1x, "ux1x", new MutableWrapper<Float>(0.6f), 2)
                .SetFunction(()->{
                    Vector2 dir;
                    if (player.movement.direction.MagnitudeSq() > 0) dir = player.movement.direction.FastNormalize();
                    else dir = Vector2.GetVectorFromAngle(player.GetTransform().GetRotation()-90);
                    dir.Mul(500.f);
                    player.getRb().AddForce(dir, Physics.ForceMode2D.IMPULSE);
                }).SetDescription("Boosts you in the direction you are moving"));
        tankData.add(new ActiveUpgrade("Grenade", R.drawable.upgradex2x, "ux2x", new MutableWrapper<Float>(3.f), 4)
                .SetFunction(()->{
                    GameObject grenade = new GameObject();
                    grenade.GetTransform().SetPosition(player.GetTransform().GetPosition());
                    Grenade src = grenade.AddComponent(Grenade.class);
                    Vector2 dir;
                    if (player.movement.direction.MagnitudeSq() > 0) dir = player.movement.direction.FastNormalize();
                    else dir = Vector2.GetVectorFromAngle(player.GetTransform().GetRotation()-90);
                    dir.Mul(600.f);
                    src.playerPosition = player;
                    src.targetPosition = Vector2.Add(player.GetTransform().GetPosition(),dir);
                }).SetDescription("Throws a grenade dealing AOE damage to enemies"));
        tankData.add(new ActiveUpgrade("Mini Tanks", R.drawable.upgradex3x, "ux3x", new MutableWrapper<Float>(15.f), 8)
                .SetFunction(()->{
                    Vector2 playerpos = player.GetTransform().GetPosition();
                    Vector2[] spawnpos = new Vector2[]{
                            Vector2.Add(playerpos, new Vector2(0f,-100f)),
                            Vector2.Add(playerpos, new Vector2(50f, 0.866f * 100f)),
                            Vector2.Add(playerpos, new Vector2(-50f,0.866f * 100f)),
                    };
                    for (int i = 0; i < 3;i++) {
                        GameObject minitank = new GameObject();
                        minitank.AddComponent(MiniTank.class);
                        minitank.GetTransform().SetPosition(spawnpos[i]);
                    }
                }).SetDescription("Deploys three small tanks to fight"));

        tankData.add( new PassiveUpgrade("Radioactive", R.drawable.upgradexx1, "", 18,
                ()->{
                    GameObject aura = new GameObject();
                    aura.AddComponent(Radiation.class).player = player;
                    return aura;
                })
                .SetDescription("Radiates a harmful aura that damages enemies in range"));
        tankData.add( new PassiveUpgrade("Trapper", R.drawable.upgradexx2, "", 12,
                ()->{
                    GameObject trapper = new GameObject();
                    trapper.AddComponent(Trapper.class).player = player;
                    return trapper;
                })
                .SetDescription("Periodically deploy caltrops that damage enemies that run into them"));
        tankData.add( new PassiveUpgrade("Turret", R.drawable.upgradexx3, "", 24,
                ()->{
                    GameObject turret = new GameObject();
                    turret.AddComponent(Turret.class).player = player;
                    return turret;
                })
                .SetDescription("Automatic turret that shoots at enemies"));
    }

    public CannonInfo FetchCannon(int id) { return cannonData.get(id); }
    public BasicUpgrade FetchBaseUpgrade(int id) { return (BasicUpgrade)tankData.get(id); }
    public ActiveUpgrade FetchActiveUpgrade(int id) { return (ActiveUpgrade)tankData.get(id+3); }
    public PassiveUpgrade FetchPassiveUpgrade(int id) { return (PassiveUpgrade)tankData.get(id+6); }

}
