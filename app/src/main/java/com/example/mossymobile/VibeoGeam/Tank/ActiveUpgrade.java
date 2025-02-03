package com.example.mossymobile.VibeoGeam.Tank;

import com.example.mossymobile.MossFramework.DesignPatterns.MutableWrapper;
import com.example.mossymobile.MossFramework.Math.MossMath;
import com.example.mossymobile.MossFramework.Math.Vector2Int;
import com.example.mossymobile.MossFramework.Systems.Audio.AudioManager;
import com.example.mossymobile.MossFramework.Systems.UserInput.Vibration;

public class ActiveUpgrade extends TankUpgrade {
    interface ActivateFunction {
        void activate();
    }
    public MutableWrapper<Float> cooldown;
    public float maxcooldown;
    public int cost;
    public ActivateFunction activate;
    public ActiveUpgrade(String name, int sprite, String soundName, MutableWrapper<Float> cooldown, int cost) {
        super(name, sprite, soundName);
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
            AudioManager.playSound(soundResourceName, MossMath.randFloatMinMax(0.9f, 1.1f));
        }
    }
}
