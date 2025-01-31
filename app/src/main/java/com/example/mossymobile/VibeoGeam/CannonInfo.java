package com.example.mossymobile.VibeoGeam;

public class CannonInfo {
    public float damage = 10f; // damage it will deal
    public float speed = 10f; // how fast it will travel
    public int pierce = 0; // number of enemies it can go through
    public float spread = 0f; // varied bullet spread in degrees
    public int firetype = 0; // arbitrary number for firing type (0 - single, 1 - burst, 2 - auto)
    public float fireinterval = 0.4f; // time between each shot
    public float ammocost = 1f; // cost to fire this bullet
    public float lifetime = 4f; // how long can the bullet exist
    public float aimspeed = 1f; // how long the gun takes to turn
    public int spriteResourceID = -1; // sprite image for the player
    public int lvlcost = 1;
    public int numofpellets = 1; // number of pellets fired per shot, only available when firetype == 1
    public float randspeed = 0f; // random modifier to bullet speed (+- multiplicative), only available when firetype == 1
    public float bulletsize = 10f; // size of the bullet
    public int soundResourceID = -1; // sound effect for the shooting
    public String cannonname; // name of the cannon
    public CannonInfo(float damage, float speed, int pierce, float spread,
                      int firetype, float fireinterval, float ammocost, float lifetime,
                      float aimspeed, int spriteResourceID, int lvlcost)
    {
        this.damage = damage;
        this.speed = speed;
        this.pierce = pierce;
        this.spread = spread;
        this.firetype = firetype;
        this.fireinterval = fireinterval;
        this.ammocost = ammocost;
        this.lifetime = lifetime;
        this.aimspeed = aimspeed;
        this.spriteResourceID = spriteResourceID;
        this.lvlcost = lvlcost;
    }

    public CannonInfo SetBurstFireInfo(int numofpellets, float randspeed){
        this.numofpellets = numofpellets;
        this.randspeed = randspeed;
        return this;
    }

    public CannonInfo SetBulletSize(float bulletsize) {
        this.bulletsize = bulletsize;
        return this;
    }

    public CannonInfo SetSoundResourceID(int soundResourceID) {
        this.soundResourceID = soundResourceID;
        return this;
    }

    public CannonInfo SetCannonName(String cannonname){
        this.cannonname = cannonname;
        return this;
    }
}
