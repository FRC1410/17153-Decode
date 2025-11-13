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


    public static void initDriver(Gamepad gamepad1) {
        DRIVE_STRAFE = () -> gamepad1.left_stick_x;
        DRIVE_FB = () -> gamepad1.left_stick_y;
        DRIVE_ROTATE = () -> gamepad1.right_stick_x;
        DRIVE_SLOW_MODE = () -> gamepad1.a;
    }

    public static void initOperator(Gamepad gamepad2) {
        INTAKE_IN = () -> gamepad2.left_trigger;
        INTAKE_OUT = () -> gamepad2.right_trigger;
    }
}