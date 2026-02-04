package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Subsystem.Drivetrain;
//import org.firstinspires.ftc.teamcode.Subsystem.HoodServo;
import org.firstinspires.ftc.teamcode.Subsystem.Intake;
//import org.firstinspires.ftc.teamcode.Subsystem.LazySusan;
//import org.firstinspires.ftc.teamcode.Subsystem.ContinuousServo;
//import org.firstinspires.ftc.teamcode.Subsystem.liftShooter;
import org.firstinspires.ftc.teamcode.Util.DriverUtil.Rumbler;
import org.firstinspires.ftc.teamcode.Subsystem.Shooter;
import org.firstinspires.ftc.teamcode.Util.RobotStates;
import org.firstinspires.ftc.teamcode.Util.Toggle;
import org.firstinspires.ftc.teamcode.Util.DriverUtil.ControlScheme;

@TeleOp
public class Robot extends OpMode {
    private final Drivetrain drivetrain = new Drivetrain();
    private final Shooter shooter = new Shooter();
    private final Rumbler driverRumbler = new Rumbler();
    private final Rumbler operatorRumbler = new Rumbler();
//    private final LazySusan lazySusan = new LazySusan();
    private final Intake intake = new Intake();
    //private final HoodServo hoodServo = new HoodServo();
//    private final ContinuousServo continuousServo = new ContinuousServo();

    private final Toggle drivetrainToggle = new Toggle();
    private final Toggle shooterToggle = new Toggle();

    public void init() {
        ControlScheme.initDriver(gamepad1);
        ControlScheme.initOperator(gamepad2);
        this.drivetrain.init(hardwareMap);
        this.intake.init(hardwareMap);
        this.shooter.init(hardwareMap);
        //this.hoodServo.init(hardwareMap);
    }

    @Override
    public void start() {
        driverRumbler.startMatchTimer();
        operatorRumbler.startMatchTimer();
    }

    public void doTelemetry() {
//        this.hoodServo.hoodTelem(telemetry);
//        this.shooter.addTelemetry(telemetry);
//        this.intake.intakeTelem(telemetry);
        this.drivetrain.drivetrainData(telemetry);
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

        if (shooterToggle.detectPress(ControlScheme.SHOOTER_CYCLE.get())) {
            this.shooter.cycle(telemetry);
        }

        if (ControlScheme.SHOOTER_REVERSE.get()) {
            this.shooter.runBackward();
        } else {
            this.shooter.stopBackward();
        }

        this.shooter.update();

        if (this.shooter.isAtTargetRPM() && this.shooter.shooterStatus == RobotStates.ShooterStates.FORWARD) {
            //this.intake.run(1, 0, 0, 0);
        } else {
            this.intake.run(
                    ControlScheme.INTAKE_IN.get(),
                    ControlScheme.INTAKE_OUT.get(),
                    ControlScheme.FEEDER_IN.get(),
                    ControlScheme.FEEDER_OUT.get()
            );
        }

//        this.hoodServo.loop(
//                ControlScheme.HOOD_POS_ONE.get(),
//                ControlScheme.HOOD_POS_TWO.get(),
//                ControlScheme.HOOD_POS_THREE.get(),
//                ControlScheme.HOOD_POS_FOUR.get(),
//                ControlScheme.HOOD_POS_FIVE.get()
//        );
//
//        this.driverRumbler.halftimeRumble(gamepad1);
//        this.operatorRumbler.halftimeRumble(gamepad2);

//        doTelemetry();
    }
}