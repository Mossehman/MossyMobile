package com.example.mossymobile.MossFramework.Systems.Scenes;
import com.example.mossymobile.MossFramework.Scene;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SceneManager {
    private static final HashMap<String, Scene> GameScenes = new HashMap<>();
    private static List<Scene> CurrentScenes = new ArrayList<>();
    private static List<Scene> ScenesToUnload = new ArrayList<>();
    private static Scene NextScene = null;

    private static SceneLoadMode sceneLoad = SceneLoadMode.SINGLE;

    public static boolean LoadScene(String sceneID, SceneLoadMode sceneLoadMode)
    {
        //Scene is invalid
        if (GameScenes.isEmpty() || !GameScenes.containsKey(sceneID)) { return false; }

        // Next scene's init was successfully called, scene can be added
        NextScene = GameScenes.get(sceneID);
        sceneLoad = sceneLoadMode;
        return true;
    }

    public static boolean LoadScene(String sceneID)
    {
        return LoadScene(sceneID, SceneLoadMode.SINGLE);
    }

    public static boolean UnloadScene(String sceneID)
    {
        if (GameScenes.containsKey(sceneID) &&                  //Scene we are trying to unload is contained within the scene list
            CurrentScenes.contains(GameScenes.get(sceneID)) &&  //Scene we are trying to unload is currently running
            CurrentScenes.size() > 1)                           //Scene we are trying to unload is NOT the only scene currently running
        {
            ScenesToUnload.add(GameScenes.get(sceneID)); //Queue to scene to be unloaded
            return true; //Successfully unloaded the scene
        }
        return false; //Scene unload was unsuccessful
    }

    public static boolean AddToSceneList(String sceneID, Scene scene)
    {
        return AddToSceneList(sceneID, scene, false);
    }

    public static boolean AddToSceneList(String sceneID, Scene scene, boolean overrideCurrentID)
    {
        //If ID is already present and we do not want to override the current scene, do not add
        if (!overrideCurrentID && GameScenes.containsKey(sceneID))
        {
            return false;
        }

        //Add scene to the list of scenes
        GameScenes.put(sceneID, scene);
        if (CurrentScenes.isEmpty()) //if the current scene list is empty
        {
            LoadScene(sceneID); //load the newly added scene as the current scene
        }

        return true;
    }

    public static List<Scene> GetCurrentScene()
    {
        return CurrentScenes;
    }

    public static Scene GetScene(String sceneName)
    {
        if (GameScenes.containsKey(sceneName)) { return GameScenes.get(sceneName); }
        return null;
    }

    public static boolean UpdateScenes()
    {
        if (NextScene != null && !CurrentScenes.contains(NextScene))
        {
            if (sceneLoad == SceneLoadMode.SINGLE)
            {
                for (Scene scene : CurrentScenes)
                {
                    scene.Exit(); //cleanup the existing scenes
                }

                CurrentScenes.clear();
            }
            NextScene.Init();
            CurrentScenes.add(NextScene);
        }

        if (!ScenesToUnload.isEmpty())
        {
            for (Scene scene : ScenesToUnload)
            {
                scene.Exit();
                CurrentScenes.remove(scene);
            }
        }

        if (CurrentScenes.isEmpty()) { return false; } //after adding and cleaning the scene list, if there is no scene left, return false


        for (Scene scene : CurrentScenes) //loop through and update all our existing scenes
        {
            scene.Run();
        }

        return true;
    }

}
