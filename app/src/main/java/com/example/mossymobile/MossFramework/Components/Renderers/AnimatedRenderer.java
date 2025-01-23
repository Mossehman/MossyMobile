package com.example.mossymobile.MossFramework.Components.Renderers;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;

import androidx.annotation.NonNull;

import com.example.mossymobile.MossFramework.DesignPatterns.MutableWrapper;
import com.example.mossymobile.MossFramework.Math.MossMath;
import com.example.mossymobile.MossFramework.Math.Vector2Int;
import com.example.mossymobile.MossFramework.Systems.Debugging.Debug;
import com.example.mossymobile.MossFramework.Systems.Time.Time;

import java.util.HashMap;
import java.util.Objects;

public class AnimatedRenderer extends Renderer {

    protected MutableWrapper<Boolean> Paused = new MutableWrapper<>(false);
    protected MutableWrapper<Integer> SpriteRows = new MutableWrapper<>(1);
    protected MutableWrapper<Integer> SpriteColumns = new MutableWrapper<>(1);
    protected Vector2Int spriteCutSize = new Vector2Int();
    protected AnimationData currentAnimation = null;
    protected int currentAnimationIndex = 0;
    protected final HashMap<String, AnimationData> Animations = new HashMap<>();

    private float animationTimer = 0.0f;


    @Override
    protected void InitializeInspectorData() {
        super.InitializeInspectorData();
        EditInInspector("Pause", Paused);
        ShowInInspector("Rows", SpriteRows);
        ShowInInspector("Columns", SpriteColumns);


    }

    public void SplitSprite(int NumRows, int NumColumns) {
        if (sprite == null)
        {
            Debug.LogError("AnimatedRenderer::SplitSprite()", "Unable to split the sprite, sprite was null!");
            return;
        }

        this.SpriteRows.value = Math.max(1, NumRows);
        this.SpriteColumns.value = Math.max(1, NumColumns);
        spriteCutSize = new Vector2Int(sprite.getWidth() / SpriteColumns.value, sprite.getHeight() / SpriteRows.value);
    }

    public int GetFrameIndex(int row, int col) {
        int rowClamped = MossMath.clamp(row, 1, SpriteRows.value);
        int colClamped = MossMath.clamp(col, 1, SpriteColumns.value);

        return (rowClamped - 1) * SpriteColumns.value + colClamped - 1;
    }

    public Vector2Int GetColRow(int frameIndex)
    {
        int frameIndexClamped = MossMath.clamp(frameIndex, 0, SpriteRows.value * SpriteColumns.value - 1);
        int y = (int)Math.ceil((float)(frameIndex + 1) / SpriteColumns.value);
        int x = (frameIndex + 1) - ((y - 1) * SpriteColumns.value);

        return new Vector2Int(x, y);
    }


    public boolean GetPausedState()
    {
        return Paused.value;
    }

    public void SetPausedState(boolean paused)
    {
        this.Paused.value = paused;
    }

    public void CreateAnimation(String AnimationName, AnimationData newAnimation)
    {
        if (newAnimation == null)
        {
            Debug.LogError("AnimatedRenderer::AddNewAnimation()", "Animation was null, unable to add!");
            return;
        }

        if (Animations.containsKey(AnimationName))
        {
            Debug.LogError("AnimatedRenderer::AddNewAnimation()", "Animation name already exists, unable to add!");
            return;
        }

        if (newAnimation.ToDefaultParameters())
        {
            newAnimation.StartFrame = 0;
            newAnimation.EndFrame = GetFrameIndex(SpriteRows.value, SpriteColumns.value);
        }
        if (currentAnimation == null) { currentAnimation = newAnimation; }
        Animations.put(AnimationName, newAnimation);
    }

    public void SetCurrentAnimation(String AnimationName)
    {
        if (!Animations.containsKey(AnimationName) || Animations.get(AnimationName) == null) {
            Debug.LogError("AnimatedRenderer::SetCurrentAnimation()", AnimationName + " not found, unable to set animation!");
            return;
        }
        currentAnimation = Animations.get(AnimationName);
        currentAnimationIndex = Objects.requireNonNull(Animations.get(AnimationName)).StartFrame;
    }


    @Override
    public void Render(Canvas canvas) {
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
        Vector2Int framePosition = GetColRow(currentAnimationIndex);
        int colIndex = framePosition.x - 1;  // Convert to 0-based
        int rowIndex = framePosition.y - 1;  // Convert to 0-based

        Vector2Int src = new Vector2Int(colIndex * spriteCutSize.x, rowIndex * spriteCutSize.y);

        Rect srcRect = new Rect(src.x, src.y, src.x + spriteCutSize.x, src.y + spriteCutSize.y);

        float positionFactorX = spriteCutSize.x;
        float positionFactorY = spriteCutSize.y;

        if (imageRatio == Ratio.FIXED)
        {
            src = new Vector2Int(1, 1);

            positionFactorX = 1;
            positionFactorY = 1;
        }

        RectF dstRect = new RectF(
                position.x - (positionFactorX * scale.x) * 0.5f + RenderOffset.x,
                position.y - (positionFactorY * scale.y) * 0.5f + RenderOffset.y,
                position.x + (positionFactorX * scale.x) * 0.5f + RenderOffset.x,
                position.y + (positionFactorY * scale.y) * 0.5f + RenderOffset.y
        );

        canvas.save();

        float scaleFactorX = src.x;
        float scaleFactorY = src.y;

        float centerX = position.x + (scale.x * scaleFactorX) * 0.5f + RenderOffset.x;
        float centerY = position.y + (scale.y * scaleFactorY) * 0.5f + RenderOffset.y;
        canvas.translate(centerX, centerY);
        canvas.rotate(rotation);
        canvas.translate(-centerX, -centerY);

        canvas.drawBitmap(sprite, srcRect, dstRect, null);
        canvas.restore();
    }

    @Override
    public void Update() {
        super.Update();
        if (!Paused.value) {
            if (currentAnimation == null) {
                animationTimer = 0;
                return;
            }
            animationTimer += Time.GetDeltaTime();
            if (animationTimer > currentAnimation.TimeBetweenFrames) {
                animationTimer = 0;
                currentAnimationIndex++;
                if (currentAnimationIndex > currentAnimation.EndFrame) {
                    currentAnimationIndex = currentAnimation.StartFrame;
                }
            }
        }
    }

    public static final class AnimationData {
        public int StartFrame = 0;
        public int EndFrame = 0;
        public float TimeBetweenFrames;

        private boolean NoParametersSet = false;

        public AnimationData(int StartFrame, int EndFrame, float TimeBetweenFrames)
        {
            this.StartFrame = StartFrame;
            this.EndFrame = EndFrame;
            this.TimeBetweenFrames = TimeBetweenFrames;
        }

        public AnimationData(int StartFrame, int EndFrame)
        {
            this.StartFrame = StartFrame;
            this.EndFrame = EndFrame;
            this.TimeBetweenFrames = 0.1f;
        }

        //if we're really lazy, we can just define a parameter-less animation
        //when adding it to the game object, this will default to use the entire sprite sheet
        public AnimationData()
        {
            this.StartFrame = 0;
            this.EndFrame = 0;
            this.TimeBetweenFrames = 0.1f;
            NoParametersSet = true;
        }

        //check if we need to automatically set the values of the animation
        public final boolean ToDefaultParameters()
        {
            return NoParametersSet;
        }

        @NonNull
        @Override
        public String toString() {
            return StartFrame + " - " + EndFrame;
        }
    }

}
