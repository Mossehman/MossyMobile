package com.example.mossymobile.MossFramework.Components;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;

import com.example.mossymobile.MossFramework.Math.Vector2;
import com.example.mossymobile.MossFramework.Math.Vector2Int;
import com.example.mossymobile.MossFramework.MonoBehaviour;

public class Renderer extends MonoBehaviour {

    public Vector2 RenderOffset = new Vector2();
    protected Bitmap sprite = null;

    public enum Ratio
    {
        FIXED,
        IMAGE
    }

    protected Ratio imageRatio = Ratio.FIXED;

    public void SetImageSizing(Ratio ratio)
    {
        this.imageRatio = ratio;
    }

    @Override
    public void Start() {
        return;
    }

    @Override
    public void Update() {
        return;
    }

    public void Render(Canvas canvas)
    {
        Transform transform = gameObject.GetTransform();
        if (!IsEnabled ||           //Renderer is not enabled, do not render
            transform == null ||    //GameObject has no positional value, do not render
            sprite == null)         //No sprite to render, do not render
        {
            return;
        }

        Vector2 position = transform.GetPosition();
        Vector2 scale = transform.GetScale();
        float rotation = transform.GetRotation();

        Vector2Int src = new Vector2Int();
        Vector2Int srcEnd = new Vector2Int(sprite.getWidth(), sprite.getHeight());

        Rect srcRect = new Rect(src.x, src.y, src.x + srcEnd.x, src.y + srcEnd.y);

        if (imageRatio == Ratio.FIXED)
        {
            srcEnd = new Vector2Int(1, 1);
        }

        RectF dstRect = new RectF(
                position.x - (srcEnd.x * scale.x) * 0.5f + RenderOffset.x,
                position.y - (srcEnd.y * scale.y) * 0.5f + RenderOffset.y,
                position.x + (srcEnd.x * scale.x) * 0.5f + RenderOffset.x,
                position.y + (srcEnd.y * scale.y) * 0.5f + RenderOffset.y
        );

        canvas.save();

        float centerX = position.x + (scale.x * srcEnd.x) * 0.5f + RenderOffset.x;
        float centerY = position.y + (scale.y * srcEnd.y) * 0.5f + RenderOffset.y;

        canvas.translate(centerX, centerY);
        canvas.rotate(rotation);
        canvas.translate(-centerX, -centerY);

        canvas.drawBitmap(sprite, srcRect, dstRect, null);
        canvas.restore();
    }
}
