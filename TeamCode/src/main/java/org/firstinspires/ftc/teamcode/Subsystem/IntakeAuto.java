package org.firstinspires.ftc.teamcode.Subsystem;

import static org.firstinspires.ftc.teamcode.Util.IDs.INTAKE_MOTOR_ID;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class IntakeAuto {
    DcMotorEx intake;
    DcMotorEx transfer;

    public void init(HardwareMap hardwareMap) {
        this.intake = hardwareMap.get(DcMotorEx.class, INTAKE_MOTOR_ID);

        this.intake.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        this.intake.setDirection(DcMotorEx.Direction.FORWARD);

        this.intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        this.intake.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        this.transfer = hardwareMap.get(DcMotorEx.class, INTAKE_MOTOR_ID);

        this.transfer.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        this.transfer.setDirection(DcMotorEx.Direction.FORWARD);

        this.transfer.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        this.transfer.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

    }

    public void run(double intake, double outtake){
        this.intake.setPower(outtake-intake);
        this.transfer.setPower(outtake-intake);
    }

    public double getRPM(){
        return intake.getVelocity();
    }

    public void intakeTelem(Telemetry telemetry){
        double p = this.intake.getPower();
        telemetry.addData("Power: ", p);
    }
}
