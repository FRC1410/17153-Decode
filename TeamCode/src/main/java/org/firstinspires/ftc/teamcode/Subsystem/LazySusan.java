package org.firstinspires.ftc.teamcode.Subsystem;

import static org.firstinspires.ftc.teamcode.Util.IDs.SUSAN_SPIN_MOTOR;
import static org.firstinspires.ftc.teamcode.Util.IDs.SUSAN_LIFT_SERVO;
import static org.firstinspires.ftc.teamcode.Util.Tuning.SUSAN_D;
import static org.firstinspires.ftc.teamcode.Util.Tuning.SUSAN_I;
import static org.firstinspires.ftc.teamcode.Util.Tuning.SUSAN_P;
import static org.firstinspires.ftc.teamcode.Util.Constants.SUSAN_POS_1;
import static org.firstinspires.ftc.teamcode.Util.Constants.SUSAN_POS_2;
import static org.firstinspires.ftc.teamcode.Util.Constants.SUSAN_POS_3;
import static org.firstinspires.ftc.teamcode.Util.Constants.SUSAN_LIFT_POS_UP;
import static org.firstinspires.ftc.teamcode.Util.Constants.SUSAN_LIFT_POS_DOWN;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PwmControl;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoImplEx;

import org.firstinspires.ftc.teamcode.Util.PIDController;
import org.firstinspires.ftc.teamcode.Util.RobotStates;

public class LazySusan {
    // me when i am motivationally challenged and
    private DcMotor spin_motor;
    private ServoImplEx lift_servo;
    private PwmControl.PwmRange pwm_range;

    private double servo_pos;
    private double susan_pos;
    private RobotStates.SusanLift current_servo_state = RobotStates.SusanLift.DOWN;

    private RobotStates.SusanSpin desired_susan_state = RobotStates.SusanSpin.ONE;

    private PIDController susan_PID_controller;

    public void init(HardwareMap hardwareMap) {
        this.spin_motor = hardwareMap.get(DcMotor.class, SUSAN_SPIN_MOTOR);
        this.lift_servo = hardwareMap.get(ServoImplEx.class, SUSAN_LIFT_SERVO);

        this.spin_motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.spin_motor.setDirection(DcMotorSimple.Direction.FORWARD);
        this.spin_motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//        this.spin_motor.setTargetPosition(0);
        this.spin_motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        this.lift_servo.setDirection(Servo.Direction.FORWARD);

        this.pwm_range = new PwmControl.PwmRange(600, 2400, 20000);

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
     * Get the current postition of the servo
     * @return Position
     */
    public double getActualServoState() {
        return this.lift_servo.getPosition();
    }

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
        this.lift_servo.setPosition(desiredClawPos);
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
        RobotStates.SusanSpin desiredSpinState = this.getDesired_susan_state();
        double desiredSusanPos = this.getSusanPos(desiredSpinState);
        double output = this.susan_PID_controller.calculate(desiredSusanPos, this.getActualSusanState());
        this.spin_motor.setPower(output);
        return output;
    }

    public void loop(boolean a, boolean b, boolean x, boolean rb) {
        if (a) {
            setDesired_susan_state(RobotStates.SusanSpin.ONE);
        } else if (b) {
            setDesired_susan_state(RobotStates.SusanSpin.TWO);
        } else if (x) {
            setDesired_susan_state(RobotStates.SusanSpin.THREE);
        }

        if (rb) {
            setCurrent_servo_state(RobotStates.SusanLift.UP);
        } else {
            setCurrent_servo_state(RobotStates.SusanLift.DOWN);
        }

        servoGoToState();
        System.out.println(susanGoToState());
    }
}
