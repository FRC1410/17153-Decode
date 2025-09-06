package org.firstinspires.ftc.teamcode.Util;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;

public class Constants {

    // Robot specs
    public static final RevHubOrientationOnRobot.LogoFacingDirection LOGO_FACING_DIRECTION =
            RevHubOrientationOnRobot.LogoFacingDirection.UP;
    public static final RevHubOrientationOnRobot.UsbFacingDirection USB_FACING_DIRECTION =
            RevHubOrientationOnRobot.UsbFacingDirection.LEFT;
    public static RevHubOrientationOnRobot HUB_ORIENTATION =
            new RevHubOrientationOnRobot(LOGO_FACING_DIRECTION, USB_FACING_DIRECTION);

    // Drive motor Constants
    public static final double GEAR_RATIO = 1 / 18.88;
    public static final double WHEEL_RADIUS = 1.5; // In inches
    public static final double WHEEL_CIRCUMFERENCE = 2 * Math.PI * WHEEL_RADIUS; // In inches
    public static final double TRACK_WIDTH = 15.238; // In inches
    public static final double ROBOT_WEIGHT = 20; // In pounds
    public static final double TICKS_PER_REVOLUTION = 28; // Encoder count per revolution
    public static final double ENCODER_COUNT_PER_WHEEL_REVOLUTION = TICKS_PER_REVOLUTION * GEAR_RATIO;
    public static final double ENCODER_COUNT_PER_INCH = (TICKS_PER_REVOLUTION * GEAR_RATIO) / WHEEL_CIRCUMFERENCE;
    public static final double MAX_RPM = 6000; // Revolutions per minute
    public static final double MAX_MOTOR_WHEEL_VELOCITY =
            (MAX_RPM / 60) * GEAR_RATIO * WHEEL_CIRCUMFERENCE; // In inches per second

    public static double MAX_VELOCITY = 50; // Inches per second
    public static double MAX_ACCELERATION = 0;
    public static double MAX_ANGULAR_VELOCITY = 0;

    // Linear slide constants
    public static double LINEAR_SLIDE_GEAR_RATIO = 1 / 19.2;
    public static double LINEAR_SLIDE_WHEEL_RADIUS = 0; // In inches
    public static double LINEAR_SLIDE_WHEEL_CIRCUFERENCE = 0; // In inches

    public static double MAX_LINEAR_SLIDE_RPM = 312; // Inches per second
    public static int MAX_LINEAR_SLIDE_EXTENTION = 2000;
    public static double MAX_LINEAR_SLIDE_VELOCITY =
            (MAX_LINEAR_SLIDE_RPM / 60) * LINEAR_SLIDE_GEAR_RATIO * LINEAR_SLIDE_WHEEL_CIRCUFERENCE;
}
