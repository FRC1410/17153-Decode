package org.firstinspires.ftc.teamcode.Subsystem;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoImplEx;

import org.firstinspires.ftc.teamcode.Util.RobotStates;

public class Claw {
    private ServoImplEx servoClaw;
    private RobotStates.Claw currentClawState = RobotStates.Claw.OPEN;
    private double clawPos;

    public void init(HardwareMap hardwareMap) {
        this.servoClaw = hardwareMap.get(ServoImplEx.class,"servoClawPosSet");
        this.servoClaw.setDirection(Servo.Direction.FORWARD);
    }

    public void setClawState(RobotStates.Claw desiredClawState) {
        this.currentClawState = desiredClawState;
    }

    public RobotStates.Claw getClawState() {
        return currentClawState;
    }

    public double getClawPos(RobotStates.Claw desiredClawState) {
        switch (desiredClawState) {
            case OPEN:
                this.clawPos = 0.4;
                break;

            case CLOSED:
                this.clawPos = 1;
                break;
        }
        return clawPos;
    }

    public void goToState() {
        RobotStates.Claw desiredClawState = this.getClawState();
        double desiredClawPos = this.getClawPos(desiredClawState);
        this.servoClaw.setPosition(desiredClawPos);
    }
}