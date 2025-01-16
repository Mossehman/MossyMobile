package com.example.mossymobile.MossFramework.Systems.Physics;

import com.example.mossymobile.MossFramework.Math.Vector2;

public final class Physics {
    public static Vector2 Gravity = new Vector2(0, 10);
    public static float AirResistance = 0.1f;

    public enum ForceMode2D {
        DEFAULT,
        IMPULSE
    }

}
