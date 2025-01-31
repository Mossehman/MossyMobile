package com.example.mossymobile.MossFramework.Systems.UserInput;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;

public class Sensors {
    public static SensorManager sensorManager;
    public static SensorEvent sensorEvent = null;

    public static int sensorAccuracy = SensorManager.SENSOR_STATUS_ACCURACY_HIGH;

    public static boolean CheckSensorsWorking() {
        return sensorAccuracy >= SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM;
    }

}
