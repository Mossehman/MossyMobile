package com.example.mossymobile.MossFramework.Math;

import static java.lang.Math.sqrt;

import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.mossymobile.MossFramework.GameView;
import com.example.mossymobile.MossFramework.Systems.Debugging.Debug;
import com.example.mossymobile.MossFramework.Systems.Debugging.ILoggable;
import com.example.mossymobile.MossFramework.Systems.Inspector.ICustomInspectorGUI;

import java.util.Objects;

public final class Vector2Int implements ILoggable, ICustomInspectorGUI {
    @Override
    public void SetGUIData(LinearLayout componentList, long updateDelay) {
        EditText xComponent = new EditText(Objects.requireNonNull(GameView.GetInstance()).GetContext());
        xComponent.setInputType(InputType.TYPE_NUMBER_VARIATION_NORMAL);
        xComponent.setText(String.valueOf(x));

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
                        x = Integer.parseInt(newText);
                    } catch (NumberFormatException e) {
                        Debug.LogError("Vector2Int::SetGUIData()", "Invalid data set via Inspector, only input integer values!");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        EditText yComponent = new EditText(Objects.requireNonNull(GameView.GetInstance()).GetContext());
        yComponent.setInputType(InputType.TYPE_NUMBER_VARIATION_NORMAL);
        yComponent.setText(String.valueOf(y));

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
                        y = Integer.parseInt(newText);
                    } catch (NumberFormatException e) {
                        Debug.LogError("Vector2Int::SetGUIData()", "Invalid data set via Inspector, only input integer values!");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        Handler handler = new Handler();
        Runnable updateTextData = new Runnable() {
            @Override
            public void run() {
                if (!xComponent.getText().toString().equals(String.valueOf(x))) {
                    xComponent.setText(String.valueOf(x));
                }
                if (!yComponent.getText().toString().equals(String.valueOf(y))) {
                    yComponent.setText(String.valueOf(y));
                }

                handler.postDelayed(this, updateDelay);
            }
        };

        handler.post(updateTextData);

        componentList.addView(xComponent);
        componentList.addView(yComponent);
    }

    @Override
    public String GetLogStatement() {
        return "Vector2Int: { " + x + ", " + y + " }";
    }

    public int x = 0;
    public int y = 0;

    public static final Vector2Int up = new Vector2Int(0, 1);
    public static final Vector2Int right = new Vector2Int(1, 0);

    public Vector2Int(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public Vector2Int()
    {
        this.x = 0;
        this.y = 0;
    }

    public Vector2Int(Vector2Int vec2I)
    {
        this.x = vec2I.x;
        this.y = vec2I.y;
    }

    /**
     * Method for easily casting a {@code Vector2Int} into a {@code Vector2}.
     *
     * @return A new {@code Vector2} with the values of the original {@code Vector2Int}.
     */
    public Vector2 ToFloat()
    {
        return new Vector2((float)x, (float)y);
    }

    /**
     * Directly adds the {@code x} and {@code y} components of another {@code Vector2Int} to the current {@code Vector2Int}.
     *
     * @param otherVec2Int the {@code Vector2Int} to add to the current {@code Vector2Int}.
     */
    public void Add(Vector2Int otherVec2Int)
    {
        this.x += otherVec2Int.x;
        this.y += otherVec2Int.y;
    }

    /**
     * Returns a new {@code Vector2Int} with the added {@code x} and {@code y} components of 2 other {@code Vector2Int}s.
     *
     * @param firstVec2I the first {@code Vector2Int} for the addition equation.
     * @param secondVec2I the second {@code Vector2Int} for the addition equation.
     *
     * @return a new {@code Vector2Int} with the added values of the 2 {@code Vector2Int}s.
     */
    public static Vector2Int Add(Vector2Int firstVec2I, Vector2Int secondVec2I)
    {
        return new Vector2Int(firstVec2I.x + secondVec2I.x, firstVec2I.y + secondVec2I.y);
    }

    /**
     * Directly subtracts the {@code x} and {@code y} components of the current {@code Vector2Int} with another {@code Vector2Int}.
     *
     * @param otherVec2Int the {@code Vector2Int} to subtract from the current {@code Vector2Int}.
     */
    public void Sub(Vector2Int otherVec2Int)
    {
        this.x -= otherVec2Int.x;
        this.y -= otherVec2Int.y;
    }

    /**
     * Returns a new {@code Vector2Int} with the subtracted {@code x} and {@code y} components of 2 other {@code Vector2Int}s.
     *
     * @param firstVec2I the first {@code Vector2Int} for the subtraction equation.
     * @param secondVec2I the second {@code Vector2Int} for the subtraction equation.
     *
     * @return a new {@code Vector2Int} with the subtracted values of the 2 {@code Vector2Int}s.
     */
    public static Vector2Int Sub(Vector2Int firstVec2I, Vector2Int secondVec2I)
    {
        return new Vector2Int(firstVec2I.x - secondVec2I.x, firstVec2I.y - secondVec2I.y);
    }

    /**
     * Directly multiplies the {@code x} and {@code y} components of the current {@code Vector2Int} with another {@code Vector2Int}.
     *
     * @param otherVec2Int the {@code Vector2Int} to multiply with the current {@code Vector2Int}.
     */
    public void Mul(Vector2Int otherVec2Int)
    {
        this.x *= otherVec2Int.x;
        this.y *= otherVec2Int.y;
    }

    /**
     * Returns a new {@code Vector2Int} with the multiplied {@code x} and {@code y} components of 2 other {@code Vector2Int}s.
     *
     * @param firstVec2I the first {@code Vector2Int} for the multiplication equation.
     * @param secondVec2I the second {@code Vector2Int} for the multiplication equation.
     *
     * @return a new {@code Vector2Int} with the multiplied values of the 2 {@code Vector2Int}s.
     */
    public static Vector2Int Mul(Vector2Int firstVec2I, Vector2Int secondVec2I)
    {
        return new Vector2Int(firstVec2I.x * secondVec2I.x, firstVec2I.y * secondVec2I.y);
    }


    /**
     * Directly divides the {@code x} and {@code y} components of the current {@code Vector2Int} with another {@code Vector2Int}. (It is advised to use {@code Vector2.Div()} instead of {@code Vector2Int.Div()}).
     *
     * @param otherVec2Int the {@code Vector2Int} to divide with the current {@code Vector2Int}.
     */
    public void Div(Vector2Int otherVec2Int)
    {
        this.x /= otherVec2Int.x;
        this.y /= otherVec2Int.y;
        Debug.LogWarning("Vector2Int::Div()", "You are dividing a Vector2Int value, this can result in lossy data. It is advised to use a Vector2 instead. Use {Debug.DisableWarning('Vector2Int Division')} to disable this warning.", "Vector2Int Division");
    }

    /**
     * Returns a new {@code Vector2Int} with the divided {@code x} and {@code y} components of 2 other {@code Vector2Int}s.
     *
     * @param firstVec2I the first {@code Vector2Int} for the division equation.
     * @param secondVec2I the second {@code Vector2Int} for the division equation.
     *
     * @return a new {@code Vector2Int} with the divided values of the 2 {@code Vector2Int}s.
     */
    public static Vector2Int Div(Vector2Int firstVec2I, Vector2Int secondVec2I)
    {
        Debug.LogWarning("Vector2Int::Div()", "You are dividing a Vector2Int value, this can result in lossy data. It is advised to use a Vector2 instead. Use {Debug.DisableWarning('Vector2Int Division')} to disable this warning.", "Vector2Int Division");
        return new Vector2Int(firstVec2I.x / secondVec2I.x, firstVec2I.y / secondVec2I.y);
    }

    /**
     * Returns the magnitude of the {@code Vector2Int}.
     *
     * @return a new {@code float} with the magnitude of the {@code Vector2Int}.
     */
    public float Magnitude() { return (float)sqrt(x * x + y * y); }

    /**
     * Returns the squared magnitude of the {@code Vector2Int}.
     *
     * @return a new {@code float} with the squared magnitude of the {@code Vector2Int}.
     */
    public float MagnitudeSq() { return (float)(x * x + y * y); }

    /**
     * A faster but less precise approach to {@code Vector2Int::Magnitude()} in the event of requiring increased performance. (This should not be needed in MOST cases).
     *
     * @return a new {@code float} with the magnitude of the {@code Vector2Int}.
     */
    public float FastMagnitude() { return (float)MossMath.fastSqrt(x * x + y * y); }

    /**
     * A faster but less precise approach to {@code Vector2Int::Magnitude()} in the event of requiring increased performance, more precise than {@code Vector2Int::FastMagnitude()}. (This should not be needed in MOST cases).
     *
     * @return a new {@code float} with the magnitude of the {@code Vector2Int}.
     */
    public float FastMagnitude_precise() { return (float)MossMath.fastSqrt_d(x * x + y * y); }

    /**
     * Returns the length between the current {@code Vector2Int} and another {@code Vector2Int}.
     *
     * @param otherVec2I the {@code Vector2Int} to check the length.
     *
     * @return a {@code float} with the length between the 2 {@code Vector2Int}s.
     */
    public float Distance(Vector2Int otherVec2I) { return (float)sqrt((x - otherVec2I.x) * (x - otherVec2I.x) + (y - otherVec2I.y) * (y - otherVec2I.y)); }

    /**
     * Returns the squared length between the current {@code Vector2Int} and another {@code Vector2Int}.
     *
     * @param otherVec2I the {@code Vector2Int} to check the squared length.
     *
     * @return a {@code float} with the squared length between the 2 {@code Vector2Int}s.
     */
    public float DistanceSq(Vector2Int otherVec2I) { return (float)((x - otherVec2I.x) * (x - otherVec2I.x) + (y - otherVec2I.y) * (y - otherVec2I.y)); }

    /**
     * A faster but less precise approach to {@code Vector2Int::Distance()} in the event of requiring increased performance. (This should not be needed in MOST cases).
     *
     * @param otherVec2I the {@code Vector2} to check the length.
     *
     * @return a {@code float} with the length between the 2 {@code Vector2Int}s.
     */
    public float FastDistance(Vector2 otherVec2I) { return (float)MossMath.fastSqrt((x - otherVec2I.x) * (x - otherVec2I.x) + (y - otherVec2I.y) * (y - otherVec2I.y)); }

    /**
     * A faster but less precise approach to {@code Vector2Int::Distance()} in the event of requiring increased performance, more precise than {@code Vector2Int::FastDistance()}. (This should not be needed in MOST cases).
     *
     * @param otherVec2I the {@code Vector2} to check the length.
     *
     * @return a new {@code float} with the length between the 2 {@code Vector2Int}s.
     */
    public float FastDistance_Precise(Vector2 otherVec2I) { return (float)MossMath.fastSqrt_d((x - otherVec2I.x) * (x - otherVec2I.x) + (y - otherVec2I.y) * (y - otherVec2I.y)); }

    /**
     * Calculates the dot product between the current {@code Vector2Int} and another {@code Vector2Int}.
     *
     * @param otherVec2 the {@code Vector2Int} to calculate the dot product
     *
     * @return a new {@code float} with the dot product between the 2 {@code Vector2Int}s.
     */
    public float Dot(Vector2 otherVec2) { return x * otherVec2.x + y * otherVec2.y; }

    /**
     * Normalizes the current {@code Vector2Int}'s magnitude to be within 0 - 1. (This is not advised due to {@code Integer division}).
     *
     */
    public void Normalize()
    {
        Debug.LogWarning("Vector2Int::Normalize()", "You are dividing a Vector2Int value, this can result in lossy data. It is advised to use a Vector2 instead. Use {Debug.DisableWarning('Vector2Int Division')} to disable this warning.", "Vector2Int Division");
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
     * Creates a new {@code Vector2Int} with the normalized values of the current {@code Vector2Int}. (This is not advised due to {@code Integer division}).
     *
     * @return a new {@code Vector2Int} with the normalized value of the current {@code Vector2Int}.
     */
    public Vector2Int Normalized()
    {
        Debug.LogWarning("Vector2Int::Normalized()", "You are dividing a Vector2Int value, this can result in lossy data. It is advised to use a Vector2 instead. Use {Debug.DisableWarning('Vector2Int Division')} to disable this warning.", "Vector2Int Division");
        float l = Magnitude();
        if (l <= MossMath.EPSILON) {
            return new Vector2Int();
        } else {
            return new Vector2Int((int)(x / l), (int)(y / l));
        }
    }

    /**
     * Checks whether the {@code x} and {@code y} components of the {@code Vector2Int} match with another {@code Vector2Int}.
     *
     * @param otherVec2 the {@code Vector2Int} to compare equality with.
     *
     * @return whether the {@code x} and {@code y} components of the current {@code Vector2Int} and the other {@code Vector2Int}. ({@code boolean}).
     */
    public boolean IsEqual(Vector2Int otherVec2) { return x == otherVec2.x && y == otherVec2.y; }

}
