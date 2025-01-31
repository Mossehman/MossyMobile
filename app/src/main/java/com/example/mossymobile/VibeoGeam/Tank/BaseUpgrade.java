package com.example.mossymobile.VibeoGeam.Tank;

public class BaseUpgrade extends TankUpgrade {
    public int[] lvlCosts;// = {0,1,1,2,3,4};
    public float[] mods;// = {0,1,1,2,3,4};
    public int currentLvl;// = 0;
    public BaseUpgrade(String name, int sprite, int[] lvlCosts, float[] mods) {
        super(name, sprite);
        this.lvlCosts = lvlCosts;
        this.mods = mods;
        currentLvl = 0;
    }
    public float GetCurrentMod() {
        return mods[currentLvl];
    }
}
