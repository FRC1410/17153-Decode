package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.Subsystem.Shooter;
import org.firstinspires.ftc.teamcode.Util.Toggle;
import org.firstinspires.ftc.teamcode.Util.DriverUtil.ControlScheme;

@TeleOp
public class RoboTelem extends OpMode {
    private boolean reachedIdle = false;
    private final double idleRPM = 2000;
    private double peakRPM = 0;
    private double RPMdrop = idleRPM;
    private final Shooter shooter = new Shooter();
//    private final ContinuousServo continuousServo = new ContinuousServo();

    ElapsedTime runtime = new ElapsedTime();
    private final Toggle drivetrainToggle = new Toggle();
    private final Toggle shooterToggle = new Toggle();

    public void init() {
        telemetry.setMsTransmissionInterval(20);
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

        if (ControlScheme.SHOOTER_REVERSE.get()) {
            this.shooter.runBackward();
        } else {
            this.shooter.stopBackward();
        }


        this.shooter.feed(ControlScheme.FEED.get());
        if (shooter.getRPM() > peakRPM){
            peakRPM = shooter.getRPM();
            if (peakRPM+100 >= idleRPM){
                reachedIdle = true;
            }
        }
        if (reachedIdle){
            if (shooter.getRPM() < RPMdrop){
                RPMdrop = shooter.getRPM();
            }
        }

        telemetry.addData("Peak RPM", peakRPM);
        telemetry.addData("Shooter RPM", shooter.getRPM());
        telemetry.addData("isIdle",reachedIdle);
        telemetry.addData("RPM drop", RPMdrop);
        telemetry.update();
    }
}