package com.example.mossymobile.VibeoGeam.Tank;

public abstract class TankUpgrade {
    public String upgradename = "Upgrade";
    public int spriteResourceID = -1;
    public TankUpgrade(String name, int sprite)
    {
        upgradename = name;
        spriteResourceID = sprite;
    }

}
