package com.example.mossymobile.VibeoGeam;

import android.view.View;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.mossymobile.MossFramework.Components.RigidBody;
import com.example.mossymobile.MossFramework.GameView;
import com.example.mossymobile.MossFramework.Math.Vector2;
import com.example.mossymobile.MossFramework.MonoBehaviour;
import com.example.mossymobile.MossFramework.Systems.UserInput.Input;
import com.example.mossymobile.R;

public class PlayerController extends MonoBehaviour {
    RigidBody rb;

    int touchID = -1;
    Vector2 touchPos = new Vector2();

    void InitateUI() {
        // Reset the UIDocker to Prepare to put in a new UI
        ConstraintLayout UIDocker = GameView.GetInstance().GetActivity().findViewById(R.id.gameUIDocker);
        UIDocker.removeAllViews();

        View ui_view = GameView.GetInstance().GetActivity().getLayoutInflater().inflate(R.layout.ui_game, UIDocker, false);

        ui_view.findViewById(R.id.joystick_region);
        ui_view.findViewById(R.id.joystick_knob);

        UIDocker.addView(ui_view);

        //View shopView = activity.getLayoutInflater().inflate(shopElementID, shopItemDisplay, false);
        //View gameView = GameView.GetInstance().GetActivity().getLayoutInflater().inflate(R.id.game_hud);
    }

    @Override
    public void Start() {
        InitateUI();
    }

    @Override
    public void Update() {
        touchID = Input.GetTouchPosition(touchPos, touchID);
//GameView.GetInstance().GetActivity().findViewById();

    }
}