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

    ElapsedTime runtime = new ElapsedTime();

    public void halfimeRumble(){

        //format is .addStep( <left motor strength(0-1)>, <right motor strength(0-1)>, <duration(miliseconds)>)
        halftimerumbleEffect = new Gamepad.RumbleEffect.Builder()
                .addStep(1.0, 0.0, 500)
                .addStep(0.0, 1.0, 500)
                .addStep(1.0, 1.0, 500)
                .build();

        runtime.reset();

        if ((runtime.seconds() > MATCH_HALF_TIME) && !secondHalf)  {
            gamepad1.runRumbleEffect(halftimerumbleEffect);
            secondHalf = true;
        }

    }

    public void rumbleLeft(float intensity, int milis){
        leftrumbleEffect = new Gamepad.RumbleEffect.Builder()
                .addStep(intensity, 0.0, milis)
                .build();
    }

    public void rumbleRight(float intensity, int milis){
        leftrumbleEffect = new Gamepad.RumbleEffect.Builder()
                .addStep(0.0, intensity, milis)
                .build();
    }
}
