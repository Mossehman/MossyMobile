package com.example.mossymobile.VibeoGeam;

import com.example.mossymobile.MossFramework.DesignPatterns.MutableWrapper;
import com.example.mossymobile.MossFramework.GameObject;
import com.example.mossymobile.MossFramework.MonoBehaviour;

public abstract class Enemy extends MonoBehaviour implements IDamageable {
    public MutableWrapper<Float> health;
    public int expGain;
    public float moveSpeed;
    public float damage;
    public GameObject player;
    public int resourceID;
    public MutableWrapper<Integer> numOfEnemies;
    public BarMeter hpBar;
    public Enemy(float hp, int xp, float speed, float dmg, int resID)
    {
        health = new MutableWrapper<Float>(hp);
        expGain = xp;
        moveSpeed = speed;
        damage = dmg;
        resourceID = resID;
    }
}