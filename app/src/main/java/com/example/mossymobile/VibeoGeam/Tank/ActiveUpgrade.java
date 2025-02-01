package com.example.mossymobile.VibeoGeam.Tank;

import com.example.mossymobile.MossFramework.DesignPatterns.MutableWrapper;
import com.example.mossymobile.MossFramework.Math.Vector2Int;
import com.example.mossymobile.MossFramework.Systems.UserInput.Vibration;

public class ActiveUpgrade extends TankUpgrade {
    interface ActivateFunction {
        void activate();
    }
    public MutableWrapper<Float> cooldown;
    public float maxcooldown;
    public int cost;
    public ActivateFunction activate;
    public ActiveUpgrade(String name, int sprite, MutableWrapper<Float> cooldown, int cost) {
        super(name, sprite);
        this.cooldown = cooldown;
        this.cost = cost;
        maxcooldown = cooldown.value;
    }

    public ActiveUpgrade SetFunction(ActivateFunction func){
        activate = func;
        return this;
    }
    public ActiveUpgrade SetDescription(String desc){
        upgradedescription = desc;
        return this;
    }
    public void TriggerActive(){
        if (activate != null) {
            activate.activate();
            Vibration.Vibrate(new Vector2Int(1000, 20));
        }
    }
}
