package com.example.mossymobile.MossFramework.Math;

import static java.lang.Math.sqrt;

import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.example.mossymobile.MossFramework.GameView;
import com.example.mossymobile.MossFramework.Systems.Debugging.Debug;
import com.example.mossymobile.MossFramework.Systems.Debugging.ILoggable;
import com.example.mossymobile.MossFramework.Systems.Inspector.ICustomInspectorGUI;

import java.io.Serializable;
import java.util.Objects;

public final class Vector2 implements ILoggable, ICustomInspectorGUI, Serializable {
    @Override
    public String GetLogStatement() {
        return "Vector2: { " + x + ", " + y + " }";
    }

    public float x = 0;
    public float y = 0;

    public static final Vector2 up = new Vector2(0, 1);
    public static final Vector2 right = new Vector2(1, 0);

    public Vector2(float x, float y)
    {
        this.x = x;
        this.y = y;
    }

    public Vector2()
    {
        this.x = 0;
        this.y = 0;
    }

    public Vector2(Vector2 vec2)
    {
        this.x = vec2.x;
        this.y = vec2.y;
    }

    /**
     * Method for easily casting a {@code Vector2} into a {@code Vector2Int}.
     *
     * @return A new {@code Vector2Int} with the values of the original {@code Vector2}
     */
    public Vector2Int ToInt() {
        return new Vector2Int((int)x, (int)y);
    }

    /**
     * Directly adds the {@code x} and {@code y} components of another {@code Vector2} to the current {@code Vector2}.
     *
     * @param otherVec2 the {@code Vector2} to add to the current {@code Vector2}.
     */
    public void Add(Vector2 otherVec2)
    {
        this.x += otherVec2.x;
        this.y += otherVec2.y;
    }

    /**
     * Returns a new {@code Vector2} with the added {@code x} and {@code y} components of 2 other {@code Vector2}s.
     *
     * @param firstVec2 the first {@code Vector2} for the addition equation.
     * @param secondVec2 the second {@code Vector2} for the addition equation.
     *
     * @return a new {@code Vector2} with the added values of the 2 {@code Vector2}s.
     */
    public static Vector2 Add(Vector2 firstVec2, Vector2 secondVec2)
    {
        return new Vector2(firstVec2.x + secondVec2.x, firstVec2.y + secondVec2.y);
    }


    /**
     * Directly subtracts the {@code x} and {@code y} components of the current {@code Vector2} with another {@code Vector2}.
     *
     * @param otherVec2 the {@code Vector2} to subtract from the current {@code Vector2}.
     */
    public void Sub(Vector2 otherVec2)
    {
        this.x -= otherVec2.x;
        this.y -= otherVec2.y;
    }

    /**
     * Returns a new {@code Vector2} with the subtracted {@code x} and {@code y} components of 2 other {@code Vector2}s.
     *
     * @param firstVec2 the first {@code Vector2} for the subtraction equation.
     * @param secondVec2 the second {@code Vector2} for the subtraction equation.
     *
     * @return a new {@code Vector2} with the subtracted values of the 2 {@code Vector2}s.
     */
    public static Vector2 Sub(Vector2 firstVec2, Vector2 secondVec2)
    {
        return new Vector2(firstVec2.x - secondVec2.x, firstVec2.y - secondVec2.y);
    }

    /**
     * Directly multiplies the {@code x} and {@code y} components of the current {@code Vector2} with another {@code Vector2}.
     *
     * @param otherVec2 the {@code Vector2} to multiply with the current {@code Vector2}.
     */
    public void Mul(Vector2 otherVec2)
    {
        this.x *= otherVec2.x;
        this.y *= otherVec2.y;
    }

    public void Mul(float val)
    {
        this.x *= val;
        this.y *= val;
    }

    /**
     * Returns a new {@code Vector2} with the multiplied {@code x} and {@code y} components of 2 other {@code Vector2}s.
     *
     * @param firstVec2 the first {@code Vector2} for the multiplication equation.
     * @param secondVec2 the second {@code Vector2} for the multiplication equation.
     *
     * @return a new {@code Vector2} with the multiplied values of the 2 {@code Vector2}s.
     */
    public static Vector2 Mul(Vector2 firstVec2, Vector2 secondVec2)
    {
        return new Vector2(firstVec2.x * secondVec2.x, firstVec2.y * secondVec2.y);
    }

    public static Vector2 Mul(Vector2 vec2, float val){
        return new Vector2(vec2.x * val, vec2.y * val);
    }

    /**
     * Directly divides the {@code x} and {@code y} components of the current {@code Vector2} with another {@code Vector2}.
     *
     * @param otherVec2 the {@code Vector2} to divide with the current {@code Vector2}.
     */
    public void Div(Vector2 otherVec2)
    {
        this.x /= otherVec2.x;
        this.y /= otherVec2.y;
    }

    public void Div(float val)
    {
        this.x /= val;
        this.y /= val;
    }

    /**
     * Returns a new {@code Vector2} with the divided {@code x} and {@code y} components of 2 other {@code Vector2}s.
     *
     * @param firstVec2 the first {@code Vector2} for the division equation.
     * @param secondVec2 the second {@code Vector2} for the division equation.
     *
     * @return a new {@code Vector2} with the divided values of the 2 {@code Vector2}s.
     */
    public static Vector2 Div(Vector2 firstVec2, Vector2 secondVec2)
    {
        return new Vector2(firstVec2.x / secondVec2.x, firstVec2.y / secondVec2.y);
    }

    public static Vector2 Div(Vector2 vec2, float val)
    {
        return new Vector2(vec2.x / val, vec2.y / val);
    }

    /**
     * Returns the magnitude of the {@code Vector2}.
     *
     * @return a new {@code float} with the magnitude of the {@code Vector2}.
     */
    public float Magnitude() { return (float)sqrt(x * x + y * y); }

    /**
     * Returns the squared magnitude of the {@code Vector2}.
     *
     * @return a new {@code float} with the squared magnitude of the {@code Vector2}.
     */
    public float MagnitudeSq() { return (float)(x * x + y * y); }

    /**
     * A faster but less precise approach to {@code Vector2::Magnitude()} in the event of requiring increased performance. (This should not be needed in MOST cases).
     *
     * @return a new {@code float} with the magnitude of the {@code Vector2}.
     */
    public float FastMagnitude() { return (float)MossMath.fastSqrt(x * x + y * y); }

    /**
     * A faster but less precise approach to {@code Vector2::Magnitude()} in the event of requiring increased performance, more precise than {@code Vector2::FastMagnitude()}. (This should not be needed in MOST cases).
     *
     * @return a new {@code float} with the magnitude of the {@code Vector2}.
     */
    public float FastMagnitude_d() { return (float)MossMath.fastSqrt_d(x * x + y * y); }

    /**
     * Returns the length between the current {@code Vector2} and another {@code Vector2}.
     *
     * @param otherVec2 the {@code Vector2} to check the length.
     *
     * @return a {@code float} with the length between the 2 {@code Vector2}s.
     */
    public float Distance(Vector2 otherVec2) { return (float)sqrt((x - otherVec2.x) * (x - otherVec2.x) + (y - otherVec2.y) * (y - otherVec2.y)); }

    /**
     * Returns the squared length between the current {@code Vector2} and another {@code Vector2}.
     *
     * @param otherVec2 the {@code Vector2} to check the squared length.
     *
     * @return a {@code float} with the squared length between the 2 {@code Vector2}s.
     */
    public float DistanceSq(Vector2 otherVec2) { return (float)((x - otherVec2.x) * (x - otherVec2.x) + (y - otherVec2.y) * (y - otherVec2.y)); }

    /**
     * A faster but less precise approach to {@code Vector2::Distance()} in the event of requiring increased performance. (This should not be needed in MOST cases).
     *
     * @param otherVec2 the {@code Vector2} to check the length.
     *
     * @return a {@code float} with the length between the 2 {@code Vector2}s.
     */
    public float FastDistance(Vector2 otherVec2) { return (float)MossMath.fastSqrt((x - otherVec2.x) * (x - otherVec2.x) + (y - otherVec2.y) * (y - otherVec2.y)); }

    /**
     * A faster but less precise approach to {@code Vector2::Distance()} in the event of requiring increased performance, more precise than {@code Vector2::FastDistance()}. (This should not be needed in MOST cases).
     *
     * @param otherVec2 the {@code Vector2} to check the length.
     *
     * @return a new {@code float} with the length between the 2 {@code Vector2}s.
     */
    public float FastDistance_Precise(Vector2 otherVec2) { return (float)MossMath.fastSqrt_d((x - otherVec2.x) * (x - otherVec2.x) + (y - otherVec2.y) * (y - otherVec2.y)); }

    /**
     * Calculates the dot product between the current {@code Vector2} and another {@code Vector2}.
     *
     * @param otherVec2 the {@code Vector2} to calculate the dot product
     *
     * @return a new {@code float} with the dot product between the 2 {@code Vector2}s.
     */
    public float Dot(Vector2 otherVec2) { return x * otherVec2.x + y * otherVec2.y; }

    /**
     * Normalizes the current {@code Vector2}'s magnitude to be within 0 - 1.
     *
     */
    public void Normalize()
    {
        float l = Magnitude();
        if (l <= MossMath.EPSILON) {
            x = 0;
            y = 0;
        } else {
            x /= l;
            y /= l;
        }
    }

    /**
     * Creates a new {@code Vector2} with the normalized values of the current {@code Vector2}.
     *
     * @return a new {@code Vector2} with the normalized value of the current {@code Vector2}.
     */
    public Vector2 Normalized()
    {
        float l = Magnitude();
        if (l <= MossMath.EPSILON) {
            return new Vector2();
        } else {
            return new Vector2(x / l, y / l);
        }
    }

    public Vector2 FastNormalize()
    {
        float l = FastMagnitude();
        if (l <= MossMath.EPSILON) {
            return new Vector2();
        } else {
            return new Vector2(x / l, y / l);
        }
    }

    public static float DirectionToAngle(Vector2 dir){
        float dx = dir.x;
        float dy = dir.y;

        // Calculate angle in radians and convert to degrees
        float angleRadians = (float) Math.atan2(dx, -dy); // Negate dx for upward 0 degrees
        float angleDegrees = (float) Math.toDegrees(angleRadians);

        // Normalize to [0, 360)
        return (angleDegrees + 360) % 360;
    }

    public float DirectionToAngle() {
        float dx = x;
        float dy = y;

        // Calculate angle in radians and convert to degrees
        float angleRadians = (float) Math.atan2(dx, -dy); // Negate dx for upward 0 degrees
        float angleDegrees = (float) Math.toDegrees(angleRadians);

        // Normalize to [0, 360)
        return (angleDegrees + 360) % 360;
    }

    public static Vector2 RotateVector(Vector2 vector, float angleDegrees) {
        double angleRadians = Math.toRadians(angleDegrees);
        float cos = (float) Math.cos(angleRadians);
        float sin = (float) Math.sin(angleRadians);

        float x = vector.x * cos - vector.y * sin;
        float y = vector.x * sin + vector.y * cos;

        return new Vector2(x, y);
    }
    public static Vector2 GetVectorFromAngle(float angleDegrees) {
        double angleRadians = Math.toRadians(angleDegrees);
        float x = (float) Math.cos(angleRadians);
        float y = (float) Math.sin(angleRadians);

        return new Vector2(x, y);
    }

    public void RotateVector(float angleDegrees) {
        double angleRadians = Math.toRadians(angleDegrees);
        float cos = (float) Math.cos(angleRadians);
        float sin = (float) Math.sin(angleRadians);

        float newX = x * cos - y * sin;
        float newY = x * sin + y * cos;

        this.x = newX;
        this.y = newY;
    }

    public static Vector2 Lerp(Vector2 from, Vector2 to, float t) {
        t = Math.max(0f, Math.min(1f, t));

        float x = from.x + (to.x - from.x) * t;
        float y = from.y + (to.y - from.y) * t;

        return new Vector2(x, y);
    }

    public static Vector2 Slerp(Vector2 from, Vector2 to, float t) {
        // Ensure both vectors are normalized
        from = from.FastNormalize();
        to = to.FastNormalize();

        // Compute the dot product (cosine of the angle)
        float dot = from.Dot(to);
        // Clamp dot product to avoid errors in acos
        dot = Math.max(-1f, Math.min(1f, dot));

        // Compute the angle between the vectors
        float theta = (float) Math.acos(dot);

        // If the angle is small, linear interpolation is sufficient
        if (theta < 1e-5) {
            return Vector2.Lerp(from, to, t).FastNormalize();
        }

        // Perform spherical linear interpolation
        float sinTheta = (float) Math.sin(theta);
        float scaleFrom = (float) Math.sin((1 - t) * theta) / sinTheta;
        float scaleTo = (float) Math.sin(t * theta) / sinTheta;

        return Vector2.Add(Vector2.Mul(from, scaleFrom), Vector2.Mul(to, scaleTo));
    }

    /**
     * Checks whether the {@code x} and {@code y} components of the {@code Vector2} match with another {@code Vector2}.
     *
     * @param otherVec2 the {@code Vector2} to compare equality with.
     *
     * @return whether the {@code x} and {@code y} components of the current {@code Vector2} and the other {@code Vector2}. ({@code boolean}).
     */
    public boolean IsEqual(Vector2 otherVec2) { return Math.abs(x - otherVec2.x) < MossMath.EPSILON && Math.abs(y - otherVec2.y) < MossMath.EPSILON; }

    @Override
    public void SetGUIData(LinearLayout componentList, long updateDelay) {
        EditText xComponent = new EditText(Objects.requireNonNull(GameView.GetInstance()).GetContext());
        xComponent.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        xComponent.setText(String.valueOf(x));
        xComponent.setMaxLines(1);
        xComponent.setSingleLine(true);

        final boolean[] isEditing = {false, false};

        xComponent.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    isEditing[0] = true;
                }
                else {
                    isEditing[0] = false;
                }
            }
        });

        xComponent.addTextChangedListener(new TextWatcher() {
            private String previousText = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                previousText = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String newText = s.toString();
                if (!newText.equals(previousText)) {
                    try {
                        // Convert text to a number
                        x = Float.parseFloat(newText);
                    } catch (NumberFormatException e) {
                        Debug.LogError("Vector2::SetGUIData()", "Invalid data set via Inspector, only input float values!");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        EditText yComponent = new EditText(Objects.requireNonNull(GameView.GetInstance()).GetContext());
        yComponent.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        yComponent.setText(String.valueOf(y));

        yComponent.setMaxLines(1);
        yComponent.setSingleLine(true);

        yComponent.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    isEditing[1] = true;
                }
                else {
                    isEditing[1] = false;
                }
            }
        });

        yComponent.addTextChangedListener(new TextWatcher() {
            private String previousText = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                previousText = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String newText = s.toString();

                if (!newText.equals(previousText)) {
                    try {
                        // Convert text to a number
                        y = Float.parseFloat(newText);
                    } catch (NumberFormatException e) {
                        Debug.LogError("Vector2::SetGUIData()", "Invalid data set via Inspector, only input float values!");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Handler handler = new Handler();
        Runnable updateTextData = new Runnable() {
            @Override
            public void run() {
                if (!xComponent.getText().toString().equals(String.valueOf(x)) && !isEditing[0]) {
                    xComponent.setText(String.valueOf(x)); // Update X text
                }
                if (!yComponent.getText().toString().equals(String.valueOf(y)) && !isEditing[1]) {
                    yComponent.setText(String.valueOf(y)); // Update Y text
                }

                handler.postDelayed(this, updateDelay); // Repeat every 100ms
            }
        };

        handler.post(updateTextData);

        componentList.addView(xComponent);
        componentList.addView(yComponent);
    }

    @NonNull
    @Override
    public String toString() {
        return "{ " + x + ", " + y + " }";
    }
}
