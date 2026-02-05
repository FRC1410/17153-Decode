package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.Subsystem.Shooter;
import org.firstinspires.ftc.teamcode.Util.Toggle;
import org.firstinspires.ftc.teamcode.Util.DriverUtil.ControlScheme;

@TeleOp(name="ShooterTune")
public class ShootTune extends OpMode {
    private double targetShooterRPM = 2100;
    private final Shooter shooter = new Shooter();
//    private final ContinuousServo continuousServo = new ContinuousServo();

    ElapsedTime runtime = new ElapsedTime();
    private final Toggle drivetrainToggle = new Toggle();
    private final Toggle shooterToggle = new Toggle();

    public void init() {
        telemetry.setMsTransmissionInterval(50); //the loop funciton is ran 20 times per second, so this is the most reasonable update rate for telemetry (a lot better than 250ms)
        ControlScheme.initDriver(gamepad1);
        ControlScheme.initOperator(gamepad2);
        this.shooter.init(hardwareMap);
//        this.hoodServo.init(hardwareMap);
    }

    @Override
    public void start() {
        runtime.reset();
    }

    public void doTelemetry() {
//        this.hoodServo.hoodTelem(telemetry);
//        this.shooter.addTelemetry(telemetry);
        telemetry.update();
    }

    @Override
    public void loop() {
        if (shooterToggle.detectPress(ControlScheme.SHOOTER_CYCLE.get())) {
            this.shooter.cycle(telemetry);
        }
        this.shooter.setTargetRPM(targetShooterRPM);

        if (ControlScheme.SHOOTER_REVERSE.get()) {
            this.shooter.runBackward();
        } else {
            this.shooter.stopBackward();
        }

        if (gamepad2.dpad_up){
            targetShooterRPM += 10;
        } else if (gamepad2.dpad_down) {
            targetShooterRPM -= 10;
        }

        this.shooter.feed(ControlScheme.FEED.get());

        telemetry.addData("Target RPM",targetShooterRPM);
        telemetry.addData("shooter RPM", shooter.getRPM());
        telemetry.update();
    }
}