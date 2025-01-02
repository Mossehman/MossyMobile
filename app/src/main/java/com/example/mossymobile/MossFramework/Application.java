package com.example.mossymobile.MossFramework;

import android.graphics.Canvas;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.mossymobile.MossFramework.Systems.Debugging.BuildConfig;
import com.example.mossymobile.MossFramework.Systems.Debugging.Debug;
import com.example.mossymobile.MossFramework.Systems.Inspector.InspectorGUI;
import com.example.mossymobile.MossFramework.Systems.Messaging.MessageHub;
import com.example.mossymobile.MossFramework.Systems.Scenes.SceneManager;
import com.example.mossymobile.MossFramework.Systems.Time.Time;
import com.example.mossymobile.MossFramework.Testing.TestScene;
import com.example.mossymobile.R;

import java.util.HashMap;
import java.util.Objects;

public class Application {
    public static HashMap<Class<? extends MonoBehaviour>, Boolean> AllowClassDuplicates = new HashMap<>();

    ///This is to check if application should close (no shit sherlock)
    public static boolean CloseApplication = false;

    ///This is to check if the game loop has started, mainly to prevent us from doing certain things at runtime (eg: creating new scenes)
    protected static boolean isRunning = false;

    ///This is to check if deltaTime should be updated, else it should be read-only and unmodifiable
    protected static boolean nextFrameUpdate = false;
    protected Canvas canvas = null;

    public final boolean Start()
    {
        //if the game view is null, end the program (this should not happen)
        if (GameView.GetInstance() == null) { return false; }
        canvas = Objects.requireNonNull(GameView.GetInstance()).canvas;

        if (Debug.GetConfig() != BuildConfig.RELEASE)
        {
            InspectorGUI.GetInstance().AddLayoutComponent("Hierarchy", GameView.GetInstance().GetActivity().findViewById(R.id.gameObjectList));
            InspectorGUI.GetInstance().AddLayoutComponent("Components", GameView.GetInstance().GetActivity().findViewById(R.id.componentList));
            InspectorGUI.GetInstance().AddLayoutComponent("GOName", GameView.GetInstance().GetActivity().findViewById(R.id.currGOName));
            InspectorGUI.GetInstance().AddLayoutComponent("LogScroll", GameView.GetInstance().GetActivity().findViewById(R.id.logScroll));

            ImageButton downScrollBtn = GameView.GetInstance().GetActivity().findViewById(R.id.logDownScroll);
            downScrollBtn.setOnClickListener(v -> {
                Debug.AutoScrollDown = !Debug.AutoScrollDown;
            });

        }

        return OnStart();
    }

    public final void Run()
    {
        while (!Objects.requireNonNull(GameView.GetInstance()).IsSurfaceReady() && Objects.requireNonNull(GameView.GetInstance()).IsSurfaceValid()) { continue; }

        //In the event our game view is invalid (this should not happen)
        if (!Objects.requireNonNull(GameView.GetInstance()).IsSurfaceValid())
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

            canvas = Objects.requireNonNull(GameView.GetInstance()).LockCanvas();
            canvas.drawColor(0, android.graphics.PorterDuff.Mode.CLEAR);

            if (!SceneManager.UpdateScenes()) //if scene list is empty, function will return false, if so, break program
            {
                Debug.LogError("Application::Run()", "No game scenes were running... ending program.");
                CloseApplication = true;

                break;
            }
            if (Debug.GetConfig() != BuildConfig.PRODUCTION) {
                UpdateInspector();
            }

            Objects.requireNonNull(GameView.GetInstance()).UnlockCanvasAndPost(canvas);
        }
    }

    private final void UpdateInspector()
    {
        InspectorGUI.GetInstance().UpdateHierarchyGUI("Hierarchy", R.layout.gameobjectdata);
    }

    public final void Exit()
    {
        OnExit();

        //cleanup all the systems
        SceneManager.Exit();
        MessageHub.Exit();

        System.gc();
    }

    protected boolean OnStart()
    {
        SceneManager.AddToSceneList("TestScene", new TestScene());
        return true;
    }

    protected void OnRunStart() {}

    protected void OnRun() {}

    protected void OnExit() {
    }

    public static boolean IsRunning()
    {
        return isRunning;
    }

    public static boolean IsFrameUpdate()
    {
        return nextFrameUpdate;
    }

    public static <T extends MonoBehaviour> boolean CheckAllowDuplicates(Class<?> type)
    {
        if (AllowClassDuplicates.containsKey(type))
        {
            return Boolean.TRUE.equals(AllowClassDuplicates.get(type));
        }

        return false;
    }


}
