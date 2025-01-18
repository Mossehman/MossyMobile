package com.example.mossymobile.MossFramework.Components.Renderers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;

import com.example.mossymobile.MossFramework.Application;
import com.example.mossymobile.MossFramework.Components.Transform;
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


    @Override
    public void Start() {
        transform = gameObject.GetTransform();
        rotation = transform.GetRotation();

        if (Application.cachedSprites.containsKey(ResourceID)) {
            sprite = Application.cachedSprites.get(ResourceID);
        }
        else
        {
            Bitmap bmp = BitmapFactory.decodeResource(Objects.requireNonNull(GameView.GetInstance()).GetContext().getResources(), ResourceID);
            Application.cachedSprites.put(ResourceID, bmp);

            this.sprite = bmp;
        }

        if (sprite == null)
        {
            Debug.LogError("Sprite", "sprite is null");
        }

    }

    @Override
    protected void InitializeInspectorData() {
        super.InitializeInspectorData();
        ShowInInspector("Sprite", sprite);
        ShowInInspector("ResourceID", ResourceID);
        ShowInInspector("Size", spriteScaling);
    }

    @Override
    public void Update() {

        if (!IsEnabled ||           //Renderer is not enabled, do not render
            transform == null ||    //GameObject has no positional value, do not render
            sprite == null)         //No sprite to render, do not render
        {
            return;
        }


        transform.SetPosition(new Vector2(transform.GetPosition().x, transform.GetPosition().y + 1));

        position = Vector2.Mul(Application.GetViewToScreenRatio(), transform.GetPosition());
        scale = Vector2.Mul(Application.GetViewToScreenRatio(), transform.GetScale());


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

        float centerX = position.x + (scale.x * srcEnd.x) * 0.5f + RenderOffset.x;
        float centerY = position.y + (scale.y * srcEnd.y) * 0.5f + RenderOffset.y;

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
}
