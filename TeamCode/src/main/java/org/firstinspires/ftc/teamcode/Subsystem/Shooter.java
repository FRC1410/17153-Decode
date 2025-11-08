package org.firstinspires.ftc.teamcode.Subsystem;

import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.FORWARD;
import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;
import static org.firstinspires.ftc.teamcode.Util.IDs.SHOOTER_MOTOR_ID;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Util.RobotStates;

public class Shooter {

    private DcMotorEx motorShooter;
    public RobotStates.ShooterStates shooterStatus = RobotStates.ShooterStates.FORWARD;


    public void init(HardwareMap hardwareMap) {

        this.motorShooter = hardwareMap.get(DcMotorEx.class, SHOOTER_MOTOR_ID);

        this.motorShooter.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        this.motorShooter.setDirection(FORWARD);

        this.motorShooter.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        this.motorShooter.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void cycle(Telemetry telemetry) {
        switch (shooterStatus) {
            case FORWARD:
                this.shooterStatus = RobotStates.ShooterStates.BACKWARD;
                telemetry.addData("Drive Mode:", this.shooterStatus);
            case BACKWARD:
                this.shooterStatus = RobotStates.ShooterStates.NEUTRAL;
                telemetry.addData("Drive Mode:", this.shooterStatus);
            case NEUTRAL:
                this.shooterStatus = RobotStates.ShooterStates.HALF_POWER;
                telemetry.addData("Drive Mode:", this.shooterStatus);
            case HALF_POWER:
                this.shooterStatus = RobotStates.ShooterStates.FORWARD;
                telemetry.addData("Drive Mode:", this.shooterStatus);
        }
        run(this.shooterStatus);
        telemetry.update();
    }

    public void run(RobotStates.ShooterStates shooterState){
        switch (shooterState) {
            case FORWARD:
                this.motorShooter.setPower(1);
            case BACKWARD:
                this.motorShooter.setPower(-1);
            case NEUTRAL:
                this.motorShooter.setPower(0);
            case HALF_POWER:
                this.motorShooter.setPower(.5);
        }
    }
}
