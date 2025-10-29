package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Subsystem.Drivetrain;
import org.firstinspires.ftc.teamcode.Subsystem.Shooter;
import org.firstinspires.ftc.teamcode.Util.RobotStates;
import org.firstinspires.ftc.teamcode.Util.Toggle;

@TeleOp
public class Robot extends OpMode {
    private final Drivetrain drivetrain = new Drivetrain();
    private final Shooter shooter = new Shooter();

    private final Toggle drivetrainToggle = new Toggle();
    private final Toggle raiseToggle = new Toggle();
    
    public void init() {
        this.drivetrain.init(hardwareMap);
        this.shooter.init(hardwareMap);

    }

    @Override
    public void loop() {

        this.drivetrain.mechanumDrive(
                gamepad1.left_stick_x,
                gamepad1.left_stick_y,
                gamepad1.right_stick_x,
                drivetrainToggle.toggleButton(gamepad1.a)
        );
        this.shooter.run(gamepad1.right_trigger, gamepad1.left_trigger);

    }
}