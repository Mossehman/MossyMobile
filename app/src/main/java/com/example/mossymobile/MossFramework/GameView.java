package com.example.mossymobile.MossFramework;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Insets;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.WindowMetrics;

import androidx.annotation.NonNull;

import com.example.mossymobile.MossFramework.Math.Vector2Int;

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

    public Vector2Int GetDeviceDimensions()
    {
        WindowManager windowManager = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();

        WindowMetrics windowMetrics = windowManager.getCurrentWindowMetrics();
        Insets insets = windowMetrics.getWindowInsets()
                .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars());
        int width = windowMetrics.getBounds().width() - insets.left - insets.right;
        int height = windowMetrics.getBounds().height() - insets.top - insets.bottom;
        return new Vector2Int(width, height);
    }

}
