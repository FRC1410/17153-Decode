package org.firstinspires.ftc.teamcode.Subsystem;

import static org.firstinspires.ftc.teamcode.Util.IDs.SUSAN_SPIN_MOTOR;
import static org.firstinspires.ftc.teamcode.Util.Tuning.SUSAN_D;
import static org.firstinspires.ftc.teamcode.Util.Tuning.SUSAN_I;
import static org.firstinspires.ftc.teamcode.Util.Tuning.SUSAN_P;
import static org.firstinspires.ftc.teamcode.Util.Constants.SUSAN_POS_1;
import static org.firstinspires.ftc.teamcode.Util.Constants.SUSAN_POS_2;
import static org.firstinspires.ftc.teamcode.Util.Constants.SUSAN_POS_3;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Sensor.OpenCV;
import org.firstinspires.ftc.teamcode.Util.PIDController;
import org.firstinspires.ftc.teamcode.Util.RobotStates;

public class LazySusan {
    private DcMotor spin_motor;

    private double susan_pos;

    private RobotStates.SusanSpin desired_susan_state = RobotStates.SusanSpin.ONE;

    private PIDController susan_PID_controller;

    private static final int SHOOTER_POSITION = 1;

    public void init(HardwareMap hardwareMap) {
        this.spin_motor = hardwareMap.get(DcMotor.class, SUSAN_SPIN_MOTOR);

        this.spin_motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.spin_motor.setDirection(DcMotorSimple.Direction.FORWARD);
        this.spin_motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.spin_motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


        this.susan_PID_controller = new PIDController(SUSAN_P, SUSAN_I, SUSAN_D);
    }

    public RobotStates.SusanSpin getDesired_susan_state() {
        return this.desired_susan_state;
    }

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

    public void loop(boolean a, boolean b, boolean x) {
        if (a) {
            setDesired_susan_state(RobotStates.SusanSpin.ONE);
        } else if (b) {
            setDesired_susan_state(RobotStates.SusanSpin.TWO);
        } else if (x) {
            setDesired_susan_state(RobotStates.SusanSpin.THREE);
        }

        susanGoToState();
    }

    public boolean shootColor(OpenCV openCV, OpenCV.ArtifactColor targetColor, Telemetry telemetry) {
        Integer colorPosition = openCV.findPositionForColor(targetColor);

        if (colorPosition == null) {
            telemetry.addData("Shoot " + targetColor, "NOT FOUND");
            return false;
        }

        telemetry.addData("Shoot " + targetColor, "Found at position " + colorPosition);

        RobotStates.SusanSpin targetState = getStateForPosition(colorPosition);
        setDesired_susan_state(targetState);

        return true;
    }

    private RobotStates.SusanSpin getStateForPosition(int position) {
        switch (position) {
            case 1: return RobotStates.SusanSpin.ONE;
            case 2: return RobotStates.SusanSpin.TWO;
            case 3: return RobotStates.SusanSpin.THREE;
            default: return RobotStates.SusanSpin.ONE;
        }
    }

    public int getCurrentPosition() {
        RobotStates.SusanSpin currentState = getDesired_susan_state();
        switch (currentState) {
            case ONE: return 1;
            case TWO: return 2;
            case THREE: return 3;
            default: return 1;
        }
    }

    public int getShooterPosition() {
        return SHOOTER_POSITION;
    }
}
