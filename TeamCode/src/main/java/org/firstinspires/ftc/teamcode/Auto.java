package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.Subsystem.Drivetrain;
import org.firstinspires.ftc.teamcode.Util.Toggle;

@Autonomous
public class Auto extends LinearOpMode {

    private final Drivetrain drivetrain = new Drivetrain();

    private final Toggle drivetrainToggle = new Toggle();

    @Override
    public void runOpMode() {
        waitForStart();
        // run pedro path

        while (opModeIsActive()){
            telemetry.addData("Hello","World!");
            telemetry.update();
        }
    }
}
