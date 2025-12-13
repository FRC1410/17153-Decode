package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Subsystem.Drivetrain;
import org.firstinspires.ftc.teamcode.Subsystem.HoodServo;
import org.firstinspires.ftc.teamcode.Subsystem.Intake;
import org.firstinspires.ftc.teamcode.Subsystem.LazySusan;
import org.firstinspires.ftc.teamcode.Subsystem.ContinuousServo;
import org.firstinspires.ftc.teamcode.Util.DriverUtil.Rumbler;
import org.firstinspires.ftc.teamcode.Subsystem.Shooter;
import org.firstinspires.ftc.teamcode.Util.RobotStates;
import org.firstinspires.ftc.teamcode.Util.Toggle;
import org.firstinspires.ftc.teamcode.Util.DriverUtil.ControlScheme;

@TeleOp
public class Robot extends OpMode {
    private final Drivetrain drivetrain = new Drivetrain();
    private final Shooter shooter = new Shooter();
    private final Rumbler rumbler = new Rumbler();
    private final LazySusan lazySusan = new LazySusan();
    private final Intake intake = new Intake();
    private final HoodServo hoodServo = new HoodServo();
    private final ContinuousServo continuousServo = new ContinuousServo();

    private final Toggle drivetrainToggle = new Toggle();
    
    public void init() {
        this.lazySusan.init(hardwareMap);
        ControlScheme.initDriver(gamepad1);
        ControlScheme.initOperator(gamepad2);
        this.drivetrain.init(hardwareMap);
        this.intake.init(hardwareMap);
        this.shooter.init(hardwareMap);
        this.hoodServo.init(hardwareMap);
        this.continuousServo.init(hardwareMap);

    }

    @Override
    public void start() {
        rumbler.startMatchTimer();
    }

    public void doTelemetry() {
//        this.drivetrain.drivetrainData(telemetry);
//        this.intake.intakeTelem(telemetry);
//        this.hoodServo.hoodTelem(telemetry);
//        this.continuousServo.continuousServoTelem(telemetry);
        this.lazySusan.susanTelem(telemetry);

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

        this.intake.run(
                ControlScheme.INTAKE_IN.get(),
                ControlScheme.INTAKE_OUT.get()
        );

        this.lazySusan.loop(
                ControlScheme.SUSAN_MANUAL_ONE.get(),
                ControlScheme.SUSAN_MANUAL_TWO.get(),
                ControlScheme.SUSAN_MANUAL_THREE.get(),
                ControlScheme.SUSAN_LIFT.get()
        );

        this.hoodServo.loop(
                ControlScheme.HOOD_POS_ONE.get(),
                ControlScheme.HOOD_POS_TWO.get(),
                ControlScheme.HOOD_POS_THREE.get(),
                ControlScheme.HOOD_POS_FOUR.get(),
                ControlScheme.HOOD_POS_FIVE.get()
        );

        this.continuousServo.loop(
                ControlScheme.CONTINUOUS_SERVO_TOGGLE.get()
        );

        if (ControlScheme.SHOOTER_CYCLE.get()) {
            this.shooter.cycle(telemetry);
        }
        //this stays last in this method:
        doTelemetry();
    }
}