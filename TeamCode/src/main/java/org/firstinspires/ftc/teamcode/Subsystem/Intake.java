package org.firstinspires.ftc.teamcode.Subsystem;
import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;
import static org.firstinspires.ftc.teamcode.Util.IDs.*;
import static org.firstinspires.ftc.teamcode.Util.Constants.*;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;


public class Intake {
    DcMotorEx intake;
    DcMotorEx transfer;
    DcMotorEx feeder;

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

        this.feeder = hardwareMap.get(DcMotorEx.class, FEEDER_MOTOR_ID);

        this.feeder.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        this.feeder.setDirection(DcMotorEx.Direction.FORWARD);

        this.feeder.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        this.feeder.setMode(DcMotor.RunMode.RUN_USING_ENCODER);



    }

    public void run(double intake, double outtake, double feederin, double feederout){
        this.intake.setVelocity((outtake * 1500) - (intake * 1500));
        this.transfer.setVelocity((outtake * 1500) - (intake * 1500));
        this.feeder.setVelocity((feederin * 1500) - (feederout * 1500));
    }

    public void intakeTelem(Telemetry telemetry){
        double p = this.intake.getPower();
        telemetry.addData("Power: ", p);
    }
}