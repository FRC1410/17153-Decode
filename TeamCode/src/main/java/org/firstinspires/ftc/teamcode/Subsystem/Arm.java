package org.firstinspires.ftc.teamcode.Subsystem;

import static org.firstinspires.ftc.teamcode.Util.IDs.ARM_MOTOR_ID;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Util.PIDController;
import org.firstinspires.ftc.teamcode.Util.RobotStates;

import static org.firstinspires.ftc.teamcode.Util.Tuning.*;

public class Arm {
    private DcMotorEx armMotor;
    private final PIDController armPIDController = new PIDController(ARM_P, ARM_I, ARM_D);
    private RobotStates.Arm currentArmState = RobotStates.Arm.DOWN;

    private int armEncoderValue;

    public void init(HardwareMap hardwareMap) {
        this.armMotor = hardwareMap.get(DcMotorEx.class, ARM_MOTOR_ID);

        this.armMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.armMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        this.armMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        this.armMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public void setState(RobotStates.Arm desiredState) {
        currentArmState = desiredState;
    }

    public RobotStates.Arm getArmState() {
        return currentArmState;
    }

    public int getArmStateEncoder(RobotStates.Arm desiredState) {
        switch (desiredState) {
            case DOWN:
                armEncoderValue = 0;
                break;

            case UP:
                armEncoderValue = 1600;
                break;
        }
        return armEncoderValue;
    }

    public void goToState() {
        RobotStates.Arm desiredState = this.getArmState();
        int desiredEncoderValue = this.getArmStateEncoder(desiredState);
        int currentArmPos = this.armMotor.getCurrentPosition();

        double armOutput = armPIDController.calculate(desiredEncoderValue, currentArmPos);

        this.armMotor.setPower(armOutput);

        if (Math.abs(desiredEncoderValue - currentArmPos) <= ARM_THRESHOLD) {
            this.armMotor.setPower(0);
        }
    }

    public void armTelemetry(Telemetry telemetry) {
        telemetry.addData("ArmEncoder", this.armMotor.getCurrentPosition());
    }
}
