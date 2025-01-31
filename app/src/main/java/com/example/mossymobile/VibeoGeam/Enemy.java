package com.example.mossymobile.VibeoGeam;

import com.example.mossymobile.MossFramework.DesignPatterns.MutableWrapper;
import com.example.mossymobile.MossFramework.MonoBehaviour;
import com.example.mossymobile.MossFramework.Systems.Physics.Collision;

import java.util.Objects;

public abstract class Enemy extends MonoBehaviour implements IDamageable {
    public MutableWrapper<Float> health;
    public int expGain;
    public float moveSpeed;
    public float damage;
    public Player player;
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

    @Override
    public void OnCollisionEnter(Collision collision) {
        if (Objects.equals(collision.GetCollider().GetCollisionLayer(), "Player")) {
            collision.GetGameObject().GetComponent(Player.class).earnExp(expGain * 0.5f).ModifyHealth(damage);
            numOfEnemies.value--;

            Destroy(this.gameObject);
        }
    }
}