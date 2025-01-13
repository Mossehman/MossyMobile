package com.example.mossymobile.MossFramework.Systems.ScriptableObjects;

import android.content.Context;
import android.content.res.AssetManager;

import com.example.mossymobile.MossFramework.DesignPatterns.Factory;
import com.example.mossymobile.MossFramework.GameView;
import com.example.mossymobile.MossFramework.Systems.Debugging.Debug;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Objects;

public abstract class ScriptableObject implements Serializable {
    public static HashMap<String, ScriptableObject> LoadedObjects = new HashMap<>();

    protected String name = "New ScriptableObject";
    protected String originalName = "New ScriptableObject";
    public boolean IsExternal = false;
    private boolean toSave = true;
    public String directory = null;


    ///This method is to allow for constructor-like behaviour when loading the Scriptable Object (since loading from files does not call the constructor)
    protected abstract void OnLoad();

    /**
     * Attempts to create a {@code ScriptableObject} based on filepath. If an existing file of the same name exists in the storage, it will instead load that.
     *
     * @param filepath the file name to search for in the storage.
     * @param soClass the class of the {@code ScriptableObject}.
     *
     * @return a new {@code ScriptableObject}, either loaded from the specified file or newly generated.
     */
    public static <T extends ScriptableObject> T Create(String filepath, String directory, Class<T> soClass, boolean IsExternal) {
        if (LoadedObjects.containsKey(filepath)) {
            Debug.LogWarning("ScriptableObject::Create()", "Scriptable Object file (" + filepath + ") already exists!");
            return (T) LoadedObjects.get(filepath);
        }

        File fileToRead;
        if (IsExternal) {
            fileToRead = new File(Objects.requireNonNull(GameView.GetInstance()).GetContext().getExternalFilesDir(directory), filepath);
        }
        else {
            fileToRead = new File(Objects.requireNonNull(GameView.GetInstance()).GetContext().getFilesDir(), filepath);
        }

        try (FileInputStream inputStream = new FileInputStream(fileToRead);
             ObjectInputStream objInput = new ObjectInputStream(inputStream)) {

            T returnVal = soClass.cast(objInput.readObject());
            if (returnVal != null) {
                returnVal.OnLoad();
                returnVal.name = filepath;
                returnVal.originalName = filepath;
                returnVal.IsExternal = IsExternal;
                returnVal.directory = directory;
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
                returnVal.IsExternal = IsExternal;
                returnVal.directory = directory;

                LoadedObjects.put(filepath, returnVal);
                Debug.Log("ScriptableObject::Create()", "No File found, creating new Object!");

                return returnVal;
            } catch (Exception ex) {
                Debug.LogError("ScriptableObject::Create()", "Creation of new ScriptableObject failed!");
                return null;
            }
        }
    }

    public static <T extends ScriptableObject> T Create(String filepath, Class<T> soClass)
    {
        return ScriptableObject.Create(filepath, null, soClass, false);
    }

    public static <T extends ScriptableObject> T Create(String filepath, Class<T> soClass, boolean IsExternal)
    {
        return ScriptableObject.Create(filepath, null, soClass, IsExternal);
    }

    public static <T extends ScriptableObject> T Create(String filepath, String directory, Class<T> soClass)
    {
        return ScriptableObject.Create(filepath, directory, soClass, false);
    }

    public static <T extends ScriptableObject> boolean WriteFromResources(String filepath, String directory, Class<T> soClass)
    {
        if (LoadedObjects.containsKey(filepath)) {
            Debug.LogWarning("ScriptableObject::Create()", "Scriptable Object file (" + filepath + ") already exists!");
            return true;
        }

        AssetManager assets = Objects.requireNonNull(GameView.GetInstance()).GetContext().getAssets();
        try {
            InputStream inputStream = assets.open(filepath);
            File folder = new File(Objects.requireNonNull(GameView.GetInstance()).GetContext().getFilesDir(), directory);
            if (!folder.exists() && directory != null) {
                if (!folder.mkdirs()) {
                    Debug.LogError("ScriptableObject::LoadFromResources()", "Error when generating folder!");
                    return false;
                }
            }

            File targetFile = new File(folder, filepath);
            if (targetFile.exists())
            {
                return true;
            }

            try (FileOutputStream outputStream = new FileOutputStream(targetFile)) {
                byte[] buffer = new byte[2048];
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }
            }

            return true;


        } catch (IOException e) {
            Debug.LogError("ScriptableObject::LoadFromResources()", "Error when reading from Assets folder!");
            return false;
        }

    }

    /**
     * Saves the {@code ScriptableObject} to a specified file in external storage depending on it's name. If its name is changed, the file with the original name is deleted and replaced.
     */
    public final void SaveToExternalStorage() {
        Context ctx = Objects.requireNonNull(GameView.GetInstance()).getContext();

        //name was changed during runtime
        if (!name.equals(originalName))
        {
            File file = new File(ctx.getExternalFilesDir(directory), originalName);
            if (file.exists()) {
                file.delete();
            }
        }

        File externalFile = new File(ctx.getExternalFilesDir(directory), name);
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

    /**
     * Saves the {@code ScriptableObject} to a specified file in internal storage depending on it's name. If its name is changed, the file with the original name is deleted and replaced.
     */
    public final void SaveToInternalStorage() {
        Context ctx = Objects.requireNonNull(GameView.GetInstance()).getContext();

        //name was changed during runtime
        if (!name.equals(originalName))
        {
            File file = new File(ctx.getFilesDir(), originalName);
            if (file.exists()) {
                file.delete();
            }
        }

        File internalFile = new File(ctx.getFilesDir(), name);
        try (ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
             ObjectOutputStream objectOut = new ObjectOutputStream(byteOut);
             FileOutputStream fileOut = new FileOutputStream(internalFile)) {

            objectOut.writeObject(this);
            objectOut.flush();

            fileOut.write(byteOut.toByteArray());
        }
        catch (IOException e) {
            throw new RuntimeException("Error saving object to file", e);
        }
    }

    /**
     * Attempts to create a {@code ScriptableObject} that will not be saved to the file directories.
     *
     * @param name the name of the {@code ScriptableObject}.
     * @param soClass the class of the {@code ScriptableObject}.
     *
     * @return a newly generated {@code ScriptableObject}.
     */
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

    /**
     * Attempts to create a {@code ScriptableObject} that will not be saved to the file directories, performs a deep copy of a specified ScriptableObject.
     *
     * @param object the {@code ScriptableObject} to deep copy.
     *
     * @return a copied {@code ScriptableObject}.
     */
    public static <T extends ScriptableObject> T Instantiate(T object)
    {
        return Factory.CopyObject(object);
    }


    public boolean ToSave()
    {
        return this.toSave;
    }


}
