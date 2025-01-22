package com.example.mossymobile.MossFramework;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.mossymobile.MossFramework.Math.Vector2;
import com.example.mossymobile.MossFramework.Math.Vector2Int;
import com.example.mossymobile.MossFramework.Systems.Audio.AudioPlayer;
import com.example.mossymobile.MossFramework.Systems.Debugging.BuildConfig;
import com.example.mossymobile.MossFramework.Systems.Debugging.Debug;
import com.example.mossymobile.MossFramework.Systems.Physics.Collision;
import com.example.mossymobile.MossFramework.Systems.ScriptableObjects.ScriptableObject;
import com.example.mossymobile.MossFramework.Systems.Inspector.InspectorGUI;
import com.example.mossymobile.MossFramework.Systems.Messaging.MessageHub;
import com.example.mossymobile.MossFramework.Systems.Scenes.SceneManager;
import com.example.mossymobile.MossFramework.Systems.Time.Time;
import com.example.mossymobile.MossFramework.Systems.UserInput.Input;
import com.example.mossymobile.MossFramework.Systems.UserInput.UI;
import com.example.mossymobile.MossFramework.Testing.TestScene;
import com.example.mossymobile.R;


import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class Application {
    private AtomicInteger completedThreads = new AtomicInteger(0);
    private final Object cleanupLock = new Object();

    public static HashMap<Class<? extends MonoBehaviour>, Boolean> AllowClassDuplicates = new HashMap<>();

    ///This is to check if application should close (no shit sherlock)
    public static boolean closeApplication = false;
    public static boolean pause = false;


    ///This is to check if the game loop has started, mainly to prevent us from doing certain things at runtime (eg: creating new scenes)
    protected static boolean isRunning = false;


    ///This is to check if deltaTime should be updated, else it should be read-only and unmodifiable
    protected static boolean nextFrameUpdate = false;
    protected static Vector2 renderingRatio = new Vector2();

    public static HashMap<Integer, Bitmap> cachedSprites = new HashMap<>();


    public final boolean Start()
    {
        //if the game view is null, end the program (this should not happen)
        if (GameView.GetInstance() == null) { return false; }

        if (Debug.GetConfig() != BuildConfig.PRODUCTION)
        {
            InspectorGUI.GetInstance().AddLayoutComponent("Hierarchy", GameView.GetInstance().GetActivity().findViewById(R.id.gameObjectList));
            InspectorGUI.GetInstance().AddLayoutComponent("Components", GameView.GetInstance().GetActivity().findViewById(R.id.componentList));
            InspectorGUI.GetInstance().AddLayoutComponent("GOName", GameView.GetInstance().GetActivity().findViewById(R.id.currGOName));
            InspectorGUI.GetInstance().AddLayoutComponent("LogScroll", GameView.GetInstance().GetActivity().findViewById(R.id.logScroll));

            InspectorGUI.GetInstance().AddLayoutComponent("CollisionsTitlebar", GameView.GetInstance().GetActivity().findViewById(R.id.collisionTagsHorizontal));
            InspectorGUI.GetInstance().AddLayoutComponent("Collisions", GameView.GetInstance().GetActivity().findViewById(R.id.collisionTagsVertical));

            ImageButton downScrollBtn = GameView.GetInstance().GetActivity().findViewById(R.id.logDownScroll);
            downScrollBtn.setOnClickListener(v -> {
                Debug.AutoScrollDown = !Debug.AutoScrollDown;
            });

            Button debugTab = GameView.GetInstance().GetActivity().findViewById(R.id.debugBtn);
            Button collisionTab = GameView.GetInstance().GetActivity().findViewById(R.id.collisionBtn);
            FrameLayout utilTab =  GameView.GetInstance().GetActivity().findViewById(R.id.utilTab);

            debugTab.setOnClickListener(v -> {
                for (int i = 0; i < utilTab.getChildCount(); i++)
                {
                    utilTab.getChildAt(i).setVisibility(View.INVISIBLE);
                }
                utilTab.getChildAt(1).setVisibility(View.VISIBLE);
            });

            collisionTab.setOnClickListener(v -> {
                for (int i = 0; i < utilTab.getChildCount(); i++)
                {
                    utilTab.getChildAt(i).setVisibility(View.INVISIBLE);
                }
                utilTab.getChildAt(0).setVisibility(View.VISIBLE);
            });

        }

        ///TODO: Move this into OnStart() for actual project
        Collision.CreateCollisionLayer("Default");

        UI.GetInstance().SetUIContainer(R.id.gameUIDocker);

        return OnStart();
    }

    public final void Run()
    {

        while (!Objects.requireNonNull(GameView.GetInstance()).IsSurfaceReady() && Objects.requireNonNull(GameView.GetInstance()).IsSurfaceValid()) { continue; }

        Collision.InitialiseCollisionMatrix();
        //In the event our game view is invalid (this should not happen)
        if (!Objects.requireNonNull(GameView.GetInstance()).IsSurfaceValid())
        {
            Debug.LogWarning("Application::Run()", "Game View was invalid! Is the surface view null or too small?", "Game View Invalid");
        }

        Vector2 screenDimensions = Objects.requireNonNull(GameView.GetInstance()).GetDeviceDimensions().ToFloat();
        Vector2 gameViewSize = new Vector2Int(Objects.requireNonNull(GameView.GetInstance()).getWidth(), Objects.requireNonNull(GameView.GetInstance()).getHeight()).ToFloat();

        renderingRatio = Vector2.Div(gameViewSize, screenDimensions);

        isRunning = true;
        OnRunStart();

        long prevTime = System.nanoTime();

        while (!closeApplication)
        {
            nextFrameUpdate = true; //encase the DT update here to avoid any modification from external locations

            long currentTime = System.nanoTime();
            float dt = (currentTime - prevTime) / 1000000000.0f; //get the time difference in nano seconds and calculate that as delta time
            prevTime = currentTime;

            //update both timers
            Time.UpdateDeltaTime(dt);
            Time.UpdateElapsedTime(dt);

            nextFrameUpdate = false;

            TextView fpsCounter = GameView.GetInstance().GetActivity().findViewById(R.id.frames);
            GameView.GetInstance().GetActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    fpsCounter.setText(1 / dt + " FPS");
                }
            });

            if (!pause) {
                Objects.requireNonNull(GameView.GetInstance()).canvas = Objects.requireNonNull(GameView.GetInstance()).LockCanvas();
                Objects.requireNonNull(GameView.GetInstance()).canvas.drawColor(0, android.graphics.PorterDuff.Mode.CLEAR);
            }

            if (!SceneManager.UpdateScenes()) //if scene list is empty, function will return false, if so, break program
            {
                Debug.LogError("Application::Run()", "No game scenes were running... ending program.");
                closeApplication = true;

                break;
            }

            if (Debug.GetConfig() != BuildConfig.PRODUCTION) {
                UpdateInspector();
            }

            AudioPlayer.Update();
            Objects.requireNonNull(GameView.GetInstance()).UnlockCanvasAndPost(Objects.requireNonNull(GameView.GetInstance()).canvas);
        }
    }

    private final void UpdateInspector()
    {
        InspectorGUI.GetInstance().UpdateHierarchyGUI("Hierarchy", R.layout.gameobjectdata);
    }

    public final void Exit()
    {
        OnExit();
        Collision.SaveCollisionMatrix();

        for (ScriptableObject so : ScriptableObject.LoadedObjects.values())
        {
            if (!so.ToSave()) { continue; }

            if (!so.IsExternal) {
                so.SaveToInternalStorage();
                continue;
            }
            so.SaveToExternalStorage();
        }


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
        return;
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
    public static Vector2 GetViewToScreenRatio()
    {
        return renderingRatio;
    }


}
