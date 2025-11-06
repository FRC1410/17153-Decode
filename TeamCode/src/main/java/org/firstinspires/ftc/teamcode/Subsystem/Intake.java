package org.firstinspires.ftc.teamcode.Subsystem;
import static org.firstinspires.ftc.teamcode.Util.IDs.*;
import static org.firstinspires.ftc.teamcode.Util.Constants.*;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;


public class Intake {
    DcMotorEx intake;

    public void init(HardwareMap hardwareMap) {
        this.intake = hardwareMap.get(DcMotorEx.class, INTAKE_MOTOR_ID);

        this.intake.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        this.intake.setDirection(DcMotorEx.Direction.FORWARD);

        this.intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        this.intake.setMode(DcMotor.RunMode.RUN_USING_ENCODER);}

    public void run(double intake, double outtake){
        this.intake.setPower((outtake) - (intake));
    }
}