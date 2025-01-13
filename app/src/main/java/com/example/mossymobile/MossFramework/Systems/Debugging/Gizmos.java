package com.example.mossymobile.MossFramework.Systems.Debugging;

import android.graphics.Paint;

import com.example.mossymobile.MossFramework.Application;
import com.example.mossymobile.MossFramework.GameView;
import com.example.mossymobile.MossFramework.Math.Vector2;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Gizmos {
    public static void DrawLine(Vector2 start, Vector2 end, int color)
    {
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setStrokeWidth(1.5f);

        Vector2 startScaled = Vector2.Mul(Application.GetViewToScreenRatio(), start);
        Vector2 endScaled = Vector2.Mul(Application.GetViewToScreenRatio(), end);

        Objects.requireNonNull(GameView.GetInstance()).canvas.drawLine(startScaled.x, startScaled.y, endScaled.x, endScaled.y, paint);
    }

    public static void DrawCircle(Vector2 center, float radius, int color, int smoothness)
    {
        if (smoothness < 3) { return; }

        List<Vector2> points = new ArrayList<>();

        float angleIncrement = (float) (2 * Math.PI / smoothness);

        for (int i = 0; i < smoothness; i++) {
            float angle = i * angleIncrement;

            float x = center.x + radius * (float) Math.cos(angle);
            float y = center.y + radius * (float) Math.sin(angle);

            points.add(new Vector2(x, y));
        }

        for (int i = 0; i < points.size(); i++) {
            Vector2 start = points.get(i);
            Vector2 end = points.get((i + 1) % points.size());

            DrawLine(start, end, color);
        }
    }

    public static void DrawBox(Vector2 center, Vector2 halfDimensions, int color)
    {
        if (halfDimensions.x <= 0 || halfDimensions.y <= 0) { return; }

        DrawLine(new Vector2(center.x - halfDimensions.x, center.y - halfDimensions.y), new Vector2(center.x - halfDimensions.x, center.y + halfDimensions.y), color);
        DrawLine(new Vector2(center.x + halfDimensions.x, center.y - halfDimensions.y), new Vector2(center.x + halfDimensions.x, center.y + halfDimensions.y), color);

        DrawLine(new Vector2(center.x - halfDimensions.x, center.y - halfDimensions.y), new Vector2(center.x + halfDimensions.x, center.y - halfDimensions.y), color);
        DrawLine(new Vector2(center.x - halfDimensions.x, center.y + halfDimensions.y), new Vector2(center.x + halfDimensions.x, center.y + halfDimensions.y), color);
    }



}
