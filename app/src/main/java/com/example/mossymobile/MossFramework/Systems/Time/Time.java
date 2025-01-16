package com.example.mossymobile.MossFramework.Systems.Time;

import com.example.mossymobile.MossFramework.Application;
import com.example.mossymobile.MossFramework.Systems.Debugging.Debug;

public class Time {
    private static float elapsedTime = 0.0f;
    private static float deltaTime = 0.0f;

    private static float fixedTime = 0.02f;

    public static void SetFixedTime(float time)
    {
        if (!Application.IsRunning()) {
            fixedTime = time;
            return;
        }
        Debug.LogError("Time::SetFixedTime(" + time + ")", "The physics TimeStep cannot be modified at runtime!");
    }

    public static void UpdateDeltaTime(float newDT)
    {
        if (Application.IsFrameUpdate()) {
            if (newDT > 0.1f)
            {
                deltaTime = 0.1f;
            }
            else
            {
                deltaTime = newDT;
            }

            return;
        }
        Debug.LogError("Time::UpdateDeltaTime(" + newDT + ")", "Delta time is a read-only value and cannot be manually modified.");
    }

    public static void UpdateElapsedTime(float newDT)
    {
        if (Application.IsFrameUpdate()) {
            elapsedTime += newDT;
            return;
        }
        Debug.LogError("Time::UpdateElapsedTime(" + newDT + ")", "Elapsed time is a read-only value and cannot be manually modified.");
    }

    public static float GetElapsedTime() { return elapsedTime; }
    public static float GetDeltaTime() { return deltaTime; }
    public static float GetFixedTime() { return fixedTime; }

}
