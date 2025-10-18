package org.firstinspires.ftc.teamcode.Subsystem;

import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.FORWARD;
import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.REVERSE;

import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

import static org.firstinspires.ftc.teamcode.Util.IDs.*;
import static org.firstinspires.ftc.teamcode.Util.Constants.*;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Util.RobotStates;

public class Drivetrain {

    private DcMotorEx tankL;
    private DcMotorEx tankR;

    private VoltageSensor controlHubVoltageSensor;
    private IMU imu;
    private double[] wheelSpeeds = new double[2];
    private double maxPower = 1;
    private RobotStates.Drivetrain currentDrivetrainMode = RobotStates.Drivetrain.FULL_SPEED;

    public void init(HardwareMap hardwareMap) {
        this.tankR = hardwareMap.get(DcMotorEx.class, TANK_RIGHT_ID);
        this.tankL = hardwareMap.get(DcMotorEx.class, TANK_LEFT_ID);

        this.tankL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.tankR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        this.tankL.setDirection(REVERSE);
        this.tankR.setDirection(FORWARD);

        this.tankL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.tankR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        this.tankL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        this.tankR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        this.controlHubVoltageSensor = hardwareMap.voltageSensor.iterator().next();
        this.imu = hardwareMap.get(IMU.class, CONTROL_HUB_IMU);
        this.imu.initialize(new IMU.Parameters(HUB_ORIENTATION));
    }

    public void tankDrive(
            double leftSpeed,
            double rightSpeed,
            boolean isHalfSpeed)
    {
        leftSpeed = Range.clip(leftSpeed, -1, 1);
        rightSpeed = Range.clip(rightSpeed, -1, 1);

        this.wheelSpeeds[0] = leftSpeed;
        this.wheelSpeeds[1] = rightSpeed;

        double voltageCorrection = 12 / controlHubVoltageSensor.getVoltage();

        for (int i = 0; i < this.wheelSpeeds.length; i++) {
            this.wheelSpeeds[i] = Math.abs(this.wheelSpeeds[i]) < 0.01 ?
                    this.wheelSpeeds[i] * voltageCorrection :
                    (this.wheelSpeeds[i] + Math.signum(this.wheelSpeeds[i]) * 0.085) * voltageCorrection;
        }

        maxPower = Math.max(Math.abs(this.wheelSpeeds[0]), Math.abs(this.wheelSpeeds[1]));

        if (maxPower > 1) {
            this.wheelSpeeds[0] /= maxPower;
            this.wheelSpeeds[1] /= maxPower;
        }

        if (isHalfSpeed) {
            this.setDrivetrainMode(RobotStates.Drivetrain.HALF_SPEED);
        } else {
            this.setDrivetrainMode(RobotStates.Drivetrain.FULL_SPEED);
        }

        double speedMultiplier = (this.getDrivetrainMode() == RobotStates.Drivetrain.HALF_SPEED) ? 1.0 : 2.0;

        this.tankL.setPower(this.wheelSpeeds[0] * speedMultiplier);
        this.tankR.setPower(this.wheelSpeeds[1] * speedMultiplier);
    }

    public RobotStates.Drivetrain getDrivetrainMode() {
        return this.currentDrivetrainMode;
    }

    public void setDrivetrainMode(RobotStates.Drivetrain desiredMode) {
        this.currentDrivetrainMode = desiredMode;
    }

    //Returns in ticks per second
    public double getCurrentVelocity() {
        double currentLVelocity = this.tankL.getVelocity();
        double currentRVelocity = this.tankR.getVelocity();
        return (currentLVelocity + currentRVelocity) / 2;
    }

    // In inches
    public double distanceToEncoderCount(double desiredDistanceInInches) {
        return desiredDistanceInInches * ENCODER_COUNT_PER_INCH;
    }

    // In inches per second
    public double velocityToEncoderCount(double desiredVelocity) {
        return desiredVelocity * ENCODER_COUNT_PER_INCH;
    }

    public void drivetrainData(Telemetry telemetry) {
        telemetry.addData("Left: ", this.tankL.getCurrentPosition());
        telemetry.addData("Right: ", this.tankR.getCurrentPosition());
        telemetry.addData("Velocity L: ", this.tankL.getVelocity());
        telemetry.addData("Velocity R:", this.tankR.getVelocity());
        telemetry.update();
    }
}
