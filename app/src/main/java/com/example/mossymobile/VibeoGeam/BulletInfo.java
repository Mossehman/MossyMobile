package com.example.mossymobile.VibeoGeam;

public class BulletInfo {
    public float damage = 10f; // damage it will deal
    public float speed = 10f; // how fast it will travel
    public int pierce = 0; // number of enemies it can go through
    public float spread = 0f; // varied bullet spread in degrees
    public int firetype = 0; // arbitrary number for firing type (0 - single, 1 - burst, 2 - auto)
    public float fireinterval = 0.4f; // time between each shot
    public float ammocost = 1f; // cost to fire this bullet
    public float lifetime = 4f; // how long can the bullet exist
    public BulletInfo(float damage, float speed, int pierce, float spread, int firetype, float fireinterval, float ammocost, float lifetime){
        this.damage = damage;
        this.speed = speed;
        this.pierce = pierce;
        this.spread = spread;
        this.firetype = firetype;
        this.fireinterval = fireinterval;
        this.ammocost = ammocost;
        this.lifetime = lifetime;
    }
}
