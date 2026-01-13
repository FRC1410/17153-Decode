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
        this.intake.setVelocity((outtake * 800) - (intake * 800));
        this.transfer.setVelocity((outtake * 800) - (intake * 800));
    }

    public void intakeTelem(Telemetry telemetry){
        double p = this.intake.getPower();
        telemetry.addData("Power: ", p);
    }
}