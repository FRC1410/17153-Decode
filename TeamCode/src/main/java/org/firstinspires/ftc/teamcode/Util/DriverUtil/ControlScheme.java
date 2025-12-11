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

    public static Supplier<Boolean> SUSAN_LIFT;

    //SHOOTER
    public static Supplier<Boolean> SHOOTER_CYCLE;


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
        SUSAN_LIFT = () -> gamepad2.y;
        SHOOTER_CYCLE = () -> gamepad2.right_bumper;
    }
}