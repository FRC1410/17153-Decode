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

    //Turret Constants
    public static final double MAX_TRACKING_POWER = 0.8;
    public static final double MIN_TRACKING_POWER = 0.1;
    public static final double ACCEPTED_BEARING = 2.0; //This is in degrees

    public static final double TURRET_TICKS_PER_DEGREE = 4.0 / 3.0;

    //Shooter Constants
    public static final double G = 9.80665;
    public static final double RHO = 1.225;
    public static final double CD = 0.30;
    public static final double MASS = 0.0748;
    public static final double DIAMETER = 0.127;
    public static final double RADIUS = DIAMETER / 2;
    public static final double AREA = Math.PI * RADIUS * RADIUS;
    public static final double OMEGA = 2 * Math.PI * (60.0 / 60.0);
    public static final double CL_BASE = 0.195;
    public static final double V0 = 8.2829;
    public static final double Y0 = 0.3302;
    public static final double Y_GOAL = 0.98425;

    public static final double DT = 0.001;
    public static final double MAX_TIME = 3.0;
    public static final double INCHES_TO_METERS = 0.0254;


    public static final int MAX_ITERATIONS = 25;
    public static final double MIN_ANGLE = 5.0;
    public static final double MAX_ANGLE = 60.0;

}
