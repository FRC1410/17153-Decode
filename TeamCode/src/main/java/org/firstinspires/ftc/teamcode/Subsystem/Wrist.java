package org.firstinspires.ftc.teamcode.Subsystem;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoImplEx;

import org.firstinspires.ftc.teamcode.Util.RobotStates;

public class Wrist {
    private ServoImplEx wristServo;

    private RobotStates.Wrist currentWristState = RobotStates.Wrist.FLOOR;
    private double wristPos;

    public void init(HardwareMap hardwareMap) {
        this.wristServo = hardwareMap.get(ServoImplEx.class,"servoWristPosSet");
        this.wristServo.setDirection(Servo.Direction.FORWARD);
    }

    public void setState(RobotStates.Wrist desiredWristState) {
        this.currentWristState = desiredWristState;
    }

    public RobotStates.Wrist getCurrentState() {
        return currentWristState;
    }

    public double getWristPosition(RobotStates.Wrist desiredWristState) {
        switch(desiredWristState) {
            case FLOOR:
                this.wristPos = 1;
                break;

            case SCORE:
                this.wristPos = 0;
                break;
        }
        return wristPos;
    }

    public void goToState(Gamepad gamepad) {

        if (gamepad.right_bumper) {
            this.setState(RobotStates.Wrist.SCORE);
        } else if (gamepad.left_bumper) {
            this.setState(RobotStates.Wrist.FLOOR);
        }

        RobotStates.Wrist desiredWristState = this.getCurrentState();
        double desiredWristPos = this.getWristPosition(desiredWristState);
        this.wristServo.setPosition(desiredWristPos);
    }
}
