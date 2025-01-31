package com.example.mossymobile.MossFramework.Systems.UserInput;

import android.content.Context;
import android.os.VibrationEffect;
import android.os.Vibrator;

import com.example.mossymobile.MossFramework.GameView;
import com.example.mossymobile.MossFramework.Math.Vector2Int;

import java.util.Objects;

public class Vibration {
    private static Vibrator vibrator = null;

    public static void Vibrate(Vector2Int vibration)
    {
        if (vibrator == null)
        {
            vibrator = (Vibrator) Objects.requireNonNull(GameView.GetInstance()).GetContext().getSystemService(Context.VIBRATOR_SERVICE);
        }

        vibrator.vibrate(VibrationEffect.createOneShot(vibration.x, vibration.y));
    }

    public static void Vibrate(long[] timings, int repeat)
    {
        if (vibrator == null)
        {
            vibrator = (Vibrator) Objects.requireNonNull(GameView.GetInstance()).GetContext().getSystemService(Context.VIBRATOR_SERVICE);
        }

        vibrator.vibrate(VibrationEffect.createWaveform(timings, repeat));
    }
}
