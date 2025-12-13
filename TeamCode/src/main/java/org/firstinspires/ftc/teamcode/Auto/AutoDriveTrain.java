package org.firstinspires.ftc.teamcode.Auto;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.FORWARD;
import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.REVERSE;

import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

import static org.firstinspires.ftc.teamcode.Util.IDs.*;
import static org.firstinspires.ftc.teamcode.Util.Constants.*;

import org.firstinspires.ftc.teamcode.Util.RobotStates;


public class AutoDriveTrain {

    DcMotorEx motorFrontLeft;

    DcMotorEx motorFrontRight;
    DcMotorEx motorBackRight;
    DcMotorEx motorBackLeft;



    public void init(HardwareMap hardwareMap) {





        motorFrontLeft = hardwareMap.get(DcMotorEx.class, FRONT_LEFT_MOTOR_ID);
        motorFrontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


        motorFrontRight = hardwareMap.get(DcMotorEx.class, FRONT_RIGHT_MOTOR_ID);
        motorFrontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);



        motorBackRight = hardwareMap.get(DcMotorEx.class, BACK_RIGHT_MOTOR_ID);
        motorBackRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


        motorBackLeft = hardwareMap.get(DcMotorEx.class, BACK_LEFT_MOTOR_ID);
        motorBackLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);




    }




    public void drive(double forwardBackwardMovement, double strafe, double rotation) {


        double motorVelocity = 10;



        motorFrontLeft.setVelocity(motorVelocity);

        motorFrontRight.setVelocity(motorVelocity);

        motorBackRight.setVelocity(motorVelocity);

        motorBackLeft.setVelocity(motorVelocity);

        //First motor - Front Left
        motorFrontLeft.setPower(forwardBackwardMovement - strafe - rotation);

        //Second Motor - Front Right
        motorFrontRight.setPower(-forwardBackwardMovement - strafe - rotation);

        //Third Motor - Back Left
        motorBackLeft.setPower(forwardBackwardMovement + strafe - rotation);

        //Fourth Motor - Back Right
        motorBackRight.setPower(forwardBackwardMovement - strafe + rotation);

        if (forwardBackwardMovement < .05 && strafe < 0.05 && rotation < .05) {
            motorFrontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            motorFrontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            motorBackLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            motorBackRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }
    }
}