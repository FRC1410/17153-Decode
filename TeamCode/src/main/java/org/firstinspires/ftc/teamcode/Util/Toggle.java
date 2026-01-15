package org.firstinspires.ftc.teamcode.Util;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.concurrent.TimeUnit;

public class Toggle {
    private int counter = 1;
    private final ElapsedTime timer = new ElapsedTime();
    private boolean pressedInTime = false;

    private boolean previousInput = false;
    // Thank you Charlie

    public boolean toggleButton(boolean input) {
        boolean wasPressed;

        if (input && !pressedInTime) {
            counter++;
            pressedInTime = true;
            timer.reset();
        }

        if (timer.time(TimeUnit.MILLISECONDS) >= 200) {
            pressedInTime = false;
        }

        if (counter % 2 == 0) {
            wasPressed = true;
        } else {
            wasPressed = false;
        }
        return wasPressed;
    }
    public boolean detectPress(boolean input) {
        boolean pressed = input && !previousInput;
        previousInput = input;
        return pressed;
    }
}
