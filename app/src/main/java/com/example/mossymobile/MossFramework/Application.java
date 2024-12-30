package com.example.mossymobile.MossFramework;

import com.example.mossymobile.MossFramework.Components.Collider;
import com.example.mossymobile.MossFramework.Components.Transform;
import com.example.mossymobile.MossFramework.Systems.Debugging.Debug;
import com.example.mossymobile.MossFramework.Systems.Scenes.SceneManager;
import com.example.mossymobile.MossFramework.Systems.Time.Time;

public class Application {

    ///This is to check if application should close (no shit sherlock)
    public static boolean CloseApplication = false;

    ///This is to check if the game loop has started, mainly to prevent us from doing certain things at runtime (eg: creating new scenes)
    protected static boolean isRunning = false;

    ///This is to check if deltaTime should be updated, else it should be read-only and unmodifiable
    protected static boolean nextFrameUpdate = false;


    ///The screen our game scene will be rendered to
    protected GameView gameView = null;

    public final boolean Start()
    {
        //if the game view is null, end the program (this should not happen)
        if (gameView == null) { return false; }

        return OnStart();
    }

    public final void Run()
    {
        while (!gameView.IsSurfaceReady() && gameView.IsSurfaceValid()) { continue; }

        //In the event our game view is invalid (this should not happen)
        if (!gameView.IsSurfaceValid())
        {
            Debug.LogWarning("Application::Run()", "Game View was invalid! Is the surface view null or too small?", "Game View Invalid");
        }

        isRunning = true;
        OnRunStart();

        long prevTime = System.nanoTime();

        while (!CloseApplication)
        {
            nextFrameUpdate = true; //encase the DT update here to avoid any modification from external locations

            long currentTime = System.nanoTime();
            float dt = (currentTime - prevTime) / 1000000000.0f; //get the time difference in nano seconds and calculate that as delta time
            prevTime = currentTime;

            //update both timers
            Time.UpdateDeltaTime(dt);
            Time.UpdateElapsedTime(dt);

            nextFrameUpdate = false;

            if (!SceneManager.UpdateScenes()) //if scene list is empty, function will return false, if so, break program
            {
                Debug.LogError("Application::Run()", "No game scenes were running... ending program.");
                CloseApplication = true;

                break;


            }
        }
    }

    public final void Exit()
    {
        SceneManager.Exit();
    }

    protected boolean OnStart()
    {
        return true;
    }

    protected void OnRunStart() {}

    protected void OnRun() {}

    protected void OnExit() {}

    public static boolean IsRunning()
    {
        return isRunning;
    }

    public static boolean IsFrameUpdate()
    {
        return nextFrameUpdate;
    }

}
