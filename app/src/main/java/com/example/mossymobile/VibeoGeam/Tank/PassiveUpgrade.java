package com.example.mossymobile.VibeoGeam.Tank;

import com.example.mossymobile.MossFramework.DesignPatterns.Factory;
import com.example.mossymobile.MossFramework.GameObject;
import com.example.mossymobile.MossFramework.Math.Vector2Int;
import com.example.mossymobile.MossFramework.MonoBehaviour;
import com.example.mossymobile.MossFramework.Systems.UserInput.Vibration;

public class PassiveUpgrade extends TankUpgrade {
    interface Init{
        GameObject Initalize();
    }
    public int cost;
    Init initFunc;

    public PassiveUpgrade(String name, int sprite, int cost, Init func) {
        super(name, sprite);
        this.cost = cost;
        initFunc = func;
    }

    public PassiveUpgrade SetDescription(String desc){
        upgradedescription = desc;
        return this;
    }

    public GameObject Initalize(){
        return initFunc.Initalize();
    }
}
