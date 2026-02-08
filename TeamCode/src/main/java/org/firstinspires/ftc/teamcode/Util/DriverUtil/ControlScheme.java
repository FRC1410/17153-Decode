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
    public static Supplier<Float> INTAKE_IN;
    public static Supplier<Float> INTAKE_OUT;

    public static Supplier<Float> FEED;

    //SUSAN
    public static Supplier<Boolean> SUSAN_MANUAL_ONE;
    public static Supplier<Boolean> SUSAN_MANUAL_TWO;
    public static Supplier<Boolean> SUSAN_MANUAL_THREE;

    public static Supplier<Float> SUSAN_ADJUST;

    public static Supplier<Boolean> SUSAN_LIFT = () -> false;

    //SHOOTER
    public static Supplier<Boolean> SHOOTER_CYCLE;
    public static Supplier<Boolean> NEAR_SHOOTER_CYCLE;
    public static Supplier<Boolean> SHOOTER_REVERSE;

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
        INTAKE_IN = () -> gamepad1.right_trigger;
        INTAKE_OUT = () -> gamepad1.left_trigger;
    }

    public static void initOperator(Gamepad gamepad2) {
        SHOOTER_CYCLE = () -> gamepad2.right_bumper;
        NEAR_SHOOTER_CYCLE = () -> gamepad2.left_bumper;
        SHOOTER_REVERSE = () -> (gamepad2.left_trigger > 0.3); // triggers are weird
        FEED = () -> gamepad2.right_trigger;

        HOOD_POS_ONE = () -> gamepad2.dpad_left;
        HOOD_POS_TWO = () -> gamepad2.dpad_down;
        HOOD_POS_THREE = () -> gamepad2.dpad_right;
        HOOD_POS_FOUR = () -> gamepad2.dpad_up;
        HOOD_POS_FIVE = () -> gamepad2.right_stick_button;
    }
}