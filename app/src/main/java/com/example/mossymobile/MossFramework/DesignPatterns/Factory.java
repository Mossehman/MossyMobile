package com.example.mossymobile.MossFramework.DesignPatterns;

import java.lang.reflect.InvocationTargetException;

public class Factory {
    public static <T> T CreateObject(Class<T> ObjectType)
    {
        try {
            return ObjectType.getDeclaredConstructor().newInstance();
        }
        catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e)
        {
            throw new RuntimeException("Error creating object instance!", e);
        }
    }

    public static <T> T CreateObject(Class<T> ObjectType, String debugName)
    {
        try {
            return ObjectType.getDeclaredConstructor().newInstance();
        }
        catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e)
        {
            throw new RuntimeException("Error creating object instance: " + debugName, e);
        }
    }
}
