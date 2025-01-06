package com.example.mossymobile.MossFramework.Systems.UserInput;

import android.view.MotionEvent;
import com.example.mossymobile.MossFramework.Math.Vector2;

///Allows us to get the user's touch position, adjusted for multi-touch compatibility
public final class Input {
    static int action = -1;
    static int pointerID = -1;
    static MotionEvent event = null;

    public static void UpdateTouch(MotionEvent motionEvent)
    {
        if (motionEvent == null) { return; }
        event = motionEvent;
        action = motionEvent.getActionMasked();
        int actionID = motionEvent.getActionIndex();
        pointerID = motionEvent.getPointerId(actionID);
    }


    /**
     * Allows us to get the touch position in the form of a {@code Vector2}. Use as follows: {@code PointerID = Input.GetTouchPosition(TouchPos, PointerID)}.
     *
     * @param touchPosOut the {@code Vector2} the touch position will be output to.
     * @param PointerID the current pointerID the code is referencing.
     *
     * @return the PointerID for the script that calls this function to reference.
     */
    public static int GetTouchPosition(Vector2 touchPosOut, int PointerID)
    {
        int returnVal = PointerID;
        if (action == -1 || pointerID == -1 || event == null) { return -1; }

        if (PointerID == -1 && (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_POINTER_DOWN))
        {
            returnVal = pointerID;
        }
        else if (PointerID == pointerID && (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_POINTER_UP))
        {
            returnVal = -1;
        }

        if (returnVal != -1)
        {
            for (int i = 0; i < event.getPointerCount(); i++)
            {
                if (event.getPointerId(i) != returnVal) { continue; }
                touchPosOut.x = event.getX(i);
                touchPosOut.y = event.getY(i);
                break;
            }
        }

        return returnVal;
    }

}
