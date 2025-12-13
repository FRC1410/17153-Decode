package org.firstinspires.ftc.teamcode.Util.DriverUtil;

import com.qualcomm.robotcore.hardware.Gamepad;
import java.util.function.Supplier;

public class ControlScheme {

    //Drivetrain
    public static Supplier<Float> DRIVE_STRAFE;
    public static Supplier<Float> DRIVE_FB;
    public static Supplier<Float> DRIVE_ROTATE;
    public static Supplier<Boolean> DRIVE_SLOW_MODE;

    //Intake
    public static Supplier<Float> INTAKE_IN;;
    public static Supplier<Float> INTAKE_OUT;

    //SUSAN
    public static Supplier<Boolean> SUSAN_MANUAL_ONE;
    public static Supplier<Boolean> SUSAN_MANUAL_TWO;
    public static Supplier<Boolean> SUSAN_MANUAL_THREE;

    public static Supplier<Float> SUSAN_ADJUST;

    public static Supplier<Boolean> SUSAN_LIFT = () -> false; // Default to false to avoid null

    //SHOOTER
    public static Supplier<Boolean> SHOOTER_CYCLE;

    //HOOD SERVO
    public static Supplier<Boolean> HOOD_POS_ONE;
    public static Supplier<Boolean> HOOD_POS_TWO;
    public static Supplier<Boolean> HOOD_POS_THREE;
    public static Supplier<Boolean> HOOD_POS_FOUR;
    public static Supplier<Boolean> HOOD_POS_FIVE;

    //CONTINUOUS SERVO
    public static Supplier<Boolean> CONTINUOUS_SERVO_TOGGLE;
    public static Supplier<Float> SHOOT;
    public static Supplier<Float> REVERSE;

    public static void initDriver(Gamepad gamepad1) {
        DRIVE_STRAFE = () -> gamepad1.left_stick_x;
        DRIVE_FB = () -> gamepad1.left_stick_y;
        DRIVE_ROTATE = () -> gamepad1.right_stick_x;
        DRIVE_SLOW_MODE = () -> gamepad1.a;
        INTAKE_IN = () -> gamepad1.left_trigger;
        INTAKE_OUT = () -> gamepad1.right_trigger;
    }

    public static void initOperator(Gamepad gamepad2) {
        SUSAN_MANUAL_ONE = () -> gamepad2.x;
        SUSAN_MANUAL_TWO = () -> gamepad2.a;
        SUSAN_MANUAL_THREE = () -> gamepad2.b;
        SUSAN_ADJUST = () -> gamepad2.left_stick_x;
        //SHOOTER_CYCLE = () -> gamepad2.rightBumperWasPressed();
        SUSAN_LIFT = () -> gamepad2.y;
        //SHOOTER_CYCLE = () -> gamepad2.rightBumperWasPressed();
        SHOOT = () -> gamepad2.right_trigger;
        REVERSE = () -> gamepad2.left_trigger;
        SUSAN_LIFT = () -> gamepad2.y; // Assign to Y button for susan lift
        SHOOTER_CYCLE = () -> gamepad2.rightBumperWasPressed();

        //Hood Servo - using dpad for 5 positions
        HOOD_POS_ONE = () -> gamepad2.dpad_left;
        HOOD_POS_TWO = () -> gamepad2.dpad_down;
        HOOD_POS_THREE = () -> gamepad2.dpad_right;
        HOOD_POS_FOUR = () -> gamepad2.dpad_up;
        HOOD_POS_FIVE = () -> gamepad2.right_stick_button;

        //Continuous Servo
        CONTINUOUS_SERVO_TOGGLE = () -> gamepad2.leftBumperWasPressed();
    }
}