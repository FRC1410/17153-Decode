package org.firstinspires.ftc.teamcode.Subsystem;

import static org.firstinspires.ftc.teamcode.Util.IDs.*;
import static org.firstinspires.ftc.teamcode.Util.Tuning.*;
import static org.firstinspires.ftc.teamcode.Util.Constants.*;


import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PwmControl;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoImplEx;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Util.PIDController;
import org.firstinspires.ftc.teamcode.Util.RobotStates;

import java.sql.Time;
import java.time.LocalTime;
import java.util.Timer;

public class LazySusan {
    // me when i am motivationally challenged and my name is susan
    private DcMotor spin_motor;
//    private ServoImplEx lift_servo;

    private double servo_pos;
    private double susan_pos;
    private RobotStates.SusanLift current_servo_state = RobotStates.SusanLift.DOWN;

    private RobotStates.SusanSpin desired_susan_state = RobotStates.SusanSpin.ONE;

    private PIDController susan_PID_controller;
    private double lastMotorPower = 0;
    LocalTime now = LocalTime.now();
    private int lastTime = 60;

    public void init(HardwareMap hardwareMap) {
        this.spin_motor = hardwareMap.get(DcMotor.class, SUSAN_SPIN_MOTOR_ID);

        this.spin_motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.spin_motor.setDirection(DcMotorSimple.Direction.FORWARD);
        this.spin_motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.spin_motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

//        this.lift_servo.setDirection(Servo.Direction.FORWARD);

        this.susan_PID_controller = new PIDController(SUSAN_P, SUSAN_I, SUSAN_D);
    }
    /**
     * Sets the desired servo state
     */
    public void setCurrent_servo_state(RobotStates.SusanLift desiredServoState) {
        this.current_servo_state = desiredServoState;
    }

    /**
     * Get the current servo state
     * @return Current Servo State (RobotStates.SusanLift)
     */
    public RobotStates.SusanLift getCurrent_servo_state() {
        return this.current_servo_state;
    }

    /**
     * Get the current position of the servo
     * @return Position
     */
//    public double getActualServoState() {
//        return this.lift_servo.getPosition();
//    }

    /**
     * Get the actual servo encoder pos based on state
     * @return Servo Position
     */
    public double getServoPos(RobotStates.SusanLift desiredClawState) {
        switch (desiredClawState) {
            case UP:
                this.servo_pos = SUSAN_LIFT_POS_UP;
                break;

            case DOWN:
                this.servo_pos = SUSAN_LIFT_POS_DOWN;
                break;
        }
        return servo_pos;
    }

    /**
     * Have servo attempt to go to the current desired state
     */
    public void servoGoToState() {
        RobotStates.SusanLift desiredClawState = this.getCurrent_servo_state();
        double desiredClawPos = this.getServoPos(desiredClawState);
//        this.lift_servo.setPosition(desiredClawPos);
    }

    /**
     * Get the current desired Susan state
     * @return Desired Susan State
     */
    public RobotStates.SusanSpin getDesired_susan_state() {
        return this.desired_susan_state;
    }

    /**
     * Set the current desired Susan state
     */
    public void setDesired_susan_state(RobotStates.SusanSpin desired_susan_state) {
        if (this.desired_susan_state != desired_susan_state) {
            this.susan_PID_controller.reset();
        }
        this.desired_susan_state = desired_susan_state;
    }

    /**
     * Get the actual Susan encoder pos based on state
     * @return Susan Position
     */
    public double getSusanPos(RobotStates.SusanSpin desiredSusanState) {
        switch (desiredSusanState) {
            case ONE:
                this.susan_pos = SUSAN_POS_1;
                break;
            case TWO:
                this.susan_pos = SUSAN_POS_2;
                break;
            case THREE:
                this.susan_pos = SUSAN_POS_3;
                break;

        }
        return this.susan_pos;
    }

    /**
     * Get the current position of the Susan
     * @return Position
     */
    public int getActualSusanState() {
        return this.spin_motor.getCurrentPosition();
    }

    /**
     * Have Susan attempt to go to the current desired state using the pid controller
     * @return Power output
     */
    public double susanGoToState() {
        now = LocalTime.now();
        if (this.lastTime == now.getSecond()) {
            this.spin_motor.setPower(0);
            this.lastMotorPower = 0;
            return 0;
        } else if (this.lastTime < 60) {
            this.lastTime = 60;
        }
        RobotStates.SusanSpin desiredSpinState = this.getDesired_susan_state();
        double desiredSusanPos = this.getSusanPos(desiredSpinState);
        double currentPos = ((this.getActualSusanState() / 1365.0) * -1);
        double error = desiredSusanPos - currentPos;

        if (currentPos > 9 || currentPos < -3) {
            this.spin_motor.setPower(0);
            this.lastMotorPower = 0;
            this.lastTime = now.getSecond();
            return 0;
        }

        if(Math.abs(error) <= SUSAN_SPIN_THRESHHOLD) {
            this.spin_motor.setPower(0);
            this.lastMotorPower = 0;
            return 0;
        }
        double output = this.susan_PID_controller.calculate(desiredSusanPos, currentPos);

        output = Math.max(-0.007, Math.min(0.007, output));

        // Apply minimum power to overcome static friction
        if (Math.abs(output) < SUSAN_MIN_POWER && Math.abs(output) > 0.0001) {
            if (output > 0) {
                output = SUSAN_MIN_POWER;
            } else {
                output = -SUSAN_MIN_POWER;
            }
        } else if (Math.abs(output) <= 0.001) {
            output = 0;
        }

        this.spin_motor.setPower(output);
        this.lastMotorPower = output;
        return output;
    }

    public void loop(boolean a, boolean b, boolean x, boolean rb) {
            // Check if we're at the target position (not moving to a new position)
            double desiredSusanPos = this.getSusanPos(this.getDesired_susan_state());
            double currentPos = ((this.getActualSusanState() / 1365.0) * -1);
            double error = Math.abs(desiredSusanPos - currentPos);
            boolean atTarget = error <= SUSAN_SPIN_THRESHHOLD;

            // Only accept new position inputs if we've reached the target
            if (atTarget) {
                if (a) {
                    setDesired_susan_state(RobotStates.SusanSpin.ONE);
                } else if (b) {
                    setDesired_susan_state(RobotStates.SusanSpin.TWO);
                } else if (x) {
                    setDesired_susan_state(RobotStates.SusanSpin.THREE);
                }
            }

            if (rb) {
                setCurrent_servo_state(RobotStates.SusanLift.UP);
            } else {
                setCurrent_servo_state(RobotStates.SusanLift.DOWN);
            }

            servoGoToState();
            susanGoToState();

        }


    public void susanTelem(Telemetry telemetry) {
        double targetPos = getSusanPos(this.desired_susan_state);
        double currentPos = ((this.getActualSusanState() / 1365.0) * -1);
        double error = targetPos - currentPos;
        double calculatedOutput = error * SUSAN_P;


        telemetry.addData("--- SUSAN DATA ---", "");
        telemetry.addData("Susan State", this.getDesired_susan_state().toString());
        telemetry.addData("Susan Target Pos", this.getSusanPos(this.getDesired_susan_state()));
        telemetry.addData("Susan Current Pos", currentPos);
        telemetry.addData("Susan Error (T-C)", error);
        telemetry.addData("Susan Calculated Output", String.format("%.3f", calculatedOutput));
        telemetry.addData("Susan Motor Power", String.format("%.3f", this.spin_motor.getPower()));
        telemetry.addData("Susan Last Power", String.format("%.3f", this.lastMotorPower));
        telemetry.addData("Susan Servo State", this.getCurrent_servo_state().toString());
    }
}
