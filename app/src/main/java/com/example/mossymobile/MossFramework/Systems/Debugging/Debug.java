package com.example.mossymobile.MossFramework.Systems.Debugging;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

///Helper class to allow for an in-app debugger, allows for debugging on the mobile device for convenience, most features are stripped in Release/Prod.
public class Debug {
    private static BuildConfig buildConfig = null;
    public static boolean DebugEnabled = true;
    public static boolean WarningsEnabled = true;
    public static boolean ErrorsEnabled = true;

    private static List<String> DisabledWarningCodes = new ArrayList<>();

    ///Initialises the build configuration
    public Debug(BuildConfig build)
    {
        buildConfig = build;
    }

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
    }

    public static void Log (String tag,boolean value)
    {
        if (buildConfig == BuildConfig.PRODUCTION || !DebugEnabled) {
            return;
        }

        if (value) {
            Log.d(tag, "TRUE");
        } else {
            Log.d(tag, "FALSE");
        }
    }

    public static void Log (String tag,float value)
    {
        if (buildConfig == BuildConfig.PRODUCTION || !DebugEnabled) {
            return;
        }
        Log.d(tag, "" + value);
    }

    public static void Log (String tag,double value)
    {
        if (buildConfig == BuildConfig.PRODUCTION || !DebugEnabled) {
            return;
        }
        Log.d(tag, "" + value);
    }

    public static void Log (String tag,long value)
    {
        if (buildConfig == BuildConfig.PRODUCTION || !DebugEnabled) {
            return;
        }
        Log.d(tag, "" + value);
    }

    public static void Log (String tag,char value)
    {
        if (buildConfig == BuildConfig.PRODUCTION || !DebugEnabled) {
            return;
        }
        Log.d(tag, "" + value);
    }

    public static void Log (String tag, String value)
    {
        if (buildConfig == BuildConfig.PRODUCTION || !DebugEnabled) {
            return;
        }
        Log.d(tag, value);
    }

    public static void Log (String tag, ILoggable value)
    {
        if (buildConfig == BuildConfig.PRODUCTION || !DebugEnabled) {
            return;
        }
        Log.d(tag, value.GetLogStatement());
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
    }

    public static void LogError (String tag, ILoggable value)
    {
        if (buildConfig == BuildConfig.PRODUCTION || !ErrorsEnabled) {
            return;
        }
        Log.e(tag, value.GetLogStatement());
    }

    public static void Test(String test)
    {
        return;
    }


}
