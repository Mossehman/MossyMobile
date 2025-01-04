package com.example.mossymobile.MossFramework.DesignPatterns;

import com.example.mossymobile.MossFramework.Systems.Debugging.Debug;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;

public class Factory {
    public static <T> T CreateObject(Class<T> ObjectType)
    {
        try {
            return ObjectType.getDeclaredConstructor().newInstance();
        }
        catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e)
        {
            Debug.LogError("Factory::CreateObject()", "Error when creating object...");
            return null;
        }
    }

    public static <T> T CreateObject(Class<T> ObjectType, String debugName)
    {
        try {
            return ObjectType.getDeclaredConstructor().newInstance();
        }
        catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e)
        {
            Debug.LogError("Factory::CreateObject()", "Error when creating object... (" + debugName + ")");
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public static <T extends Serializable> T CopyObject(T Object)
    {
        try (ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
             ObjectOutputStream out = new ObjectOutputStream(byteOut)) {

            out.writeObject(Object);

            try (ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
                 ObjectInputStream in = new ObjectInputStream(byteIn)) {

                return (T) in.readObject();
            }
        } catch (IOException | ClassNotFoundException e) {
            Debug.LogError("Factory::CopyObject()", "Error when copying object... ensure all data in the object is serializable and that there is no circular dependency");
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public static <T extends Serializable> T CopyObject(T Object, String debugName)
    {
        try (ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
             ObjectOutputStream out = new ObjectOutputStream(byteOut)) {

            out.writeObject(Object);

            try (ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
                 ObjectInputStream in = new ObjectInputStream(byteIn)) {

                return (T) in.readObject();
            }
        } catch (IOException | ClassNotFoundException e) {
            Debug.LogError("Factory::CopyObject()", "Error when copying object... (" + debugName + "), ensure all data in the object is serializable and that there is no circular dependency");
            return null;
        }
    }
}
