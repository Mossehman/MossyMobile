package com.example.mossymobile.MossFramework.Systems.Time;

import com.example.mossymobile.MossFramework.Application;
import com.example.mossymobile.MossFramework.Systems.Debugging.Debug;

public class Time {
    private static float elapsedTime = 0.0f;
    private static float deltaTime = 0.0f;

    public static void UpdateDeltaTime(float newDT)
    {
        if (Application.IsFrameUpdate()) {
            deltaTime = newDT;
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

}
