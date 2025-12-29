package org.firstinspires.ftc.teamcode.Subsystem;

import static org.firstinspires.ftc.teamcode.Util.IDs.*;
import static org.firstinspires.ftc.teamcode.Util.Tuning.*;
import static org.firstinspires.ftc.teamcode.Util.Constants.*;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Util.PIDController;
import org.firstinspires.ftc.teamcode.Util.RobotStates;

public class HoodServo {
    private Servo hoodServo;

    private double servoPos;
    private RobotStates.HoodPosition desiredHoodState = RobotStates.HoodPosition.POS_ONE;

    private PIDController hoodPIDController;
    private double lastOutput = 0;

    public void init(HardwareMap hardwareMap) {
        this.hoodServo = hardwareMap.get(Servo.class, HOOD_SERVO_ID);
        this.hoodServo.setDirection(Servo.Direction.FORWARD);

        this.hoodPIDController = new PIDController(HOOD_P, HOOD_I, HOOD_D);
    }

    public RobotStates.HoodPosition getDesiredHoodState() {
        return this.desiredHoodState;
    }

    public void setDesiredHoodState(RobotStates.HoodPosition desiredHoodState) {
        if (this.desiredHoodState != desiredHoodState) {
            this.hoodPIDController.reset();
        }
        this.desiredHoodState = desiredHoodState;
    }

    public double getHoodPos(RobotStates.HoodPosition desiredHoodState) {
        switch (desiredHoodState) {
            case POS_ONE:
                this.servoPos = HOOD_POS_1;
                break;
            case POS_TWO:
                this.servoPos = HOOD_POS_2;
                break;
            case POS_THREE:
                this.servoPos = HOOD_POS_3;
                break;
            case POS_FOUR:
                this.servoPos = HOOD_POS_4;
                break;
            case POS_FIVE:
                this.servoPos = HOOD_POS_5;
                break;
        }
        return this.servoPos;
    }

    public double getActualHoodPosition() {
        return this.hoodServo.getPosition();
    }

    public double hoodGoToState() {
        RobotStates.HoodPosition desiredState = this.getDesiredHoodState();
        double desiredPos = this.getHoodPos(desiredState);
        double currentPos = this.getActualHoodPosition();
        double error = desiredPos - currentPos;

        if (Math.abs(error) <= HOOD_THRESHOLD) {
            this.hoodServo.setPosition(desiredPos);
            this.lastOutput = 0;
            return 0;
        }

        double output = this.hoodPIDController.calculate(desiredPos, currentPos);

        output = Math.max(-1.0, Math.min(1.0, output));

        if (Math.abs(output) < HOOD_MIN_POWER && Math.abs(output) > 0.001) {
            if (output > 0) {
                output = HOOD_MIN_POWER;
            } else {
                output = -HOOD_MIN_POWER;
            }
        } else if (Math.abs(output) <= 0.001) {
            output = 0;
        }


        double newPos = currentPos + output;
        newPos = Math.max(0.0, Math.min(1.0, newPos));

        this.hoodServo.setPosition(newPos);
        this.lastOutput = output;
        return output;
    }

    public void loop(boolean pos1, boolean pos2, boolean pos3, boolean pos4, boolean pos5) {
        double desiredPos = this.getHoodPos(this.getDesiredHoodState());
        double currentPos = this.getActualHoodPosition();
        double error = Math.abs(desiredPos - currentPos);
        boolean atTarget = error <= HOOD_THRESHOLD;

        if (atTarget) {
            if (pos1) {
                setDesiredHoodState(RobotStates.HoodPosition.POS_ONE);
            } else if (pos2) {
                setDesiredHoodState(RobotStates.HoodPosition.POS_TWO);
            } else if (pos3) {
                setDesiredHoodState(RobotStates.HoodPosition.POS_THREE);
            } else if (pos4) {
                setDesiredHoodState(RobotStates.HoodPosition.POS_FOUR);
            } else if (pos5) {
                setDesiredHoodState(RobotStates.HoodPosition.POS_FIVE);
            }
        }

        hoodGoToState();
    }
    public void RAWloop(double servoPos){
        double desiredPos = this.getHoodPos(this.getDesiredHoodState());
        double currentPos = this.getActualHoodPosition();
        double error = Math.abs(desiredPos - currentPos);
        boolean atTarget = error <= HOOD_THRESHOLD;

        if (atTarget) {
            if (servoPos == HOOD_POS_1){
                setDesiredHoodState(RobotStates.HoodPosition.POS_ONE);
            } else if (servoPos == HOOD_POS_2){
                setDesiredHoodState(RobotStates.HoodPosition.POS_TWO);
            } else if (servoPos == HOOD_POS_3){
                setDesiredHoodState(RobotStates.HoodPosition.POS_THREE);
            } else if (servoPos == HOOD_POS_4){
                setDesiredHoodState(RobotStates.HoodPosition.POS_FOUR);
            } else if (servoPos == HOOD_POS_5){
                setDesiredHoodState(RobotStates.HoodPosition.POS_FIVE);
            }
        }
        hoodGoToState();
    }

    public void hoodTelem(Telemetry telemetry) {
        double targetPos = getHoodPos(this.desiredHoodState);
        double currentPos = this.getActualHoodPosition();
        double error = targetPos - currentPos;

        telemetry.addData("Hood State", this.getDesiredHoodState().toString());
        telemetry.addData("Hood Target Pos", String.format("%.3f", targetPos));
        telemetry.addData("Hood Current Pos", String.format("%.3f", currentPos));
        telemetry.addData("Hood Error", String.format("%.3f", error));
        telemetry.addData("Hood Last Output", String.format("%.3f", this.lastOutput));
    }
}