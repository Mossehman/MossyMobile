package com.example.mossymobile.MossFramework.Systems.Files;

import android.os.Environment;

public class SerializerTest {
    public static boolean IsExternalStorageReadable()
    {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(Environment.getExternalStorageState()))
        {
            return true;
        }

        return false;
    }
}
