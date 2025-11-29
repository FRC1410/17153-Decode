package org.firstinspires.ftc.teamcode.pedroPathing;

import static org.firstinspires.ftc.teamcode.Util.IDs.BACK_LEFT_MOTOR_ID;
import static org.firstinspires.ftc.teamcode.Util.IDs.BACK_RIGHT_MOTOR_ID;
import static org.firstinspires.ftc.teamcode.Util.IDs.FRONT_LEFT_MOTOR_ID;
import static org.firstinspires.ftc.teamcode.Util.IDs.FRONT_RIGHT_MOTOR_ID;

import com.pedropathing.control.FilteredPIDFCoefficients;
import com.pedropathing.control.PIDFCoefficients;
import com.pedropathing.follower.Follower;
import com.pedropathing.follower.FollowerConstants;
import com.pedropathing.ftc.FollowerBuilder;
import com.pedropathing.ftc.drivetrains.MecanumConstants;
import com.pedropathing.ftc.localization.Encoder;
import com.pedropathing.ftc.localization.constants.DriveEncoderConstants;
import com.pedropathing.paths.PathConstraints;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Constants {
    public static MecanumConstants driveConstants = new MecanumConstants()
            .rightFrontMotorName(FRONT_RIGHT_MOTOR_ID)
            .rightRearMotorName(BACK_RIGHT_MOTOR_ID)
            .leftRearMotorName(BACK_LEFT_MOTOR_ID)
            .leftFrontMotorName(FRONT_LEFT_MOTOR_ID)
            .leftFrontMotorDirection(DcMotorSimple.Direction.REVERSE)
            .leftRearMotorDirection(DcMotorSimple.Direction.FORWARD)
            .rightFrontMotorDirection(DcMotorSimple.Direction.FORWARD)
            .rightRearMotorDirection(DcMotorSimple.Direction.FORWARD)
            .xVelocity(46.12)
            .yVelocity(37.07);
    public static DriveEncoderConstants localizerConstants = new DriveEncoderConstants()
            .rightFrontMotorName(FRONT_RIGHT_MOTOR_ID)
            .rightRearMotorName(BACK_RIGHT_MOTOR_ID)
            .leftRearMotorName(BACK_LEFT_MOTOR_ID)
            .leftFrontMotorName(FRONT_LEFT_MOTOR_ID)
            .leftFrontEncoderDirection(Encoder.REVERSE)
            .leftRearEncoderDirection(Encoder.FORWARD)
            .rightFrontEncoderDirection(Encoder.FORWARD)
            .rightRearEncoderDirection(Encoder.FORWARD)
            .robotWidth(15.4)
            .robotLength(14.3)
            .forwardTicksToInches(0.00440555683476)
            .strafeTicksToInches(0.00383179729959)
            .turnTicksToInches(0.00896866643441);
    public static FollowerConstants followerConstants = new FollowerConstants()
            .mass(6.5)
            .forwardZeroPowerAcceleration(-114.7371925)
            .lateralZeroPowerAcceleration(-141.977375)
            .translationalPIDFCoefficients(new PIDFCoefficients(
                    0.7,
                    0,
                    0,
                    0.025
            ))
            .headingPIDFCoefficients(new PIDFCoefficients(
                    1.7,
                    0,
                    0,
                    0.005
            ))
            .drivePIDFCoefficients(new FilteredPIDFCoefficients(
                    0.7,
                    0,
                    0,
                    1,
                    0.025
            ))
            .centripetalScaling(0);

    public static PathConstraints pathConstraints = new PathConstraints(
            0.995,
            0.1,
            0.1,
            0.01,
            50,
            6,
            10,
            10);

    public static Follower createFollower(HardwareMap hardwareMap) {
        return new FollowerBuilder(followerConstants, hardwareMap)
                .mecanumDrivetrain(driveConstants)
                .driveEncoderLocalizer(localizerConstants)
                .pathConstraints(pathConstraints)
                .build();
    }
}
