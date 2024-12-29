package com.example.mossymobile.MossFramework;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder holder;
    private boolean SurfaceReady = false;
    private boolean SurfaceValid = true;

    public GameView(Context context, AttributeSet attributes) {
        super(context, attributes);
        holder = getHolder();
        holder.addCallback(this);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {

        if (holder.getSurface() != null && holder.getSurface().isValid())
        {
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

    public boolean IsSurfaceValid()
    {
        return SurfaceValid;
    }

    public boolean IsSurfaceReady()
    {
        return SurfaceReady;
    }

    public Canvas LockCanvas()
    {
        return holder.lockCanvas();
    }

    public void UnlockCanvasAndPost(Canvas canvas)
    {
        holder.unlockCanvasAndPost(canvas);
    }

}
