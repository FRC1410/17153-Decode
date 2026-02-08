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
    // Intake the Artifact into the Robot.
    DcMotorEx intake;
    //Transfer the Artifacts into storage.
    DcMotorEx transfer;
    // Feed the Artifacts in storage into the shooter to get ready to score.
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

    public void run(double intake, double outtake){ // for legacy usage (does the same thing, just subjectively better)
        // The max gear speed we can currently run on this is 6000rpm per motor.
        this.intake.setVelocity((outtake * 3000) - (intake * 3000));
        this.transfer.setVelocity((outtake * 3000) - (intake * 3000));
        this.feeder.setVelocity((outtake * 300) - (intake * 300));
    }

    public void run(double intake, double outtake, double feederin, double feederout){
        // The max gear speed we can currently run on this is 6000rpm per motor.
        this.intake.setVelocity((outtake * 3000) - (intake * 3000));
        this.transfer.setVelocity((outtake * 3000) - (intake * 3000));
        this.feeder.setVelocity((feederin * 3000) - (feederout * 3000));
    }

    public void intakeTelem(Telemetry telemetry){
        double intakep = this.intake.getPower();
        double transferp = this.transfer.getPower();
        double feederp = this.feeder.getPower();
        telemetry.addData("Intake Power: ", intakep);
        telemetry.addLine();
        telemetry.addData("Transfer Power: ", transferp);
        telemetry.addLine();
        telemetry.addData("Feeder Power: ", feederp);
        telemetry.update();
    }
}