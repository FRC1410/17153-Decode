package org.firstinspires.ftc.teamcode.Subsystem;

import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.FORWARD;
import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;
import static org.firstinspires.ftc.teamcode.Util.IDs.CONTINUOUS_SERVO_ID;
import static org.firstinspires.ftc.teamcode.Util.IDs.SHOOTER_MOTOR_ID;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Util.RobotStates;

public class liftShooter {

    private DcMotorEx motorShooter;
    private CRServo servo;
    public RobotStates.ShooterStates shooterStatus = RobotStates.ShooterStates.HALF_POWER;


    public void init(HardwareMap hardwareMap) {

        this.motorShooter = hardwareMap.get(DcMotorEx.class, SHOOTER_MOTOR_ID);

        this.motorShooter.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        this.motorShooter.setDirection(DcMotorSimple.Direction.FORWARD);

        this.motorShooter.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        this.motorShooter.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        this.servo = hardwareMap.get(CRServo.class, CONTINUOUS_SERVO_ID);

    }


    public void cycle(Telemetry telemetry, double intake, double outtake) {

        if(intake > outtake){
            this.shooterStatus = RobotStates.ShooterStates.FORWARD;
        }
        if(intake < outtake){
            this.shooterStatus = RobotStates.ShooterStates.BACKWARD;
        }
        else{
            this.shooterStatus = RobotStates.ShooterStates.NEUTRAL;
        }
//        switch (double intake, double outtake) {
//            case( intake > outtake):
//                break;
//            case FORWARD:
//                this.shooterStatus = RobotStates.ShooterStates.BACKWARD;
//                break;
//            case BACKWARD:
//                this.shooterStatus = RobotStates.ShooterStates.NEUTRAL;
//                break;
//            case NEUTRAL:
//                this.shooterStatus = RobotStates.ShooterStates.HALF_POWER;
//                break;
//            case HALF_POWER:
//                this.shooterStatus = RobotStates.ShooterStates.FORWARD;
//                break;
        run(intake - outtake);
        telemetry.addData("Drive Mode:", this.shooterStatus);
        telemetry.addData("Shooter Power", this.motorShooter.getPower());
        telemetry.update();
    }

    public void run(double val){
        this.motorShooter.setPower(val);
        servo.setPower(val);
    }
}
