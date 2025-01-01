package com.example.mossymobile.MossFramework;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import java.lang.ref.WeakReference;

@SuppressLint("ViewConstructor")
public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    private static WeakReference<GameView> MainGameView = null;
    private Context ctx = null;
    private Activity activity = null;


    private SurfaceHolder holder = null;
    private boolean SurfaceReady = false;
    private boolean SurfaceValid = true;

    public Canvas canvas = null;

    public GameView(Context context, AttributeSet attributes) {
        super(context, attributes);

        ctx = context;

        holder = getHolder();
        holder.addCallback(this);
    }

    public void Init(Activity activity)
    {
        this.activity = activity;

        if (MainGameView == null) {
            MainGameView = new WeakReference<>(this);
        }
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {

        if (holder.getSurface() != null && holder.getSurface().isValid()) {
            SurfaceValid = true;
            SurfaceReady = true;
            return;
        }
        SurfaceReady = false;

    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        SurfaceReady = false;
    }

    public boolean IsSurfaceValid() {
        return SurfaceValid;
    }

    public boolean IsSurfaceReady() {
        return SurfaceReady;
    }

    public Canvas LockCanvas() {
        return holder.lockCanvas();
    }

    public void UnlockCanvasAndPost(Canvas canvas) {
        holder.unlockCanvasAndPost(canvas);
    }

    public static GameView GetInstance() {
        return MainGameView != null ? MainGameView.get() : null;
    }

    public Activity GetActivity() {
        return activity;
    }

    public Context GetContext()
    {
        return ctx;
    }

}
