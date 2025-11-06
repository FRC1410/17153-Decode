package org.firstinspires.ftc.teamcode.Util.DriverUtil;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.gamepad1;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

public class Rumbler {

    boolean highLevel = false;

    boolean secondHalf = false;

    Gamepad.RumbleEffect rumbleEffect;

    ElapsedTime runtime = new ElapsedTime();

    final double TRIGGER_TIME = 60.0; //seconds

    public void rumble(){

        //format is .addStep( <left motor strength(0-1)>, <right motor strength(0-1)>, <duration(miliseconds)>)
        rumbleEffect = new Gamepad.RumbleEffect.Builder()
                .addStep(1.0, 0.0, 500)
                .addStep(0.0, 1.0, 500)
                .addStep(1.0, 1.0, 500)
                .build();

        runtime.reset();

        if ((runtime.seconds() > TRIGGER_TIME) && !secondHalf)  {
            gamepad1.runRumbleEffect(rumbleEffect);
            secondHalf = true;
        }

    }
}
