package org.firstinspires.ftc.teamcode.Subsystem;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.VoltageSensor;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Util.RobotStates;
import org.firstinspires.ftc.teamcode.Util.PIDController;

import static org.firstinspires.ftc.teamcode.Util.IDs.*;
import static org.firstinspires.ftc.teamcode.Util.Tuning.*;

public class LinearSlide {
    private DcMotorEx leftSlideMotor;
    private DcMotorEx rightSlideMotor;
    private VoltageSensor controlHubVoltageSensor;

    private PIDController leftPIDController = new PIDController(LINEAR_SLIDE_P, LINEAR_SLIDE_I, LINEAR_SLIDE_D);
    private PIDController rightPIDController = new PIDController(LINEAR_SLIDE_P, LINEAR_SLIDE_I, LINEAR_SLIDE_D);

    private RobotStates.LinearSlide currentSlideState = RobotStates.LinearSlide.START_POS;
    private int slideEncoderValue;
    private int customEncoderVal;

    public void init(HardwareMap hardwareMap) {
        this.leftSlideMotor = hardwareMap.get(DcMotorEx.class, LEFT_SLIDE_MOTOR_ID);
        this.rightSlideMotor = hardwareMap.get(DcMotorEx.class, RIGHT_SLIDE_MOTOR_ID);

        this.leftSlideMotor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        this.rightSlideMotor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);

        this.leftSlideMotor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        this.rightSlideMotor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);

        this.leftSlideMotor.setDirection(DcMotorEx.Direction.REVERSE);
        this.rightSlideMotor.setDirection(DcMotorEx.Direction.FORWARD);

        this.leftSlideMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.rightSlideMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        this.controlHubVoltageSensor = hardwareMap.voltageSensor.iterator().next();

        this.leftPIDController = new PIDController(LINEAR_SLIDE_P, LINEAR_SLIDE_I, LINEAR_SLIDE_D);
        this.rightPIDController = new PIDController(LINEAR_SLIDE_P, LINEAR_SLIDE_I, LINEAR_SLIDE_D);
    }

    public RobotStates.LinearSlide getCurrentState() {
        return currentSlideState;
    }
    public void setState(RobotStates.LinearSlide desiredState) {
        currentSlideState = desiredState;
    }

    public void setStateEncoderVal(RobotStates.LinearSlide linearSlideState) {
        switch (linearSlideState) {
            case START_POS:
                slideEncoderValue = 0;
                break;

            case MANUEL:
                slideEncoderValue = customEncoderVal;
                break;

            case HIGH_SCORE:
                slideEncoderValue = 2_000;
                break;
        }
    }

    public void goToState(Gamepad gamepad) {
        RobotStates.LinearSlide desiredState = this.getCurrentState();

        this.setStateEncoderVal(desiredState);

        int currentLeftPos = this.leftSlideMotor.getCurrentPosition() * -1;
        double leftOutput = leftPIDController.calculate(slideEncoderValue, -currentLeftPos);

        int currentRightPos = this.rightSlideMotor.getCurrentPosition() * -1;
        double rightOutput = this.rightPIDController.calculate(slideEncoderValue, -currentRightPos);

        this.leftSlideMotor.setPower(leftOutput);
        this.rightSlideMotor.setPower(rightOutput);

        if (desiredState == RobotStates.LinearSlide.MANUEL) {
            this.manualMode(gamepad);
        }

        if (Math.abs(slideEncoderValue - currentLeftPos) <= LINEAR_SLIDE_THRESHOLD) {
            leftSlideMotor.setPower(0);
        }
        if (Math.abs(slideEncoderValue - currentRightPos) <= LINEAR_SLIDE_THRESHOLD) {
            rightSlideMotor.setPower(0);
        }
    }

    public void manualMode(Gamepad gamepad) {
        double leftVal = -gamepad.left_trigger * 100;
        double rightVal = gamepad.right_trigger * 100;

        int currentLeftPos = this.leftSlideMotor.getCurrentPosition() * -1;
        int currentRightPos = this.rightSlideMotor.getCurrentPosition() * -1;

        this.customEncoderVal = (int) (rightVal + leftVal);

        if (customEncoderVal > LINEAR_SLIDE_MANUAL_LIMIT) {
            customEncoderVal = LINEAR_SLIDE_MANUAL_LIMIT;
        }

        double leftOutput = this.leftPIDController.calculate(this.customEncoderVal, -currentLeftPos);
        double rightOutput = this.rightPIDController.calculate(this.customEncoderVal, -currentRightPos);

        this.leftSlideMotor.setPower(leftOutput);
        this.rightSlideMotor.setPower(rightOutput);
    }

    public void setSlidePower(float leftTrigger, float rightTrigger) {
        double voltageCorrection = 12 / controlHubVoltageSensor.getVoltage();

        this.leftSlideMotor.setPower((rightTrigger - leftTrigger) * voltageCorrection);
        this.rightSlideMotor.setPower((rightTrigger - leftTrigger) * voltageCorrection);
    }

    public void slideData(Telemetry telemetry) {
        telemetry.addData("Left Slide Encoder: ", this.leftSlideMotor.getCurrentPosition());
        telemetry.addData("Right Slide Encoder: ", this.rightSlideMotor.getCurrentPosition());
    }
}
