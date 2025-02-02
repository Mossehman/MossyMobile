package com.example.mossymobile.MossFramework.Systems.UserInput;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class Sensors implements SensorEventListener {
    public static SensorManager sensorManager;
    private static Sensor accelerometer;

    // Shake detection variables
    private static final float SHAKE_THRESHOLD = 12.0f; // Adjust for sensitivity
    private static final int SHAKE_COOLDOWN_MS = 300;   // Minimum time between shakes
    private long lastShakeTime = 0;

    private static Sensors instance;
    private OnShakeListener shakeListener;
    public static int sensorAccuracy = SensorManager.SENSOR_STATUS_ACCURACY_HIGH;

    public static Sensors GetInstance(Context context) {
        if (instance == null) {
            instance = new Sensors();
            sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }
        return instance;
    }

    public static boolean CheckSensorsWorking() {
        return sensorAccuracy >= SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM;
    }

    public void StartListening(OnShakeListener listener) {
        this.shakeListener = listener;
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
    }

    public void StopListening() {
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            // Calculate the shake force (acceleration magnitude)
            float acceleration = (float) Math.sqrt(x * x + y * y + z * z) - SensorManager.GRAVITY_EARTH;

            if (acceleration > SHAKE_THRESHOLD) {
                long currentTime = System.currentTimeMillis();
                if (currentTime - lastShakeTime > SHAKE_COOLDOWN_MS) {
                    lastShakeTime = currentTime;
                    if (shakeListener != null) {
                        shakeListener.onShake(); // Notify shake event
                    }
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not used for shake detection
    }

    // Interface for handling shake events
    public interface OnShakeListener {
        void onShake();
    }
}
