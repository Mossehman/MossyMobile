package com.example.mossymobile.VibeoGeam;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Button;

import com.example.mossymobile.MossFramework.Application;
import com.example.mossymobile.MossFramework.GameView;
import com.example.mossymobile.MossFramework.Math.Vector2;
import com.example.mossymobile.MossFramework.Math.Vector2Int;
import com.example.mossymobile.MossFramework.MonoBehaviour;
import com.example.mossymobile.MossFramework.Systems.Debugging.Debug;
import com.example.mossymobile.MossFramework.Systems.Debugging.Gizmos;
import com.example.mossymobile.MossFramework.Systems.UserInput.Input;

import java.util.Objects;

public class JoystickKnob extends MonoBehaviour {
    public Button knob;

    // Logical positions for touch
    public Vector2 originalTouchPosition = new Vector2(0f, 0f);
    public Vector2 currentTouchPosition = new Vector2(0f, 0f);
    public Vector2 direction = new Vector2(0f, 0f); // Direction vector for joystick movement

    private int touchId = -1; // Default: no touch is registered
    private final float maxDistance = 100f; // Maximum distance the knob can move

    public boolean isJoystickUp = false; // Boolean for when the joystick was just let go off

    // Offset for display position
    public Vector2 offset = new Vector2(
            300f,
            new Vector2Int(
                    Objects.requireNonNull(GameView.GetInstance()).getWidth(),
                    Objects.requireNonNull(GameView.GetInstance()).getHeight()
            ).ToFloat().y - 300f
    );

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void Start() {
        // Initialize the knob's transform with the offset
        GetTransform().SetPosition(offset);
        GetTransform().SetScale(new Vector2(200f, 200f));

        // Check if the knob button is assigned
        if (knob != null) {
            knob.setOnTouchListener((v, event) -> {
                Vector2 touchPos = new Vector2(event.getX(), event.getY()); // Get touch position relative to the view

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        isJoystickUp = false;
                        // Register the touch ID and set the original touch position
                        if (touchId == -1) { // Only process if no other touch is active
                            touchId = event.getPointerId(0); // Capture the first pointer
                            originalTouchPosition = touchPos; // Logical starting position
                        }
                        break;

                    case MotionEvent.ACTION_MOVE:
                        if (touchId == event.getPointerId(0)) {
                            currentTouchPosition = touchPos;

                            // Calculate direction vector and clamp movement to max distance
                            direction = Vector2.Sub(currentTouchPosition, originalTouchPosition);
                            if (direction.Magnitude() > maxDistance) {
                                direction.Normalize();
                                direction.Mul(maxDistance);
                            }

                            // Update the visual knob position based on direction and offset
                            GetTransform().SetPosition(Vector2.Add(offset, direction));
                        }
                        break;

                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        if (touchId == event.getPointerId(0)) {
                            // Reset the knob position and touch ID
                            touchId = -1;
                            direction = new Vector2(0f, 0f);
                            GetTransform().SetPosition(offset); // Reset to the original offset position
                            isJoystickUp = true;
                        }
                        break;
                }
                return true; // Consume the touch event
            });
        }
    }

    @Override
    public void Update(){
    }


}
