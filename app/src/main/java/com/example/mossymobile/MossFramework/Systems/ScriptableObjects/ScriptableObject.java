package com.example.mossymobile.MossFramework.Systems.ScriptableObjects;

import android.content.Context;

import com.example.mossymobile.MossFramework.DesignPatterns.Factory;
import com.example.mossymobile.MossFramework.GameView;
import com.example.mossymobile.MossFramework.Systems.Debugging.Debug;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Objects;

public abstract class ScriptableObject implements Serializable {
    public static HashMap<String, ScriptableObject> LoadedObjects = new HashMap<>();

    protected String name = "New ScriptableObject";
    protected String originalName = "New ScriptableObject";

    ///This method is to allow for constructor-like behaviour when loading the Scriptable Object (since loading from files does not call the constructor)
    protected abstract void OnLoad();

    public static <T extends ScriptableObject> T Create(String filepath, Class<T> soClass) {
        if (LoadedObjects.containsKey(filepath)) {
            Debug.LogWarning("ScriptableObject::Create()", "Scriptable Object file (" + filepath + ") already exists!");
            return (T) LoadedObjects.get(filepath);
        }

        File fileToRead = new File(Objects.requireNonNull(GameView.GetInstance()).GetContext().getExternalFilesDir(null), filepath);
        try (FileInputStream inputStream = new FileInputStream(fileToRead);
             ObjectInputStream objInput = new ObjectInputStream(inputStream)) {

            T returnVal = soClass.cast(objInput.readObject());
            if (returnVal != null) {
                returnVal.OnLoad();
                returnVal.name = filepath;
                returnVal.originalName = filepath;
            }


            LoadedObjects.put(filepath, returnVal);
            Debug.Log("ScriptableObject::Create()", "File found!");

            return returnVal;
        }
        catch (IOException | ClassNotFoundException e) {
            Debug.LogError("ScriptableObject::LoadObjFromFile()", "Error when loading Scriptable Object from file... Creating new ScriptableObject");

            try {
                T returnVal = soClass.newInstance();

                returnVal.OnLoad();
                returnVal.name = filepath;
                returnVal.originalName = filepath;

                LoadedObjects.put(filepath, returnVal);
                Debug.Log("ScriptableObject::Create()", "No File found, creating new Object!");

                return returnVal;
            } catch (Exception ex) {
                Debug.LogError("ScriptableObject::Create()", "Creation of new ScriptableObject failed!");
                return null;
            }
        }
    }
    public final void SaveToStorage() {
        Context ctx = Objects.requireNonNull(GameView.GetInstance()).getContext();

        //name was changed during runtime
        if (!name.equals(originalName))
        {
            File file = new File(ctx.getExternalFilesDir(null), originalName);
            if (file.exists()) {
                file.delete();
            }
        }

        File externalFile = new File(ctx.getExternalFilesDir(null), name);
        try (ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
             ObjectOutputStream objectOut = new ObjectOutputStream(byteOut);
             FileOutputStream fileOut = new FileOutputStream(externalFile)) {

            objectOut.writeObject(this);
            objectOut.flush();

            fileOut.write(byteOut.toByteArray());
        }
        catch (IOException e) {
            throw new RuntimeException("Error saving object to file", e);
        }
    }

    public static <T extends ScriptableObject> T CreateUnsaved(String name, Class<T> soClass)
    {
        T val = Factory.CreateObject(soClass);

        if (val != null) {
            val.name = name;
            val.originalName = name;

            val.OnLoad();
        }
        return val;
    }

    public static <T extends ScriptableObject> T Instantiate(T object)
    {
        return Factory.CopyObject(object);
    }



}
