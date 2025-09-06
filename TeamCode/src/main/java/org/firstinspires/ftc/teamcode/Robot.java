package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Actions.Lower;
import org.firstinspires.ftc.teamcode.Actions.Raise;
import org.firstinspires.ftc.teamcode.Subsystem.Arm;
import org.firstinspires.ftc.teamcode.Subsystem.Claw;
import org.firstinspires.ftc.teamcode.Subsystem.Drivetrain;
import org.firstinspires.ftc.teamcode.Subsystem.LinearSlide;
import org.firstinspires.ftc.teamcode.Subsystem.Wrist;
import org.firstinspires.ftc.teamcode.Util.RobotStates;
import org.firstinspires.ftc.teamcode.Util.Toggle;

@TeleOp
public class Robot extends OpMode {
    private final Drivetrain drivetrain = new Drivetrain();
    private final Arm arm = new Arm();
    private final LinearSlide linearSlide = new LinearSlide();
    private final Claw claw = new Claw();
    private final Wrist wrist = new Wrist();

    private final Raise raiseCommand = new Raise(linearSlide, arm, wrist);
    private final Lower lowerCommand = new Lower(linearSlide, arm, wrist);

    private final Toggle drivetrainToggle = new Toggle();
    private final Toggle raiseToggle = new Toggle();
    
    public void init() {
        this.drivetrain.init(hardwareMap);
        this.arm.init(hardwareMap);
        this.linearSlide.init(hardwareMap);
        this.claw.init(hardwareMap);
        this.wrist.init(hardwareMap);
    }

    @Override
    public void loop() {
        this.linearSlide.slideData(telemetry);
        this.arm.armTelemetry(telemetry);

        if (raiseToggle.toggleButton(gamepad2.y)) {
            if (wrist.getCurrentState() != RobotStates.Wrist.SCORE) {
                this.raiseCommand.raise();
            }
        } else {
            if (this.wrist.getCurrentState() != RobotStates.Wrist.FLOOR) {
                this.lowerCommand.lower();
                this.linearSlide.setState(RobotStates.LinearSlide.MANUEL);
            }
        }

        this.drivetrain.mechanumDrive(
                gamepad1.left_stick_x,
                gamepad1.left_stick_y,
                gamepad1.right_stick_x,
                drivetrainToggle.toggleButton(gamepad1.a)
        );

        this.linearSlide.goToState(gamepad2);
        this.arm.goToState();
        this.wrist.goToState(gamepad2);
        this.claw.goToState();
    }
}