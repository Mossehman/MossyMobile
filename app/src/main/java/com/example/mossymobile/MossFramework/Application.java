package com.example.mossymobile.MossFramework;

public class Application {

    public static boolean CloseApplication = false;
    protected static boolean isRunning = false;
    protected static boolean nextFrameUpdate = false;

    protected GameView gameView = null;

    public final boolean Start()
    {
        if (gameView == null) { return false; }

        return true;
    }

    public final void Run()
    {
        while (!gameView.IsSurfaceReady() && gameView.IsSurfaceValid()) { continue; }

        isRunning = true;
        OnRunStart();

        while (!CloseApplication)
        {
            nextFrameUpdate = true;

            ///TODO: Implement timer updates here

            nextFrameUpdate = false;
        }
    }

    public final void Exit()
    {

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
