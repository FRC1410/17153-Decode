package org.firstinspires.ftc.teamcode.Subsystem;

import static org.firstinspires.ftc.teamcode.Util.IDs.SUSAN_SPIN_MOTOR;
import static org.firstinspires.ftc.teamcode.Util.IDs.SUSAN_LIFT_SERVO;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoImplEx;

import org.firstinspires.ftc.teamcode.Util.RobotStates;

public class LazySuzan {
    // me when i am motivationally challenged
    private DcMotor spin_motor;
    private ServoImplEx lift_servo;

    private double servoPos;
    private RobotStates.SusanLift currentServoState;

    private RobotStates.SusanSpin desiredSusanState;

    public void init(HardwareMap hardwareMap) {
        this.spin_motor = hardwareMap.get(DcMotor.class, SUSAN_SPIN_MOTOR);
        this.lift_servo = hardwareMap.get(ServoImplEx.class, SUSAN_LIFT_SERVO);

        this.spin_motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.spin_motor.setDirection(DcMotorSimple.Direction.FORWARD);
        this.spin_motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.spin_motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        this.lift_servo.setDirection(Servo.Direction.FORWARD);
    }
    public void setCurrentServoState(RobotStates.SusanLift desiredServoState) {
        this.currentServoState = desiredServoState;
    }

    public RobotStates.SusanLift getCurrentServoState() {
        return this.currentServoState;
    }

    public double getServoPos(RobotStates.SusanLift desiredClawState) {
        switch (desiredClawState) {
            case UP:
                this.servoPos = 0.4;
                break;

            case DOWN:
                this.servoPos = 1;
                break;
        }
        return servoPos;
    }

    public void servoGoToState() {
        RobotStates.SusanLift desiredClawState = this.getCurrentServoState();
        double desiredClawPos = this.getServoPos(desiredClawState);
        this.lift_servo.setPosition(desiredClawPos);
    }

    public RobotStates.SusanSpin getDesiredSusanState() {
        return this.desiredSusanState;
    }

}
