package org.firstinspires.ftc.teamcode.Util.DriverUtil;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.gamepad1;
import static org.firstinspires.ftc.teamcode.Util.Constants.*;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

public class Rumbler {

    boolean highLevel = false;

    boolean secondHalf = false;

    Gamepad.RumbleEffect halftimerumbleEffect;

    Gamepad.RumbleEffect leftrumbleEffect;
    Gamepad.RumbleEffect rightrumbleEffect;

    Gamepad.RumbleEffect rumblecontinuousEffect;

    ElapsedTime runtime = new ElapsedTime();
    //format is .addStep( <left motor strength(0-1)>, <right motor strength(0-1)>, <duration(miliseconds)>)
        public Rumbler() {
            halftimerumbleEffect = new Gamepad.RumbleEffect.Builder()
                    .addStep(1.0, 0.0, 500)  // Left motor
                    .addStep(0.0, 1.0, 500)  // Right motor
                    .addStep(1.0, 1.0, 500)  // Both motors
                    .build();
        }

    public void rumbleLeft(Gamepad gamepad, int milis){
        leftrumbleEffect = new Gamepad.RumbleEffect.Builder()
                .addStep(1.0, 0.0, milis)
                .build();
    }

    public void rumbleRight(Gamepad gamepad, int milis){
        leftrumbleEffect = new Gamepad.RumbleEffect.Builder()
                .addStep(0.0, 1.0, milis)
                .build();
    }

    public void halftimeRumble(Gamepad gamepad) {
        if ((runtime.seconds() > MATCH_HALF_TIME) && !secondHalf) {
            gamepad.runRumbleEffect(halftimerumbleEffect);
            secondHalf = true;
        }
    }

    public void rumbleWhileTrue(Gamepad gamepad, boolean truth){
        rumblecontinuousEffect = new Gamepad.RumbleEffect.Builder()
                .addStep(0.5, 0.5, 2)
                .addStep(0.5, 0.5, 2)
                .build();
        if (truth){
            gamepad.runRumbleEffect(rumblecontinuousEffect);
        } else {
            stopRumble(gamepad);
        }
    }

    public boolean isRumbling(Gamepad gamepad) {
        return gamepad.isRumbling();
    }

    public void stopRumble(Gamepad gamepad) {
        gamepad.stopRumble();
    }

    public void startMatchTimer() {
        runtime.reset();
        secondHalf = false;
    }

    public void rumbleBlips(Gamepad gamepad, int count) {
        gamepad.rumbleBlips(count);
    }

    public void rumbleBlip(Gamepad gamepad) {
        gamepad.rumbleBlips(1);
    }
}
