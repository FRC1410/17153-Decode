package org.firstinspires.ftc.teamcode.Util;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;

public class Constants {

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
            (MAX_RPM / 60) * GEAR_RATIO * WHEEL_CIRCUMFERENCE;

    public static final RevHubOrientationOnRobot.LogoFacingDirection LOGO_FACING_DIRECTION =
            RevHubOrientationOnRobot.LogoFacingDirection.UP;
    public static final RevHubOrientationOnRobot.UsbFacingDirection USB_FACING_DIRECTION =
            RevHubOrientationOnRobot.UsbFacingDirection.LEFT;
    public static RevHubOrientationOnRobot HUB_ORIENTATION =
            new RevHubOrientationOnRobot(LOGO_FACING_DIRECTION, USB_FACING_DIRECTION);

    //Susan
    public static final double SUSAN_POS_1 = 0;
    public static final double SUSAN_POS_2 = 4860;
    public static final double SUSAN_POS_3 = 2658;
    public static final double SUSAN_LIFT_POS_UP = 0.4;
    public static final double SUSAN_LIFT_POS_DOWN = 1;

    //Hood Servo Positions
    public static final double HOOD_POS_1 = 0.0;
    public static final double HOOD_POS_2 = 0.1;
    public static final double HOOD_POS_3 = 0.2;
    public static final double HOOD_POS_4 = 0.3;
    public static final double HOOD_POS_5 = 0.4;

    //ball color ranges
    public static int PURPLE_LOWER_RED = 115;
    public static int PURPLE_UPPER_RED = 150;

    public static int PURPLE_LOWER_GREEN = 50;
    public static int PURPLE_UPPER_GREEN = 90;

    public static int PURPLE_LOWER_BLUE = 90;
    public static int PURPLE_UPPER_BLUE = 120;

    public static int GREEN_LOWER_RED = 115;
    public static int GREEN_UPPER_RED = 140;

    public static int GREEN_LOWER_GREEN = 200;
    public static int GREEN_UPPER_GREEN = 255;

    public static int GREEN_LOWER_BLUE = 115;
    public static int GREEN_UPPER_BLUE = 140;

    //Controller

    public static double MATCH_HALF_TIME = 10.0; //Seconds
}