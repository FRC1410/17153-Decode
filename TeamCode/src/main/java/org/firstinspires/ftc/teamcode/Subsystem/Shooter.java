package org.firstinspires.ftc.teamcode.Subsystem;

import static org.firstinspires.ftc.teamcode.Util.IDs.SHOOTER_MOTOR_ID;
import static org.firstinspires.ftc.teamcode.Util.IDs.FEEDER_MOTOR_ID;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Util.RobotStates;

public class Shooter {

    private DcMotorEx motorShooter;
    private DcMotorEx motorFeeder;
    public RobotStates.ShooterStates shooterStatus = RobotStates.ShooterStates.NEUTRAL;

    private static final double TARGET_RPM = 1;
    private static final double RPM_TOLERANCE = 1500; // RPM threshold to start feeder

    public void init(HardwareMap hardwareMap) {
        this.motorShooter = hardwareMap.get(DcMotorEx.class, SHOOTER_MOTOR_ID);
        this.motorFeeder = hardwareMap.get(DcMotorEx.class, FEEDER_MOTOR_ID);

        this.motorShooter.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.motorFeeder.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        this.motorShooter.setDirection(DcMotorSimple.Direction.FORWARD);
        this.motorFeeder.setDirection(DcMotorSimple.Direction.FORWARD);

        this.motorShooter.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.motorFeeder.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        this.motorShooter.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        this.motorFeeder.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        shooterStatus = RobotStates.ShooterStates.NEUTRAL;
        run(shooterStatus);
    }

    public void cycle(Telemetry telemetry) {
        switch (this.shooterStatus) {
            case FORWARD:
                this.shooterStatus = RobotStates.ShooterStates.NEUTRAL;
                break;
            case NEUTRAL:
                this.shooterStatus = RobotStates.ShooterStates.FORWARD;
                break;
        }
        run(this.shooterStatus);
    }

    public void run(RobotStates.ShooterStates shooterState){
        switch (shooterState) {
            case FORWARD:
                this.motorShooter.setPower(TARGET_RPM);
                break;
            case NEUTRAL:
                this.motorShooter.setVelocity(0);
                this.motorFeeder.setPower(0);
                break;
        }
    }

    public void runBackward() {
        this.motorShooter.setVelocity(-1500);
        this.motorFeeder.setVelocity(-1500);
    }

    public void stopBackward() {
        run(this.shooterStatus);
    }
    public void update() {
        if (this.shooterStatus == RobotStates.ShooterStates.FORWARD) {
            double currentVelocity = this.motorShooter.getVelocity();

            if (Math.abs(currentVelocity - TARGET_RPM) < RPM_TOLERANCE) {
                this.motorFeeder.setVelocity(2000);
            } else {
                this.motorFeeder.setPower(0);
            }
        }
    }

    public void feed(float power){
        this.motorFeeder.setVelocity(power * 2000);
    }
    public boolean isAtTargetRPM() {
        double currentVelocity = this.motorShooter.getVelocity();
        return Math.abs(currentVelocity - TARGET_RPM) < RPM_TOLERANCE;
    }
    public void addTelemetry(Telemetry telemetry) {
        telemetry.addData("Shooter State", this.shooterStatus);
        telemetry.addData("Shooter RPM", this.motorShooter.getVelocity());
        telemetry.addData("Target RPM", TARGET_RPM);
        telemetry.addData("At Target", isAtTargetRPM());
        telemetry.addData("Feeder Power", this.motorFeeder.getPower());
    }
}