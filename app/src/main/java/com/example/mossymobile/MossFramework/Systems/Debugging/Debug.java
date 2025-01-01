package com.example.mossymobile.MossFramework.Systems.Debugging;

import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mossymobile.MossFramework.GameView;
import com.example.mossymobile.MossFramework.Systems.Inspector.InspectorGUI;
import com.example.mossymobile.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

///Helper class to allow for an in-app debugger, allows for debugging on the mobile device for convenience, most features are stripped in Release/Prod.
public class Debug {
    private static BuildConfig buildConfig = null;
    public static boolean DebugEnabled = true;
    public static boolean WarningsEnabled = true;
    public static boolean ErrorsEnabled = true;

    private static final List<String> DisabledWarningCodes = new ArrayList<>();

    private static int LogID = 0;

    ///Initialises the build configuration
    public static void SetConfig(BuildConfig build)
    {
        if (buildConfig != null) { return; }
        buildConfig = build;

    }

    public static BuildConfig GetConfig() { return buildConfig; }

    /**
     * Disables a specific warning code via string identifier.
     *
     * @param WarningCode The specified warning code to disable.
     *
     */
    public static void DisableWarning(String WarningCode)
    {
        if (DisabledWarningCodes.contains(WarningCode) || buildConfig == BuildConfig.PRODUCTION) { return; }
        DisabledWarningCodes.add(WarningCode);
    }

    /**
     * Re-enables a disabled warning code via string identifier.
     *
     * @param WarningCode The specified warning code to re-enable.
     *
     */
    public static void EnableWarning(String WarningCode)
    {
        if (buildConfig == BuildConfig.PRODUCTION) { return; }
        DisabledWarningCodes.remove(WarningCode);
    }


    ///TODO: Change modify the Log code to also output data to the application itself as opposed to just being a wrapper for Logcat

    //Default log
    public static void Log (String tag,int value)
    {
        if (buildConfig == BuildConfig.PRODUCTION || !DebugEnabled) {
            return;
        }
        Log.d(tag, "" + value);

        PrintLogToGUI(tag, "" + value, Color.WHITE);
    }

    public static void Log (String tag,boolean value)
    {
        if (buildConfig == BuildConfig.PRODUCTION || !DebugEnabled) {
            return;
        }

        if (value) {
            Log.d(tag, "TRUE");
            PrintLogToGUI(tag, "TRUE", Color.WHITE);
        } else {
            Log.d(tag, "FALSE");
            PrintLogToGUI(tag, "FALSE", Color.WHITE);
        }
    }

    public static void Log (String tag,float value)
    {
        if (buildConfig == BuildConfig.PRODUCTION || !DebugEnabled) {
            return;
        }
        Log.d(tag, "" + value);

        PrintLogToGUI(tag, "" + value, Color.WHITE);
    }

    public static void Log (String tag,double value)
    {
        if (buildConfig == BuildConfig.PRODUCTION || !DebugEnabled) {
            return;
        }
        Log.d(tag, "" + value);
        PrintLogToGUI(tag, "" + value, Color.WHITE);
    }

    public static void Log (String tag,long value)
    {
        if (buildConfig == BuildConfig.PRODUCTION || !DebugEnabled) {
            return;
        }
        Log.d(tag, "" + value);
        PrintLogToGUI(tag, "" + value, Color.WHITE);
    }

    public static void Log (String tag,char value)
    {
        if (buildConfig == BuildConfig.PRODUCTION || !DebugEnabled) {
            return;
        }
        Log.d(tag, "" + value);
        PrintLogToGUI(tag, "" + value, Color.WHITE);
    }

    public static void Log (String tag, String value)
    {
        if (buildConfig == BuildConfig.PRODUCTION || !DebugEnabled) {
            return;
        }
        Log.d(tag, value);

        PrintLogToGUI(tag, value, Color.WHITE);
    }

    public static void Log (String tag, ILoggable value)
    {
        if (buildConfig == BuildConfig.PRODUCTION || !DebugEnabled) {
            return;
        }
        Log.d(tag, value.GetLogStatement());
        PrintLogToGUI(tag, value.GetLogStatement(), Color.WHITE);
    }



    public static void LogWarning (String tag, int value, String WarningCode)
    {
        if (buildConfig == BuildConfig.PRODUCTION || !WarningsEnabled || DisabledWarningCodes.contains(WarningCode)) {
            return;
        }
        Log.w(tag, "" + value);
    }

    public static void LogWarning (String tag,boolean value, String WarningCode)
    {
        if (buildConfig == BuildConfig.PRODUCTION || !WarningsEnabled || DisabledWarningCodes.contains(WarningCode)) {
            return;
        }

        if (value) {
            Log.w(tag, "TRUE");
        } else {
            Log.w(tag, "FALSE");
        }
    }

    public static void LogWarning (String tag,float value, String WarningCode)
    {
        if (buildConfig == BuildConfig.PRODUCTION || !WarningsEnabled || DisabledWarningCodes.contains(WarningCode)) {
            return;
        }
        Log.w(tag, "" + value);
    }

    public static void LogWarning (String tag,double value, String WarningCode)
    {
        if (buildConfig == BuildConfig.PRODUCTION || !WarningsEnabled || DisabledWarningCodes.contains(WarningCode)) {
            return;
        }
        Log.w(tag, "" + value);
    }

    public static void LogWarning (String tag,long value, String WarningCode)
    {
        if (buildConfig == BuildConfig.PRODUCTION || !WarningsEnabled || DisabledWarningCodes.contains(WarningCode)) {
            return;
        }
        Log.w(tag, "" + value);
    }

    public static void LogWarning (String tag,char value, String WarningCode)
    {
        if (buildConfig == BuildConfig.PRODUCTION || !WarningsEnabled || DisabledWarningCodes.contains(WarningCode)) {
            return;
        }
        Log.w(tag, "" + value);
    }

    public static void LogWarning (String tag, String value, String WarningCode)
    {
        if (buildConfig == BuildConfig.PRODUCTION || !WarningsEnabled || DisabledWarningCodes.contains(WarningCode)) {
            return;
        }
        Log.w(tag, value);
    }

    public static void LogWarning (String tag, ILoggable value, String WarningCode)
    {
        if (buildConfig == BuildConfig.PRODUCTION || !WarningsEnabled || DisabledWarningCodes.contains(WarningCode)) {
            return;
        }
        Log.w(tag, value.GetLogStatement());
    }

    public static void LogWarning (String tag, int value)
    {
        LogWarning(tag, value, "Default Warning Tag");
    }

    public static void LogWarning (String tag,boolean value)
    {
        LogWarning(tag, value, "Default Warning Tag");
    }

    public static void LogWarning (String tag,float value)
    {
        LogWarning(tag, value, "Default Warning Tag");
    }

    public static void LogWarning (String tag,double value)
    {
        LogWarning(tag, value, "Default Warning Tag");
    }

    public static void LogWarning (String tag,long value)
    {
        LogWarning(tag, value, "Default Warning Tag");
    }

    public static void LogWarning (String tag,char value)
    {
        LogWarning(tag, value, "Default Warning Tag");
    }

    public static void LogWarning (String tag, String value)
    {
        LogWarning(tag, value, "Default Warning Tag");
    }

    public static void LogWarning (String tag, ILoggable value)
    {
        LogWarning(tag, value, "Default Warning Tag");
    }





    public static void LogError (String tag,int value)
    {
        if (buildConfig == BuildConfig.PRODUCTION || !ErrorsEnabled) {
            return;
        }
        Log.e(tag, "" + value);
    }

    public static void LogError (String tag,boolean value)
    {
        if (buildConfig == BuildConfig.PRODUCTION || !ErrorsEnabled) {
            return;
        }

        if (value) {
            Log.e(tag, "TRUE");
        } else {
            Log.e(tag, "FALSE");
        }
    }

    public static void LogError (String tag,float value)
    {
        if (buildConfig == BuildConfig.PRODUCTION || !ErrorsEnabled) {
            return;
        }
        Log.e(tag, "" + value);
    }

    public static void LogError (String tag,double value)
    {
        if (buildConfig == BuildConfig.PRODUCTION || !ErrorsEnabled) {
            return;
        }
        Log.e(tag, "" + value);
    }

    public static void LogError (String tag,long value)
    {
        if (buildConfig == BuildConfig.PRODUCTION || !ErrorsEnabled) {
            return;
        }
        Log.e(tag, "" + value);
    }

    public static void LogError (String tag,char value)
    {
        if (buildConfig == BuildConfig.PRODUCTION || !ErrorsEnabled) {
            return;
        }
        Log.e(tag, "" + value);
    }

    public static void LogError (String tag, String value)
    {
        if (buildConfig == BuildConfig.PRODUCTION || !ErrorsEnabled) {
            return;
        }
        Log.e(tag, value);

        PrintLogToGUI(tag, value, Color.RED);


    }

    public static void LogError (String tag, ILoggable value)
    {
        if (buildConfig == BuildConfig.PRODUCTION || !ErrorsEnabled) {
            return;
        }
        Log.e(tag, value.GetLogStatement());
        PrintLogToGUI(tag, value.GetLogStatement(), Color.RED);
    }

    private static void PrintLogToGUI(String tag, String data, int color)
    {
        Objects.requireNonNull(GameView.GetInstance()).GetActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LogID++;
                LinearLayout logDataPanel = (LinearLayout) InspectorGUI.GetInstance().GetLayoutComponent("LogPanel");
                View logComponent = Objects.requireNonNull(GameView.GetInstance()).GetActivity().getLayoutInflater().inflate(R.layout.logcomponent, logDataPanel, false);
                TextView logData = logComponent.findViewById(R.id.logData);
                TextView logDesc = logComponent.findViewById(R.id.logDesc);

                String tagData = tag + " (" + LogID + ")";

                logData.setText(tagData);
                logDesc.setText(data);

                logDesc.setTextColor(color);

                logDataPanel.addView(logComponent);
            }
        });
    }

}
