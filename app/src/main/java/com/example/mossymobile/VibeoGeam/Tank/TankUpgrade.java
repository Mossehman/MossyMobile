package com.example.mossymobile.VibeoGeam.Tank;

import com.example.mossymobile.MossFramework.GameObject;

public abstract class TankUpgrade {
    public String upgradename;
    public String upgradedescription = "No Description";
    public int spriteResourceID = -1;
    public TankUpgrade(String name, int sprite)
    {
        upgradename = name;
        spriteResourceID = sprite;
    }
}

