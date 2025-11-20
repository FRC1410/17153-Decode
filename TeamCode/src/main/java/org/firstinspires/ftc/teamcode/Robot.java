package org.firstinspires.ftc.teamcode;

import android.service.controls.Control;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Subsystem.Drivetrain;
import org.firstinspires.ftc.teamcode.Subsystem.Intake;
import org.firstinspires.ftc.teamcode.Util.RobotStates;
import org.firstinspires.ftc.teamcode.Util.DriverUtil.Rumbler;
import org.firstinspires.ftc.teamcode.Util.Toggle;
import org.firstinspires.ftc.teamcode.Util.DriverUtil.ControlScheme;

@TeleOp
public class Robot extends OpMode {
    private final Rumbler rumbler = new Rumbler();
    private final Drivetrain drivetrain = new Drivetrain();
    private final Intake intake = new Intake();

    private final Toggle drivetrainToggle = new Toggle();
    
    public void init() {
        ControlScheme.initDriver(gamepad1);
        ControlScheme.initOperator(gamepad2);
        this.drivetrain.init(hardwareMap);
        this.intake.init(hardwareMap);
    }

    @Override
    public void start() {
        rumbler.startMatchTimer();
    }

    public void doTelemetry() {
        this.drivetrain.drivetrainData(telemetry);
        this.intake.intakeTelem(telemetry);

        //this always goes last in this method:
        telemetry.update();


    }

    @Override
    public void loop() {

        this.drivetrain.mechanumDrive(
                ControlScheme.DRIVE_STRAFE.get(),
                ControlScheme.DRIVE_FB.get(),
                ControlScheme.DRIVE_ROTATE.get(),
                drivetrainToggle.toggleButton(ControlScheme.DRIVE_SLOW_MODE.get())
        );
//        this.intake.run(ControlScheme.INTAKE_IN.get(), ControlScheme.INTAKE_OUT.get());
        this.intake.run(gamepad1.left_trigger, gamepad1.right_trigger);


        //this stays last in this method:
        doTelemetry();
    }
}