package org.firstinspires.ftc.teamcode.Subsystem;

import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.FORWARD;
import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;
import static org.firstinspires.ftc.teamcode.Util.IDs.SHOOTER_MOTOR_ID;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Util.RobotStates;

public class Shooter {

    private DcMotorEx motorShooter;
    public RobotStates.ShooterStates shooterStatus = RobotStates.ShooterStates.HALF_POWER;


    public void init(HardwareMap hardwareMap) {

        this.motorShooter = hardwareMap.get(DcMotorEx.class, SHOOTER_MOTOR_ID);

        this.motorShooter.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        this.motorShooter.setDirection(DcMotorSimple.Direction.REVERSE);

        this.motorShooter.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        this.motorShooter.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void cycle(Telemetry telemetry) {
        switch (this.shooterStatus) {
            case FORWARD:
                this.shooterStatus = RobotStates.ShooterStates.BACKWARD;
                break;
            case BACKWARD:
                this.shooterStatus = RobotStates.ShooterStates.NEUTRAL;
                break;
            case NEUTRAL:
                this.shooterStatus = RobotStates.ShooterStates.HALF_POWER;
                break;
            case HALF_POWER:
                this.shooterStatus = RobotStates.ShooterStates.FORWARD;
                break;
        }
        run(this.shooterStatus);
        telemetry.addData("Drive Mode:", this.shooterStatus);
        telemetry.addData("Shooter Power", this.motorShooter.getPower());
        telemetry.update();
    }

    public void run(RobotStates.ShooterStates shooterState){
        switch (shooterState) {
            case FORWARD:
                this.motorShooter.setPower(1);
                break;
//            case BACKWARD:
//                this.motorShooter.setPower(-0.5);
//                break;
            case NEUTRAL:
                this.motorShooter.setPower(0);
                break;
//            case HALF_POWER:
//                this.motorShooter.setPower(0.5);
//                break;
        }
    }
}
