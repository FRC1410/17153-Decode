package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Subsystem.Drivetrain;
import org.firstinspires.ftc.teamcode.Util.RobotStates;
import org.firstinspires.ftc.teamcode.Util.Toggle;


@TeleOp
public class Robot extends OpMode {
    private final Drivetrain drivetrain = new Drivetrain();


    private final Toggle drivetrainToggle = new Toggle();
    
    public void init() {
        this.drivetrain.init(hardwareMap);

    }

    @Override
    public void loop() {

        this.drivetrain.drivetrainData(telemetry);

        this.drivetrain.tankDrive(
                gamepad1.right_stick_y,
                gamepad1.left_stick_y,
                drivetrainToggle.toggleButton(gamepad1.a)
        );

    }
}