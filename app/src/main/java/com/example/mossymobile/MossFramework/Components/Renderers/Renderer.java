package com.example.mossymobile.MossFramework.Components.Renderers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;

import com.example.mossymobile.MossFramework.Application;
import com.example.mossymobile.MossFramework.Components.Transform;
import com.example.mossymobile.MossFramework.DesignPatterns.MutableWrapper;
import com.example.mossymobile.MossFramework.GameView;
import com.example.mossymobile.MossFramework.Math.Vector2;
import com.example.mossymobile.MossFramework.Math.Vector2Int;
import com.example.mossymobile.MossFramework.MonoBehaviour;
import com.example.mossymobile.MossFramework.Systems.Debugging.Debug;

import java.util.Objects;

public class Renderer extends MonoBehaviour {

    public Vector2 RenderOffset = new Vector2();
    protected Bitmap sprite = null;
    protected Transform transform = null;

    public boolean DoAntiAliasing = false;
    protected MutableWrapper<Integer> zLayer = new MutableWrapper<>(0);

    Vector2 position = new Vector2();
    Vector2 scale = new Vector2();
    float rotation = 0.0f;

    Vector2 spriteScaling = new Vector2();

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
    public int ResourceID = -1;
    protected int InitialResID = -1;


    @Override
    public void Start() {
        transform = gameObject.GetTransform();
        Debug.Log("Transform", "transform");
    }

    @Override
    protected void InitializeInspectorData() {
        super.InitializeInspectorData();
        ShowInInspector("Sprite", sprite);
        ShowInInspector("ResourceID", ResourceID);
        ShowInInspector("Size", spriteScaling);
        EditInInspector("Layer", zLayer);
    }

    @Override
    public void Update() {
        if (InitialResID != ResourceID) {
            if (Application.cachedSprites.containsKey(ResourceID)) {
                sprite = Application.cachedSprites.get(ResourceID);
            }
            else
            {
                Bitmap bmp = BitmapFactory.decodeResource(Objects.requireNonNull(GameView.GetInstance()).GetContext().getResources(), ResourceID);
                Application.cachedSprites.put(ResourceID, bmp);

                this.sprite = bmp;
            }
            InitialResID = ResourceID;
        }

        if (!IsEnabled ||           //Renderer is not enabled, do not render
            transform == null ||    //GameObject has no positional value, do not render
            sprite == null)         //No sprite to render, do not render
        {
            return;
        }


        position = Vector2.Mul(Application.GetViewToScreenRatio(), transform.GetPosition());
        scale = Vector2.Mul(Application.GetViewToScreenRatio(), transform.GetScale());
        rotation = transform.GetRotation();


        if (gameObject.GetScene() != null) {
            gameObject.GetScene().RenderGameObject(this);
        }
    }

    public void Render(Canvas canvas)
    {
        if (!IsEnabled ||           //Renderer is not enabled, do not render
            transform == null ||    //GameObject has no positional value, do not render
            sprite == null)         //No sprite to render, do not render
        {
            return;
        }

        if (canvas == null)
        {
            Debug.LogError("Canvas", "Canvas is null!");
            return;
        }

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

        spriteScaling.x = (scale.x * srcEnd.x) * 0.5f;
        spriteScaling.y = (scale.y * srcEnd.y) * 0.5f;

        float centerX = dstRect.centerX();
        float centerY = dstRect.centerY();

        canvas.translate(centerX, centerY);
        canvas.rotate(rotation);
        canvas.translate(-centerX, -centerY);

        canvas.drawBitmap(sprite, srcRect, dstRect, null);
        canvas.restore();
    }

    /**
     *
     * @return the size of the sprite in world coordinates ({@code Vector2}).
     */
    public Vector2 GetSpriteSize()
    {
        Vector2Int srcEnd = new Vector2Int(sprite.getWidth(), sprite.getHeight());

        if (imageRatio == Ratio.FIXED)
        {
            srcEnd = new Vector2Int(1, 1);
        }

        return new Vector2((srcEnd.x * scale.x) * 0.5f, (srcEnd.y * scale.y) * 0.5f);
    }

    public final void SetSprite(int resourceID) {
        if (Application.cachedSprites.containsKey(resourceID)) {
            sprite = Application.cachedSprites.get(resourceID);
        }
        else
        {
            Bitmap bmp = BitmapFactory.decodeResource(Objects.requireNonNull(GameView.GetInstance()).GetContext().getResources(), resourceID);
            Application.cachedSprites.put(resourceID, bmp);

            this.sprite = bmp;
        }
        this.ResourceID = resourceID;
        InitialResID = resourceID;

    }

    public final void SetZLayer(int layer)
    {
        this.zLayer.value = layer;
    }

    public final int GetZLayer()
    {
        return this.zLayer.value;
    }

}
