package com.example.mossymobile.MossFramework.DesignPatterns;

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
            throw new RuntimeException("Error during object copy", e);
        }
    }
}
