package org.firstinspires.ftc.teamcode.Auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name="Robot: Auto Concept 1", group="Auto")
public class AutoOne extends LinearOpMode {

    private AutoDriveTrain drivetrain;

    @Override
    public void runOpMode() throws InterruptedException {
        drivetrain = new AutoDriveTrain();
        drivetrain.init(hardwareMap);  // You were missing this!

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();

        telemetry.addData("Auto started", opModeIsActive());
        telemetry.update();

        drivetrain.drive(-1, 0, 0);
        sleep(800);
        drivetrain.drive(0, 0, 0);
    }
}
